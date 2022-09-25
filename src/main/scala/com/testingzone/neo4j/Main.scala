package com.testingzone.neo4j

import cats.effect.{IO, IOApp}
import com.testingzone.neo4j.config.Neo4jConfig
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Main extends IOApp.Simple {

  override def run: IO[Unit] = {
    for {
      logger <- Slf4jLogger.create[IO]
      config <- Neo4jConfig.build.load[IO]
      _ <- logger.info(s"Connecting to ${config.host}:${config.port} with ${config.user}/${config.password}")
    } yield ()
  }
}
