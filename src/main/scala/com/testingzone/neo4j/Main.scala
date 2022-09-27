package com.testingzone.neo4j

import cats.effect.{ExitCode, IO, IOApp, Resource}
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
import org.typelevel.log4cats.slf4j.Slf4jLogger
import sttp.tapir.server.http4s.Http4sServerInterpreter


object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    (for {
      neo4jConfig <- Neo4jConfig.build.resource[IO]
      kafkaConfig <- KafkaConfig.build.resource[IO]
      httpConfig = HttpConfig.default
      logger <- Resource.eval(Slf4jLogger.create[IO])
      driver <- GraphDatabase.driver[IO](s"bolt://${neo4jConfig.host}:${neo4jConfig.port}", AuthTokens.basic(neo4jConfig.user, neo4jConfig.password))
      producer <- KafkaProducer.resource(ProducerSettings[IO, String, String].withBootstrapServers(s"${kafkaConfig.host}:${kafkaConfig.port}"))
      publisher = new KafkaSimplePublisher(producer, kafkaConfig.testTopic)
      personRepository = new Neo4jPersonRepository(driver)
      personService = new PersonService(personRepository)
      kafkaService = new KafkaService(publisher)
      personEndpoint = new PersonEndpoint(personService)
      kafkaEndpoint = new KafkaEndpoint(kafkaService)
      _ <- Resource.eval(logger.info(s"Starting up http4s blaze"))
      routes = Http4sServerInterpreter[IO]().toRoutes(List(personEndpoint.getPersonsEndpoint, kafkaEndpoint.postToKafkaEndpoint))
      _ <- BlazeServerBuilder[IO]
        .withExecutionContext(scala.concurrent.ExecutionContext.Implicits.global)
        .bindHttp(httpConfig.port, httpConfig.host)
        .withHttpApp(Router("/" -> routes).orNotFound)
        .resource
      consumer <- KafkaConsumer.resource(ConsumerSettings[IO, String, String]
        .withAutoOffsetReset(AutoOffsetReset.Earliest)
        .withBootstrapServers(s"${kafkaConfig.host}:${kafkaConfig.port}")
        .withGroupId(kafkaConfig.consumerGroup))
      handler = new EventHandler(logger)
    } yield (logger, kafkaConfig, consumer, handler)).use {
      case (logger, kafkaConfig, consumer, handler) =>
        (for {
          _ <- consumer.subscribeTo(kafkaConfig.testTopic)
          _ <- logger.info(s"Subscribed to Kafka topic [${kafkaConfig.testTopic}]")
          _ <- consumer.stream
            .mapAsync(20) { msg => handler.handle(msg.record).as(msg.offset) }
            .through(commitBatchWithin(kafkaConfig.offsetConfig.batchSize, kafkaConfig.offsetConfig.interval))
            .compile
            .drain
        } yield ()).as(ExitCode.Success)
    }
  }
}
