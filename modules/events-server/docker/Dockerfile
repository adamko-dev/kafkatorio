# syntax=docker/dockerfile:1

FROM eclipse-temurin:11-jdk-jammy
ARG KAFKATORIO_VERSION=0.5.1
ARG DIST_DIR="../build/distributions/"

WORKDIR /app

ENV SERVER_PORT=8080
EXPOSE $SERVER_PORT

ENV SERVER_DATA_DIR="/app/data"
ENV KAFKA_STREAMS_STATE_DIR="/app/kafka-state"
VOLUME ${SERVER_DATA_DIR} \
       ${KAFKA_STREAMS_STATE_DIR}

COPY ${DIST_DIR}/events-server-${KAFKATORIO_VERSION}.tar events-server.tar

RUN tar -xf events-server.tar --strip-components=1 \
 && rm events-server.tar

ENTRYPOINT /app/bin/events-server
