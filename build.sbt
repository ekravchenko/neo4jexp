ThisBuild / organization := "com.testingzone"
ThisBuild / scalaVersion := "2.13.8"

val dependencies = Seq(
  "org.typelevel" %% "cats-effect" % "3.3.14",
  "is.cir" %% "ciris" % "2.4.0",
  "org.typelevel" %% "log4cats-slf4j" % "2.5.0",
  "org.slf4j" % "slf4j-jdk14" % "2.0.2"
)

lazy val root = (project in file(".")).enablePlugins(NativeImagePlugin).settings(
  name := "neo4jexp",
  libraryDependencies ++= dependencies,
  Compile / mainClass := Some("com.testingzone.neo4j.Main"),
  nativeImageOptions += "--no-fallback",
  nativeImageVersion := "22.1.0"
)