
#FROM debian:buster-slim
FROM docker:dind-rootless

ENV FACTORIO_SERVER_CONTAINER_NAME="factorio-server"
ENV KAFKA_HOST="kafka-kraft"

#RUN apt-get update && apt-get install -y --no-install-recommends \
#  curl \
#  docker \
#  kafkacat

# root is required for following Docker logs
USER root

RUN apk add --update \
    curl \
    kafkacat
#USER rootless

#     \
#  && rm -rf /var/lib/apt/lists/*

COPY kafka-pipe.sh kafka-pipe.sh

#RUN useradd --user-group --system --create-home --no-log-init the_engineer
#USER the_engineer

ENTRYPOINT [ "/bin/sh", "/kafka-pipe.sh" ]
#ENTRYPOINT /bin/bash
#CMD exec /kafka-pipe.sh
