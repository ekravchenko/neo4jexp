FROM azul/zulu-openjdk-alpine:19-latest
COPY src.main.scala.com.testingzone.services.neo4j/bin.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]