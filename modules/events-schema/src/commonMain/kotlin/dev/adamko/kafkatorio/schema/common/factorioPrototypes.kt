package dev.adamko.kafkatorio.schema.common

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@SerialName("kafkatorio.prototype.FactorioPrototype")
sealed interface FactorioPrototype {
  val name: PrototypeName
  val mapColour: Colour

  @SerialName("kafkatorio.prototype.MapTile")
  @Serializable
  data class MapTile(
    override val name: PrototypeName,
    override val mapColour: Colour,

    val layer: UInt,
    @Contextual
    val collisionMasks: List<String>,
    val order: String,
    /** Can the tile be mined for resources? */
    val canBeMined: Boolean,
  ) : FactorioPrototype
}
