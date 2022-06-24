ARG KAFKA_DL_URL=https://dlcdn.apache.org/kafka/3.1.0/kafka_2.13-3.1.0.tgz

## builder ##
FROM debian:11-slim as Kafka-Download
ARG KAFKA_DL_URL

WORKDIR /kafka

RUN apt-get update && apt-get install -y \
  curl \
  && rm -rf /var/lib/apt/lists/*

RUN echo "Downloading Kafka from $KAFKA_DL_URL" \
 && curl "$KAFKA_DL_URL" | tar --strip-components=1 -xz \
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
RUN echo "$(./bin/kafka-storage.sh random-uuid)" > cluster_id \
 && echo "Generated a Kafka Cluster ID: $(cat cluster_id)"

COPY ./kafka-server.properties ./server.properties

# Format storage directories
RUN ./bin/kafka-storage.sh format \
  --ignore-formatted \
  --config ./server.properties \
  --cluster-id "$(cat cluster_id)"

# launch the broker in KRaft mode, which means that it runs without ZooKeeper
ENTRYPOINT ["./bin/kafka-server-start.sh", "./server.properties"]


### Kafka Connect ##
#FROM openjdk:11 as kafka-connect
## https://github.com/confluentinc/cp-docker-images/blob/5.3.3-post/debian/kafka-connect-base/Dockerfile
#
#ENV COMPONENT=kafka-connect
#
#WORKDIR /kafka-connect
#
#COPY --from=Kafka-Download /kafka .
#
## Default kafka-connect rest.port
#EXPOSE 8083
#
#VOLUME ["/etc/${COMPONENT}/jars", "/etc/${COMPONENT}/secrets"]
#
## launch the broker in KRaft mode, which means that it runs without ZooKeeper
#ENTRYPOINT ["./bin/connect-standalone.sh", "./config/connect-standalone.properties"]
