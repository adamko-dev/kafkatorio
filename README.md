1. output events from Factorio into Kafka.
    1. Factorio mod listens to events.
    2. For each event, it emits a file.
    3. A Kafka Connector consumes each file.
2. Factorio Web Map reads data from Kafka
    1. converts data to images
    2. hosts a web map

## todo

* add settings
  * is active - whether files are exported or not
  * delete files - clean up all files
  * tick-rate - how often reports are generated (
    * requires on some sort of in-mod cache of map<entity-id, event>)
    * also divide by game-speed? to keep ratio the same

### to investigate:

* https://github.com/starwing/lua-protobuf
* https://github.com/grafi-tt/lunajson
* http://lua-users.org/wiki/CompressionAndArchiving
* https://luapower.com/zlib

* https://square.github.io/okio/#communicate-on-a-socket-javakotlin

* https://github.com/cloudhut/kowl
* https://opensource.expediagroup.com/graphql-kotlin/docs/server/spring-server/spring-subscriptions
* https://github.com/YousefED/typescript-json-schema

* https://jbrandhorst.com/post/go-protobuf-tips/

#### CBOR

https://www.zash.se/lua-cbor.html
https://code.zash.se/lua-cbor/archive/68b3a36d0816.tar.gz