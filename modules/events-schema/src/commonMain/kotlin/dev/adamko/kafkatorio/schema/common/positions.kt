package dev.adamko.kafkatorio.schema.common

import dev.adamko.kxstsgen.core.*
import dev.adamko.kxstsgen.core.experiments.TupleSerializer
import kotlin.math.floor
import kotlinx.serialization.Serializable

/**
 * Coordinates of a [EntityData] on a map.
 *
 * A [MapEntityPosition] can be translated to a [MapChunkPosition] by dividing the `x`/`y` values
 * by [32][MAP_CHUNK_SIZE].
 */
@Serializable(with = MapEntityPosition.Serializer::class)
data class MapEntityPosition(
  val x: Double,
  val y: Double,
) {
  object Serializer : TupleSerializer<MapEntityPosition>(
    "MapEntityPosition",
    {
      element(MapEntityPosition::x)
      element(MapEntityPosition::y)
    }
  ) {
    override fun tupleConstructor(elements: Iterator<*>): MapEntityPosition {
      return MapEntityPosition(
        elements.next() as Double,
        elements.next() as Double,
      )
    }
  }
}


/** Coordinates of a chunk in a [SurfaceData] where each integer `x`/`y` represents a different
 * chunk.
 *
 * A [MapChunkPosition] can be translated to a [MapEntityPosition] by multiplying the `x`/`y`
 * values by [32][MAP_CHUNK_SIZE].
 */
@Serializable(with = MapChunkPosition.Serializer::class)
data class MapChunkPosition(
  val x: Int,
  val y: Int,
) {
  object Serializer : TupleSerializer<MapChunkPosition>(
    "MapChunkPosition",
    {
      element(MapChunkPosition::x)
      element(MapChunkPosition::y)
    }
  ) {
    override fun tupleConstructor(elements: Iterator<*>): MapChunkPosition {
      return MapChunkPosition(
        elements.next() as Int,
        elements.next() as Int,
      )
    }
  }
}


/**
 * Coordinates of a tile in a chunk on a [SurfaceData] where each integer `x`/`y` represents a
 * different [MapTile].
 *
 * It rounds any `x`/`y` down to whole numbers.
 */
@Serializable(with = MapTilePosition.Serializer::class)
data class MapTilePosition(
  val x: Int,
  val y: Int,
) {
  object Serializer : TupleSerializer<MapTilePosition>(
    "MapTilePosition",
    {
      element(MapTilePosition::x)
      element(MapTilePosition::y)
    }
  ) {
    override fun tupleConstructor(elements: Iterator<*>): MapTilePosition {
      val x = requireNotNull(elements.next() as? Int)
      val y = requireNotNull(elements.next() as? Int)
      return MapTilePosition(x, y)
    }
  }
}


/* *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  * */


operator fun MapTilePosition.times(factor: Int) =
  MapTilePosition(x * factor, y * factor)

operator fun MapTilePosition.div(divisor: Int) =
  MapTilePosition(x / divisor, y / divisor)

operator fun MapTilePosition.plus(addend: Int) =
  MapTilePosition(x + addend, y + addend)


fun MapTilePosition.toMapChunkPosition(chunkSize: Int = MAP_CHUNK_SIZE) =
  MapChunkPosition(
    floor(x.toDouble() / chunkSize.toDouble()).toInt(),
    floor(y.toDouble() / chunkSize.toDouble()).toInt(),
  )


fun MapEntityPosition.toMapTilePosition() =
  MapChunkPosition(floor(x).toInt(), floor(y).toInt())

fun MapEntityPosition.toMapChunkPosition(chunkSize: Int = MAP_CHUNK_SIZE) =
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


fun MapChunkPosition.leftTopTile(chunkSize: Int = MAP_CHUNK_SIZE): MapTilePosition =
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
