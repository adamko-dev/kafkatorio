package dev.adamko.kafkatorio.events.schema

import kotlin.jvm.JvmInline
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable


const val MAP_CHUNK_SIZE = 32


@Serializable
@JvmInline
value class Tick(val value: UInt)


/**
 * Coordinates of a [EntityData] on a map.
 *
 * A [MapEntityPosition] can be translated to a [MapChunkPosition] by dividing the `x`/`y` values
 * by [32][MAP_CHUNK_SIZE].
 */
@Serializable
data class MapEntityPosition(
  val x: Double,
  val y: Double,
)


/** Coordinates of a chunk in a [SurfaceData] where each integer `x`/`y` represents a different
 * chunk.
 *
 * A [MapChunkPosition] can be translated to a [MapEntityPosition] by multiplying the `x`/`y`
 * values by [32][MAP_CHUNK_SIZE].
 */
@Serializable
data class MapChunkPosition(
  val x: Int,
  val y: Int,
)


/**
 * Coordinates of a tile in a chunk on a [SurfaceData] where each integer `x`/`y` represents a
 * different [MapTile].
 *
 * It rounds any `x`/`y` down to whole numbers.
 */
@Serializable
data class MapTilePosition(
  val x: Int,
  val y: Int,
)


@Serializable
data class MapBoundingBox(
  val topLeft: MapTilePosition,
  val bottomRight: MapTilePosition,
)


/**
 * Red, green, blue and alpha values, all in range `[0, 1]` or all in range `[0, 255]` if any
 * value is > 1.
 *
 * All values here are optional. Colour channels default to `0`, the alpha channel defaults to `1`.
 */
@Serializable
data class Colour(
  @EncodeDefault // remove encode default?
  val red: Float = 0f,
  @EncodeDefault
  val green: Float = 0f,
  @EncodeDefault
  val blue: Float = 0f,
  @EncodeDefault
  val alpha: Float = 1f,
)


/** Size-efficient version of [Colour] (`4*4` bytes vs `4*1` bytes) */
@Serializable
data class ColourHex(
  @EncodeDefault // remove encode default?
  val red: UByte = UByte.MIN_VALUE,
  @EncodeDefault
  val green: UByte = UByte.MIN_VALUE,
  @EncodeDefault
  val blue: UByte = UByte.MIN_VALUE,
  @EncodeDefault
  val alpha: UByte = UByte.MAX_VALUE,
) {
  companion object {
    val TRANSPARENT = ColourHex(0u, 0u, 0u, 0u)
  }
}


@Serializable
data class MapTile(
  val x: Int,
  val y: Int,
  val proto: PrototypeName,
)


interface EntityIdentifiers {
  val unitNumber: UnitNumber?
  val name: String
  val type: String
}
