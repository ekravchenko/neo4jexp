package com.testingzone.services.neo4j

import cats.effect.{ExitCode, IOApp, Resource}
import com.testingzone.infrastructure.trace.TraceIO
import com.testingzone.infrastructure.trace.http.HttpTraceInterceptor
import com.testingzone.infrastructure.trace.kafka.{KafkaTraceableHandler, KafkaTraceableProducer}
import com.testingzone.services.neo4j.config.{HttpConfig, KafkaConfig, Neo4jConfig}
import com.testingzone.services.neo4j.endpoint.{KafkaEndpoint, PersonEndpoint, TraceEndpoint}
import com.testingzone.services.neo4j.handler.SimpleEventHandler
import com.testingzone.services.neo4j.publisher.KafkaSimplePublisher
import com.testingzone.services.neo4j.repository.neo4j.Neo4jPersonRepository
import com.testingzone.services.neo4j.service.{KafkaService, PersonService, TraceService}
import fs2.kafka._
import neotypes.GraphDatabase
import neotypes.cats.effect.implicits._
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import org.neo4j.driver.AuthTokens
import org.typelevel.log4cats.slf4j.Slf4jFactory
import org.typelevel.log4cats.syntax._
import org.typelevel.log4cats.{Logger, LoggerFactory}
import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}

object Main extends IOApp {

  type F[A] = cats.effect.IO[A]

  implicit val loggerFactory: LoggerFactory[F] = Slf4jFactory[F]

  def run(args: List[String]): F[ExitCode] =
    (for {
      implicit0(trace: TraceIO) <- TraceIO.resource
      implicit0(logger: Logger[F]) = loggerFactory.getLogger
      neo4jConfig <- Neo4jConfig.build.resource[F]
      kafkaConfig <- KafkaConfig.build.resource[F]
      httpConfig = HttpConfig.default
      driver <- GraphDatabase.driver[F](s"bolt://${neo4jConfig.host}:${neo4jConfig.port}", AuthTokens.basic(neo4jConfig.user, neo4jConfig.password))
      producer <- KafkaTraceableProducer.resource(ProducerSettings[F, String, String].withBootstrapServers(s"${kafkaConfig.host}:${kafkaConfig.port}"))
      publisher = new KafkaSimplePublisher[F](producer, kafkaConfig.testTopic)
      personRepository = new Neo4jPersonRepository[F](driver)
      personService = new PersonService[F](personRepository)
      kafkaService = new KafkaService[F](publisher)
      traceService = new TraceService[F]
      personEndpoint = new PersonEndpoint[F](personService)
      kafkaEndpoint = new KafkaEndpoint[F](kafkaService)
      traceEndpoint = new TraceEndpoint[F](traceService)
      _ <- Resource.eval(info"Starting up http4s blaze")
      options = Http4sServerOptions.customiseInterceptors[F].prependInterceptor(HttpTraceInterceptor[F]).options
      routes = Http4sServerInterpreter[F](options).toRoutes(List(
        personEndpoint.getPersonsEndpoint,
        kafkaEndpoint.postToKafkaEndpoint,
        traceEndpoint.postSimpleTraceEndpoint
      ))
      server = BlazeServerBuilder[F]
        .withExecutionContext(scala.concurrent.ExecutionContext.Implicits.global)
        .bindHttp(httpConfig.port, httpConfig.host)
        .withHttpApp(Router("/" -> routes).orNotFound)
        .resource
      consumer <- KafkaConsumer.resource(ConsumerSettings[F, String, String]
        .withAutoOffsetReset(AutoOffsetReset.Earliest)
        .withBootstrapServers(s"${kafkaConfig.host}:${kafkaConfig.port}")
        .withGroupId(kafkaConfig.consumerGroup))
      handler = KafkaTraceableHandler[F, String, String](new SimpleEventHandler[F](traceService))
      _ <- Resource.eval(consumer.subscribeTo(kafkaConfig.testTopic))
      _ <- Resource.eval(logger.info(s"Subscribed to Kafka topic [${kafkaConfig.testTopic}]"))
      stream = consumer.stream
        .mapAsync(20)(msg => handler.handle(msg.record).as(msg.offset))
        .through(commitBatchWithin(kafkaConfig.offsetConfig.batchSize, kafkaConfig.offsetConfig.interval))
        .compile
        .resource
        .drain
      done <- server.both(stream)
    } yield done).use_.as(ExitCode.Success)
}
