package com.testingzone.infrastructure.kafka

import fs2.kafka.ConsumerRecord

trait EventHandler[F[_], K, V] {

  def handle(record: ConsumerRecord[K, V]): F[Unit]
}
