package dev.adamko.kafkatorio.processor.topology

import kotlin.math.pow
import kotlin.math.roundToInt
import kotlinx.serialization.Serializable


@JvmInline
@Serializable
value class FactorioServerId(private val id: String) {
  override fun toString() = id
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
