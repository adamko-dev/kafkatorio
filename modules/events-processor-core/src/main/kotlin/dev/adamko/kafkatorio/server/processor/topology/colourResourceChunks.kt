package dev.adamko.kafkatorio.server.processor.topology

import dev.adamko.kafkatorio.library.kxsBinary
import dev.adamko.kafkatorio.library.toMapTilePosition
import dev.adamko.kafkatorio.processor.core.DebounceProcessor.Companion.addDebounceProcessor
import dev.adamko.kafkatorio.schema.common.ChunkSize
import dev.adamko.kafkatorio.schema.common.ColourHex
import dev.adamko.kafkatorio.schema.common.FactorioPrototype
import dev.adamko.kafkatorio.schema.common.FactorioServerId
import dev.adamko.kafkatorio.schema.common.MapBoundingBox
import dev.adamko.kafkatorio.schema.common.MapTilePosition
import dev.adamko.kafkatorio.schema.common.PrototypeHashCode
import dev.adamko.kafkatorio.schema.common.ServerMapChunkId
import dev.adamko.kafkatorio.schema.common.ServerMapTileLayer
import dev.adamko.kafkatorio.schema.common.toHex
import dev.adamko.kafkatorio.schema.packets.MapChunkEntityUpdate
import dev.adamko.kafkatorio.schema.packets.MapChunkResourceUpdate
import dev.adamko.kafkatorio.schema.packets.MapChunkTileUpdate
import dev.adamko.kafkatorio.schema.packets.PrototypesUpdate
import dev.adamko.kotka.extensions.consumedAs
import dev.adamko.kotka.extensions.materializedAs
import dev.adamko.kotka.extensions.stream
import dev.adamko.kotka.extensions.streams.filter
import dev.adamko.kotka.extensions.streams.flatMap
import dev.adamko.kotka.extensions.tableJoined
import dev.adamko.kotka.extensions.tables.join
import dev.adamko.kotka.kxs.serde
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.math.floor
import kotlin.time.Duration.Companion.seconds
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.KTable


/**
 * Add colour to each [MapChunkTileUpdate]
 *
 * @param[colouredUpdatesStreamTopic] Output topic for the result of converting input updates into 32 sized chunks
 * @param[colouredChunkStateTopic] Post-debounce state topic, used to recursively combine chunks
 */
fun colourEntityChunks(
  builder: StreamsBuilder,
  entityChunksStream: KStream<ServerMapChunkId, ServerMapChunkTiles<PrototypeHashCode>>,
  protosStream: KStream<FactorioServerId, PrototypesUpdate>,
  layer: ServerMapTileLayer,
  colouredUpdatesStreamTopic: String,
  colouredChunkStateTopic: String,
): Topology {
  val pid = "colourEntityChunks.${layer.dir}"

  val protosTable: KTable<FactorioServerId, EntityPrototypeMap> =
    protosStream.createEntityPrototypeMap(pid)

  val chunkEntitiesColouredTable032: KTable<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> =
    entityChunksStream
      .reduceServerMapChunkTilesToTable(pid)
      .enrichWithColourData(pid, protosTable)

  chunkEntitiesColouredTable032.streamMapChunkColouredTo(
    "$pid.${ChunkSize.CHUNK_032}",
    colouredUpdatesStreamTopic,
  )

  val chunkTilesColoured: KStream<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> =
    builder.stream(
      consumedAs(
        "$pid.consume.map-chunks-debounced",
        kxsBinary.serde(ServerMapChunkId.serializer()),
        kxsBinary.serde(ServerMapChunkTiles.serializer(ColourHex.serializer())),
      ),
      colouredChunkStateTopic,
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
      .filter("$pid.combine-chunks.filter-$from-to-$to") { key, _ ->
        key.chunkSize == from && key.layer == layer
      }.groupByChunkPosition(pid, to)
      .streamMapChunkColouredTo(
        "$pid.from-$from-to-$to",
        colouredChunkStateTopic,
      )
  }

  return builder.build().addDebounceProcessor(
    namePrefix = "groupMapChunks.${layer.dir}",
    sourceTopic = colouredUpdatesStreamTopic,
    sinkTopic = colouredChunkStateTopic,
    inactivityDuration = 15.seconds,
    keySerde = kxsBinary.serde(ServerMapChunkId.serializer()),
    valueSerde = kxsBinary.serde(ServerMapChunkTiles.serializer(ColourHex.serializer())),
  )
}


