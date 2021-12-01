package dev.adamko.kafkatorio.lang

import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension


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

}
