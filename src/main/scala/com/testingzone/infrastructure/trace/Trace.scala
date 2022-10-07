package com.testingzone.infrastructure.trace

import com.testingzone.infrastructure.trace.Trace.TraceId

trait Trace[F[_]] {

  def traceId: F[TraceId]
}

object Trace {

  def apply[F[_] : Trace]: Trace[F] = implicitly

  case class TraceId(value: String) extends AnyVal
}
