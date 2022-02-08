package dev.adamko.kafkatorio.events.schema

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable


@Serializable
data class FactorioConfigurationUpdate(
  override val modVersion: String,
  val factorioData: FactorioGameDataUpdate,
  val allMods: List<FactorioModInfo>,
) : KafkatorioPacket() {
  @EncodeDefault
  override val packetType = PacketType.CONFIG
}

@Serializable
data class FactorioGameDataUpdate(
  val oldVersion: String? = null,
  val newVersion: String? = null,
)

@Serializable
data class FactorioModInfo(
  val modName: String,
  val currentVersion: String? = null,
  val previousVersion: String? = null,
)
