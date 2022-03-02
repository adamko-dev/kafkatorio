package dev.adamko.kafkatorio.events.schema

import kotlin.jvm.JvmInline
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
  val red: Float = 0f,
  val green: Float = 0f,
  val blue: Float = 0f,
  val alpha: Float = 1f,
)


/** Size-efficient version of [Colour] (`4*4` bytes vs `4*1` bytes) */
@Serializable
data class ColourHex(
  val red: UByte = UByte.MIN_VALUE,
  val green: UByte = UByte.MIN_VALUE,
  val blue: UByte = UByte.MIN_VALUE,
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


/**
 * Size-optimised 2D map of x/y tile positions and prototype-name.
 *
 * Each X/Y coordinate maps to an arbitrary [PrototypeKey] (which is only valid for any specific
 * instance). [protos] is specific to each instance of [MapTileDictionary], can be used to convert from a
 */
@Serializable
data class MapTileDictionary(
  /** Map an X,Y coordinate a prototype name */
  val tilesXY: Map<String, Map<String, PrototypeKey>>,
  val protos: Map<PrototypeKey, PrototypeName>
) {
  fun toMapTileList(): List<MapTile> = buildList {
    tilesXY.forEach { (xString, row) ->
      row.forEach { (yString, protoIndex) ->
        val x = xString.toIntOrNull()
        val y = yString.toIntOrNull()
        val protoName = protos[protoIndex]
        if (x != null && y != null && protoName != null) {
          add(MapTile(x, y, protoName))
        }
      }
    }
  }

  companion object {
    fun MapTileDictionary?.isNullOrEmpty(): Boolean {
      return this == null || (tilesXY.isEmpty() && tilesXY.all { it.value.isEmpty() })
    }
  }
}


@Serializable
@JvmInline
value class PrototypeKey(val index: String)
