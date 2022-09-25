package com.testingzone.neo4j.config

import ciris.{ConfigValue, Effect, env}
import cats.syntax.parallel._

case class Neo4jConfig(host: String, port: Int, user: String, password: String)

object Neo4jConfig {

  def build: ConfigValue[Effect, Neo4jConfig] = (
    env("neo4j_host").as[String],
    env("neo4j_port").as[Int],
    env("neo4j_user").as[String],
    env("neo4j_password").as[String],
    ).parMapN(Neo4jConfig.apply)
}
