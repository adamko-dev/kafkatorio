package dev.adamko.kafkatorio.server.config

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addEnvironmentSource
import com.sksamuel.hoplite.addResourceOrFileSource
import java.nio.file.Path


// loaded by hoplite
data class ApplicationProperties(
  val webPort: PortVal,
  val serverDataDir: Path,
  val kafkaAdminConfig: Map<String, String>,
  val kafkaStreamsConfig: Map<String, String>,

  val socketServerHost: String,
  val socketServerPort: Int,
) {

  companion object {
    /** use Hoplite to load config from environment variables or properties files */
    fun load(): ApplicationProperties =
      ConfigLoaderBuilder.default()
        .addDefaults()
        .allowUnresolvedSubstitutions()
        .allowEmptyTree()
        .addEnvironmentSource()
        .addResourceOrFileSource("/.secret.config.yml", optional = true)
        .addResourceOrFileSource("/config.yml", optional = false)
        .build()
        .loadConfigOrThrow()
  }
}
