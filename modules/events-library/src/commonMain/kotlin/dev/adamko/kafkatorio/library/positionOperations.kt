package dev.adamko.kafkatorio.library

import dev.adamko.kafkatorio.schema.common.ChunkSize
import dev.adamko.kafkatorio.schema.common.MapChunkPosition
import dev.adamko.kafkatorio.schema.common.MapEntityPosition
import dev.adamko.kafkatorio.schema.common.MapTilePosition
import kotlin.math.floor


operator fun MapTilePosition.times(factor: Int): MapTilePosition =
  MapTilePosition(x * factor, y * factor)


operator fun MapTilePosition.div(divisor: Int): MapTilePosition =
  MapTilePosition(x / divisor, y / divisor)


operator fun MapTilePosition.plus(addend: Int): MapTilePosition =
  MapTilePosition(x + addend, y + addend)


operator fun MapTilePosition.plus(addend: MapTilePosition): MapTilePosition =
  MapTilePosition(x + addend.x, y + addend.y)


operator fun MapTilePosition.minus(subtrahend: MapTilePosition): MapTilePosition =
  MapTilePosition(x - subtrahend.x, y - subtrahend.y)


/** Get the [MapChunkPosition] of the tile */
fun MapTilePosition.toMapChunkPosition(chunkSize: ChunkSize): MapChunkPosition =
  MapChunkPosition(
    x = floor(x.toDouble() / chunkSize.lengthInTiles.toDouble()).toInt(),
    y = floor(y.toDouble() / chunkSize.lengthInTiles.toDouble()).toInt(),
    chunkSize = chunkSize,
  )


/** Get the [MapTilePosition] of the entity */
fun MapEntityPosition.toMapTilePosition(): MapTilePosition =
  MapTilePosition(
    x = floor(x).toInt(),
    y = floor(y).toInt(),
  )


/** Get the [MapChunkPosition] of the entity */
fun MapEntityPosition.toMapChunkPosition(chunkSize: ChunkSize): MapChunkPosition =
  MapChunkPosition(
    x = floor(x / chunkSize.lengthInTiles.toDouble()).toInt(),
    y = floor(y / chunkSize.lengthInTiles.toDouble()).toInt(),
    chunkSize = chunkSize,
  )


fun MapChunkPosition.resize(chunkSize: ChunkSize): MapChunkPosition =
  leftTopTile().toMapChunkPosition(chunkSize)


operator fun MapChunkPosition.times(factor: Int): MapChunkPosition =
  copy(x = x * factor, y = y * factor)


operator fun MapChunkPosition.plus(addend: Int): MapChunkPosition =
  copy(x = x + addend, y = y + addend)


operator fun MapChunkPosition.minus(subtrahend: Int): MapChunkPosition =
  copy(x = x - subtrahend, y = y - subtrahend)


fun MapChunkPosition.leftTopTile(): MapTilePosition =
  MapTilePosition(
    x * chunkSize.lengthInTiles,
    y * chunkSize.lengthInTiles,
  )


fun MapChunkPosition.rightBottomTile(): MapTilePosition =
  leftTopTile() + (chunkSize.lengthInTiles - 1)


/** Iterate over all possible [MapTilePosition]s in the chunk. */
fun MapChunkPosition.iterateTiles(): Iterator<MapTilePosition> {
  val leftTop = leftTopTile()
  val rightBottom = rightBottomTile()

  return iterator {
    (leftTop.x..rightBottom.x).forEach { x ->
      (leftTop.y..rightBottom.y).forEach { y ->
        yield(MapTilePosition(x, y))
      }
    }
  }
}


//val MapChunkPosition.rightBottomTile: MapTilePosition
//  get() = leftTopTile + (MAP_CHUNK_SIZE - 1)

//
//val MapChunkPosition.boundingBox: MapBoundingBox
//  get() = MapBoundingBox(leftTopTile, rightBottomTile)
