package dev.adamko.kafkatorio.processor.topology

import dev.adamko.kafkatorio.events.schema.ColourHex
import dev.adamko.kafkatorio.events.schema.FactorioEvent
import dev.adamko.kafkatorio.events.schema.MapChunk
import dev.adamko.kafkatorio.events.schema.MapChunkPosition
import dev.adamko.kafkatorio.events.schema.MapTilePosition
import dev.adamko.kafkatorio.events.schema.MapTiles
import dev.adamko.kafkatorio.events.schema.converters.toMapChunkPosition
import dev.adamko.kafkatorio.processor.serdes.kxsBinary
import dev.adamko.kotka.extensions.groupedAs
import dev.adamko.kotka.extensions.materializedAs
import dev.adamko.kotka.extensions.materializedWith
import dev.adamko.kotka.extensions.streams.filter
import dev.adamko.kotka.extensions.streams.flatMap
import dev.adamko.kotka.extensions.streams.mapValues
import dev.adamko.kotka.extensions.streams.merge
import dev.adamko.kotka.extensions.streams.reduce
import dev.adamko.kotka.extensions.tables.join
import dev.adamko.kotka.kxs.serde
import kotlinx.serialization.Serializable
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.KTable


@Serializable
data class ServerMapChunkId(
  val serverId: FactorioServerId,
  val chunkPosition: MapChunkPosition,
  val surfaceIndex: SurfaceIndex,
  val zoomLevel: ZoomLevel,
)


@Serializable
data class ServerMapChunkTiles<Data>(
  val chunkId: ServerMapChunkId,
  val map: Map<MapTilePosition, Data>,
) {
  operator fun plus(other: ServerMapChunkTiles<Data>) = copy(map = map + other.map)
}


fun groupTilesIntoChunksWithColours(
  mapTilePacketsStream: KStream<FactorioServerId, FactorioEvent>,
  mapChunksPacketsStream: KStream<FactorioServerId, FactorioEvent>,
  tileProtoColourDict: KTable<FactorioServerId, TileColourDict>,
): KTable<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> {


  val mapChunkTilesStream: KStream<FactorioServerId, MapTiles> =
    mapChunksPacketsStream
      .filter { _, packet: FactorioEvent? ->
        packet != null
            && packet.data is MapChunk
            && !((packet.data as? MapChunk)?.tiles?.tiles.isNullOrEmpty())
      }
      .mapValues("map-chunk-packets.convert-to-map-tiles") { _, packet: FactorioEvent ->
        (packet.data as MapChunk).tiles
      }

  val mapTilesStream: KStream<FactorioServerId, MapTiles> =
    mapTilePacketsStream
      .filter { _, packet: FactorioEvent? ->
        packet != null
            && packet.data is MapTiles
            && !((packet.data as? MapTiles)?.tiles.isNullOrEmpty())
      }
      .mapValues("map-tiles-packets.convert-to-map-tiles") { _, packet: FactorioEvent ->
        packet.data as MapTiles
      }

  // group tiles by server & surface & chunk
  val chunkedTilesTable: KTable<ServerMapChunkId, ServerMapChunkTiles<TileProtoHashCode>> =
    mapTilesStream
      .merge("merge-map-chunk-tiles-with-map-tiles", mapChunkTilesStream)
      .filter { _, value -> value != null && value.tiles.isNotEmpty() }
      .flatMap("server-map-data.tiles.flatMapByChunk") { key: FactorioServerId, mapTiles: MapTiles ->

        val zoomLevel = ZoomLevel.ZOOM_0

        mapTiles
          .tiles
          .groupBy { tile ->
            tile.position.toMapChunkPosition(zoomLevel.chunkSize)
          }
          .map { (chunkPos, tiles) ->

            val chunkId = ServerMapChunkId(
              key,
              chunkPos,
              SurfaceIndex(mapTiles.surfaceIndex),
              zoomLevel,
            )

            val tilesMap = tiles.associate { it.position to TileProtoHashCode(it) }

            val chunkTiles = ServerMapChunkTiles(chunkId, tilesMap)

            chunkId to chunkTiles
          }
      }
      .filter("server-map-data.tiles.filter-not-empty") { _, value ->
        value.map.isNotEmpty()
      }
      .groupByKey(
        groupedAs("server-map-data.tiles.group", kxsBinary.serde(), kxsBinary.serde())
      )
      .reduce(
        "server-map-data.tiles.reduce",
        materializedAs("server-map-data.tiles.store", kxsBinary.serde(), kxsBinary.serde()),
      ) { a, b -> a + b }


  val colourisedChunkTable: KTable<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> =
    chunkedTilesTable
      .join(
        other = tileProtoColourDict,
        name = "server-map-data.join-tiles-with-prototypes",
        materialized = materializedWith(kxsBinary.serde(), kxsBinary.serde()),
        foreignKeyExtractor = { chunkTiles: ServerMapChunkTiles<TileProtoHashCode> -> chunkTiles.chunkId.serverId }
      ) { tiles: ServerMapChunkTiles<TileProtoHashCode>, colourDict: TileColourDict ->

        val missingProtos = mutableSetOf<TileProtoHashCode>()

        val tileColours = tiles.map.mapValues { (_, code) ->
          colourDict.map.getOrElse(code) {
            missingProtos.add(code)
            ColourHex.TRANSPARENT
          }
        }

        if (missingProtos.isNotEmpty()) {
          println("missing ${missingProtos.size} tile prototypes: ${missingProtos.joinToString { "${it.code}" }}")
        }

        ServerMapChunkTiles(tiles.chunkId, tileColours)
      }

  return colourisedChunkTable

}
