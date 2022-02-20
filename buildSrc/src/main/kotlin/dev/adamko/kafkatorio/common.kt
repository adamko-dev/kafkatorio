package dev.adamko.kafkatorio

import kotlinx.serialization.json.Json
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

val jsonMapper = Json {
  prettyPrint = true
  prettyPrintIndent = "  "
}


object Versions {
  const val kotlinLang = "1.6"
  const val jvmTarget = "11"

  const val node = "14.19.0"

  const val kotlinWrappers = "0.0.1-pre.300-kotlin-1.6.10"
  const val junit = "5.8.2"
  const val kotest = "5.1.0"
  const val mockk = "1.12.1"
}


/**
 * `kotlin-js` adds a directory in the root-dir for the Yarn lock.
 * That's a bit annoying. It's a little neater if it's in the
 * gradle dir, next to the version-catalog.
 */
fun Project.relocateKotlinJsStore() {

  afterEvaluate {
    rootProject.extensions.configure<YarnRootExtension> {
      lockFileDirectory = project.rootDir.resolve("gradle/kotlin-js-store")
    }
  }

//  rootProject.plugins.withType(YarnPlugin::class) {
//    rootProject.the<YarnRootExtension>().lockFileDirectory =
//      project.rootDir.resolve("gradle/kotlin-js-store")
//  }

  // KVision tries to be clever too - see io.kvision.gradle.KVisionGradleSubplugin
  rootProject.extra.set("io.kvision.plugin.enableHiddenKotlinJsStore", "false")

}
