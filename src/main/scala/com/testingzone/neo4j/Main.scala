package com.testingzone.neo4j

import cats.effect.{ExitCode, IOApp, Resource}
import com.testingzone.neo4j.config.{HttpConfig, KafkaConfig, Neo4jConfig}
import com.testingzone.neo4j.endpoint.{KafkaEndpoint, PersonEndpoint}
import com.testingzone.neo4j.handler.EventHandler
import com.testingzone.neo4j.publisher.KafkaSimplePublisher
import com.testingzone.neo4j.repository.neo4j.Neo4jPersonRepository
import com.testingzone.neo4j.service.{KafkaService, PersonService}
import fs2.kafka._
import neotypes.GraphDatabase
import neotypes.cats.effect.implicits._
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import org.neo4j.driver.AuthTokens
import org.typelevel.log4cats.LoggerFactory
import org.typelevel.log4cats.slf4j.Slf4jFactory
import sttp.tapir.server.http4s.Http4sServerInterpreter


object Main extends IOApp {

  type F[A] = cats.effect.IO[A]

  implicit val loggerFactory: LoggerFactory[F] = Slf4jFactory[F]

  def run(args: List[String]): F[ExitCode] = {
    (for {
      neo4jConfig <- Neo4jConfig.build.resource[F]
      kafkaConfig <- KafkaConfig.build.resource[F]
      httpConfig = HttpConfig.default
      logger = loggerFactory.getLogger
      driver <- GraphDatabase.driver[F](s"bolt://${neo4jConfig.host}:${neo4jConfig.port}", AuthTokens.basic(neo4jConfig.user, neo4jConfig.password))
      producer <- KafkaProducer.resource(ProducerSettings[F, String, String].withBootstrapServers(s"${kafkaConfig.host}:${kafkaConfig.port}"))
      publisher = new KafkaSimplePublisher[F](producer, kafkaConfig.testTopic)
      personRepository = new Neo4jPersonRepository[F](driver)
      personService = new PersonService[F](personRepository)
      kafkaService = new KafkaService[F](publisher)
      personEndpoint = new PersonEndpoint[F](personService)
      kafkaEndpoint = new KafkaEndpoint[F](kafkaService)
      _ <- Resource.eval(logger.info(s"Starting up http4s blaze"))
      routes = Http4sServerInterpreter[F]().toRoutes(List(personEndpoint.getPersonsEndpoint, kafkaEndpoint.postToKafkaEndpoint))
      _ <- BlazeServerBuilder[F]
        .withExecutionContext(scala.concurrent.ExecutionContext.Implicits.global)
        .bindHttp(httpConfig.port, httpConfig.host)
        .withHttpApp(Router("/" -> routes).orNotFound)
        .resource
      consumer <- KafkaConsumer.resource(ConsumerSettings[F, String, String]
        .withAutoOffsetReset(AutoOffsetReset.Earliest)
        .withBootstrapServers(s"${kafkaConfig.host}:${kafkaConfig.port}")
        .withGroupId(kafkaConfig.consumerGroup))
    } yield (logger, kafkaConfig, consumer)).use {
      case (logger, kafkaConfig, consumer) =>
        (for {
          _ <- consumer.subscribeTo(kafkaConfig.testTopic)
          _ <- logger.info(s"Subscribed to Kafka topic [${kafkaConfig.testTopic}]")
          handler = new EventHandler[F]
          _ <- consumer.stream
            .mapAsync(20) { msg => handler.handle(msg.record).as(msg.offset) }
            .through(commitBatchWithin(kafkaConfig.offsetConfig.batchSize, kafkaConfig.offsetConfig.interval))
            .compile
            .drain
        } yield ()).as(ExitCode.Success)
    }
  }
}
