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
* https://github.com/appgurueu/modlib#bluon
* https://github.com/dingyi222666/lua-table-serialize
* https://github.com/uriid1/lua-serialize
* https://github.com/rxi/lume#lumeserializex
* https://github.com/SwadicalRag/bON

* https://square.github.io/okio/#communicate-on-a-socket-javakotlin

* https://github.com/cloudhut/kowl
* https://opensource.expediagroup.com/graphql-kotlin/docs/server/spring-server/spring-subscriptions
* https://github.com/YousefED/typescript-json-schema

* https://jbrandhorst.com/post/go-protobuf-tips/

##### flatbuffers

https://github.com/dvidelabs/flatcc


##### z85 encoding

* Kotlin https://gist.github.com/ischumacher/b4929f26341ebef62cbce35c65543eda
* Lua https://web-eworks.github.io/lZ85/

#### CBOR

https://www.zash.se/lua-cbor.html
https://code.zash.se/lua-cbor/archive/68b3a36d0816.tar.gz