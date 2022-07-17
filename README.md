# Kafkatorio

[Kafkatorio](https://github.com/adamko-dev/kafkatorio) is a platform used for creating
[Factorio](https://www.factorio.com/) mods that require communication with an external server.
It was created to explore the possibilities of using [Apache Kafka](https://kafka.apache.org/)
to process updates from a Factorio server.

Kafkatorio receives updates from a Factorio server, processes them, and exposes them over a REST
and WebSocket APIs.

Development is ongoing. At present the only application is a
[live-view web map](https://factorio.adamko.dev), that shows the current status of a Factorio
server, and any connected players. Other possibilities are exposing metrics, and inter-server
communication.

## License

While this project is under development, [no license](https://choosealicense.com/no-permission/)
is provided.
