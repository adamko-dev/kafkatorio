package dev.adamko.kafkatorio.processor.topology

import dev.adamko.kafkatorio.events.schema.ColourHex
import dev.adamko.kafkatorio.events.schema.FactorioEvent
import dev.adamko.kafkatorio.events.schema.KafkatorioPacket
import dev.adamko.kafkatorio.events.schema.MapChunk
import dev.adamko.kafkatorio.events.schema.MapChunkPosition
import dev.adamko.kafkatorio.events.schema.MapTilePosition
import dev.adamko.kafkatorio.events.schema.MapTiles
import dev.adamko.kafkatorio.events.schema.converters.toMapChunkPosition
import dev.adamko.kafkatorio.processor.serdes.kxsBinary
import dev.adamko.kotka.extensions.groupedAs
import dev.adamko.kotka.extensions.materializedAs
import dev.adamko.kotka.extensions.materializedWith
import dev.adamko.kotka.extensions.streams.flatMap
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
  factorioServerPacketStream: KStream<FactorioServerId, KafkatorioPacket>,
  tileProtoColourDict: KTable<FactorioServerId, TileColourDict>,
): KTable<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> {

  // group tiles by server & surface & chunk
  val chunkedTilesTable: KTable<ServerMapChunkId, ServerMapChunkTiles<TileProtoHashCode>> =
    factorioServerPacketStream
      .flatMap("server-map-data.tiles.flatMapByChunk") { key: FactorioServerId, packet: KafkatorioPacket ->

        val mapTileData = when (packet) {
          is FactorioEvent ->
            when (val data = packet.data) {
              is MapChunk -> data.tiles
              is MapTiles -> data
              else        -> null
            }
          else             -> null
        }

        val zoomLevel = ZoomLevel.ZOOM_0

        mapTileData
          ?.tiles
          ?.groupBy { tile ->
            tile.position.toMapChunkPosition(zoomLevel.chunkSize)
          }
          ?.map { (chunkPos, tiles) ->

            val chunkId = ServerMapChunkId(
              key,
              chunkPos,
              SurfaceIndex(mapTileData.surfaceIndex),
              zoomLevel,
            )

            val tilesMap = tiles.associate { it.position to TileProtoHashCode(it) }

            val chunkTiles = ServerMapChunkTiles(chunkId, tilesMap)

            chunkId to chunkTiles
          } ?: listOf()

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
