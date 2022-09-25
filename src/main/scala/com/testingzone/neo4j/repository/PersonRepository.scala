package com.testingzone.neo4j.repository

import cats.effect.IO
import com.testingzone.neo4j.domain.Person

trait PersonRepository {

  def findAll(): IO[Vector[Person]]
}