fun KStream<FactorioServerId, MapChunkEntityUpdate>.convertEntityUpdateToServerMapChunkTiles(
  callerPid: String,
): KStream<ServerMapChunkId, ServerMapChunkTiles<PrototypeHashCode>> {

  val pid = "$callerPid.convertToServerMapChunkTiles"

  return flatMap("$pid.map") { serverId: FactorioServerId, update: MapChunkEntityUpdate ->

//
//    // TODO There might be multiple resources per tile position, therefore this will arbitrarily
//    //      exclude resources. Maybe change it from a Map to a List? Or have multiple resources per
//    //      tile position?
//    val mapValidEntitiesToPrototypeHash = validEntityPositions.associate { (pos, resource) ->
//      pos to PrototypeHashCode(resource.protoId)
//    }

    val entitiesToPrototypeHash = update.entities().associate { entity ->
      entity.position.toMapTilePosition() to PrototypeHashCode(entity.protoId)
    }

    val chunkId = ServerMapChunkId(
      serverId = serverId,
      layer = ServerMapTileLayer.BUILDING,
      chunkPosition = update.key.chunkPosition,
      surfaceIndex = update.key.surfaceIndex,
    )

    val tiles = ServerMapChunkTiles(chunkId, entitiesToPrototypeHash)

    reChunkTiles(pid, tiles)
  }
}


fun KStream<FactorioServerId, MapChunkResourceUpdate>.convertResourceUpdateToServerMapChunkTiles(
  callerPid: String,
): KStream<ServerMapChunkId, ServerMapChunkTiles<PrototypeHashCode>> {

  val pid = "$callerPid.convertResourceUpdateToChunkTiles"

  return flatMap("$pid.map") { serverId: FactorioServerId, update: MapChunkResourceUpdate ->

//    // TODO There might be multiple resources per tile position, therefore this will arbitrarily
//    //      exclude resources. Maybe change it from a Map to a List? Or have multiple resources per
//    //      tile position?
//    val mapValidEntitiesToPrototypeHash = validEntityPositions.associate { (pos, resource) ->
//      pos to PrototypeHashCode(resource.protoId)
//    }

    val entitiesToPrototypeHash = update.resources().associate { entity ->
      entity.position.toMapTilePosition() to PrototypeHashCode(entity.protoId)
    }

    val chunkId = ServerMapChunkId(
      serverId = serverId,
      layer = ServerMapTileLayer.RESOURCE,
      chunkPosition = update.key.chunkPosition,
      surfaceIndex = update.key.surfaceIndex,
    )

    val tiles = ServerMapChunkTiles(chunkId, entitiesToPrototypeHash)

    reChunkTiles(pid, tiles)
  }
}


private fun KTable<ServerMapChunkId, ServerMapChunkTiles<PrototypeHashCode>>.enrichWithColourData(
  callerPid: String,
  protosTable: KTable<FactorioServerId, EntityPrototypeMap>,
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
    }) { tiles: ServerMapChunkTiles<PrototypeHashCode>, prototypes: EntityPrototypeMap ->
    println("joining tiles:${tiles.map.size} with prototypes:${prototypes.size}")

    val missingProtos = mutableSetOf<PrototypeHashCode>()

    val tileColours: Map<MapTilePosition, ColourHex> = buildList {
      tiles.map.forEach { (position, code) ->
        when (val proto = prototypes[code]) {

          null -> missingProtos += code

          else -> when (val box = proto.collisionBox) {

            null -> add(position to proto.mapColour())

            else -> box.iterator(position).forEach { entitySubTile ->
              add(entitySubTile to proto.mapColour())
            }
          }
        }
      }
    }.fold(mapOf()) { acc, (pos, colour) ->
      // merge overlapping colours
      val newColour = when (val existing = acc[pos]) {
        null -> colour
        else -> (existing / 2u) + (colour / 2u)
      }
      acc + (pos to newColour)
    }

    if (missingProtos.isNotEmpty()) {
      println("[$pid] missing ${missingProtos.size} tile prototypes: ${missingProtos.joinToString { "${it.code}" }}")
    }

    println("[$pid] Set ${tileColours.size} tile colours for chunk ${tiles.chunkId}")

    ServerMapChunkTiles(tiles.chunkId, tileColours)
  }
}


private fun MapBoundingBox.iterator(origin: MapTilePosition): Iterator<MapTilePosition> {

  // check if tile width/height should be divided by 2. I think I read that it's not divided by 2
  // if it's an odd number?

  val xOffset: Int = floor(tileWidth.toDouble() / 2).toInt()
  val startX = origin.x - xOffset
  val endX = origin.x + xOffset

  val yOffset: Int = floor(tileHeight.toDouble() / 2).toInt()
  val startY = origin.y - yOffset
  val endY = origin.y + yOffset

  return iterator {
    for (x in (startX..endX)) {
      for (y in (startY..endY)) { // screen coordinates = +y points to the floor, -y to the ceiling
        yield(MapTilePosition(x, y))
      }
    }
  }
}


private fun FactorioPrototype.Entity.mapColour(): ColourHex =
  (colour ?: mapColour ?: mapColourFriend)?.toHex() ?: ColourHex.WHITE


private operator fun ColourHex.div(divisor: UInt): ColourHex = copy(
  red = (red / divisor).toUByte(),
  green = (green / divisor).toUByte(),
  blue = (blue / divisor).toUByte(),
  alpha = (alpha / divisor).toUByte(),
)


private operator fun ColourHex.plus(other: ColourHex): ColourHex = copy(
  red = (red + other.red).toUByte(),
  green = (green + other.green).toUByte(),
  blue = (blue + other.blue).toUByte(),
  alpha = (alpha + other.alpha).toUByte(),
)
