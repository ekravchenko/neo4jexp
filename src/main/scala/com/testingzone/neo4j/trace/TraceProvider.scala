package com.testingzone.neo4j.trace

import cats.effect.{Resource, Sync}
import com.testingzone.neo4j.trace.Trace.TraceId

import java.util.UUID

trait TraceProvider[F[_]] {

  def resource(traceId: Option[TraceId]): Resource[F, TraceId]
}

object TraceProvider {

  def apply[F[_] : TraceProvider]: TraceProvider[F] = implicitly

  def generate[F[_] : Sync]: F[TraceId] = Sync[F].delay(TraceId(UUID.randomUUID().toString))
}
