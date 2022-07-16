package dev.adamko.kafkatorio.schema.common

import dev.adamko.kafkatorio.library.leftTopTile
import dev.adamko.kafkatorio.library.rightBottomTile
import dev.adamko.kxstsgen.core.experiments.TupleSerializer
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * The position of something that exists in-game (not conceptually, like a
 * [chunk][MapChunkPosition])
 */
sealed interface MapPosition : Comparable<MapPosition> {
  val x: Number
  val y: Number
}


/**
 * Coordinates of an [entity][FactorioEntityData.Standard] on a map.
 *
 * Positive y points south, positive x points east.
 *
 * A [MapEntityPosition] can be translated to a [MapChunkPosition] by dividing the `x`/`y` values
 * by [32][MAP_CHUNK_SIZE].
 */
@Serializable(with = MapEntityPosition.Companion.Serializer::class)
data class MapEntityPosition(
  override val x: Double,
  override val y: Double,
) : MapPosition {


  override fun compareTo(other: MapPosition): Int =
    when (val resultX = x.compareTo(other.x.toDouble())) {
      0    -> y.compareTo(other.y.toDouble())
      else -> resultX
    }


  companion object {
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
}


/**
 * Coordinates of a tile on a surface, where each `x`/`y` coordinate represents a different
 * [MapTile].
 *
 * Positive y points south, positive x points east.
 *
 * It rounds any `x`/`y` down to whole numbers.
 */
@Serializable(with = MapTilePosition.Companion.Serializer::class)
data class MapTilePosition(
  override val x: Int,
  override val y: Int,
) : MapPosition {


  override fun compareTo(other: MapPosition): Int =
    when (val resultX = x.compareTo(other.x.toInt())) {
      0    -> y.compareTo(other.y.toInt())
      else -> resultX
    }


  companion object {
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

}


/**
 * Coordinates of a chunk on a surface, where each `x`/`y` coordinate represents a different
 * chunk.
 *
 * Positive y points south, positive x points east.
 *
 * A [MapChunkPosition] can be translated to a [MapEntityPosition] by multiplying the `x`/`y`
 * values by [32][MAP_CHUNK_SIZE].
 */
@Serializable(with = MapChunkPosition.Companion.Serializer::class)
data class MapChunkPosition(
  val x: Int,
  val y: Int,
  // Don't set a default for ChunkSize. It could default to 32, but it's clearer to make it explicit
  // and avoid accidentally not setting it. Also, a default doesn't save much in serialization size.
  val chunkSize: ChunkSize,
) {

  val bounds: MapChunkPositionBounds by lazy {
    MapChunkPositionBounds(this)
  }

  operator fun contains(tilePosition: MapTilePosition): Boolean = tilePosition in bounds
  operator fun contains(entityPosition: MapEntityPosition): Boolean = entityPosition in bounds

  companion object {
    object Serializer : TupleSerializer<MapChunkPosition>(
      "MapChunkPosition",
      {
        element(MapChunkPosition::x)
        element(MapChunkPosition::y)
        element(MapChunkPosition::chunkSize)
      }
    ) {
      override fun tupleConstructor(elements: Iterator<*>): MapChunkPosition {
        return MapChunkPosition(
          elements.next() as Int,
          elements.next() as Int,
          elements.next() as ChunkSize,
        )
      }
    }
  }
}


/**
 * The bounds of a [MapChunkPosition], that may contain a [MapPosition].
 *
 * Because Factorio uses screen-coordinates, the lowest `x/y` coordinate is the [leftTop], and
 * the highest is [rightBottomTile].
 */
@Serializable
data class MapChunkPositionBounds(
  val leftTop: MapTilePosition,
  val rightBottom: MapTilePosition,
  val chunkSize: ChunkSize, // should be computable based on the tiles, but include it for completeness
) : ClosedRange<MapPosition> {

  constructor(chunkPosition: MapChunkPosition) : this(
    chunkPosition.leftTopTile(),
    chunkPosition.rightBottomTile(),
    chunkPosition.chunkSize,
  )

  override val start: MapTilePosition
    get() = leftTop

  override val endInclusive: MapTilePosition
    get() = rightBottom
}


@Serializable
enum class ChunkSize(
  val zoomLevel: Int,
) : Comparable<ChunkSize> {
  CHUNK_512(-1),
  CHUNK_256(0),
  CHUNK_128(1),
  CHUNK_064(2),
  CHUNK_032(3),
  ;

  /** 32, 64, 128, 256, or 512 */
  val lengthInTiles: Int = 2f.pow(8 - zoomLevel).roundToInt()

  init {
    require(lengthInTiles > 0) { "tilesPerChunk $lengthInTiles must be positive" }
    // 1000 & 0111 = 0000 =>  pow^2
    // 1001 & 1000 = 1000 => !pow^2
    require(lengthInTiles and (lengthInTiles - 1) == 0) {
      "tilesPerChunk $lengthInTiles must be a power-of-two number"
    }
  }

  companion object {
    // cache values for better performance. KT-48872
    val entries: Set<ChunkSize> = values().toSet()

    val MAX: ChunkSize = entries.maxByOrNull { it.lengthInTiles }!!
    val MIN: ChunkSize = entries.minByOrNull { it.lengthInTiles }!!
    val STANDARD: ChunkSize = entries.first { it.zoomLevel == 0 }
  }
}


@Serializable
@SerialName("kafkatorio.position.MapBoundingBox")
data class MapBoundingBox(
  val leftTop: MapEntityPosition,
  val rightBottom: MapEntityPosition,
) {
  val width: Double get() = abs(leftTop.x - rightBottom.x)
  val height: Double get() = abs(leftTop.y - rightBottom.y)

  val tileWidth: Int get() = ceil(width).toInt()
  val tileHeight: Int get() = ceil(height).toInt()
}
