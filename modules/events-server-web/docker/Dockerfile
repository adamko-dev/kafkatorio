# syntax=docker/dockerfile:1

FROM openjdk:11
ARG KAFKATORIO_VERSION
ARG DIST_DIR="../build/distributions/"
ARG APP_NAME="events-server-web"

WORKDIR /app

ENV WEB_SERVER_PORT=80
EXPOSE $WEB_SERVER_PORT

ENV SERVER_DATA_DIR="/app/data"
ENV KAFKA_STREAMS_STATE_DIR="/app/state/kafka"

VOLUME ${SERVER_DATA_DIR} \
       ${KAFKA_STREAMS_STATE_DIR}

COPY ${DIST_DIR}/${APP_NAME}-${KAFKATORIO_VERSION}.tar ${APP_NAME}.tar

RUN tar -xf ${APP_NAME}.tar --strip-components=1 \
 && rm ${APP_NAME}.tar \
 && chmod +x /app/bin/${APP_NAME}

ENTRYPOINT /app/bin/${APP_NAME}
