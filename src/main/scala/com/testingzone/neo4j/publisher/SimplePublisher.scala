package com.testingzone.neo4j.publisher

import cats.effect.IO

trait SimplePublisher {

  def publish(key: String, value: String): IO[Unit]
}
