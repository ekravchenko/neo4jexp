package com.testingzone.neo4j

import cats.effect.{ExitCode, IO, IOApp, Resource}
import com.testingzone.neo4j.config.{HttpConfig, Neo4jConfig}
import com.testingzone.neo4j.endpoint.PersonEndpoint
import com.testingzone.neo4j.repository.neo4j.Neo4jPersonRepository
import com.testingzone.neo4j.service.PersonService
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
      httpConfig = HttpConfig.default
      logger <- Resource.eval(Slf4jLogger.create[IO])
      driver <- GraphDatabase.driver[IO](s"bolt://${neo4jConfig.host}:${neo4jConfig.port}", AuthTokens.basic(neo4jConfig.user, neo4jConfig.password))
      repository = new Neo4jPersonRepository(driver)
      service = new PersonService(repository)
      endpoint = new PersonEndpoint(service)
      _ <- Resource.eval(logger.info(s"Starting up http4s blaze"))
      routes = Http4sServerInterpreter[IO]().toRoutes(endpoint.getPersonsEndpoint)
      server <- BlazeServerBuilder[IO]
        .withExecutionContext(scala.concurrent.ExecutionContext.Implicits.global)
        .bindHttp(httpConfig.port, httpConfig.host)
        .withHttpApp(Router("/" -> routes).orNotFound)
        .resource
    } yield (logger, server)).use {
      case (logger, _) =>
        logger.info("Server started successfully!") >> IO.never
    }
  }
}
