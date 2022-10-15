#!/usr/bin/bash

# Pipe Docker logs from a Factorio server to a Kafka topic

echo "Kafkatorio-Pipe version $KAFKATORIO_VERSION"
echo "Piping logs from $FACTORIO_SERVER_CONTAINER_NAME to $KAFKA_HOST"

trap endProcess SIGINT
endProcess() {
  echo "Exiting Factorio Events Kafka Pipe"
  exit
}

# some random ID, to use as a message key
#INSTANCE_ID="${INSTANCE_ID}"$(cat /proc/sys/kernel/random/uuid)
KEY="${INSTANCE_ID:-none}"

while :; do
  docker logs --tail 0 -f "$FACTORIO_SERVER_CONTAINER_NAME" |
    sed -n -e 's/^KafkatorioPacket encoded::: //p' |
    kcat -P \
      -b "$KAFKA_HOST" \
      -t kafkatorio.src.server-log \
      -k "$KEY" \
      -H "KAFKA-PIPE-VERSION=$KAFKATORIO_VERSION"
#      -T \

# -P producer mode
# -T echo to stdout
# -t the topic name
# -k the key of the message
# -H add a header

  echo "Error - retrying in 10 seconds"
  sleep 10

done
