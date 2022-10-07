package com.testingzone.services.neo4j.service

import cats.FlatMap
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.testingzone.infrastructure.trace.Trace
import com.testingzone.services.neo4j.publisher.SimplePublisher
import org.typelevel.log4cats.{Logger, LoggerFactory}

class KafkaService[F[_] : Trace : LoggerFactory : FlatMap](publisher: SimplePublisher[F]) {

  private implicit val logger: Logger[F] = LoggerFactory[F].getLogger

  def publish(key: String, value: String): F[Unit] =
    for {
      traceId <- Trace[F].traceId
      _ <- logger.info(s"${traceId.value}. Publishing Key=[$key] Value=[$value] to Kafka")
      result <- publisher.publish(key, value)
    } yield result
}
