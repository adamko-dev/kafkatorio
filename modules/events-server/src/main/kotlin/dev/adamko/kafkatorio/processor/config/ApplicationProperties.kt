package dev.adamko.kafkatorio.processor.config

import dev.adamko.kafkatorio.processor.config.PortVal.Companion.portVal
import java.time.Clock
import java.util.Properties
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.nonEmptyString

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
