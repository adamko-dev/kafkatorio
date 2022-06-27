package dev.adamko.kafkatorio.processor.config

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addEnvironmentSource
import com.sksamuel.hoplite.addResourceOrFileSource
import java.nio.file.Path


// loaded by hoplite
data class ApplicationProperties(
  val webPort: PortVal,
  val tileDir: Path,
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


//import dev.adamko.kafkatorio.processor.config.PortVal.Companion.portVal
//import java.nio.file.Path
//import java.time.Clock
//import java.util.Properties
//import kotlin.io.path.Path
//import org.http4k.cloudnative.env.Environment
//import org.http4k.cloudnative.env.EnvironmentKey
//import org.http4k.lens.nonEmptyString
//
///**
// * Load properties, in descending priority order, from
// *
// * 1. Environment variables
// * 2. This app's env file (as defined by the environment variable [Lenses.APPLICATION_PROPERTIES_FILE_PATH])
// * # 3. A resources-dir yaml file
// *
// */
//internal class ApplicationProperties(
//  vararg environments: Environment = arrayOf(
//    Environment.env(),
//    Environment.fromEnvVarPath(),
//    Environment.fromResourcesFile(".secret.config.yml")
//  )
//) {
//
//
//  private val appProps: Environment =
//    environments.reduce { acc, environment -> acc.overrides(environment) }
//
//
//  fun clock(): Clock = Clock.systemUTC()
//
//
//  /**
//   * The port of the server
//   *
//   * Set by GCP ([docs](https://cloud.google.com/run/docs/reference/container-contract#env-vars)).
//   */
//  val serverPort: PortVal
//    get() = EnvironmentKey.portVal().defaulted("PORT", PortVal.DEFAULT)[appProps]
//
//
//  val kafkaConfig: Properties
//    get() = EnvironmentKey
//      .nonEmptyString()
//      .map { srcString ->
//        srcString.reader().use { stringReader ->
//          Properties().also { properties ->
//            properties.load(stringReader)
//          }
//        }
//      }
//      .required("kafka.config")[appProps]
//
//
//  val webmapTileDir: Path
//    get() = EnvironmentKey
//      .nonEmptyString()
//      .map(::Path)
//      .required("tileServer.webmapTileDir")[appProps]
//
//}
