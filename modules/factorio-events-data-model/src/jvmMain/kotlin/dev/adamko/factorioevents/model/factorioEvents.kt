package dev.adamko.factorioevents.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator


@Serializable
data class FactorioEvent(
  val modVersion: String,
  val tick: UInt,
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
  val surface_index: Int,
  val unit_number: UInt?,
  val position: PositionData,
  val player_index: UInt?,
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