package com.testingzone.neo4j.service

import cats.FlatMap
import cats.implicits.showInterpolator
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.testingzone.neo4j.domain.Person
import com.testingzone.neo4j.repository.PersonRepository
import org.typelevel.log4cats.{Logger, LoggerFactory}

class PersonService[F[_] : LoggerFactory : FlatMap](repository: PersonRepository[F]) {

  private implicit val logger: Logger[F] = LoggerFactory[F].getLogger

  def findAll(): F[Vector[Person]] =
    for {
      _ <- logger.info("Find all persons...")
      result <- repository.findAll()
      _ <- logger.info(show"Result: [$result]")
    } yield result
}
