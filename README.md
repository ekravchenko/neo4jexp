# neo4jexp
Use latest technologies in Scala stack to work with neo4j

# Scala stack
- [Ciris](https://cir.is/docs/quick-example) - functional way to load configs
- [Log4cats](https://github.com/typelevel/log4cats) - functional logging
- [Neotypes](https://neotypes.github.io/neotypes/) - lightweight, type-safe, functional way to work with neo4j
- [Tapir](https://tapir.softwaremill.com/en/latest/server/http4s.html) - describe HTTP API endpoints as immutable Scala values
- [Http4s](https://http4s.org/) - functional HTTP server for scala
- [fs2-kafka](https://fd4s.github.io/fs2-kafka/docs/consumers) - functional Kafka consumers

_Stack above is fully compliant with GraalVM native compilation_

# GraalVM
Using command `sbt nativeImage` http server can be compiled into native binary. Native binary **does not need** JVM to run

# How to run
- neo4j db should be up and running
- kafka should be up and running, and `test` topic should be created
- neo4j db should have `Person` nodes with `name` and `surname`
- create environment variables:
    - `neo4j_host` host address of Neo4j
    - `neo4j_port` port (to connect via bolt) of Neo4j
    - `neo4j_user` Neo4j user
    - `neo4j_password` Neo4j password
    - `kafka_host` Kafka host
    - `kafka_port` Kafka port

# Docker
Native image can be built using Docker multi stage build via command
```
docker build -t neo4jexp .
```
To run container use command below
```
docker run -d -p 8090:8090 \
        -e neo4j_host=docker.for.mac.localhost \
        -e neo4j_port=7687 -e neo4j_user=neo4j \
        -e neo4j_password=test \
        --name neo4jexp neo4jexp
```

Base image for this docker build is really small `ubi8-minimal` if something extra is needed (for example `ps`) use command below:
```
microdnf update && microdnf install procps
```



