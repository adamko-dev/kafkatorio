# syntax=docker/dockerfile:1

FROM eclipse-temurin:11-jdk-jammy
ARG KAFKATORIO_VERSION
ARG APP_FILES="build/"
ARG USER_NAME="container_user"
ARG USER_UID=1000
ARG USER_GID=$USER_ID


ENV SERVER_DATA_DIR="/app/data" \
    KAFKA_STREAMS_STATE_DIR="/app/state/kafka" \
    WEB_SERVER_PORT=80


EXPOSE $WEB_SERVER_PORT


VOLUME ${SERVER_DATA_DIR} \
       ${KAFKA_STREAMS_STATE_DIR}


COPY ${APP_FILES} /app/


RUN groupadd --gid $USER_GID $USERNAME \
    && useradd --uid $USER_UID --gid $USER_GID -m $USERNAME \
    && chown -R $USER_UID:$USER_GID /app/


USER $USER_ID:$USER_GID


ENTRYPOINT /app/bin/run
