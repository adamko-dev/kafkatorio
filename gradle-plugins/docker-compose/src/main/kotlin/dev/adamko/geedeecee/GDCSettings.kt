package dev.adamko.geedeecee

import dev.adamko.geedeecee.config.DotEnvContent
import dev.adamko.geedeecee.internal.adding
import javax.inject.Inject
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.newInstance

abstract class GDCSettings @Inject constructor(
  objects: ObjectFactory
) : ExtensionAware {

  /**
   * The project name used by Docker Compose.
   *
   * See https://docs.docker.com/compose/how-tos/project-name/
   */
  abstract val composeProjectName: Property<String>

  abstract val composeProjectVersion: Property<String>

  abstract val containerRegistryHost: Property<String>

  /**
   * Source directory of files that are used to run Docker Compose.
   *
   * The directory must contain the `docker-compose.yml` file.
   *
   * The contents of this directory will be used for up-to-date checks.
   */
  abstract val srcDir: DirectoryProperty

  val dotEnv: DotEnvContent =
    extensions.adding("dotEnv", objects.newInstance())

  abstract val dockerActive: Property<Boolean>

  abstract val dockerBuildContextDir: DirectoryProperty

  abstract val stateDir: DirectoryProperty

//  @Suppress("PropertyName", "FunctionName")
//  abstract class DotEnv(
//    private val dotEnv: MapProperty<String, String>,
//    private val providers: ProviderFactory,
//  ) {
//    var COMPOSE_API_VERSION: Provider<String> by dotEnv
//    fun COMPOSE_API_VERSION(value: () -> String) {
//      COMPOSE_API_VERSION = providers.provider(value)
//    }
//    //    var COMPOSE_CONVERT_WINDOWS_PATHS: String
////    var COMPOSE_FILE: String
////    var COMPOSE_HTTP_TIMEOUT: String
////    var COMPOSE_PROFILES: String
////    var COMPOSE_PROJECT_NAME: String
////    var COMPOSE_TLS_VERSION: String
////    var DOCKER_CERT_PATH: String
////    var DOCKER_HOST: String
////    var DOCKER_TLS_VERIFY: String
//    fun setComposeApiVersion(composeApiVersion: String) {}
//    fun setComposeConvertWindowsPaths(composeConvertWindowsPaths: String) {}
//    fun setComposeFile(composeFile: String) {}
//    fun setComposeHttpTimeout(composeHttpTimeout: String) {}
//    fun setComposeProfiles(composeProfiles: String) {}
//    fun setComposeProjectName(composeProjectName: String) {}
//    fun setComposeTlsVersion(composeTlsVersion: String) {}
//    fun setDockerCertPath(dockerCertPath: String) {}
//    fun setDockerHost(dockerHost: String) {}
//    fun setDockerTlsVerify(dockerTlsVerify: String) {}
//  }

}
//
//private operator fun <PV : Provider<String>> MapProperty<String, String>.setValue(
//  dotEnv: GDCSettings.DotEnv,
//  property: KProperty<*>,
//  v: PV
//) {
//  put(property.name, v)
//}
//
//private operator fun MapProperty<String, String>.getValue(
//  dotEnv: GDCSettings.DotEnv,
//  property: KProperty<*>
//): Provider<String> {
//  return getting(property.name)
//}
