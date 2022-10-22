# syntax=docker/dockerfile:1

FROM eclipse-temurin:11-jdk-jammy
ARG KAFKATORIO_VERSION
ARG APP_FILES="build/"
ARG USER_NAME="container_user"
ARG USER_UID=1000
ARG USER_GID=$USER_UID


ENV SERVER_DATA_DIR="/app/data" \
    KAFKA_STREAMS_STATE_DIR="/app/state/kafka" \
    SOCKET_SERVER_HOST=0.0.0.0 \
    SOCKET_SERVER_PORT=9002


EXPOSE $SOCKET_SERVER_PORT


COPY ${APP_FILES} /app/


RUN groupadd --gid $USER_GID $USER_NAME \
    && useradd --uid $USER_UID --gid $USER_GID -m $USER_NAME \
    && chown -R $USER_UID:$USER_GID /app/


USER $USER_UID:$USER_GID


ENTRYPOINT /app/bin/run
