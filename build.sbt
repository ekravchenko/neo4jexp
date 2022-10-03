ThisBuild / organization := "com.testingzone"
ThisBuild / scalaVersion := "2.13.8"

val dependencies = Seq(
  "org.typelevel" %% "cats-effect" % "3.3.14",
  "is.cir" %% "ciris" % "2.4.0",
  "org.typelevel" %% "log4cats-slf4j" % "2.5.0",
  "ch.qos.logback" % "logback-classic" % "1.4.1",
  "io.github.neotypes" %% "neotypes-cats-effect" % "0.22.0",
  "io.github.neotypes" %% "neotypes-generic" % "0.22.0",
  "org.neo4j.driver" % "neo4j-java-driver" % "4.4.9",
  "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % "1.1.1",
  "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % "1.1.1",
  "org.http4s" %% "http4s-blaze-server" % "0.23.12",
  "com.github.fd4s" %% "fs2-kafka" % "3.0.0-M9"
)

addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full)
addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")

lazy val root = (project in file(".")).enablePlugins(NativeImagePlugin).settings(
  name := "neo4jexp",
  libraryDependencies ++= dependencies,
  Compile / mainClass := Some("com.testingzone.neo4j.Main"),
  nativeImageOptions += "--no-fallback",
  nativeImageOptions += "-H:IncludeResources=logback.xml",
  nativeImageOptions += s"-H:ReflectionConfigurationFiles=${baseDirectory.value}/reflection/logback.json,${baseDirectory.value}/reflection/kafka.json",
  nativeImageVersion := "22.1.0"
)