package com.testingzone.neo4j.repository.neo4j

import cats.effect.IO
import com.testingzone.neo4j.domain.Person
import com.testingzone.neo4j.repository.PersonRepository
import com.testingzone.neo4j.repository.neo4j.Mappers._
import neotypes.Driver
import neotypes.implicits.syntax.string._

class Neo4jPersonRepository(driver: Driver[IO]) extends PersonRepository {

  override def findAll(): IO[Vector[Person]] =
    "MATCH (p: Person) RETURN p"
      .query[Person]
      .vector(driver)
}
