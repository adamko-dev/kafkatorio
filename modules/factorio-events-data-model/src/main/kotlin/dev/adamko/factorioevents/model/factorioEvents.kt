package dev.adamko.factorioevents.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator


@Serializable
data class FactorioEvent(
  /** Schema versioning */
  val modVersion: String,
  /** game time */
  val tick: UInt,
  /** the initial Factorio event ({defines.events}) trigger */
  val eventType: String,
  val data: FactorioObjectData,
)

@JsonClassDiscriminator("object_name")
sealed interface FactorioObjectData {
  val objectName: String
}

@SerialName("player")
data class PlayerData(
  override val objectName: String,
  val characterUnitNumber: UInt,
  val associatedCharacterUnitNumbers: List<UInt>,
  val positionData: PositionData,
) : FactorioObjectData

data class EntityData(
  override val objectName: String,
  val name: String,
  val type: String,
  val active: Boolean,
  val healthRatio: Float,
  val health: Float?,
  val surfaceIndex: Int,
  val unitNumber: UInt?,
  val position: PositionData,
  val playerIndex: UInt?,
) : FactorioObjectData

data class SurfaceData(
  override val objectName: String,
  val name: String,
  val index: UInt,
  val daytime: Float,
) : FactorioObjectData

data class PositionData(
  val x: Int,
  val y: Int,
)