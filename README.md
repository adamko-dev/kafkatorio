# Kafkatorio

[Kafkatorio](https://github.com/adamko-dev/kafkatorio) is a platform used for creating
[Factorio](https://www.factorio.com/) mods that require communication with an external server.
It was created to explore the possibilities of using [Apache Kafka](https://kafka.apache.org/)
to process updates from a Factorio server.

Kafkatorio receives updates from a Factorio server, processes them, and exposes them over a REST
and WebSocket APIs.

Development is ongoing. At present the only application is a
[live-view web map](https://factorio.adamko.dev), that shows the current status of a Factorio
server, and any connected players. Other usage possibilities include exposing live metrics, and
inter-server communication.

### Overview

```mermaid
sequenceDiagram

Factorio multiplayer server->>events-mod: in-game events
Note over events-mod: converts events to packets
events-mod->>logs processor: packets logged to console
logs processor->>Kafka: publishes packets<br/>(Requires API key!)

Kafka->>events-server: 

Note over events-server: Packets are processed

events-server->>web-map: Web map tiles (REST API)
events-server-)web-map: WebSocket updates<br/>(e.g. player movement)
```

1. The Kafkatorio game mod, [`events-mod`](./modules/events-mod), collects event data, de-bouncing
   and grouping events, and converts them to JSON packets
2. `events-mod` prints the packets to the Factorio game server logs
3. The Factorio logs are forwarded to a Kafka cluster, either using
    * [syslog](https://docs.docker.com/config/containers/logging/syslog/) (using Docker logging
      plugin)
    * or [Kafka Pipe](./modules/infra-kafka-pipe) (a script that reads the server logs and forwards
      them to a Kafka topic)
4. The packets are consumed by [`events-server`](./modules/events-server), which processes the
   packets using Kafka Streams, creating and hosting map tiles.

   `events-server` serves the map tiles via a REST API, and produces WebSocket messages regarding
   updates (for example, when a player's location has changed).
5. The `web-map` uses Leaflet/JS to display the map, using the tiles and WebSocket messages from
   `events-server`

## License

While this project is under development, [no license](https://choosealicense.com/no-permission/)
is provided.
