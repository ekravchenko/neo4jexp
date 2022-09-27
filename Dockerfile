FROM ghcr.io/graalvm/graalvm-ce:ol8-java17-22.1.0 AS builder
WORKDIR /workdir/neo4jexp

# Add native-image to graalvm
RUN gu install native-image

# Add sbt
ENV SBT_VERSION="1.6.2"
RUN \
  curl -fsL "https://github.com/sbt/sbt/releases/download/v$SBT_VERSION/sbt-$SBT_VERSION.tgz" | tar xfz - -C /usr/share && \
  chown -R root:root /usr/share/sbt && \
  chmod -R 755 /usr/share/sbt && \
  ln -s /usr/share/sbt/bin/sbt /usr/local/bin/sbt

# Copy SBT related files and load dependencies
COPY ./build.sbt /workdir/neo4jexp/build.sbt
COPY ./project/build.properties /workdir/neo4jexp/project/build.properties
COPY ./project/plugins.sbt /workdir/neo4jexp/project/plugins.sbt
COPY ./reflection /workdir/neo4jexp/reflection
RUN sbt reload
RUN sbt update

# Copy sources and compile native image
COPY ./src /workdir/neo4jexp/src
RUN sbt nativeImage -Dnative-image-installed=true

# Final stage of multi-stage build
FROM redhat/ubi8-minimal:latest
COPY --from=builder /workdir/neo4jexp/target/native-image/neo4jexp neo4jexp
EXPOSE 8090
ENTRYPOINT ["./neo4jexp"]