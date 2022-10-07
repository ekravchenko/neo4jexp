package com.testingzone.services.neo4j.publisher

import cats.Functor
import cats.syntax.functor._
import fs2.kafka.{KafkaProducer, ProducerRecord, ProducerRecords}

class KafkaSimplePublisher[F[_] : Functor](producer: KafkaProducer[F, String, String], topic: String) extends SimplePublisher[F] {

  override def publish(key: String, value: String): F[Unit] =
    producer.produce(ProducerRecords.one(ProducerRecord(topic, key, value))).void
}
