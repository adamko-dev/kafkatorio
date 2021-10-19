1. output events from Factorio into Kafka.
    1. Factorio mod listens to events.
    2. For each event, it emits a file.
    3. A Kafka Connector consumes each file.
2. Factorio Web Map reads data from Kafka
    1. converts data to images
    2. hosts a web map

todo

* add settings
  * is active - whether files are exported or not
  * delete files - clean up all files
  * tick-rate - how often reports are generated (
    * requires on some sort of in-mod cache of map<entity-id, event>)
    * also divide by game-speed? to keep ratio the same

to investigate:

* https://github.com/starwing/lua-protobuf
* https://github.com/grafi-tt/lunajson

