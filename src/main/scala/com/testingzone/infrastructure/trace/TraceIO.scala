package com.testingzone.infrastructure.trace

import cats.data.OptionT
import cats.effect.{IO, IOLocal, Resource}
import cats.syntax.option._
import com.testingzone.infrastructure.trace.Trace.TraceId

class TraceIO(local: IOLocal[Option[TraceId]]) extends Trace[IO] with TraceProvider[IO] {

  private def generateNewTraceId: IO[TraceId] =
    for {
      traceId <- TraceProvider.generate[IO]
      _ <- local.set(traceId.some)
    } yield traceId

  private def setTraceId(maybeTraceId: Option[TraceId]): IO[TraceId] =
    OptionT(local.set(maybeTraceId).as(maybeTraceId)).getOrElseF(generateNewTraceId)

  private def clear: IO[Unit] = local.reset

  override def traceId: IO[Trace.TraceId] = OptionT(local.get).getOrElseF(generateNewTraceId)

  override def resource(maybeTraceId: Option[TraceId]): Resource[IO, TraceId] =
    Resource.make(setTraceId(maybeTraceId))(_ => clear)
}

object TraceIO {

  def apply(local: IOLocal[Option[TraceId]]): TraceIO = new TraceIO(local)

  def resource: Resource[IO, TraceIO] = Resource.eval(IOLocal(none[TraceId]).map(TraceIO.apply))
}