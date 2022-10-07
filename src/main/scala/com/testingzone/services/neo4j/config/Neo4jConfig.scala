package com.testingzone.services.neo4j.config

import ciris.{ConfigValue, Effect, env}
import cats.syntax.parallel._

case class Neo4jConfig(host: String, port: Int, user: String, password: String)

object Neo4jConfig {

  def build: ConfigValue[Effect, Neo4jConfig] = (
    env("neo4j_host").as[String].default("localhost"),
    env("neo4j_port").as[Int].default(7687),
    env("neo4j_user").as[String].default("neo4j"),
    env("neo4j_password").as[String].default("test"),
    ).parMapN(Neo4jConfig.apply)
}
