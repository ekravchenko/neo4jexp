package com.testingzone.neo4j

import cats.effect.{IO, IOApp}
import cats.implicits.showInterpolator
import com.testingzone.neo4j.config.Neo4jConfig
import com.testingzone.neo4j.repository.neo4j.Neo4jPersonRepository
import neotypes.cats.effect.implicits._
import neotypes.{Driver, GraphDatabase}
import org.neo4j.driver.AuthTokens
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Main extends IOApp.Simple {

  override def run: IO[Unit] = {
    for {
      logger <- Slf4jLogger.create[IO]
      config <- Neo4jConfig.build.load[IO]
      _ <- logger.info(s"Connecting to ${config.host}:${config.port} with ${config.user}/${config.password}")
      done <- GraphDatabase.driver[IO](s"bolt://${config.host}:${config.port}", AuthTokens.basic(config.user, config.password))
        .use(testMe(_, logger))
    } yield done
  }

  private def testMe(driver: Driver[IO], logger: Logger[IO]): IO[Unit] = for {
    _ <- logger.info("Executing query to find Person(s)")
    repository = new Neo4jPersonRepository(driver)
    persons <- repository.findAll()
    done <- logger.info(show"Found values => $persons")
  } yield done
}
