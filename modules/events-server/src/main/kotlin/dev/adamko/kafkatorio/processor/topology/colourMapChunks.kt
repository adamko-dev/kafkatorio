package dev.adamko.kafkatorio.processor.topology

import dev.adamko.kafkatorio.processor.admin.TOPIC_GROUPED_MAP_CHUNKS_STATE
import dev.adamko.kafkatorio.processor.serdes.kxsBinary
import dev.adamko.kafkatorio.schema.common.ChunkSize
import dev.adamko.kafkatorio.schema.common.ColourHex
import dev.adamko.kafkatorio.schema.common.leftTopTile
import dev.adamko.kafkatorio.schema.common.toMapChunkPosition
import dev.adamko.kafkatorio.schema.packets.MapChunkUpdate
import dev.adamko.kafkatorio.schema.packets.PrototypesUpdate
import dev.adamko.kotka.extensions.consumedAs
import dev.adamko.kotka.extensions.groupedAs
import dev.adamko.kotka.extensions.materializedAs
import dev.adamko.kotka.extensions.producedAs
import dev.adamko.kotka.extensions.repartitionedAs
import dev.adamko.kotka.extensions.streams.filter
import dev.adamko.kotka.extensions.streams.map
import dev.adamko.kotka.extensions.streams.mapValues
import dev.adamko.kotka.extensions.streams.peek
import dev.adamko.kotka.extensions.streams.reduce
import dev.adamko.kotka.extensions.tableJoined
import dev.adamko.kotka.extensions.tables.join
import dev.adamko.kotka.extensions.tables.toStream
import dev.adamko.kotka.kxs.serde
import kotlin.collections.component1
import kotlin.collections.component2
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.KTable


private const val pid: String = "colourMapChunks"


/** Add colour to each [MapChunkUpdate] */
fun colourMapChunks(builder: StreamsBuilder): Topology {

  val mapChunksStream: KStream<FactorioServerId, MapChunkUpdate> = builder.streamPacketData()
  val protosStream: KStream<FactorioServerId, PrototypesUpdate> = builder.streamPacketData()

  val protosTable: KTable<FactorioServerId, TileColourDict> =
    protosStream.createTilePrototypeTable()

  val chunkTilesColouredTable032: KTable<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> =
    mapChunksStream
      .convertToServerMapChunkTiles()
      .reduceServerMapChunkTilesToTable()
      .enrichWithColourData(protosTable)

  chunkTilesColouredTable032.streamTo(ChunkSize.CHUNK_032, TOPIC_GROUPED_MAP_CHUNKS_STATE)

  val groupedMapChunks = builder.stream(
    TOPIC_GROUPED_MAP_CHUNKS_STATE,
    consumedAs(
      "$pid.consume.grouped-map-chunks",
      kxsBinary.serde<ServerMapChunkId>(),
      kxsBinary.serde<ServerMapChunkTiles<ColourHex>>(),
    )
  )

  mapOf(
    ChunkSize.CHUNK_032 to ChunkSize.CHUNK_064,
    ChunkSize.CHUNK_064 to ChunkSize.CHUNK_128,
    ChunkSize.CHUNK_128 to ChunkSize.CHUNK_256,
    ChunkSize.CHUNK_256 to ChunkSize.CHUNK_512,
  ).forEach { (from, to) ->
    groupedMapChunks
      .filter("$pid.combine-chunks.filter-$from-to-$to") { key, _ ->
      key?.chunkSize == from
    }.groupByChunkPosition(to)
      .streamTo(to, TOPIC_GROUPED_MAP_CHUNKS_STATE)
  }

  return builder.build()
}


private fun KStream<FactorioServerId, MapChunkUpdate>.convertToServerMapChunkTiles()
    : KStream<ServerMapChunkId, ServerMapChunkTiles<TileProtoHashCode>> {

  val pid = "$pid.convertToMapTiles"

  return map("$pid.map") { serverId: FactorioServerId, update: MapChunkUpdate ->

    val mapTileList = update.tileDictionary?.toMapTileList() ?: emptyList()

    val (validMapTilesList, invalidMapTilesList) = mapTileList.partition {
      it.position in update.key.chunkPosition
    }

    if (invalidMapTilesList.isNotEmpty()) {
      println("WARNING [reduceMapTilesToTable] MapChunkUpdate contained out-of-bounds tiles $invalidMapTilesList")
    }

    val validMapTilesToProtoHashCode =
      validMapTilesList.associate { it.position to TileProtoHashCode(it) }

    val chunkId = ServerMapChunkId(
      serverId = serverId,
      chunkPosition = update.key.chunkPosition,
      surfaceIndex = update.key.surfaceIndex,
      chunkSize = ChunkSize.CHUNK_032,
    )

    val chunkTiles = ServerMapChunkTiles(chunkId, validMapTilesToProtoHashCode)

    chunkId to chunkTiles
  }
}


