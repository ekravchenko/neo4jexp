package com.testingzone.neo4j.config

import ciris.{ConfigValue, Effect, env}
import cats.syntax.parallel._

import scala.concurrent.duration.{DurationInt, FiniteDuration}

case class KafkaConfig(host: String,
                       port: Int,
                       consumerGroup: String,
                       testTopic: TopicConfig,
                       offsetCommitConfig: OffsetCommitConfig)

case class TopicConfig(name: String, partitions: Int)

case class OffsetCommitConfig(batchSize: Int, interval: FiniteDuration)

object KafkaConfig {

  def apply(host: String, port: Int): KafkaConfig = new KafkaConfig(
    host = host,
    port = port,
    consumerGroup = "testConsumer",
    testTopic = TopicConfig("test", 5),
    offsetCommitConfig = OffsetCommitConfig(50, 10.seconds)
  )

  def build: ConfigValue[Effect, KafkaConfig] = (
    env("kafka_host").as[String],
    env("kafka_port").as[Int])
    .parMapN(KafkaConfig.apply)
}