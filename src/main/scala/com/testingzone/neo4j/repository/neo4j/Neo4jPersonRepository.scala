package com.testingzone.neo4j.repository.neo4j

import com.testingzone.neo4j.domain.Person
import com.testingzone.neo4j.repository.PersonRepository
import com.testingzone.neo4j.repository.neo4j.Mappers._
import neotypes.Driver
import neotypes.implicits.syntax.string._

class Neo4jPersonRepository[F[_]](driver: Driver[F]) extends PersonRepository[F] {

  override def findAll(): F[Vector[Person]] =
    "MATCH (p: Person) RETURN p"
      .query[Person]
      .vector(driver)
}
