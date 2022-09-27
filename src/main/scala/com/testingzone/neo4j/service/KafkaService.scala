package com.testingzone.neo4j.service

import cats.effect.IO
import com.testingzone.neo4j.publisher.SimplePublisher
import org.typelevel.log4cats.slf4j.Slf4jLogger

class KafkaService(publisher: SimplePublisher) {

  def publish(key: String, value: String): IO[Unit] =
    for {
      logger <- Slf4jLogger.create[IO]
      _ <- logger.info(s"Publishing Key=[$key] Value=[$value] to Kafka")
      result <- publisher.publish(key, value)
    } yield result
}
