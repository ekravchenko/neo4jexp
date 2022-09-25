package com.testingzone.neo4j.domain

import cats.Show
import cats.implicits.toShow
import com.testingzone.neo4j.domain.Person._

case class Person(name: Name, surname: Surname)

object Person {

  case class Name(value: String) extends AnyVal

  case class Surname(value: String) extends AnyVal

  implicit val show: Show[Person] = Show.show(person => s"${person.name.value} ${person.surname.value}")

  implicit val showMany: Show[Vector[Person]] = Show.show(persons => persons.map(_.show).mkString(", "))
}
