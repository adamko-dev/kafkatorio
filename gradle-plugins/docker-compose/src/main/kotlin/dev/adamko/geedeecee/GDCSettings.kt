package dev.adamko.geedeecee

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal

interface GDCSettings {

  @get:Input
  val composeProjectName: Property<String>

  @get:Input
  val composeProjectVersion: Property<String>

  @get:Input
  val containerRegistryHost: Property<String>

  @get:Internal
  val srcDir: DirectoryProperty

  @get:Input
  val dotEnv: MapProperty<String, String>

  @get:Input
  val dockerActive: Property<Boolean>

  @get:Input
  val dockerBuildContextDir: DirectoryProperty

  val stateDir: DirectoryProperty

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
