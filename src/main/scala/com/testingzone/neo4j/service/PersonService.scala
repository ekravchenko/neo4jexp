package com.testingzone.neo4j.service

import cats.effect.IO
import com.testingzone.neo4j.domain.Person
import com.testingzone.neo4j.repository.PersonRepository

class PersonService(repository: PersonRepository) {

  def findAll(): IO[Vector[Person]] = repository.findAll()
}
