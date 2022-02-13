package dev.adamko.kafkatorio

import dev.adamko.kafkatorio.processor.config.env
import dev.adamko.kafkatorio.processor.config.fromEnvVarPath
import dev.adamko.kafkatorio.processor.config.fromResourcesFile
import java.time.Clock
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.cloudnative.env.Secret
import org.http4k.lens.nonEmptyString
import org.http4k.lens.secret


/**
 * Load properties, in descending priority order, from
 *
 * 1. Environment variables
 * 2. This app's env file (as defined by the environment variable [Lenses.APPLICATION_PROPERTIES_FILE_PATH])
 * # 3. A resources-dir yaml file
 *
 */
internal class ApplicationProperties(
  vararg environments: Environment = arrayOf(
    Environment.env(),
    Environment.fromEnvVarPath(),
    Environment.fromResourcesFile(".secret.config.yml")
  )
) {

  private val appProps: Environment =
    environments.reduce { acc, environment -> acc.overrides(environment) }


  fun clock(): Clock = Clock.systemUTC()


  val factorioModPortalUsername: String
    get() = EnvironmentKey
      .nonEmptyString()
      .required("factorio-mod-portal.username")[appProps]


  val factorioModPortalPassword: Secret
    get() = EnvironmentKey
      .secret()
      .required("factorio-mod-portal.password")[appProps]

}
