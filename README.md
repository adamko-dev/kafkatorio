# Kafkatorio

1. output events from Factorio into Kafka.
    1. Factorio mod listens to events https://mods.factorio.com/mod/kafkatorio-events
    2. For each event, it emits a log message
    3. A tool (e.g. Kafkatorio Kafka Pipe) reads the logs, and sends the messages to a Kafka cluster
2. Kafkatorio Events Server reads the messages from Kafka
    1. create images of the map
    2. hosts a web map

## License

While this project is under development, [no license](https://choosealicense.com/no-permission/)
is provided.

## cmds

`./bin/kafka-streams-application-reset.sh --application-id kafkatorio-events-processor --input-topics factorio-server-log`

`:screen-fps=60.000000 :live-caching=300 :screen-width=1920 :screen-height=1080 :screen-top=100 :screen-left:20`
`:screen-fps=60.000000 :live-caching=300 --screen-width=1920 --screen-height=1080  :sout=#transcode{vcodec=h264,vb=0,scale=0,acodec=mpga,ab=128,channels=2,samplerate=44100}:file{dst=D:\\savedir.mp4} :sout-keep`

## todo

* add settings
    * is active - whether files are exported or not
    * delete files - clean up all files
    * tick-rate - how often reports are generated
        * requires on some sort of in-mod cache of map<entity-id, event>
        * also divide by game-speed? to keep ratio the same
* Move to a vector tile supported map. PNGs are slow. 
  * Maplibre GL JS? https://maplibre.org/maplibre-gl-js-docs/example/
  * Add VectorGrid? https://leaflet.github.io/Leaflet.VectorGrid/vectorgrid-api-docs.html
    * https://leaflet.github.io/Leaflet.VectorGrid/vectorgrid-api-docs.html#styling-vectorgrids

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

* https://github.com/bakpakin/binser

##### wasm

kt -> wasm -> lua? kt-flatbuffers -> wasm -> lua?

https://www.npmjs.com/package/wasm2lua?activeTab=readme

##### Encodings

* flatbuffers https://github.com/dvidelabs/flatcc
* z85 encoding
    * Kotlin https://gist.github.com/ischumacher/b4929f26341ebef62cbce35c65543eda
    * Lua https://web-eworks.github.io/lZ85/
* https://github.com/SafeteeWoW/LibDeflate
* CBOR
    * https://www.zash.se/lua-cbor.html
    * https://code.zash.se/lua-cbor/archive/68b3a36d0816.tar.gz

* BSON
    * https://github.com/mpaland/bsonfy
    * https://github.com/tcoram/bson-lua

* bebop
  https://rainway.com/blog/2020/12/09/bebop-an-efficient-schema-based-binary-serialization-format/

* binser
    * https://github.com/bakpakin/binser
    * https://github.com/maxiy01/tstl-binser 
