package dev.adamko.kafkatorio.events.schema

import kotlinx.serialization.Serializable

@Serializable
data class PositionData(
  val x: Double,
  val y: Double,
  val type: PositionType?,
)

@Serializable
enum class PositionType {
  MAP, CHUNK, TILE,
}

/**
 * Red, green, blue and alpha values, all in range `[0, 1]` or all in range `[0, 255]` if any
 * value is > 1.
 *
 * All values here are optional. Color channels default to 0, the alpha channel defaults to 1.
 */
@Serializable
data class Colour(
  val red: Float = 0f,
  val green: Float = 0f,
  val blue: Float = 0f,
  val alpha: Float = 1f,
) {
  
  /** True if any value is greater than 1, so the values are hexadecimal. */
  fun isDecimal(): Boolean {
    return red > 1f
        || green > 1f
        || blue > 1f
        || alpha > 1f
  }

  /** True if all values are between `[0..1]`. */
  fun isPercentage(): Boolean = !isDecimal()

}

//@Serializable
//@SerialName("FactorioTiles")
//data class FactorioTiles(
//  val tiles: List<FactorioTile>,
//) : FactorioObjectData() {
//  @Transient
//  override val objectName: String = "FactorioTiles"
//}
