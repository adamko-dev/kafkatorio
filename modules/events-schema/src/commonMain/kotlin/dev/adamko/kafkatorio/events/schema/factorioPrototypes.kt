package dev.adamko.kafkatorio.events.schema

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
class FactorioPrototypes<T : FactorioPrototype>(
  val prototypes: List<T>,
)

@Serializable
@JsonClassDiscriminator(FactorioPrototype.discriminatorKey)
sealed class FactorioPrototype {
  abstract val objectName: PrototypeObjectName

  enum class PrototypeObjectName {
    LuaTilePrototype
  }

  companion object {
    const val discriminatorKey: String = "objectName"
  }
}

@Serializable
@SerialName("LuaTilePrototype")
data class FactorioMapTilePrototype(
  val name: String,
  val layer: UInt,
  val mapColor: Colour,
  val collisionMasks: List<String>,
  val order: String,

  /** Can the tile be mined for resources? */
  val canBeMined: Boolean,
) : FactorioPrototype() {
  @Transient
  override val objectName: PrototypeObjectName = PrototypeObjectName.LuaTilePrototype
}
