# syntax=docker/dockerfile:1

ARG KAFKA_VERSION=3.1.0
ARG KAFKA_DL_URL=https://dlcdn.apache.org/kafka/${KAFKA_VERSION}/kafka_2.13-${KAFKA_VERSION}.tgz
ARG KAFKA_DL_ARCHIVE_URL=https://archive.apache.org/dist/kafka/${KAFKA_VERSION}/kafka_2.13-${KAFKA_VERSION}.tgz

## builder ##
FROM debian:11-slim as Kafka-Download
ARG KAFKA_VERSION
ARG KAFKA_DL_URL
ARG KAFKA_DL_ARCHIVE_URL

WORKDIR /kafka

RUN apt-get update && apt-get install -y \
  curl \
  && rm -rf /var/lib/apt/lists/*

RUN echo "Trying to download Kafka from $KAFKA_DL_URL" \
 && if curl --fail "$KAFKA_DL_URL" -o kafka.tgz; then \
      echo "Primary download successful"; \
    else \
      echo "Primary download failed, trying $KAFKA_DL_ARCHIVE_URL" \
      && curl --fail "$KAFKA_DL_ARCHIVE_URL" -o kafka.tgz; \
    fi \
 && tar --strip-components=1 -xz -f kafka.tgz \
 && rm kafka.tgz \
 && echo "Finished Kafka download" \
 && ls -la

## Kafka ##
# KRaft (aka KIP-500) mode Preview Release
# https://github.com/apache/kafka/blob/041b76dc57f096be2a2d7532d917122992bff6e2/config/kraft/README.md
FROM openjdk:11 as kafka-kraft

WORKDIR /kafka

COPY --from=Kafka-Download /kafka .

# hardcoded for now, based on values from './config/kraft/server.properties'
# log.dirs
VOLUME /tmp/kraft-combined-logs
# listeners, controller.quorum.voters
EXPOSE 9092 9093

# Generate a cluster ID (must use this .sh to generate a UUID)
RUN ./bin/kafka-storage.sh random-uuid > cluster_id \
 && echo "Generated a Kafka Cluster ID: $(cat cluster_id)"

COPY ./kafka-server.properties ./server.properties
COPY --chmod=755 ./entrypoint.sh ./entrypoint.sh

# launch the broker in KRaft mode, which means that it runs without ZooKeeper
ENTRYPOINT ["/kafka/entrypoint.sh"]
