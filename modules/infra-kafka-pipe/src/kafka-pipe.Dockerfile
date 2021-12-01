FROM docker:dind-rootless

ENV FACTORIO_SERVER_CONTAINER_NAME="factorio-server"
ENV KAFKA_HOST="kafka"

# root is required for installing dependencies and
# using the docker socket (listening to logs)
USER root
RUN apk add --update \
    curl \
    kafkacat
#USER rootless

COPY kafka-pipe.sh kafka-pipe.sh

ENTRYPOINT [ "/bin/sh", "/kafka-pipe.sh" ]
