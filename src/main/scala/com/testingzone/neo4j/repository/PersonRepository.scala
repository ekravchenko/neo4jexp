package com.testingzone.neo4j.repository

import com.testingzone.neo4j.domain.Person

trait PersonRepository[F[_]] {

  def findAll(): F[Vector[Person]]
}
