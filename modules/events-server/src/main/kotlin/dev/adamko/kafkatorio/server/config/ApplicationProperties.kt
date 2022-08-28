package dev.adamko.kafkatorio.server.config

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addEnvironmentSource
import com.sksamuel.hoplite.addResourceOrFileSource
import dev.adamko.kafkatorio.schema.common.FactorioServerId
import dev.adamko.kafkatorio.schema.common.FactorioServerToken
import dev.adamko.kafkatorio.schema.common.MaskedValue
import java.nio.file.Path


// loaded by hoplite
data class ApplicationProperties(
  val webPort: PortVal,
  val serverDataDir: Path,
  val kafkaAdminConfig: Map<String, String>,
  val kafkaStreamsConfig: Map<String, String>,

  val socketServerHost: String,
  val socketServerPort: Int,

  val jwtSecret: MaskedValue,

  val kafkatorioServers: Map<FactorioServerToken, FactorioServerId>
) {

  companion object {
    /** use Hoplite to load config from environment variables or properties files */
    fun load(): ApplicationProperties =
      ConfigLoaderBuilder.default()
        .addDefaults()
        .allowUnresolvedSubstitutions()
        .allowEmptySources()
        .addEnvironmentSource()
        .addResourceOrFileSource("/.secret.config.yml", optional = true)
        .addResourceOrFileSource("/config.yml", optional = false)
        .build()
        .loadConfigOrThrow()
  }
}
