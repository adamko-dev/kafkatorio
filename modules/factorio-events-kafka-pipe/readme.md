### Setup notes

Kafka must be connectable from both `localhost` (for cli) and `kafa` (the docker container name).

### Useful commands

#### Powershell

```shell
# might help with mounting the unix socket on Windows? 
$Env:COMPOSE_CONVERT_WINDOWS_PATHS=1 

# full rebuild
docker-compose down; docker-compose build; docker-compose up -d; docker-compose logs -f

```

#### zsh

```shell
# listen to events from factorio-server
docker logs --tail 0 -f factorio-server | sed -n -e 's/^FactorioEvent: //p' 

# tail logs with docker api (except it doesn't work)
curl -vvv \ 
  --unix-socket /var/run/docker.sock \
  --no-buffer \
  --header "Connection: Upgrade" \
  --header "Upgrade: websocket" \
  --header "Host: localhost:80" \
  --header "Origin: http://localhost:80" \
  --header "Sec-WebSocket-Version: 13" \
  --header "Sec-WebSocket-Key: SGVsbG8sIHdvcmxkIQ==" \
  "http://localhost:80/v1.41/containers/factorio-server/attach/ws?stream=true&logs=true&stdout=true"

# tail logs 
curl --no-buffer -sS --unix-socket /var/run/docker.sock \
    --get --data-urlencode 'filters={"container":["my-service"]}' \
    "http://localhost/v1.41/events" | jq --unbuffered

# get logs from Kafka
kafkacat -u -C -b localhost -t factorio-server-log | jq --unbuffered   

# stream logs, and try trimming first 8 bytes (the header) but it doesn't work
curl \
  -sS \
  --unix-socket /var/run/docker.sock \
  -X POST \
  --insecure "http://localhost/v1.41/containers/factorio-server/attach?logs=true&stream=true&stdout=true&follow=true" |
  tail -c+9

```