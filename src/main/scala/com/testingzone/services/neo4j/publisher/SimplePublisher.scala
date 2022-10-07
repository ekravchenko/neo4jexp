package com.testingzone.services.neo4j.publisher

trait SimplePublisher[F[_]] {

  def publish(key: String, value: String): F[Unit]
}
