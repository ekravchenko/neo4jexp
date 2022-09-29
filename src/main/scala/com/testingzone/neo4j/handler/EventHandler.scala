package com.testingzone.neo4j.handler

import cats.effect.Sync
import cats.syntax.flatMap._
import cats.syntax.functor._
import fs2.kafka.ConsumerRecord
import org.typelevel.log4cats.slf4j.Slf4jLogger

class EventHandler[F[_] : Sync] {

  def handle(record: ConsumerRecord[String, String]): F[Unit] = {
    for {
      logger <- Slf4jLogger.create[F]
      _ <- logger.info(s"Processing record on partition ${record.partition} with offset=${record.offset}. Key=${record.key}. Value=${record.value}")
    } yield ()
  }
}
