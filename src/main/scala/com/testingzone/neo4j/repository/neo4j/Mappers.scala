package com.testingzone.neo4j.repository.neo4j

import com.testingzone.neo4j.domain.Person
import neotypes.generic.semiauto
import neotypes.mappers.{ResultMapper, ValueMapper}

object Mappers {
  implicit val personName:ValueMapper[Person.Name] = semiauto.deriveUnwrappedValueMapper
  implicit val personSurname: ValueMapper[Person.Surname] = semiauto.deriveUnwrappedValueMapper
  implicit val personResultMapper: ResultMapper[Person] = semiauto.deriveProductResultMapper[Person]
}
