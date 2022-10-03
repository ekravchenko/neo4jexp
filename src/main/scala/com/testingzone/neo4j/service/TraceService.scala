package com.testingzone.neo4j.service

import cats.FlatMap
import cats.effect.Async
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.testingzone.neo4j.trace.Trace
import com.testingzone.neo4j.trace.Trace.TraceId
import org.typelevel.log4cats.syntax._
import org.typelevel.log4cats.{Logger, LoggerFactory}

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

class TraceService[F[_] : Trace : FlatMap : LoggerFactory : Async] {

  private implicit val logger: Logger[F] = LoggerFactory[F].getLogger

  private val customExecutionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(2))

  def test: F[Unit] =
    for {
      traceId <- Trace[F].traceId
      _ <- testB(traceId)
      _ <- testC(traceId)
      _ <- testA(traceId)
    } yield ()

  def testA(traceId: TraceId): F[Unit] = info"${traceId.value}. Executing method A"

  def testB(traceId: TraceId): F[Unit] = Async[F].evalOn(info"${traceId.value}. Executing method B", customExecutionContext)

  def testC(traceId: TraceId): F[Unit] = Async[F].evalOn(info"${traceId.value}. Executing method C", customExecutionContext)
}
