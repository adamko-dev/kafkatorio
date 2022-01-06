package dev.adamko.kafkatorio.events.schema

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
@JsonClassDiscriminator(FactorioObjectDataDiscriminatorKey)
sealed class FactorioPrototype {
  abstract val objectName: String
}

@Serializable
@SerialName("LuaTilePrototype")
data class FactorioMapTilePrototype(
  val name: String,
  val layer: UInt,
  val mapColor: Colour,
  val collisionMasks: List<String>,
  val order: String,

  /** Is this tile mineable at all? */
  val canBeMined: Boolean,
) : FactorioPrototype() {
  @Transient
  override val objectName: String = "LuaTilePrototype"
}
