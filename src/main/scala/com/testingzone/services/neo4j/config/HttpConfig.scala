package com.testingzone.services.neo4j.config

case class HttpConfig(host: String, port: Int)

object HttpConfig {

  def default: HttpConfig = HttpConfig("0.0.0.0", 8090)
}
