[![Mod Portal](https://img.shields.io/badge/dynamic/json?label=Mod%20Portal&query=%24.releases%5B-1%3A%5D.version&url=https%3A%2F%2Fmods.factorio.com%2Fapi%2Fmods%2Fkafkatorio-events&style=flat-square&color=%23600401)](https://mods.factorio.com/mod/kafkatorio-events)
[![Factorio Version](https://img.shields.io/badge/dynamic/json?label=Factorio%20Version&query=%24.releases%5B-1%3A%5D.info_json.factorio_version&url=https%3A%2F%2Fmods.factorio.com%2Fapi%2Fmods%2Fkafkatorio-events&style=flat-square&color=%23d7cdf6)](https://mods.factorio.com/mod/kafkatorio-events)

# Kafkatorio

[Kafkatorio](https://github.com/adamko-dev/kafkatorio) is a platform used for creating
[Factorio](https://www.factorio.com/) mods that require communication with an external server.

Development is ongoing.

At present Kafkatorio is used to create a [live-view web map](https://factorio.adamko.dev), that
shows the current status of a Factorio server, and any connected players.

However, Kafkatorio can be used for more than this. It has the potential to export metrics, and
allow for inter-server communication.

Kafkatorio was created to explore the possibilities of using
[Apache Kafka](https://kafka.apache.org/) to process updates from a Factorio server.

### Running

[Instructions for running a Kafkatorio instance are available in the docs](./docs/guide.md).

### Overview

This is a brief overview of how Kafkatorio gathers data, processes it, and uses it to create the
live web-map.

Kafkatorio receives updates from a Factorio server, processes them, and exposes them over a REST
and WebSocket APIs.

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

## License

Because I care about sharing improvements, Kafkatorio is licensed under the  
[**GNU General Public License v3.0**](https://choosealicense.com/licenses/gpl-3.0/).
