scala_sources(
    name = "fs2",
    sources = ["package.scala"],
    dependencies = [
        "3rdparty/jvm/co/fs2:fs2-core",
        "3rdparty/jvm/co/fs2:fs2-io",
    ]
)

scala_sources(
    name = "kafka",
    sources = ["package.scala"],
    dependencies = [
        ":fs2",
        "3rdparty/jvm/com/github/fd4s:fs2-kafka",
    ]
)

scala_sources(
    name = "cats",
    sources = ["package.scala"],
    dependencies = [
        "3rdparty/jvm/org/typelevel:cats-core",
    ]
)

scala_sources(
    name = "cats-effect",
    sources = ["package.scala"],
    dependencies = [
        ":cats",
        "3rdparty/jvm/org/typelevel:cats-effect",
    ]
)

scala_sources(
    name = "log",
    sources = ["package.scala"],
    dependencies = [
        "3rdparty/jvm/org/typelevel:log4cats-slf4j",
        "3rdparty/jvm/ch/qos/logback:logback-classic",
    ]
)

scala_sources(
    name = "config",
    sources = ["package.scala"],
    dependencies = [
        "3rdparty/jvm/is/cir:ciris",
    ]
)

scala_sources(
    name = "neotypes",
    sources = ["package.scala"],
    dependencies = [
        "3rdparty/jvm/com/chuusai:shapeless",
        "3rdparty/jvm/org/neo4j/driver:neo4j-java-driver",
        "3rdparty/jvm/io/github/neotypes:neotypes-generic",
        "3rdparty/jvm/io/github/neotypes:neotypes-core",
        "3rdparty/jvm/io/github/neotypes:neotypes-cats-effect"
    ]
)

scala_sources(
    name = "http4s",
    sources = ["package.scala"],
    dependencies = [
        ":fs2",
        "3rdparty/jvm/org/http4s:http4s-core",
        "3rdparty/jvm/org/http4s:http4s-server",
        "3rdparty/jvm/org/http4s:http4s-blaze-server",
    ]
)

scala_sources(
    name = "tapir-core",
    sources = ["package.scala"],
    dependencies = [
        "3rdparty/jvm/com/softwaremill/sttp/model:core",
        "3rdparty/jvm/com/softwaremill/sttp/tapir:tapir-core",
    ]
)

scala_sources(
    name = "tapir-server",
    sources = ["package.scala"],
    dependencies = [
        ":tapir-core",
        "3rdparty/jvm/com/softwaremill/sttp/tapir:tapir-server",
    ]
)

scala_sources(
    name = "tapir-http4s",
    sources = ["package.scala"],
    dependencies = [
        ":http4s",
        ":tapir-server",
         "3rdparty/jvm/com/softwaremill/sttp/shared:fs2",
         "3rdparty/jvm/com/softwaremill/sttp/tapir:tapir-cats",
         "3rdparty/jvm/com/softwaremill/sttp/tapir:tapir-json-circe",
         "3rdparty/jvm/com/softwaremill/sttp/tapir:tapir-http4s-server",
    ]
)