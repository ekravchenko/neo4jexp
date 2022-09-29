package com.testingzone.neo4j.publisher

trait SimplePublisher[F[_]] {

  def publish(key: String, value: String): F[Unit]
}
