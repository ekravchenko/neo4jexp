package com.testingzone.neo4j.endpoint

import cats.effect.IO
import cats.syntax.either._
import com.testingzone.neo4j.endpoint.json.PersonResponse
import com.testingzone.neo4j.endpoint.json.PersonResponse.PersonVectorOps
import com.testingzone.neo4j.service.PersonService
import io.circe.generic.auto._
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._

//noinspection TypeAnnotation
class PersonEndpoint(service: PersonService) {

  val getPersonsApi = endpoint
    .get
    .in("persons")
    .out(jsonBody[Vector[PersonResponse]])

  val getPersonsEndpoint =
    getPersonsApi.serverLogic[IO](_ => service.findAll().map(_.toResponse.asRight))
}
