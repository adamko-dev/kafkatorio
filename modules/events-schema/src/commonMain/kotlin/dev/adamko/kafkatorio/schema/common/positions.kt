package dev.adamko.kafkatorio.schema.common

import dev.adamko.kxstsgen.core.*
import dev.adamko.kxstsgen.core.experiments.TupleSerializer
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.roundToInt
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

  private val bounds: MapChunkPositionBounds by lazy {
    MapChunkPositionBounds(this, ChunkSize.CHUNK_032)
  }

  operator fun contains(tilePosition: MapTilePosition): Boolean {
    return tilePosition in bounds
  }
}


@Serializable
data class MapChunkPositionBounds(
  val leftTop: MapTilePosition,
  val rightBottom: MapTilePosition,
) : ClosedRange<MapTilePosition> {

  constructor(chunkPosition: MapChunkPosition, chunkSize: ChunkSize) : this(
    chunkPosition.leftTopTile(chunkSize),
    chunkPosition.rightBottomTile(chunkSize),
  )

  override val start: MapTilePosition
    get() = leftTop

  override val endInclusive: MapTilePosition
    get() = rightBottom
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
) : Comparable<MapTilePosition> {
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

  override operator fun compareTo(other: MapTilePosition): Int =
    when {
      other.x != x -> x.compareTo(other.x)
      else         -> y.compareTo(other.y)
    }
}


/* *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  * */


operator fun MapTilePosition.times(factor: Int) =
  MapTilePosition(x * factor, y * factor)

operator fun MapTilePosition.div(divisor: Int) =
  MapTilePosition(x / divisor, y / divisor)

operator fun MapTilePosition.plus(addend: Int) =
  MapTilePosition(x + addend, y + addend)

operator fun MapTilePosition.plus(addend: MapTilePosition) =
  MapTilePosition(x + addend.x, y + addend.y)


operator fun MapTilePosition.minus(subtrahend: MapTilePosition) =
  MapTilePosition(x - subtrahend.x, y - subtrahend.y)


fun MapTilePosition.toMapChunkPosition(chunkSize: ChunkSize) =
  MapChunkPosition(
    floor(x.toDouble() / chunkSize.lengthInTiles.toDouble()).toInt(),
    floor(y.toDouble() / chunkSize.lengthInTiles.toDouble()).toInt(),
  )


fun MapEntityPosition.toMapTilePosition() =
  MapChunkPosition(floor(x).toInt(), floor(y).toInt())

fun MapEntityPosition.toMapChunkPosition(chunkSize: ChunkSize) =
  MapChunkPosition(
    floor(x / chunkSize.lengthInTiles.toDouble()).toInt(),
    floor(y / chunkSize.lengthInTiles.toDouble()).toInt(),
  )


operator fun MapChunkPosition.times(factor: Int) =
  MapChunkPosition(x * factor, y * factor)

operator fun MapChunkPosition.plus(addend: Int) =
  MapChunkPosition(x + addend, y + addend)

operator fun MapChunkPosition.minus(subtrahend: Int) =
  MapChunkPosition(x - subtrahend, y - subtrahend)


fun MapChunkPosition.leftTopTile(chunkSize: ChunkSize): MapTilePosition =
  MapTilePosition(
    x * chunkSize.lengthInTiles,
    y * chunkSize.lengthInTiles,
  )

fun MapChunkPosition.rightBottomTile(chunkSize: ChunkSize): MapTilePosition =
  leftTopTile(chunkSize) + (chunkSize.lengthInTiles - 1)


fun MapChunkPosition.iterateTiles(chunkSize: ChunkSize): Iterator<MapTilePosition> {
  val leftTop = leftTopTile(chunkSize)
  val rightBottom = rightBottomTile(chunkSize)
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
