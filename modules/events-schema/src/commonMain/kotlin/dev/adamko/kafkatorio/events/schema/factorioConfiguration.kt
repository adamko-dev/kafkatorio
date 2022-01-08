package dev.adamko.kafkatorio.events.schema

import kotlinx.serialization.Serializable


@Serializable
data class FactorioConfigurationUpdate(
  override val modVersion: String,
  val factorioData: FactorioGameDataUpdate,
  val allMods: List<FactorioModInfo>,
  val prototypes: List<FactorioPrototype>,
) : KafkatorioPacket

@Serializable
data class FactorioGameDataUpdate(
  val oldVersion: String?,
  val newVersion: String?,
)

@Serializable
data class FactorioModInfo(
  val modName: String,
  val currentVersion: String?,
  val previousVersion: String?,
)
