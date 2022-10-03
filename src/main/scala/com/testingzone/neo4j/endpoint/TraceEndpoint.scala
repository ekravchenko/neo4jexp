package com.testingzone.neo4j.endpoint

import cats.FlatMap
import cats.syntax.either._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.testingzone.neo4j.service.TraceService
import com.testingzone.neo4j.trace.Trace
import org.typelevel.log4cats.syntax._
import org.typelevel.log4cats.{Logger, LoggerFactory}
import sttp.tapir.{emptyOutput, endpoint}

//noinspection TypeAnnotation
class TraceEndpoint[F[_] : Trace : FlatMap : LoggerFactory](traceService: TraceService[F]) {

  private implicit val logger: Logger[F] = LoggerFactory[F].getLogger

  val postSimpleTrace = endpoint.post
    .in("trace")
    .out(emptyOutput)

  val postSimpleTraceEndpoint = postSimpleTrace.serverLogic[F](_ =>
    for {
      traceId <- Trace[F].traceId
      _ <- info"${traceId.value}. POST /trace"
      result <- traceService.test
    } yield result.asRight
  )
}