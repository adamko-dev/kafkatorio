package dev.adamko.kafkatorio.schema2

import dev.adamko.kafkatorio.schema.common.PlayerIndex
import dev.adamko.kafkatorio.schema.common.SurfaceIndex
import dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2
import kotlinx.serialization.Serializable


/** No key, no caching. Must be sent instantly. */
@Serializable
sealed class KafkatorioInstantPacketData2 : KafkatorioPacketData2()


/*  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  *** */


@Serializable
data class ConfigurationUpdate(
  val factorioData: ConfigurationUpdateGameData,
  val allMods: List<ConfigurationUpdateModData>,
  val modStartupSettingsChange: Boolean,
  val migrationApplied: Boolean,
) : KafkatorioInstantPacketData2() {

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
) : KafkatorioInstantPacketData2()


@Serializable
data class ConsoleCommandUpdate(
  val authorPlayerIndex: PlayerIndex?,
  val command: String,
  val parameters: String,
) : KafkatorioInstantPacketData2()


@Serializable
data class PrototypesUpdate(
  val prototypes: List<FactorioPrototype2>,
) : KafkatorioInstantPacketData2()


@Serializable
data class SurfaceUpdate(
  val index: SurfaceIndex,
  val daytime: Double,
  val name: String,
) : KafkatorioInstantPacketData2()


/*  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  ***  *** */
