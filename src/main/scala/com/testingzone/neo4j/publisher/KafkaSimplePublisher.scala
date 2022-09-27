package com.testingzone.neo4j.publisher

import cats.effect.IO
import fs2.kafka.{KafkaProducer, ProducerRecord, ProducerRecords}

class KafkaSimplePublisher(producer: KafkaProducer[IO, String, String], topic: String) extends SimplePublisher {

  override def publish(key: String, value: String): IO[Unit] =
    producer.produce(ProducerRecords.one(ProducerRecord(topic, key, value))).void
}
