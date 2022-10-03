package com.testingzone.neo4j.handler

import fs2.kafka.ConsumerRecord

trait EventHandler[F[_], K, V] {

  def handle(record: ConsumerRecord[K, V]): F[Unit]
}
