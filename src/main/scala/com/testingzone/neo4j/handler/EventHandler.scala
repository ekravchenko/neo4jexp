package com.testingzone.neo4j.handler

import fs2.kafka.ConsumerRecord
import org.typelevel.log4cats.syntax._
import org.typelevel.log4cats.{Logger, LoggerFactory}

class EventHandler[F[_] : LoggerFactory] {

  private implicit val logger: Logger[F] = LoggerFactory[F].getLogger

  def handle(record: ConsumerRecord[String, String]): F[Unit] =
    info"Processing record on partition ${record.partition} with offset=${record.offset}. Key=${record.key}. Value=${record.value}"
}
