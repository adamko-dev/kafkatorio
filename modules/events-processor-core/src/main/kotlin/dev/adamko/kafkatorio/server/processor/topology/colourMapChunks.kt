package dev.adamko.kafkatorio.server.processor.topology

import dev.adamko.kafkatorio.library.kxsBinary
import dev.adamko.kafkatorio.library.resize
import dev.adamko.kafkatorio.library.toMapChunkPosition
import dev.adamko.kafkatorio.processor.core.DebounceProcessor.Companion.addDebounceProcessor
import dev.adamko.kafkatorio.schema.common.ChunkSize
import dev.adamko.kafkatorio.schema.common.ColourHex
import dev.adamko.kafkatorio.schema.common.FactorioServerId
import dev.adamko.kafkatorio.schema.common.MapTilePosition
import dev.adamko.kafkatorio.schema.common.PrototypeHashCode
import dev.adamko.kafkatorio.schema.common.ServerMapChunkId
import dev.adamko.kafkatorio.schema.common.ServerMapTileLayer
import dev.adamko.kafkatorio.schema.packets.MapChunkTileUpdate
import dev.adamko.kafkatorio.schema.packets.PrototypesUpdate
import dev.adamko.kafkatorio.processor.config.TOPIC_MAP_CHUNK_TERRAIN_COLOURED_032_UPDATES
import dev.adamko.kafkatorio.processor.config.TOPIC_MAP_CHUNK_TERRAIN_COLOURED_STATE
import dev.adamko.kotka.extensions.consumedAs
import dev.adamko.kotka.extensions.groupedAs
import dev.adamko.kotka.extensions.materializedAs
import dev.adamko.kotka.extensions.producedAs
import dev.adamko.kotka.extensions.repartitionedAs
import dev.adamko.kotka.extensions.stream
import dev.adamko.kotka.extensions.streams.filter
import dev.adamko.kotka.extensions.streams.flatMap
import dev.adamko.kotka.extensions.streams.map
import dev.adamko.kotka.extensions.streams.peek
import dev.adamko.kotka.extensions.streams.reduce
import dev.adamko.kotka.extensions.tableJoined
import dev.adamko.kotka.extensions.tables.join
import dev.adamko.kotka.extensions.tables.toStream
import dev.adamko.kotka.kxs.serde
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.time.Duration.Companion.seconds
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.KTable


/** Add colour to each [MapChunkTileUpdate] */
fun colourMapChunks(
  builder: StreamsBuilder,
  protosStream: KStream<FactorioServerId, PrototypesUpdate>
): Topology {
  val pid = "colourMapChunks.terrain"

  val mapChunksStream: KStream<FactorioServerId, MapChunkTileUpdate> = builder.streamPacketData()

  val protosTable: KTable<FactorioServerId, TileColourDict> =
    protosStream.createTilePrototypeTable()

  val chunkTilesColouredTable032: KTable<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> =
    mapChunksStream
      .convertToServerMapChunkTiles(pid)
      .reduceServerMapChunkTilesToTable(pid)
      .enrichWithColourData(pid, protosTable)

  chunkTilesColouredTable032.streamMapChunkColouredTo(
    "$pid.${ChunkSize.CHUNK_032}",
    TOPIC_MAP_CHUNK_TERRAIN_COLOURED_032_UPDATES,
  )

  val chunkTilesColoured: KStream<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> =
    builder.stream(
      consumedAs(
        "$pid.consume.map-chunks-debounced",
        kxsBinary.serde(ServerMapChunkId.serializer()),
        kxsBinary.serde(ServerMapChunkTiles.serializer(ColourHex.serializer())),
      ),
      TOPIC_MAP_CHUNK_TERRAIN_COLOURED_STATE,
    )

  mapOf(
//    ChunkSize.CHUNK_032 to ChunkSize.CHUNK_064,
//    ChunkSize.CHUNK_064 to ChunkSize.CHUNK_128,
//    ChunkSize.CHUNK_128 to ChunkSize.CHUNK_256,
//    ChunkSize.CHUNK_256 to ChunkSize.CHUNK_512,

    ChunkSize.CHUNK_032 to ChunkSize.CHUNK_256,
    ChunkSize.CHUNK_256 to ChunkSize.CHUNK_512,
  ).forEach { (from, to) ->
    chunkTilesColoured
      .filter("$pid.combine-chunks.filter-$from-to-$to") { key: ServerMapChunkId, _: ServerMapChunkTiles<ColourHex> ->
        key.chunkSize == from
      }.groupByChunkPosition("$pid.from-$from-to-$to", to)
      .streamMapChunkColouredTo(
        "$pid.from-$from-to-$to",
        TOPIC_MAP_CHUNK_TERRAIN_COLOURED_STATE
      )
  }

  return builder.build()
    .addDebounceProcessor(
      namePrefix = "groupMapChunks.terrain",
      sourceTopic = TOPIC_MAP_CHUNK_TERRAIN_COLOURED_032_UPDATES,
      sinkTopic = TOPIC_MAP_CHUNK_TERRAIN_COLOURED_STATE,
      inactivityDuration = 15.seconds,
      keySerde = kxsBinary.serde(ServerMapChunkId.serializer()),
      valueSerde = kxsBinary.serde(ServerMapChunkTiles.serializer(ColourHex.serializer())),
    )
}


