ThisBuild / organization := "com.testingzone"
ThisBuild / scalaVersion := "2.13.8"

val dependencies = Seq(
  "org.typelevel" %% "cats-effect" % "3.3.14",
  "is.cir" %% "ciris" % "2.4.0",
  "org.typelevel" %% "log4cats-slf4j" % "2.5.0",
  "ch.qos.logback" % "logback-classic" % "1.4.1",
  "io.github.neotypes" %% "neotypes-cats-effect" % "0.22.0",
  "io.github.neotypes" %% "neotypes-generic" % "0.22.0",
  "org.neo4j.driver" % "neo4j-java-driver" % "4.4.9"
)

lazy val root = (project in file(".")).enablePlugins(NativeImagePlugin).settings(
  name := "neo4jexp",
  libraryDependencies ++= dependencies,
  Compile / mainClass := Some("com.testingzone.neo4j.Main"),
  nativeImageOptions += "--no-fallback",
  nativeImageVersion := "22.1.0"
)