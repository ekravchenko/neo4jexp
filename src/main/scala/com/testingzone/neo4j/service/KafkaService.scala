package com.testingzone.neo4j.service

import cats.FlatMap
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.testingzone.neo4j.publisher.SimplePublisher
import org.typelevel.log4cats.{Logger, LoggerFactory}

class KafkaService[F[_] : LoggerFactory : FlatMap](publisher: SimplePublisher[F]) {

  implicit val logger: Logger[F] = LoggerFactory[F].getLogger

  def publish(key: String, value: String): F[Unit] =
    for {
      _ <- logger.info("Publishing Key=[$key] Value=[$value] to Kafka")
      result <- publisher.publish(key, value)
    } yield result
}
