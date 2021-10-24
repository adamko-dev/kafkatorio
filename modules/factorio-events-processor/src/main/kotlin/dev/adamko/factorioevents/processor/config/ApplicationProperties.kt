package dev.adamko.factorioevents.processor.config

import com.fasterxml.jackson.databind.JsonNode
import dev.adamko.factorioevents.processor.config.PortVal.Companion.portVal
import java.io.StringReader
import java.time.Clock
import java.util.Properties
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.core.Credentials
import org.http4k.core.Uri
import org.http4k.format.Jackson
import org.http4k.format.Json
import org.http4k.lens.boolean
import org.http4k.lens.map
import org.http4k.lens.nonEmptyString
import org.http4k.lens.uri

/**
 * Load properties, in descending priority order, from
 *
 * 1. Environment variables
 * 2. This app's env file (as defined by the environment variable [Lenses.APPLICATION_PROPERTIES_FILE_PATH])
 * # 3. A resources-dir yaml file
 *
 */
class ApplicationProperties(
  vararg environments: Environment = arrayOf(
    Environment.env(),
    Environment.fromEnvVarPath(),
    Environment.fromResourcesFile(".secret.config.yml")
  )
) {
  private val appProps: Environment =
    environments.reduce { acc, environment -> acc.overrides(environment) }

  fun clock(): Clock = Clock.systemUTC()

  //<editor-fold desc="Server properties">

  /**
   * The port of the server
   *
   * Set by GCP ([docs](https://cloud.google.com/run/docs/reference/container-contract#env-vars)).
   */
  val serverPort: PortVal
    get() = EnvironmentKey.portVal().defaulted("PORT", PortVal.DEFAULT)[appProps]

  //</editor-fold>


  //<editor-fold desc="Kafka properties">

  val kafkaConfig: Properties
    get() = EnvironmentKey
      .nonEmptyString()
      .map {
        it.reader().use { sr ->
          Properties().also { p ->
            p.load(sr)
          }
        }
      }
      .required("kafka.config")[appProps]

  //</editor-fold>

}
