package dev.adamko.factorioevents.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator


@Serializable
data class FactorioEvent<T : FactorioObjectData>(
  /** the initial Factorio event ({defines.events}) trigger */
  val eventType: String,
  /** Schema versioning */
  val modVersion: String,
  /** game time */
  val tick: UInt,
  val data: T,
)

@JsonClassDiscriminator("object_name")
sealed interface FactorioObjectData {
  val objectName: String
}

@SerialName("LuaPlayer")
data class PlayerData(
  override val objectName: String,
  val associatedCharactersUnitNumbers: List<UInt>,
  val characterUnitNumber: UInt?,
  val name: String,
  val position: PositionData,
) : FactorioObjectData

@SerialName("LuaEntity")
data class EntityData(
  override val objectName: String,

  val active: Boolean,
  val health: Float?,
  val healthRatio: Float,
  val name: String,
  val position: PositionData,
  val surfaceIndex: Int,
  val type: String,
  val unitNumber: UInt?,

  val playerIndex: UInt?,
) : FactorioObjectData

@SerialName("LuaSurface")
data class SurfaceData(
  override val objectName: String,
  val daytime: Float,
  val index: UInt,
  val name: String,
) : FactorioObjectData

@Serializable
data class PositionData(
  val x: Int,
  val y: Int,
)