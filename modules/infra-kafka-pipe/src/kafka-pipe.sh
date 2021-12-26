#!/usr/bin/bash

# Pipe Docker logs from a Factorio server to a Kafka topic

echo "Piping logs from $FACTORIO_SERVER_CONTAINER_NAME to $KAFKA_HOST"

trap endProcess SIGINT
endProcess() {
  echo "Exiting Factorio Events Kafka Pipe"
  exit
}

# some random ID, to use as a message key
INSTANCE_ID=$(cat /proc/sys/kernel/random/uuid)

while :; do
  docker logs --tail 0 -f "$FACTORIO_SERVER_CONTAINER_NAME" |
    sed -n -e 's/^FactorioEvent: //p' |
    kcat -P -T -b "$KAFKA_HOST" -t factorio-server-log -k "$INSTANCE_ID"

  echo "Error - retrying in 10 seconds"
  sleep 10

done
