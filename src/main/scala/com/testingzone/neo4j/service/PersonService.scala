package com.testingzone.neo4j.service

import cats.effect.IO
import cats.implicits.showInterpolator
import com.testingzone.neo4j.domain.Person
import com.testingzone.neo4j.repository.PersonRepository
import org.typelevel.log4cats.slf4j.Slf4jLogger

class PersonService(repository: PersonRepository) {

  def findAll(): IO[Vector[Person]] =
    for {
      logger <- Slf4jLogger.create[IO]
      _ <- logger.info("Find all persons...")
      result <- repository.findAll()
      _ <- logger.info(show"Result: [$result]")
    } yield result
}
