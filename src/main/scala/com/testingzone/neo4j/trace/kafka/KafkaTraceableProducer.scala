package com.testingzone.neo4j.trace.kafka

import cats.FlatMap
import cats.effect.Resource
import cats.effect.kernel.Async
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.testingzone.neo4j.trace.Trace
import com.testingzone.neo4j.trace.Trace.TraceId
import com.testingzone.neo4j.trace.kafka.KafkaTraceableProducer.headerName
import fs2.kafka.{Header, KafkaProducer, ProducerRecords, ProducerResult, ProducerSettings}

class KafkaTraceableProducer[F[_] : Trace : FlatMap, K, V](delegate: KafkaProducer[F, K, V]) extends KafkaProducer[F, K, V] {

  override def produce(records: ProducerRecords[K, V]): F[F[ProducerResult[K, V]]] = {
    for {
      traceId <- Trace[F].traceId
      recordsWithTrace = traceableRecords(records, traceId)
      result <- delegate.produce(recordsWithTrace)
    } yield result
  }

  private def traceableRecords(records: ProducerRecords[K, V], traceId: TraceId): ProducerRecords[K, V] =
    records.map(record => record.withHeaders(record.headers.append(Header(headerName, traceId.value))))
}

object KafkaTraceableProducer {
  val headerName = "trace.id"

  def apply[F[_] : Trace : FlatMap, K, V](producer: KafkaProducer[F, K, V]) = new KafkaTraceableProducer[F, K, V](producer)

  def resource[F[_] : Async : Trace, K, V](settings: ProducerSettings[F, K, V]): Resource[F, KafkaProducer[F, K, V]] =
    KafkaProducer.resource(settings).map(KafkaTraceableProducer.apply[F, K, V])
}

