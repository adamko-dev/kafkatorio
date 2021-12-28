ARG factorioVersion=1.1.42


FROM factoriotools/factorio:$factorioVersion

# In progress
# The intention is to remove kafka-pipe and have a
# pre-preared Factorio Docker container than can
# just run, without needing an additional
# container with kcat.

#RUN apk add --update \
#    curl \
#    kafkacat \
#    ;

ENV KCAT_CONFIG="/kcat.conf"


RUN apt-get update && apt-get install -y \
  kafkacat \
  && rm -rf /var/lib/apt/lists/*

WORKDIR /
COPY kafka-pipe.sh kafka-pipe.sh
