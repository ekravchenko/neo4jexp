package com.testingzone.neo4j.endpoint

import cats.effect.IO
import cats.syntax.either._
import com.testingzone.neo4j.service.KafkaService
import sttp.tapir.{emptyOutput, endpoint, query}

//noinspection TypeAnnotation
class KafkaEndpoint(service: KafkaService) {

  val postToKafkaApi = endpoint.post
    .in("kafka")
    .in(query[String]("key"))
    .in(query[String]("value"))
    .out(emptyOutput)

  val postToKafkaEndpoint = postToKafkaApi.serverLogic[IO] { case (key, value) => service.publish(key, value).map(_.asRight) }
}
