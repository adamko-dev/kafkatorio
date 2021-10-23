#!/usr/bin/zsh

# stream logs from 'factorio' container
# follow the logs, and
# combine stdout and stderr
# only match lines that start with 'FactorioEvent: ',
# and only return the stuff *after* 'FactorioEvent: '
docker logs --tail 0 -f factorio-server 2>&1 |
  sed -n -e 's/^FactorioEvent: //p' |
  kafkacat -P -b localhost -t factorio-server-log

# listen to events
# kafkacat -C -b localhost -t factorio-server-log | jq

# curl -sS --unix-socket /var/run/docker.sock "http://localhost/v1.41/events?filters={&quot;service&quot;:&quot;factorio-server&quot;}" --output - | jq

curl --no-buffer -sS \
  --unix-socket /var/run/docker.sock \
  "http://localhost/v1.41/containers/factorio-server/logs?stdout=true&follow=true&tail=0"  |
   tail -c+9
#  --output -

curl \
  -sS \
  --unix-socket /var/run/docker.sock \
  -X POST \
  --insecure "http://localhost/v1.41/containers/factorio-server/attach?logs=true&stream=true&stdout=true&follow=true" |
  tail -c+9

#  sed 's/^........//'


# https://stackoverflow.com/questions/42873285/curl-retry-mechanism
# --retry-all-errors

#     --header "Sec-WebSocket-Key: SGVsbG8sIHdvcmxkIQ==" \
#     --header "Sec-WebSocket-Version: 13" \
#     --header "Host: localhost:80" \
#     --header "Origin: http://localhost:80" \

curl --include \
     --unix-socket /var/run/docker.sock \
     --no-buffer \
     --header "Connection: Upgrade" \
     --header "Upgrade: websocket" \
     --header "Host: localhost:80" \
     --header "Origin: http://localhost:80" \
     --header "Sec-WebSocket-Version: 13" \
     --header "Sec-WebSocket-Key: SGVsbG8sIHdvcmxkIQ==" \
     "http://localhost/v1.41/containers/factorio-server/attach/ws?logs=true&stdout=true"
