package com.testingzone.neo4j

import scala.util.Try

object NewMain extends App {

  case class PositionPercentage(value: Int) extends AnyVal
  case class TotalParticipants(value: Long) extends AnyVal
  case class Position(value: Long) extends AnyVal

  def calculate(position: Position, totalParticipants: TotalParticipants): Option[PositionPercentage] =
    Try(position.value * 100d / totalParticipants.value)
      .map(value => PositionPercentage(value.ceil.toInt))
      .toOption

  println(calculate(Position(13), TotalParticipants(2467)))
}
