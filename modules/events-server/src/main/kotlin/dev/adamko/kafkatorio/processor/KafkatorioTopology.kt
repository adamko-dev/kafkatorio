package dev.adamko.kafkatorio.processor

import dev.adamko.kafkatorio.processor.topology.buildFactorioServerMap
import dev.adamko.kafkatorio.processor.topology.factorioServerPacketStream
import dev.adamko.kafkatorio.processor.topology.playerUpdatesToWsServer
import dev.adamko.kafkatorio.processor.topology.saveMapTiles
import dev.adamko.kafkatorio.processor.topology.splitFactorioServerPacketStream
import java.time.Duration
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.TopologyDescription


class KafkatorioTopology(
  private val websocketServer: WebsocketServer,
  private val builder: StreamsBuilder = StreamsBuilder(),
) {

  companion object {
    const val sourceTopic = "factorio-server-log"
  }

  fun build() {

    val packets = factorioServerPacketStream(builder)

    splitFactorioServerPacketStream(packets)

    playerUpdatesToWsServer(websocketServer, builder)

    val serverMapDataTable = buildFactorioServerMap(packets)

    saveMapTiles(serverMapDataTable)


    // the old way...
//    val allMapTilesTable: KTable<TileUpdateRecordKey, MapTile> = allMapTilesTable(builder)
//    val tilePrototypeColourTable: KTable<PrototypeName, Colour> = tilePrototypeColourTable(builder)
//
//    val webMapTiles: KTable<WebMapTileChunkPosition, WebMapTileChunkPixels> =
//      aggregateWebMapTiles(allMapTilesTable, tilePrototypeColourTable)
//
//    saveTileImages(webMapTiles)

    val topology = builder.build()

    val streams = KafkaStreams(topology, appProps.kafkaConfig)

//    streams.cleanUp()
    streams.setUncaughtExceptionHandler(StreamsExceptionHandler())
    Runtime.getRuntime().addShutdownHook(Thread { streams.close(Duration.ofSeconds(1)) })

    val description: TopologyDescription = topology.describe()
    println(description)

    streams.start()

  }

}
