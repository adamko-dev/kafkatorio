package dev.adamko.kafkatorio.events.schema.converters

import dev.adamko.kafkatorio.events.schema.MAP_CHUNK_SIZE
import dev.adamko.kafkatorio.events.schema.MapBoundingBox
import dev.adamko.kafkatorio.events.schema.MapChunkPosition
import dev.adamko.kafkatorio.events.schema.MapEntityPosition
import dev.adamko.kafkatorio.events.schema.MapTilePosition
import kotlin.math.floor

operator fun MapTilePosition.times(factor: Int) =
  MapTilePosition(x * factor, y * factor)

operator fun MapTilePosition.div(divisor: Int) =
  MapTilePosition(x / divisor, y / divisor)

operator fun MapTilePosition.plus(addend: Int) =
  MapTilePosition(x + addend, y + addend)


fun MapTilePosition.toMapChunkPosition(  chunkSize: Int = MAP_CHUNK_SIZE) =
  MapChunkPosition(
    floor(x.toDouble() / chunkSize.toDouble()).toInt(),
    floor(y.toDouble() / chunkSize.toDouble()).toInt(),
  )


fun MapEntityPosition.toMapTilePosition() =
  MapChunkPosition(floor(x).toInt(), floor(y).toInt())

fun MapEntityPosition.toMapChunkPosition( chunkSize: Int = MAP_CHUNK_SIZE) =
  MapChunkPosition(
    floor(x / chunkSize.toDouble()).toInt(),
    floor(y / chunkSize.toDouble()).toInt(),
  )


operator fun MapChunkPosition.times(factor: Int) =
  MapChunkPosition(x * factor, y * factor)

operator fun MapChunkPosition.plus(addend: Int) =
  MapChunkPosition(x + addend, y + addend)

operator fun MapChunkPosition.minus(subtrahend: Int) =
  MapChunkPosition(x - subtrahend, y - subtrahend)


fun MapChunkPosition.leftTopTile( chunkSize: Int = MAP_CHUNK_SIZE): MapTilePosition =
    MapTilePosition(
    x * chunkSize,
    y * chunkSize,
  )

//val MapChunkPosition.rightBottomTile: MapTilePosition
//  get() = leftTopTile + (MAP_CHUNK_SIZE - 1)
//
//
//val MapChunkPosition.boundingBox: MapBoundingBox
//  get() = MapBoundingBox(leftTopTile, rightBottomTile)
