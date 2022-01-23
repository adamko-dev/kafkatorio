package dev.adamko.kafkatorio.processor

import dev.adamko.kafkatorio.events.schema.MapTile
import dev.adamko.kafkatorio.events.schema.MapTilePrototype
import dev.adamko.kafkatorio.processor.topology.PrototypeName
import dev.adamko.kafkatorio.processor.topology.TileUpdateRecordKey
import dev.adamko.kafkatorio.processor.topology.WebMapTileChunkPixels
import dev.adamko.kafkatorio.processor.topology.WebMapTileChunkPosition
import dev.adamko.kafkatorio.processor.topology.aggregateWebMapTiles
import dev.adamko.kafkatorio.processor.topology.allMapTilesTable
import dev.adamko.kafkatorio.processor.topology.playerUpdatesToWsServer
import dev.adamko.kafkatorio.processor.topology.prototypesTable
import dev.adamko.kafkatorio.processor.topology.saveTileImages
import dev.adamko.kafkatorio.processor.topology.splitFactorioServerLog
import java.time.Duration
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.TopologyDescription
import org.apache.kafka.streams.kstream.KTable


class KafkatorioTopology(
  private val websocketServer: WebsocketServer,
  private val builder: StreamsBuilder = StreamsBuilder(),
) {

  companion object {
    const val sourceTopic = "factorio-server-log"
  }

  fun build() {

    splitFactorioServerLog(builder)

    playerUpdatesToWsServer(websocketServer, builder)

    val allMapTilesTable: KTable<TileUpdateRecordKey, MapTile> = allMapTilesTable(builder)
    val tilePrototypesTable: KTable<PrototypeName, MapTilePrototype> = prototypesTable(builder)

    val webMapTiles: KTable<WebMapTileChunkPosition, WebMapTileChunkPixels> =
      aggregateWebMapTiles(allMapTilesTable, tilePrototypesTable)

    saveTileImages(webMapTiles)

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
