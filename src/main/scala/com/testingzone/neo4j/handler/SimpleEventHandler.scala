package com.testingzone.neo4j.handler

import cats.FlatMap
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.testingzone.neo4j.service.TraceService
import com.testingzone.neo4j.trace.Trace
import fs2.kafka.ConsumerRecord
import org.typelevel.log4cats.syntax._
import org.typelevel.log4cats.{Logger, LoggerFactory}

class SimpleEventHandler[F[_] : Trace : FlatMap : LoggerFactory](traceService: TraceService[F]) extends EventHandler[F, String, String] {

  private implicit val logger: Logger[F] = LoggerFactory[F].getLogger

  override def handle(record: ConsumerRecord[String, String]): F[Unit] =
    for {
      traceId <- Trace[F].traceId
      _ <- info"${traceId.value}.  Processing record on partition ${record.partition} with offset=${record.offset}. Key=${record.key}. Value=${record.value}"
      done <- traceService.test
    } yield done
}