#!/usr/bin/bash

# Pipe Docker logs from a Factorio server to a Kafka topic

echo "Piping logs from $FACTORIO_SERVER_CONTAINER_NAME to $KAFKA_HOST"

# generate some distinctive key for messages from this server
SERVER_HASH=openssl dgst -sha3-512 "$CONFIG/server-settings.json"

trap endProcess SIGINT
endProcess() {
  echo "Exiting Factorio Events Kafka Pipe"
  exit
}

while :; do

    sed -n -e 's/^FactorioEvent: //p' |
    kafkacat -F "$KCAT_PROP_FILE"

  echo "Error - retrying in 10 seconds"

  sleep 10
done
