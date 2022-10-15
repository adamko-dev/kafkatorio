# syntax=docker/dockerfile:1

FROM eclipse-temurin:11-jdk-jammy
ARG KAFKATORIO_VERSION
ARG DIST_DIR="../build/distributions/"
ARG APP_NAME="events-server-syslog"

WORKDIR /app

ENV SOCKET_SERVER_PORT=9002
EXPOSE $SOCKET_SERVER_PORT

ENV SERVER_DATA_DIR="/app/data"
ENV KAFKA_STREAMS_STATE_DIR="/app/state/kafka"

VOLUME ${SERVER_DATA_DIR} \
       ${KAFKA_STREAMS_STATE_DIR}

COPY ${DIST_DIR}/${APP_NAME}-${KAFKATORIO_VERSION}.tar ${APP_NAME}.tar

RUN tar -xf ${APP_NAME}.tar --strip-components=1 \
 && rm ${APP_NAME}.tar \
 && cp /app/bin/${APP_NAME} /app/bin/run \
 && chmod 775 /app/bin/run

ENTRYPOINT /app/bin/run
