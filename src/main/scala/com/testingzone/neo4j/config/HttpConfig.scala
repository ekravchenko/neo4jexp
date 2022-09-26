package com.testingzone.neo4j.config

case class HttpConfig(host: String, port: Int)

object HttpConfig {

  def default: HttpConfig = HttpConfig("localhost", 8090)
}
