package dev.adamko.kafkatorio.processor.topology

import dev.adamko.kafkatorio.events.schema.ColourHex
import dev.adamko.kafkatorio.events.schema.FactorioEventUpdate
import dev.adamko.kafkatorio.events.schema.FactorioEventUpdatePacket
import dev.adamko.kafkatorio.events.schema.FactorioPrototypes
import dev.adamko.kafkatorio.events.schema.KafkatorioPacket
import dev.adamko.kafkatorio.events.schema.MapChunkPosition
import dev.adamko.kafkatorio.events.schema.MapChunkUpdate
import dev.adamko.kafkatorio.events.schema.MapTile
import dev.adamko.kafkatorio.events.schema.MapTileDictionary.Companion.isNullOrEmpty
import dev.adamko.kafkatorio.events.schema.MapTilePosition
import dev.adamko.kafkatorio.events.schema.MapTiles
import dev.adamko.kafkatorio.events.schema.SurfaceIndex
import dev.adamko.kafkatorio.events.schema.converters.toMapChunkPosition
import dev.adamko.kafkatorio.processor.KafkatorioTopology
import dev.adamko.kafkatorio.processor.serdes.jsonMapper
import dev.adamko.kafkatorio.processor.serdes.kxsBinary
import dev.adamko.kotka.extensions.consumedAs
import dev.adamko.kotka.extensions.groupedAs
import dev.adamko.kotka.extensions.materializedWith
import dev.adamko.kotka.extensions.producedAs
import dev.adamko.kotka.extensions.streams.*
import dev.adamko.kotka.extensions.tables.join
import dev.adamko.kotka.extensions.tables.toStream
import dev.adamko.kotka.kxs.serde
import kotlinx.serialization.Serializable
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.Topology
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


fun groupMapChunks(
  builder: StreamsBuilder,
  prototypesTopicName: String = "kafkatorio.${KafkatorioPacket.PacketType.PROTOTYPES}.all",
  mapChunkUpdatesTopicName: String = "kafkatorio.${KafkatorioPacket.PacketType.UPDATE}.${FactorioEventUpdate.FactorioEventUpdateType.MAP_CHUNK}",
): Topology {

  val protosStream: KStream<FactorioServerId, FactorioPrototypes> =
    builder.stream(
      prototypesTopicName,
      consumedAs("consume.factorio-protos.all", jsonMapper.serde(), jsonMapper.serde())
    )
  val tileProtoColourDict: KTable<FactorioServerId, TileColourDict> =
    tileProtoColourDictionary(protosStream)


//    val mapTilesStream: KStream<FactorioServerId, FactorioEvent> =
//      builder.stream(
//        "kafkatorio.${KafkatorioPacket.PacketType.EVENT}.${FactorioObjectData.ObjectName.LuaTiles}",
//        consumedAs("consume.map-tiles", jsonMapper.serde(), jsonMapper.serde())
//      )
//
//    val mapChunksStream: KStream<FactorioServerId, FactorioEvent> =
//      builder.stream(
//        "kafkatorio.${KafkatorioPacket.PacketType.EVENT}.${FactorioObjectData.ObjectName.MapChunk}",
//        consumedAs("consume.map-chunks", jsonMapper.serde(), jsonMapper.serde())
//      )

  val mapChunksUpdatesStream: KStream<FactorioServerId, FactorioEventUpdatePacket> =
    builder.stream(
      mapChunkUpdatesTopicName,
      consumedAs(
        "consume.map-chunk-updates",
        jsonMapper.serde<FactorioServerId>(),
        jsonMapper.serde<FactorioEventUpdatePacket>(),
      )
    )
//        .repartition(
//        repartitionedAs(
//          "map-chunk-updates.repartition.change-serde",
//          kxsBinary.serde<FactorioServerId>(),
//          kxsBinary.serde<FactorioEventUpdatePacket>(),
//          numberOfPartitions = 3,
//        )
//      )

  val groupedMapChunkTiles: KTable<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> =
    groupTilesIntoChunksWithColours(
//        mapTilesStream,
//        mapChunksStream,
      mapChunksUpdatesStream,
      tileProtoColourDict,
    )

  groupedMapChunkTiles
    .toStream("stream-grouped-map-tiles")
    .to(
      KafkatorioTopology.TOPIC_GROUPED_MAP_CHUNKS,
      producedAs(
        "produce.grouped-map-chunks",
        kxsBinary.serde<ServerMapChunkId?>(),
        kxsBinary.serde<ServerMapChunkTiles<ColourHex>?>()
      )
    )

  return builder.build()
}

private fun groupTilesIntoChunksWithColours(
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
      .flatMap("server-map-data.tiles.flatMapByChunk") { serverId: FactorioServerId, mapTiles: MapTiles ->

        val standardChunkSize = ChunkSize.MAX

        mapTiles
          .tiles
          .groupBy { tile ->
            tile.position.toMapChunkPosition(standardChunkSize.tilesPerChunk)
          }
          .map { (chunkPos, tiles) ->

            val chunkId = ServerMapChunkId(
              serverId = serverId,
              chunkPosition = chunkPos,
              surfaceIndex = mapTiles.surfaceIndex,
              chunkSize = standardChunkSize,
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
        groupedAs(
          "server-map-data.tiles.group",
          kxsBinary.serde<ServerMapChunkId?>(),
          kxsBinary.serde<ServerMapChunkTiles<TileProtoHashCode>?>(),
        )
      )
      .reduce(
        "server-map-data.tiles.reduce", materializedWith(
          //          "server-map-data.tiles.store",
          kxsBinary.serde<ServerMapChunkId>(),
          kxsBinary.serde<ServerMapChunkTiles<TileProtoHashCode>>(),
        ),
        ServerMapChunkTiles<TileProtoHashCode>::plus
      )


  val colourisedChunkTable: KTable<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> =
    chunkedTilesTable
      .join(
        other = tileProtoColourDict,
        name = "server-map-data.join-tiles-with-prototypes",
        materialized = materializedWith(
          kxsBinary.serde<ServerMapChunkId>(),
          kxsBinary.serde<ServerMapChunkTiles<ColourHex>>(),
        ),
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
