# syntax=docker/dockerfile:1

FROM eclipse-temurin:11-jdk-jammy
ARG KAFKATORIO_VERSION


ENV SERVER_DATA_DIR="/app/data" \
    KAFKA_STREAMS_STATE_DIR="/app/state/kafka" \
    SERVER_PORT=8080 \
    SOCKET_SERVER_PORT=9002


EXPOSE $SERVER_PORT \
       $SOCKET_SERVER_PORT


COPY ${APP_FILES} /app/


ENTRYPOINT /app/bin/run
