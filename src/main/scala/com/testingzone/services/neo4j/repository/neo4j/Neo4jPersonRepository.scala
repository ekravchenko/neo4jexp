package com.testingzone.services.neo4j.repository.neo4j

import cats.FlatMap
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.testingzone.infrastructure.trace.Trace
import com.testingzone.services.neo4j.domain.Person
import com.testingzone.services.neo4j.repository.PersonRepository
import com.testingzone.services.neo4j.repository.neo4j.Mappers._
import neotypes.Driver
import neotypes.implicits.syntax.string._
import org.typelevel.log4cats.syntax._
import org.typelevel.log4cats.{Logger, LoggerFactory}

class Neo4jPersonRepository[F[_] : LoggerFactory : Trace : FlatMap](driver: Driver[F]) extends PersonRepository[F] {

  private implicit val logger: Logger[F] = LoggerFactory.getLogger[F]

  override def findAll(): F[Vector[Person]] = {
    for {
      traceId <- Trace[F].traceId
      _ <- info"${traceId.value}. Fetching persons from neo4j"
      result <- "MATCH (p: Person) RETURN p"
        .query[Person]
        .vector(driver)
    } yield result
  }
}
