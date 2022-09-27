package com.testingzone.neo4j.handler

import cats.effect.IO
import fs2.kafka.ConsumerRecord
import org.typelevel.log4cats.Logger

class EventHandler(logger: Logger[IO]) {

  def handle(record: ConsumerRecord[String, String]): IO[Unit] =
    logger.info(s"Processing record on partition ${record.partition} with offset=${record.offset}. Key=${record.key}. Value=${record.value}")
}
