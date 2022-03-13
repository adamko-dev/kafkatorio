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
import dev.adamko.kotka.extensions.materializedAs
import dev.adamko.kotka.extensions.materializedWith
import dev.adamko.kotka.extensions.producedAs
import dev.adamko.kotka.extensions.repartitionedAs
import dev.adamko.kotka.extensions.streams.filter
import dev.adamko.kotka.extensions.streams.flatMap
import dev.adamko.kotka.extensions.streams.mapValues
import dev.adamko.kotka.extensions.streams.peek
import dev.adamko.kotka.extensions.streams.reduce
import dev.adamko.kotka.extensions.streams.toTable
import dev.adamko.kotka.extensions.tableJoined
import dev.adamko.kotka.extensions.tables.filter
import dev.adamko.kotka.extensions.tables.join
import dev.adamko.kotka.extensions.tables.mapValues
import dev.adamko.kotka.extensions.tables.toStream
import dev.adamko.kotka.kxs.serde
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
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

  val mapChunksUpdatesStream: KStream<FactorioServerId, FactorioEventUpdatePacket> =
    builder.stream(
      mapChunkUpdatesTopicName,
      consumedAs(
        "consume.map-chunk-updates",
        jsonMapper.serde<FactorioServerId>(),
        jsonMapper.serde<FactorioEventUpdatePacket>(),
      )
    )

  val groupedMapChunkTiles: KTable<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> =
    groupTilesIntoChunksWithColours(
      mapChunksUpdatesStream,
      tileProtoColourDict,
    )

  groupedMapChunkTiles
    .toStream("stream-grouped-map-tiles")
    .filter("stream-grouped-map-tiles.filter-not-null") { _, v ->
      !v?.map.isNullOrEmpty()
    }
    .mapValues("stream-grouped-map-tiles.map-not-null") { _, v ->
      v as ServerMapChunkTiles<ColourHex>
    }
    .peek("stream-grouped-map-tiles.peek") { _, v ->
      println("Grouping map tiles result: ${v.chunkId} / ${v.map.size}")
    }
    .to(
      KafkatorioTopology.TOPIC_GROUPED_MAP_CHUNKS,
      producedAs(
        "produce.grouped-map-chunks",
        kxsBinary.serde<ServerMapChunkId>(),
        kxsBinary.serde<ServerMapChunkTiles<ColourHex>>()
      )
    )

  return builder.build()
}


private val coroutineContext: CoroutineContext =
  Dispatchers.Default + SupervisorJob() + CoroutineName("groupTilesIntoChunksWithColours")


private fun groupTilesIntoChunksWithColours(
  mapChunkUpdatePacketsStream: KStream<FactorioServerId, FactorioEventUpdatePacket>,
  tileProtoColourDict: KTable<FactorioServerId, TileColourDict>,
): KTable<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> {

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


  val chunkedTilesTable: KTable<ServerMapChunkId, ServerMapChunkTiles<TileProtoHashCode>> =
    mapChunkUpdatesStream
      // group tiles by server & surface & chunk
      .flatMap("server-map-data.tiles.flatMapByChunk") { serverId: FactorioServerId, mapTiles: MapTiles ->

        val standardChunkSize = ChunkSize.MAX

        val flatMappedChunkTiles: List<Pair<ServerMapChunkId, ServerMapChunkTiles<TileProtoHashCode>>> =
          //        println("[${System.currentTimeMillis()}] Flat-mapped ${flatMappedChunkTiles.size} chunk, with ${flatMappedChunkTiles.joinToString { it.second.map.size.toString() }} tiles")
          runBlocking(coroutineContext) {

            mapTiles
              .tiles
              .groupBy { tile ->
                tile.position.toMapChunkPosition(standardChunkSize.tilesPerChunk)
              }
              .map { (chunkPos, tiles) ->

                async {

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
              }.awaitAll()

//        println("[${System.currentTimeMillis()}] Flat-mapped ${flatMappedChunkTiles.size} chunk, with ${flatMappedChunkTiles.joinToString { it.second.map.size.toString() }} tiles")

          }
        flatMappedChunkTiles
      }
      .filter("server-map-data.tiles.filter-not-empty") { _, value ->
        value.map.isNotEmpty()
      }
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
          "server-map-data.tiles.store",
          kxsBinary.serde<ServerMapChunkId>(),
          kxsBinary.serde<ServerMapChunkTiles<TileProtoHashCode>>(),
        )
      ) { chunkTiles, other -> chunkTiles + other }
      .toStream("server-map-data.tiles.repartition.to-stream")
      .repartition(
        repartitionedAs(
          "server-map-data.tiles.repartition.config",
          kxsBinary.serde<ServerMapChunkId>(),
          kxsBinary.serde<ServerMapChunkTiles<TileProtoHashCode>?>(),
          3,
        )
      ).toTable(
        "server-map-data.tiles.repartition.to-table",
        materializedAs(
          "server-map-data.tiles.repartition.to-table.store",
          kxsBinary.serde<ServerMapChunkId>(),
          kxsBinary.serde<ServerMapChunkTiles<TileProtoHashCode>?>(),
        )
      )
      .filter("server-map-data.tiles.repartition.filter-not-null") { _: ServerMapChunkId, v: ServerMapChunkTiles<TileProtoHashCode>? ->
        v != null
      }
      .mapValues(
        "server-map-data.tiles.repartition.map-not-null",
        materializedWith(
          kxsBinary.serde<ServerMapChunkId>(),
          kxsBinary.serde<ServerMapChunkTiles<TileProtoHashCode>>(),
        )
      ) { _: ServerMapChunkId, v: ServerMapChunkTiles<TileProtoHashCode>? ->
        v as ServerMapChunkTiles<TileProtoHashCode>
      }


  // repartition to try and fix KTable-KTable joining skipping records
  val tileProtoColourDictRepartitioned =
    tileProtoColourDict
      .toStream("tileProtoColourDict.repartition.to-stream")
      .repartition(
        repartitionedAs(
          "tileProtoColourDict.repartition.config",
          kxsBinary.serde<FactorioServerId>(),
          kxsBinary.serde<TileColourDict?>(),
          3,
        )
      ).toTable(
        "tileProtoColourDict.repartition.to-table",
        materializedAs(
          "tileProtoColourDict.repartition.to-table.store",
          kxsBinary.serde<FactorioServerId>(),
          kxsBinary.serde<TileColourDict?>(),
        )
      )
      .filter("tileProtoColourDict.repartition.filter-not-null") { _: FactorioServerId, v: TileColourDict? ->
        v != null
      }
      .mapValues(
        "tileProtoColourDict.repartition.map-not-null",
        materializedWith(
          kxsBinary.serde<FactorioServerId>(),
          kxsBinary.serde<TileColourDict>(),
        )
      ) { _: FactorioServerId, v: TileColourDict? ->
        v as TileColourDict
      }


  val colourisedChunkTable: KTable<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> =
    chunkedTilesTable
      .join(
        other = tileProtoColourDictRepartitioned,
        tableJoined = tableJoined("server-map-data.join-tiles-with-prototypes"),
        materialized = materializedAs(
          "server-map-data.join-tiles-with-prototypes.store",
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

        println("Set tile colours for chunk ${tiles.chunkId}")

        ServerMapChunkTiles(tiles.chunkId, tileColours)
      }

  return colourisedChunkTable
}

val MapTile.position
  get() = MapTilePosition(x, y)
