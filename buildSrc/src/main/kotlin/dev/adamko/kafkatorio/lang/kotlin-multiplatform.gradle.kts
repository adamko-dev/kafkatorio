package dev.adamko.kafkatorio.lang

import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension


plugins {
  id("dev.adamko.kafkatorio.base")
  id("dev.adamko.kafkatorio.lang.node")
  kotlin("multiplatform")
}


afterEvaluate {

  rootProject.extensions.configure<NodeJsRootExtension> {
    val projectNodeInstallDir = file("$rootDir/.gradle").resolve("nodejs")
    if (installationDir != projectNodeInstallDir) {
      installationDir = projectNodeInstallDir
    }
  }

  rootProject.extensions.configure<YarnRootExtension> {
    // kotlin-js adds a directory in the root-dir for the Yarn lock.
    // That's a bit annoying. It's a little neater if it's in the
    // gradle dir, next to the version-catalog.
    lockFileDirectory = rootProject.rootDir.resolve("gradle/kotlin-js-store")
  }


}
