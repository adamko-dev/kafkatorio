package dev.adamko.kafkatorio.processor.topology

import dev.adamko.kafkatorio.processor.admin.TOPIC_GROUPED_MAP_CHUNKS_STATE
import dev.adamko.kafkatorio.processor.admin.TOPIC_GROUPED_MAP_CHUNKS_STATE_DEBOUNCED
import dev.adamko.kafkatorio.processor.misc.DebounceProcessor.Companion.addDebounceProcessor
import dev.adamko.kafkatorio.processor.serdes.kxsBinary
import dev.adamko.kafkatorio.schema.common.ColourHex
import dev.adamko.kafkatorio.schema.common.MapChunkPosition
import dev.adamko.kafkatorio.schema.common.MapTile
import dev.adamko.kafkatorio.schema.common.MapTileDictionary.Companion.isNullOrEmpty
import dev.adamko.kafkatorio.schema.common.MapTilePosition
import dev.adamko.kafkatorio.schema.common.MapTiles
import dev.adamko.kafkatorio.schema.common.SurfaceIndex
import dev.adamko.kafkatorio.schema.common.toMapChunkPosition
import dev.adamko.kafkatorio.schema.packets.MapChunkUpdate
import dev.adamko.kafkatorio.schema.packets.PrototypesUpdate
import dev.adamko.kotka.extensions.groupedAs
import dev.adamko.kotka.extensions.materializedAs
import dev.adamko.kotka.extensions.producedAs
import dev.adamko.kotka.extensions.repartitionedAs
import dev.adamko.kotka.extensions.streams.filter
import dev.adamko.kotka.extensions.streams.flatMap
import dev.adamko.kotka.extensions.streams.mapValues
import dev.adamko.kotka.extensions.streams.peek
import dev.adamko.kotka.extensions.streams.reduce
import dev.adamko.kotka.extensions.tableJoined
import dev.adamko.kotka.extensions.tables.join
import dev.adamko.kotka.extensions.tables.toStream
import dev.adamko.kotka.kxs.serde
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.time.Duration.Companion.seconds
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


fun groupMapChunks(builder: StreamsBuilder): Topology {

  val mapChunksStream: KStream<FactorioServerId, MapChunkUpdate> = builder.streamPacketData()
  val protosStream: KStream<FactorioServerId, PrototypesUpdate> = builder.streamPacketData()

  val tileProtoColourDict: KTable<FactorioServerId, TileColourDict> =
    tileProtoColourDictionary(protosStream)

  val groupedMapChunkTiles: KTable<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> =
    groupTilesIntoChunksWithColours(
      mapChunksStream,
      tileProtoColourDict,
    )

  val pid = "grouped-map-chunk-tiles.output"
  groupedMapChunkTiles
    .toStream("$pid.to-stream")
    .filter("$pid.filter-tiles-not-empty") { _, v ->
      !v?.map.isNullOrEmpty()
    }
    .mapValues("$pid.map-not-null") { _, v ->
      requireNotNull(v)
    }
    .peek("$pid.print-group-result") { _, v ->
      println("Grouping map tiles result: ${v.chunkId} / ${v.map.size}")
    }
    .to(
      TOPIC_GROUPED_MAP_CHUNKS_STATE,
      producedAs(
        "$pid.grouped-map-chunks",
        kxsBinary.serde(),
        kxsBinary.serde(),
      )
    )

  return builder.build()
    .addDebounceProcessor<ServerMapChunkId, ServerMapChunkTiles<ColourHex>>(
      namePrefix = pid,
      sourceTopic = TOPIC_GROUPED_MAP_CHUNKS_STATE,
      sinkTopic = TOPIC_GROUPED_MAP_CHUNKS_STATE_DEBOUNCED,
      inactivityDuration = 15.seconds,
      keySerde = kxsBinary.serde(),
      valueSerde = kxsBinary.serde(),
    )
}


