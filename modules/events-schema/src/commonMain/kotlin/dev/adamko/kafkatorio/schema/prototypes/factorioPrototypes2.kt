package dev.adamko.kafkatorio.schema.prototypes

import dev.adamko.kafkatorio.schema.common.Colour
import dev.adamko.kafkatorio.schema.common.PrototypeName
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable


@Serializable
sealed class FactorioPrototype2 {

  @Serializable
  data class MapTile(
    val name: PrototypeName,
    val layer: UInt,
    val mapColour: Colour,
    @Contextual
    val collisionMasks: List<String>,
    val order: String,
    /** Can the tile be mined for resources? */
    val canBeMined: Boolean,
  ) : FactorioPrototype2()
}
