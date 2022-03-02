package dev.adamko.kafkatorio.processor.topology

import dev.adamko.kafkatorio.events.schema.ColourHex
import dev.adamko.kafkatorio.events.schema.FactorioEventUpdatePacket
import dev.adamko.kafkatorio.events.schema.MapChunkPosition
import dev.adamko.kafkatorio.events.schema.MapChunkUpdate
import dev.adamko.kafkatorio.events.schema.MapTile
import dev.adamko.kafkatorio.events.schema.MapTileDictionary.Companion.isNullOrEmpty
import dev.adamko.kafkatorio.events.schema.MapTilePosition
import dev.adamko.kafkatorio.events.schema.MapTiles
import dev.adamko.kafkatorio.events.schema.SurfaceIndex
import dev.adamko.kafkatorio.events.schema.converters.toMapChunkPosition
import dev.adamko.kafkatorio.processor.serdes.kxsBinary
import dev.adamko.kotka.extensions.groupedAs
import dev.adamko.kotka.extensions.materializedAs
import dev.adamko.kotka.extensions.materializedWith
import dev.adamko.kotka.extensions.streams.flatMap
import dev.adamko.kotka.extensions.streams.mapValues
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
  val chunkSize: ChunkSize,
)


@Serializable
data class ServerMapChunkTiles<Data>(
  val chunkId: ServerMapChunkId,
  val map: Map<MapTilePosition, Data>,
) {
  operator fun plus(other: ServerMapChunkTiles<Data>) = copy(map = map + other.map)
}


fun groupTilesIntoChunksWithColours(
//  mapTilePacketsStream: KStream<FactorioServerId, FactorioEvent>,
//  mapChunksPacketsStream: KStream<FactorioServerId, FactorioEvent>,
  mapChunkUpdatePacketsStream: KStream<FactorioServerId, FactorioEventUpdatePacket>,
  tileProtoColourDict: KTable<FactorioServerId, TileColourDict>,
): KTable<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> {


//  val mapChunkTilesStream: KStream<FactorioServerId, MapTiles> =
//    mapChunksPacketsStream
//      .filter { _, packet: FactorioEvent? ->
//        when (val data = packet?.data) {
//          is MapChunk -> data.tiles.tiles.isNotEmpty()
//          else        -> false
//        }
//      }
//      .mapValues("map-chunk-packets.convert-to-map-tiles") { _, packet: FactorioEvent ->
//        (packet.data as MapChunk).tiles
//      }
//
//
//  val mapTilesStream: KStream<FactorioServerId, MapTiles> =
//    mapTilePacketsStream
//      .filter { _, packet: FactorioEvent? ->
//        when (val data = packet?.data) {
//          is MapTiles -> data.tiles.isNotEmpty()
//          else        -> false
//        }
//      }
//      .mapValues("map-tiles-packets.convert-to-map-tiles") { _, packet: FactorioEvent ->
//        packet.data as MapTiles
//      }

  val mapChunkUpdatesStream: KStream<FactorioServerId, MapTiles> =
    mapChunkUpdatePacketsStream
      .filter { _, packet: FactorioEventUpdatePacket? ->
        when (val data = packet?.update) {
          is MapChunkUpdate -> !data.tileDictionary.isNullOrEmpty()
          else              -> false
        }
      }
      .mapValues("map-chunk-update-packets.convert-to-map-tiles") { _, packet: FactorioEventUpdatePacket ->

        val mapChunkUpdate: MapChunkUpdate = requireNotNull(packet.update as? MapChunkUpdate) {
          "invalid UpdatePacket type $packet"
        }

        val tiles: List<MapTile> = requireNotNull(mapChunkUpdate.tileDictionary?.toMapTileList()) {
          "invalid MapChunkUpdate packet, tiles must not be null"
        }
        require(tiles.isNotEmpty()) {
          "invalid MapChunkUpdate packet, tiles must not be empty"
        }

        MapTiles(mapChunkUpdate.surfaceIndex, tiles)
      }


  // group tiles by server & surface & chunk
  val chunkedTilesTable: KTable<ServerMapChunkId, ServerMapChunkTiles<TileProtoHashCode>> =
    mapChunkUpdatesStream
//    mapTilesStream
//      .merge("merge-mapTilesStream-with-mapChunkTilesStream", mapChunkTilesStream)
//      .merge("merge-mapTilesStream-with-mapChunkUpdatesStream", mapChunkUpdatesStream)
//      .filter { _, value -> !value?.tiles.isNullOrEmpty() }
      .flatMap("server-map-data.tiles.flatMapByChunk") { key: FactorioServerId, mapTiles: MapTiles ->

        val standardChunkSize = ChunkSize.MAX

        mapTiles
          .tiles
          .groupBy { tile ->
            tile.position.toMapChunkPosition(standardChunkSize.tilesPerChunk)
          }
          .map { (chunkPos, tiles) ->

            val chunkId = ServerMapChunkId(
              key,
              chunkPos,
              mapTiles.surfaceIndex,
              standardChunkSize,
            )

            val tilesMap = tiles.associate { it.position to TileProtoHashCode(it) }

            val chunkTiles = ServerMapChunkTiles(chunkId, tilesMap)

            chunkId to chunkTiles
          }
      }
//      .filter("server-map-data.tiles.filter-not-empty") { _, value ->
//        value.map.isNotEmpty()
//      }
      .groupByKey(
        groupedAs(
          "server-map-data.tiles.group",
          kxsBinary.serde<ServerMapChunkId>(),
          kxsBinary.serde<ServerMapChunkTiles<TileProtoHashCode>>()
        )
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

val MapTile.position
  get() = MapTilePosition(x, y)