private fun groupTilesIntoChunksWithColours(
  mapChunksStream: KStream<FactorioServerId, MapChunkUpdate>,
  tileProtoColourDict: KTable<FactorioServerId, TileColourDict>,
): KTable<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> {


  val mapTilesStream: KStream<FactorioServerId, MapTiles> =
    mapChunksStream.filter { _, value ->
      !value.tileDictionary.isNullOrEmpty()
    }.mapValues("map-chunk-update-packets.convert-to-map-tiles") { _, packet: MapChunkUpdate ->
      val mapTiles = packet.tileDictionary?.toMapTileList() ?: emptyList()
      MapTiles(packet.key.surfaceIndex, mapTiles)
    }.filter("map-chunk-update-packets.filter-out.no-tiles") { _, value ->
      value.tiles.isNotEmpty()
    }
//      .peek { _, value ->
//      println("[${System.currentTimeMillis()}] mapTilesStream: surface:${value.surfaceIndex}, tile count: ${value.tiles.size}")
//    }


  val chunkedTilesTable: KTable<ServerMapChunkId, ServerMapChunkTiles<TileProtoHashCode>> =
    mapTilesStream
      // group tiles by server & surface & chunk
      .flatMap("server-map-data.tiles.flatMapByChunk") { serverId: FactorioServerId, mapTiles: MapTiles ->

        val standardChunkSize = ChunkSize.MAX

        val flatMappedChunkTiles: List<Pair<ServerMapChunkId, ServerMapChunkTiles<TileProtoHashCode>>> =
          mapTiles
            .tiles
            .groupBy { tile ->
              tile.position.toMapChunkPosition(standardChunkSize.lengthInTiles)
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

//        println("[${System.currentTimeMillis()}] Flat-mapped ${flatMappedChunkTiles.size} chunk, with ${flatMappedChunkTiles.joinToString { it.second.map.size.toString() }} tiles")

        flatMappedChunkTiles
      }
      .filter("server-map-data.tiles.filter-not-empty") { _, value ->
        value.map.isNotEmpty()
      }
      .repartition(
        repartitionedAs(
          "server-map-data.tiles.pre-table-repartition",
          kxsBinary.serde(),
          kxsBinary.serde(),
          // force, otherwise KTable-KTable FK join doesn't work
          numberOfPartitions = 1,
        )
      )
      .groupByKey(
        groupedAs(
          "server-map-data.tiles.group",
          kxsBinary.serde<ServerMapChunkId>(),
          kxsBinary.serde<ServerMapChunkTiles<TileProtoHashCode>>(),
        )
      )
      .reduce(
        "server-map-data.tiles.reduce",
        materializedAs(
          "server-map-data.tiles.reduce.store",
          kxsBinary.serde<ServerMapChunkId>(),
          kxsBinary.serde<ServerMapChunkTiles<TileProtoHashCode>>(),
        )
      ) { chunkTiles, other ->
//        println(
//          "[${System.currentTimeMillis()}] reducing tiles " +
//              "chunkTiles:${chunkTiles.chunkId.chunkPosition}, ${chunkTiles.chunkId.chunkSize}, ${chunkTiles.map.size} " +
//              "other:${other.chunkId.chunkPosition}, ${other.chunkId.chunkSize}, ${other.map.size}"
//        )
        chunkTiles + other
      }

  val colourisedChunkTable: KTable<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> =
    chunkedTilesTable
      .join(
        other = tileProtoColourDict,
        tableJoined = tableJoined("server-map-data.join-tiles-with-prototypes"),
        materialized = materializedAs(
          "server-map-data.join-tiles-with-prototypes.store",
          kxsBinary.serde(),
          kxsBinary.serde(),
        ),
        foreignKeyExtractor = { chunkTiles: ServerMapChunkTiles<TileProtoHashCode> ->
          chunkTiles.chunkId.serverId
        }
      ) { tiles: ServerMapChunkTiles<TileProtoHashCode>, colourDict: TileColourDict ->
//        println("joining tiles:${tiles.map.size} with colourDict:${colourDict.map.size}")

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

        println("Set tile colours for chunk ${tiles.chunkId}")

        ServerMapChunkTiles(tiles.chunkId, tileColours)
      }

  return colourisedChunkTable
}

val MapTile.position
  get() = MapTilePosition(x, y)
