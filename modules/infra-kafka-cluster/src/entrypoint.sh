#!/bin/bash

# Format storage directories
./bin/kafka-storage.sh format \
  --ignore-formatted \
  --config ./server.properties \
  --cluster-id "$(cat cluster_id)"

./bin/kafka-server-start.sh ./server.properties