private fun KStream<FactorioServerId, MapChunkTileUpdate>.convertToServerMapChunkTiles(
  callerPid: String
): KStream<ServerMapChunkId, ServerMapChunkTiles<PrototypeHashCode>> {

  val pid = "$callerPid.convertToMapTiles"

  return map("$pid.map") { serverId: FactorioServerId, update: MapChunkTileUpdate ->

    val mapTileList = update.tileDictionary?.toMapTileList() ?: emptyList()

    val (validMapTiles, invalidMapTiles) = mapTileList.partition {
      it.position in update.key.chunkPosition
    }

    if (invalidMapTiles.isNotEmpty()) {
      println(
        "WARNING [$pid] MapChunkTileUpdate contained ${invalidMapTiles.size} out-of-bounds tiles, " +
            "key: ${update.key},\n" +
            "invalid tiles:" + invalidMapTiles.joinToString(limit = 10)
      )
    }

    val validMapTilesToProtoHashCode = validMapTiles.associate {
      it.position to PrototypeHashCode(it.protoId)
    }

    val chunkId = ServerMapChunkId(
      serverId = serverId,
      layer = ServerMapTileLayer.TERRAIN,
      chunkPosition = update.key.chunkPosition,
      surfaceIndex = update.key.surfaceIndex,
    )

    chunkId to ServerMapChunkTiles(chunkId, validMapTilesToProtoHashCode)
  }
}


fun KStream<ServerMapChunkId, ServerMapChunkTiles<PrototypeHashCode>>.reduceServerMapChunkTilesToTable(
  callerPid: String,
): KTable<ServerMapChunkId, ServerMapChunkTiles<PrototypeHashCode>> {

  val pid = "$callerPid.reduceServerMapChunkTilesToTable"

  return repartition(
    repartitionedAs(
      "$pid.pre-table-repartition",
      kxsBinary.serde<ServerMapChunkId>(),
      kxsBinary.serde<ServerMapChunkTiles<PrototypeHashCode>>(),
      // force, otherwise KTable-KTable FK join doesn't work
      numberOfPartitions = 1,
    )
  ).groupByKey(
    groupedAs(
      "$pid.group-by-key",
      kxsBinary.serde<ServerMapChunkId>(),
      kxsBinary.serde<ServerMapChunkTiles<PrototypeHashCode>>(),
    )
  ).reduce(
    "$pid.reduce",
    materializedAs(
      "$pid.reduce.store",
      kxsBinary.serde<ServerMapChunkId>(),
      kxsBinary.serde<ServerMapChunkTiles<PrototypeHashCode>>(),
    )
  ) { chunkTiles, otherChunkTiles ->
    chunkTiles + otherChunkTiles
  }
}


private fun KTable<ServerMapChunkId, ServerMapChunkTiles<PrototypeHashCode>>.enrichWithColourData(
  callerPid: String,
  protosTable: KTable<FactorioServerId, TileColourDict>,
): KTable<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> {

  val pid = "$callerPid.enrichWithColourData"

  return join(
    other = protosTable,
    tableJoined = tableJoined("$pid.join-tiles-with-prototypes"),
    materialized = materializedAs(
      "$pid.join-tiles-with-prototypes.store",
      kxsBinary.serde(),
      kxsBinary.serde(),
    ),
    foreignKeyExtractor = { chunkTiles: ServerMapChunkTiles<PrototypeHashCode> ->
      chunkTiles.chunkId.serverId
    }
  ) { tiles: ServerMapChunkTiles<PrototypeHashCode>, colourDict: TileColourDict ->
    println("joining tiles:${tiles.map.size} with colourDict:${colourDict.size}")

    val missingProtos = mutableSetOf<PrototypeHashCode>()

    val tileColours: Map<MapTilePosition, ColourHex> =
      tiles.map.mapNotNull { (position, code) ->
        when (val colour = colourDict[code]) {
          null -> {
            missingProtos.add(code)
            null
          }
          else -> position to colour
        }
      }.toMap()

    if (missingProtos.isNotEmpty()) {
      println("[$pid] missing ${missingProtos.size} tile prototypes: ${missingProtos.joinToString { "${it.code}" }}")
    }

    println("[$pid] Set ${tileColours.size} tile colours for chunk ${tiles.chunkId}")

    ServerMapChunkTiles(tiles.chunkId, tileColours)
  }
}


