scala_sources(
    sources=["**/*.scala"],
    dependencies=[
        "src/main/scala/com/testingzone/infrastructure/depends:log",
        "src/main/scala/com/testingzone/infrastructure/depends:config",
        "src/main/scala/com/testingzone/infrastructure/depends:cats",
        "src/main/scala/com/testingzone/infrastructure/depends:kafka",
        "src/main/scala/com/testingzone/infrastructure/depends:neotypes",
        "src/main/scala/com/testingzone/infrastructure/depends:tapir-http4s",
        "src/main/scala/com/testingzone/infrastructure/trace:trace",
    ],
)

deploy_jar(
    name="bin",
    main="com.testingzone.services.neo4j.Main",
    dependencies=[
        "src/main/resources:logback",
        ":neo4j",
    ],
)

docker_image(
  name="neo4j-jvm",
  dependencies=[":bin"],
  source="Dockerfile.jvm"
)

docker_image(
  name="neo4j-native",
  dependencies=[
    ":bin",
    "src/main/resources",
  ],
  source="Dockerfile.native"
)