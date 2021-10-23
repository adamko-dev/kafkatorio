#!/usr/bin/bash

# Pipe Docker logs from a Factorio server to a Kafka topic

echo "Piping logs from $FACTORIO_SERVER_CONTAINER_NAME to $KAFKA_HOST"

trap endProcess SIGINT
endProcess() {
  echo "Exiting Factorio Events Kafka Pipe"
  exit
}

while :; do
  docker logs --tail 0 -f "$FACTORIO_SERVER_CONTAINER_NAME" |
    sed -n -e 's/^FactorioEvent: //p' |
    kafkacat -P -T -b "$KAFKA_HOST" -t factorio-server-log

  echo "Error - retrying in 10 seconds"

  sleep 10
done
