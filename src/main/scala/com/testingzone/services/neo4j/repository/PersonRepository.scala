package com.testingzone.services.neo4j.repository

import com.testingzone.services.neo4j.domain.Person

trait PersonRepository[F[_]] {

  def findAll(): F[Vector[Person]]
}
