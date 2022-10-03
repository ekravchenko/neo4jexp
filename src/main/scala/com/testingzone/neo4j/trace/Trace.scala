package com.testingzone.neo4j.trace

import com.testingzone.neo4j.trace.Trace.TraceId

trait Trace[F[_]] {

  def traceId: F[TraceId]
}

object Trace {

  def apply[F[_] : Trace]: Trace[F] = implicitly

  case class TraceId(value: String) extends AnyVal
}