fun KTable<ServerMapChunkId, ServerMapChunkTiles<ColourHex>>.streamMapChunkColouredTo(
  callerPid: String,
  outputTopic: String,
) {
  val pid = "$callerPid.output-chunk"

  toStream("$pid.stream")
    .flatMap("$pid.group-by-chunks") { _: ServerMapChunkId, tiles: ServerMapChunkTiles<ColourHex>? ->
      reChunkTiles(pid, tiles)
    }.peek("$pid.print-group-result") { _, chunkTiles ->
      println("[$pid] Grouping map tiles result: ${chunkTiles.chunkId} / size:${chunkTiles.map.size}")
    }.to(
      outputTopic,
      producedAs(
        "$pid.grouped-map-chunks",
        kxsBinary.serde<ServerMapChunkId>(),
        kxsBinary.serde<ServerMapChunkTiles<ColourHex>>(),
      )
    )
}


/**
 * Group [MapChunkTileUpdate]s by the Chunk position.
 *
 * They should already be grouped, but do it again to make sure, and to filter out empty updates.
 */
fun KStream<ServerMapChunkId, ServerMapChunkTiles<ColourHex>>.groupByChunkPosition(
  callerPid: String,
  targetChunkSize: ChunkSize = ChunkSize.CHUNK_032
): KTable<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> {

  val pid = "$callerPid.groupByChunkPosition.${targetChunkSize.name}"

  return flatMap("$pid.change-chunk-size") { _: ServerMapChunkId, chunkTiles: ServerMapChunkTiles<ColourHex>? ->
//    println("[$pid] changing chunk size for $chunkId")
    chunkTiles ?: return@flatMap emptyList()

    val newChunkPosition = chunkTiles.chunkId.chunkPosition.resize(targetChunkSize)
    val newId = chunkTiles.chunkId.copy(chunkPosition = newChunkPosition)
    val newChunkTiles = chunkTiles.copy(chunkId = newId)

    reChunkTiles(pid, newChunkTiles)
  }.groupByKey(
    groupedAs(
      "$pid.group-by-key",
      kxsBinary.serde<ServerMapChunkId>(),
      kxsBinary.serde<ServerMapChunkTiles<ColourHex>>(),
    )
  ).reduce(
    "$pid.reduce",
    materializedAs(
      "$pid.reduce.store",
      kxsBinary.serde<ServerMapChunkId>(),
      kxsBinary.serde<ServerMapChunkTiles<ColourHex>>(),
    )
  ) { chunkTiles, otherChunkTiles ->
    chunkTiles + otherChunkTiles
  }
}


fun <T : Any> reChunkTiles(
  pid: String,
  tiles: ServerMapChunkTiles<T>?,
): List<Pair<ServerMapChunkId, ServerMapChunkTiles<T>>> {

  return when {
    tiles == null || tiles.map.isEmpty() -> emptyList()

    tiles.map.keys.all { it in tiles.chunkId.chunkPosition } -> listOf(tiles.chunkId to tiles)

    else -> {
      val chunkId = tiles.chunkId

      val (boundedTiles, unboundedTiles) = tiles.map.entries.partition { (position, _) ->
        position in chunkId.chunkPosition
      }

      if (unboundedTiles.isNotEmpty()) {
        val unboundedEntities = unboundedTiles.map { (_, entity) -> entity }
        println(
          "WARNING [$pid] MapChunkEntityUpdate $chunkId "
              + "contained ${unboundedEntities.size} out-of-bounds entities "
              + unboundedEntities.joinToString(limit = 10)
        )
      }

      // the existing chunkId and its tiles
      val boundedTilesChunk =
        chunkId to ServerMapChunkTiles(chunkId, boundedTiles.associate { it.key to it.value })

      // re-calculate a chunk for the out-of-bounds tiles
      val chunkedUnboundedTiles = unboundedTiles
        .map { it.key to it.value }
        .groupBy { (position, _) ->
          position.toMapChunkPosition(chunkId.chunkSize)
        }.map { (newChunkPosition, tiles) ->
          val newChunkId = chunkId.copy(chunkPosition = newChunkPosition)
          newChunkId to ServerMapChunkTiles(newChunkId, tiles.toMap())
        }

      println("[$pid] re-bounded ${unboundedTiles.size} tiles into ${chunkedUnboundedTiles.size} chunks in $chunkId")

      listOf(boundedTilesChunk) + chunkedUnboundedTiles
    }
  }
}
