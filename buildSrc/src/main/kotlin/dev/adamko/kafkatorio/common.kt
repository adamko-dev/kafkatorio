package dev.adamko.kafkatorio

import io.kvision.gradle.KVisionExtension
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension


object Versions {
  const val node = "14.19.1"
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

  // KVision tries to be clever too
  project.extensions.findByType<KVisionExtension>()?.apply {
    enableHiddenKotlinJsStore.set(false)
  }
}
