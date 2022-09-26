package com.testingzone.neo4j.endpoint.json

import com.testingzone.neo4j.domain.Person

case class PersonResponse(name: String, surname: String)

object PersonResponse {

  def fromDomain(person: Person): PersonResponse =
    new PersonResponse(person.name.value, person.surname.value)

  implicit class PersonVectorOps(persons: Vector[Person]) {
    def toResponse: Vector[PersonResponse] = persons.map(PersonResponse.fromDomain)
  }
}