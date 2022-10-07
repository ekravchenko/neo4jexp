package com.testingzone.services.neo4j.config

import cats.syntax.parallel._
import ciris.{ConfigValue, Effect, env}

import scala.concurrent.duration.{DurationInt, FiniteDuration}

case class KafkaConfig(host: String,
                       port: Int,
                       consumerGroup: String,
                       offsetConfig: OffsetCommitConfig,
                       testTopic: String)

case class OffsetCommitConfig(batchSize: Int, interval: FiniteDuration)

object KafkaConfig {

  def apply(host: String, port: Int): KafkaConfig = new KafkaConfig(
    host = host,
    port = port,
    consumerGroup = "testConsumer",
    offsetConfig = OffsetCommitConfig(50, 10.seconds),
    testTopic = "test"
  )

  def build: ConfigValue[Effect, KafkaConfig] = (
    env("kafka_host").as[String].default("localhost"),
    env("kafka_port").as[Int].default(29092))
    .parMapN(KafkaConfig.apply)
}