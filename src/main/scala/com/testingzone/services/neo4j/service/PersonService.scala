package com.testingzone.services.neo4j.service

import cats.FlatMap
import cats.implicits.showInterpolator
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.testingzone.infrastructure.trace.Trace
import com.testingzone.services.neo4j.domain.Person
import com.testingzone.services.neo4j.repository.PersonRepository
import org.typelevel.log4cats.{Logger, LoggerFactory}

class PersonService[F[_] : Trace : LoggerFactory : FlatMap](repository: PersonRepository[F]) {

  private implicit val logger: Logger[F] = LoggerFactory[F].getLogger

  def findAll(): F[Vector[Person]] =
    for {
      traceId <- Trace[F].traceId
      _ <- logger.info(s"${traceId.value}. Find all persons...")
      result <- repository.findAll()
      _ <- logger.info(show"${traceId.value}. Result: [$result]")
    } yield result
}