private fun KStream<ServerMapChunkId, ServerMapChunkTiles<TileProtoHashCode>>.reduceServerMapChunkTilesToTable()
    : KTable<ServerMapChunkId, ServerMapChunkTiles<TileProtoHashCode>> {

  val pid = "$pid.reduceServerMapChunkTilesToTable"

  return repartition(
    repartitionedAs(
      "$pid.pre-table-repartition",
      kxsBinary.serde<ServerMapChunkId>(),
      kxsBinary.serde<ServerMapChunkTiles<TileProtoHashCode>>(),
      // force, otherwise KTable-KTable FK join doesn't work
      numberOfPartitions = 1,
    )
  ).groupByKey(
    groupedAs(
      "$pid.group-by-key",
      kxsBinary.serde<ServerMapChunkId>(),
      kxsBinary.serde<ServerMapChunkTiles<TileProtoHashCode>>(),
    )
  ).reduce(
    "$pid.reduce",
    materializedAs(
      "$pid.reduce.store",
      kxsBinary.serde<ServerMapChunkId>(),
      kxsBinary.serde<ServerMapChunkTiles<TileProtoHashCode>>(),
    )
  ) { chunkTiles, otherChunkTiles ->
    chunkTiles + otherChunkTiles
  }
}


private fun KTable<ServerMapChunkId, ServerMapChunkTiles<TileProtoHashCode>>.enrichWithColourData(
  protosTable: KTable<FactorioServerId, TileColourDict>,
): KTable<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> {

  val pid = "$pid.enrichWithColourData"

  return this.join(
    other = protosTable,
    tableJoined = tableJoined("$pid.join-tiles-with-prototypes"),
    materialized = materializedAs(
      "$pid.join-tiles-with-prototypes.store",
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
      println("[$pid] missing ${missingProtos.size} tile prototypes: ${missingProtos.joinToString { "${it.code}" }}")
    }

    println("[$pid] Set tile colours for chunk ${tiles.chunkId}")

    ServerMapChunkTiles(tiles.chunkId, tileColours)
  }
}


private fun KTable<ServerMapChunkId, ServerMapChunkTiles<ColourHex>>.streamTo(
  chunkSize: ChunkSize,
  outputTopic: String = TOPIC_GROUPED_MAP_CHUNKS_STATE,
) {
  val pid = "$pid.output-chunk.${chunkSize.name}"

  toStream("$pid.stream")
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
      outputTopic,
      producedAs(
        "$pid.grouped-map-chunks",
        kxsBinary.serde(),
        kxsBinary.serde(),
      )
    )
}


///**
// * Group [MapChunkUpdate]s by the Chunk position.
// *
// * They should already be grouped, but do it again to make sure, and to filter out empty updates.
// */
//private fun KTable<ServerMapChunkId, ServerMapChunkTiles<ColourHex>>.groupByChunkPosition(
//  chunkSize: ChunkSize = ChunkSize.CHUNK_032
//): KTable<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> {
//
//  val pid = "$pid.groupByChunkPosition.${chunkSize.name}"
//
//  return toStream("$pid.intake-stream")
//    .map("$pid.change-chunk-size") { chunkId: ServerMapChunkId, chunkTiles: ServerMapChunkTiles<ColourHex>? ->
//
//      val newChunkPosition = chunkId.chunkPosition
//        .leftTopTile(chunkId.chunkSize)
//        .toMapChunkPosition(chunkSize)
//
//      val newId = chunkId.copy(
//        chunkSize = chunkSize,
//        chunkPosition = newChunkPosition,
//      )
//
//      val newChunkTiles = chunkTiles?.map ?: emptyMap()
//
//      newId to ServerMapChunkTiles(chunkId = newId, map = newChunkTiles)
//    }.groupByKey(
//      groupedAs(
//        "$pid.group-by-key",
//        kxsBinary.serde<ServerMapChunkId>(),
//        kxsBinary.serde<ServerMapChunkTiles<ColourHex>>(),
//      )
//    ).reduce(
//      "$pid.reduce",
//      materializedAs(
//        "$pid.reduce.store",
//        kxsBinary.serde<ServerMapChunkId>(),
//        kxsBinary.serde<ServerMapChunkTiles<ColourHex>>(),
//      )
//    ) { chunkTiles, otherChunkTiles ->
//      chunkTiles + otherChunkTiles
//    }
//}

/**
 * Group [MapChunkUpdate]s by the Chunk position.
 *
 * They should already be grouped, but do it again to make sure, and to filter out empty updates.
 */
private fun KStream<ServerMapChunkId, ServerMapChunkTiles<ColourHex>>.groupByChunkPosition(
  chunkSize: ChunkSize = ChunkSize.CHUNK_032
): KTable<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> {

  val pid = "$pid.groupByChunkPosition.${chunkSize.name}"

  return map("$pid.change-chunk-size") { chunkId: ServerMapChunkId, chunkTiles: ServerMapChunkTiles<ColourHex>? ->

    val newChunkPosition = chunkId.chunkPosition
      .leftTopTile(chunkId.chunkSize)
      .toMapChunkPosition(chunkSize)

    val newId = chunkId.copy(
      chunkSize = chunkSize,
      chunkPosition = newChunkPosition,
    )

    val newChunkTiles = chunkTiles?.map ?: emptyMap()

    newId to ServerMapChunkTiles(chunkId = newId, map = newChunkTiles)
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
