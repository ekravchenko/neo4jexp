package com.testingzone.services.neo4j.endpoint

import cats.FlatMap
import cats.syntax.either._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.testingzone.infrastructure.trace.Trace
import com.testingzone.services.neo4j.endpoint.json.PersonResponse.PersonVectorOps
import com.testingzone.services.neo4j.endpoint.json.PersonResponse
import com.testingzone.services.neo4j.service.PersonService
import io.circe.generic.auto._
import org.typelevel.log4cats.syntax._
import org.typelevel.log4cats.{Logger, LoggerFactory}
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._

//noinspection TypeAnnotation
class PersonEndpoint[F[_] : Trace : FlatMap : LoggerFactory](service: PersonService[F]) {

  private implicit val logger: Logger[F] = LoggerFactory.getLogger[F]

  val getPersonsApi = endpoint
    .get
    .in("persons")
    .out(jsonBody[Vector[PersonResponse]])

  val getPersonsEndpoint =
    getPersonsApi.serverLogic[F] { _ =>
      for {
        traceId <- Trace[F].traceId
        _ <- info"${traceId.value}. GET /persons"
        result <- service.findAll()
      } yield result.toResponse.asRight
    }
}
