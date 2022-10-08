docker_image(
  name="neo4j-jvm",
  dependencies=[
    "src/main/scala/com/testingzone/services/neo4j:bin",
  ],
  source="Dockerfile.jvm"
)

docker_image(
  name="neo4j-native",
  dependencies=[
    "src/main/scala/com/testingzone/services/neo4j:bin",
    "src/main/resources",
  ],
  source="Dockerfile.native"
)