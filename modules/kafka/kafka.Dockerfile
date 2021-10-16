ARG KAFKA_DL_URL=https://dlcdn.apache.org/kafka/3.0.0/kafka_2.13-3.0.0.tgz

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


FROM openjdk:11

WORKDIR /kafka

COPY --from=Kafka-Download /kafka /kafka

# hardcoded for now, based on values from './config/kraft/server.properties'
# log.dirs
VOLUME /tmp/kraft-combined-logs
# listeners, controller.quorum.voters
EXPOSE 9092, 9093

# KRaft (aka KIP-500) mode Preview Release
# https://github.com/apache/kafka/blob/041b76dc57f096be2a2d7532d917122992bff6e2/config/kraft/README.md

# Generate a cluster ID
RUN echo "$(./bin/kafka-storage.sh random-uuid)" > cluster_id \
 && echo "Generated a Kafka Cluster ID: $(cat cluster_id)"

# Format storage directories
RUN ./bin/kafka-storage.sh format -t "$(cat cluster_id)" -c ./config/kraft/server.properties

# launch the broker in KRaft mode, which means that it runs without ZooKeeper
RUN ./bin/kafka-storage.sh format \
  --ignore-formatted \
  --config ./config/kraft/server.properties \
  --cluster-id "$(cat cluster_id)"

ENTRYPOINT ["./bin/kafka-server-start.sh", "./config/kraft/server.properties"]
