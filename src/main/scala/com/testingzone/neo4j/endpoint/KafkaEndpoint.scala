package com.testingzone.neo4j.endpoint

import cats.FlatMap
import cats.syntax.either._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.testingzone.neo4j.service.KafkaService
import com.testingzone.neo4j.trace.Trace
import org.typelevel.log4cats.syntax._
import org.typelevel.log4cats.{Logger, LoggerFactory}
import sttp.tapir.{emptyOutput, endpoint, query}

//noinspection TypeAnnotation
class KafkaEndpoint[F[_] : Trace : FlatMap : LoggerFactory](service: KafkaService[F]) {

  private implicit val logger: Logger[F] = LoggerFactory.getLogger[F]

  val postToKafkaApi = endpoint.post
    .in("kafka")
    .in(query[String]("key"))
    .in(query[String]("value"))
    .out(emptyOutput)

  val postToKafkaEndpoint = postToKafkaApi.serverLogic[F] { case (key, value) =>
    for {
      traceId <- Trace[F].traceId
      _ <- info"${traceId.value}. /POST kafka"
      _ <- service.publish(key, value)
      _ <- info"${traceId.value}. -----------"
    } yield ().asRight
  }
}

