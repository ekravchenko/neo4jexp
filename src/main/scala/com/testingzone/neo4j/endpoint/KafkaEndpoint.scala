package com.testingzone.neo4j.endpoint

import cats.Functor
import cats.syntax.either._
import cats.syntax.functor._
import com.testingzone.neo4j.service.KafkaService
import sttp.tapir.{emptyOutput, endpoint, query}

//noinspection TypeAnnotation
class KafkaEndpoint[F[_] : Functor](service: KafkaService[F]) {

  val postToKafkaApi = endpoint.post
    .in("kafka")
    .in(query[String]("key"))
    .in(query[String]("value"))
    .out(emptyOutput)

  val postToKafkaEndpoint = postToKafkaApi.serverLogic[F] { case (key, value) => service.publish(key, value).map(_.asRight) }
}
