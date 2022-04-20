package dev.adamko.kafkatorio.schema.packets

import dev.adamko.kafkatorio.schema.common.PlayerIndex
import dev.adamko.kafkatorio.schema.common.SurfaceIndex
import dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype
import kotlinx.serialization.Serializable


/** No key, no caching. Must be sent instantly. */
@Serializable
sealed class KafkatorioInstantPacketData : KafkatorioPacketData()


/*  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  *** */


@Serializable
data class ConfigurationUpdate(
  val factorioData: ConfigurationUpdateGameData,
  val allMods: List<ConfigurationUpdateModData>,
  val modStartupSettingsChange: Boolean,
  val migrationApplied: Boolean,
) : KafkatorioInstantPacketData() {

  @Serializable
  data class ConfigurationUpdateGameData(
    val oldVersion: String? = null,
    val newVersion: String? = null,
  )

  @Serializable
  data class ConfigurationUpdateModData(
    val modName: String,
    val currentVersion: String? = null,
    val previousVersion: String? = null,
  )
}


@Serializable
data class ConsoleChatUpdate(
  val authorPlayerIndex: PlayerIndex?,
  val content: String,
) : KafkatorioInstantPacketData()


@Serializable
data class ConsoleCommandUpdate(
  val authorPlayerIndex: PlayerIndex?,
  val command: String,
  val parameters: String,
) : KafkatorioInstantPacketData()


@Serializable
data class PrototypesUpdate(
  val prototypes: List<FactorioPrototype>,
) : KafkatorioInstantPacketData()


@Serializable
data class SurfaceUpdate(
  val index: SurfaceIndex,
  val daytime: Double,
  val name: String,
) : KafkatorioInstantPacketData()


/*  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  *** */
