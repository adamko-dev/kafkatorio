#!/usr/bin/env bash

# stream logs from 'factorio' container
# follow the logs, and
# combine stdout and stderr
# only match lines that start with 'FactorioEvent: ',
# and only return the stuff *after* 'FactorioEvent: '
docker logs --tail 0 -f factorio 2>&1 |
  sed -n -e 's/^FactorioEvent: //p' |
  kafkacat -P -b localhost -t factorio-server-log

# listen to events
# kafkacat -C -b localhost -t factorio-server-log | jq
