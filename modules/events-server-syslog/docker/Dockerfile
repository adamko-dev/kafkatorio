# syntax=docker/dockerfile:1

FROM eclipse-temurin:11-jdk-jammy
ARG KAFKATORIO_VERSION
ARG APP_FILES="build/"


ENV SERVER_DATA_DIR="/app/data" \
    KAFKA_STREAMS_STATE_DIR="/app/state/kafka" \
    SOCKET_SERVER_HOST=0.0.0.0 \
    SOCKET_SERVER_PORT=9002


EXPOSE $SOCKET_SERVER_PORT


COPY ${APP_FILES} /app/


ENTRYPOINT /app/bin/run
