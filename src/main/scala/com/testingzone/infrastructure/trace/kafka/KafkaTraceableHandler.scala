package com.testingzone.infrastructure.trace.kafka

import cats.effect.MonadCancel
import com.testingzone.infrastructure.kafka.EventHandler
import com.testingzone.infrastructure.trace.Trace.TraceId
import com.testingzone.infrastructure.trace.TraceProvider
import com.testingzone.infrastructure.trace.kafka.KafkaTraceableProducer.headerName
import fs2.kafka.ConsumerRecord

class KafkaTraceableHandler[F[_] : TraceProvider : MonadCancel[*[_], Throwable], K, V](delegate: EventHandler[F, K, V]) extends EventHandler[F, K, V] {

  private def maybeTraceId(record: ConsumerRecord[K, V]): Option[TraceId] = record.headers
    .withKey(headerName)
    .map(_.as[String])
    .map(TraceId.apply)

  override def handle(record: ConsumerRecord[K, V]): F[Unit] =
    TraceProvider[F].resource(maybeTraceId(record)).use(_ => delegate.handle(record))
}

object KafkaTraceableHandler {

  def apply[F[_] : TraceProvider : MonadCancel[*[_], Throwable], K, V](delegate: EventHandler[F, K, V]): KafkaTraceableHandler[F, K, V] =
    new KafkaTraceableHandler(delegate)
}