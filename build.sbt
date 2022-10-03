ThisBuild / organization := "com.testingzone"
ThisBuild / scalaVersion := "2.13.8"

conflictManager := ConflictManager.strict

val excludeFs2Core = ExclusionRule(organization = "co.fs2", name = "fs2-core_2.13")
val excludeFs2Io = ExclusionRule(organization = "co.fs2", name = "fs2-io_2.13")
val excludeCatsEffect = ExclusionRule(organization = "org.typelevel", name = "cats-effect_2.13")
val excludeCatsEffectKernel = ExclusionRule(organization = "org.typelevel", name = "cats-effect-kernel_2.13")
val excludeCatsEffectStd = ExclusionRule(organization = "org.typelevel", name="cats-effect-std_2.13")
val excludeCatsCore = ExclusionRule(organization = "org.typelevel", name = "cats-core_2.13")
val excludeSl4jApi = ExclusionRule(organization = "org.slf4j", name = "slf4j-api")
val excludeHttp4sServer = ExclusionRule(organization = "org.http4s", name = "http4s-server_2.13")
val excludeHttp4sCore = ExclusionRule(organization = "org.http4s", name = "http4s-core_2.13")
val excludeSttpModelCore = ExclusionRule(organization = "com.softwaremill.sttp.model", name = "core_2.13")
val excludeShapeless = ExclusionRule(organization = "com.chuusai", name = "shapeless_2.13")

val dependencies = Seq(
  // Cats
  "org.typelevel" %% "cats-core" % "2.8.0",
  "org.typelevel" %% "cats-effect" % "3.3.14" excludeAll excludeCatsCore,

  // Fs2 + Kafka
  "co.fs2" %% "fs2-core" % "3.3.0" excludeAll(excludeCatsCore, excludeCatsEffect),
  "co.fs2" %% "fs2-io" % "3.3.0" excludeAll(excludeCatsCore, excludeCatsEffect),
  "com.github.fd4s" %% "fs2-kafka" % "3.0.0-M9" excludeAll(excludeFs2Core, excludeCatsEffect, excludeSl4jApi),

  // Ciris config
  "is.cir" %% "ciris" % "2.4.0" excludeAll(
    excludeCatsCore,
    excludeCatsEffect,
    excludeCatsEffectKernel),

  // Logs
  "org.typelevel" %% "log4cats-slf4j" % "2.5.0" excludeAll(
    excludeCatsCore,
    excludeCatsEffect,
    excludeCatsEffectStd,
    excludeSl4jApi),
  "ch.qos.logback" % "logback-classic" % "1.4.1" excludeAll excludeSl4jApi,
  "org.slf4j" % "slf4j-api" % "2.0.3",

  // Neotypes
  "io.github.neotypes" %% "neotypes-cats-effect" % "0.22.0",
  "io.github.neotypes" %% "neotypes-generic" % "0.22.0" excludeAll excludeShapeless,
  "com.chuusai" %% "shapeless" % "2.3.10",
  //noinspection SbtDependencyVersionInspection - (compliant with neotypes version)
  "org.neo4j.driver" % "neo4j-java-driver" % "4.4.9",

  // Tapir + http4s
  "com.softwaremill.sttp.model" %% "core" % "1.5.2",
  "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % "1.1.2" excludeAll(
    excludeFs2Core,
    excludeFs2Io,
    excludeCatsCore,
    excludeCatsEffect,
    excludeSl4jApi,
    excludeSttpModelCore),
  "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % "1.1.2" excludeAll(
    excludeCatsCore,
    excludeCatsEffect,
    excludeShapeless,
    excludeSttpModelCore),
  "org.http4s" %% "http4s-blaze-server" % "0.23.12" excludeAll(
    excludeHttp4sCore,
    excludeHttp4sServer,
    excludeSl4jApi)
)

addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full)
addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")

//noinspection ScalaUnusedSymbol
lazy val root = (project in file(".")).enablePlugins(NativeImagePlugin).settings(
  name := "neo4jexp",
  libraryDependencies ++= dependencies,
  Compile / mainClass := Some("com.testingzone.neo4j.Main"),
  nativeImageOptions += "--no-fallback",
  nativeImageOptions += "-H:IncludeResources=logback.xml",
  nativeImageOptions += s"-H:ReflectionConfigurationFiles=${baseDirectory.value}/reflection/logback.json,${baseDirectory.value}/reflection/kafka.json",
  nativeImageVersion := "22.1.0"
)