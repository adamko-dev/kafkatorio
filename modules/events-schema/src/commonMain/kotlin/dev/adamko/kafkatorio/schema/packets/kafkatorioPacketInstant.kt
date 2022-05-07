package dev.adamko.kafkatorio.schema.packets

import dev.adamko.kafkatorio.schema.common.FactorioPrototype
import dev.adamko.kafkatorio.schema.common.PlayerIndex
import dev.adamko.kafkatorio.schema.common.SurfaceIndex
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/** No key, no caching. Must be sent instantly. */
@Serializable
@SerialName("kafkatorio.packet.KafkatorioPacket")
sealed class KafkatorioInstantPacketData : KafkatorioPacketData()


/*  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  *** */


@Serializable
@SerialName("kafkatorio.packet.instant.ConfigurationUpdate")
data class ConfigurationUpdate(
  val factorioData: ConfigurationUpdateGameData,
  val allMods: List<ConfigurationUpdateModData>,
  val modStartupSettingsChange: Boolean,
  val migrationApplied: Boolean,
) : KafkatorioInstantPacketData() {

  @Serializable
  @SerialName("kafkatorio.packet.instant.ConfigurationUpdate.ConfigurationUpdateGameData")
  data class ConfigurationUpdateGameData(
    val oldVersion: String? = null,
    val newVersion: String? = null,
  )

  @Serializable
  @SerialName("kafkatorio.packet.instant.ConfigurationUpdate.ConfigurationUpdateModData")
  data class ConfigurationUpdateModData(
    val modName: String,
    val currentVersion: String? = null,
    val previousVersion: String? = null,
  )
}


@Serializable
@SerialName("kafkatorio.packet.instant.ConsoleChatUpdate")
data class ConsoleChatUpdate(
  val authorPlayerIndex: PlayerIndex?,
  val content: String,
) : KafkatorioInstantPacketData()


@Serializable
@SerialName("kafkatorio.packet.instant.ConsoleCommandUpdate")
data class ConsoleCommandUpdate(
  val authorPlayerIndex: PlayerIndex?,
  val command: String,
  val parameters: String,
) : KafkatorioInstantPacketData()


@Serializable
@SerialName("kafkatorio.packet.instant.PrototypesUpdate")
data class PrototypesUpdate(
  val prototypes: List<FactorioPrototype>,
) : KafkatorioInstantPacketData()


@Serializable
@SerialName("kafkatorio.packet.instant.SurfaceUpdate")
data class SurfaceUpdate(
  val index: SurfaceIndex,
  val daytime: Double,
  val name: String,
) : KafkatorioInstantPacketData()


/*  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  *** */
