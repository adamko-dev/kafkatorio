package dev.adamko.kafkatorio

import kotlinx.serialization.json.Json
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

val jsonMapper = Json {
  prettyPrint = true
  prettyPrintIndent = "  "
}


/**
 * `kotlin-js` adds a directory in the root-dir for the Yarn lock.
 * That's a bit annoying. It's a little neater if it's in the
 * gradle dir, next to the version-catalog.
 */
fun Project.relocateKotlinJsStore() {
  afterEvaluate {
    rootProject.extensions.configure<YarnRootExtension> {
      lockFileDirectory = rootProject.rootDir.resolve("gradle/kotlin-js-store")
    }
  }
}
