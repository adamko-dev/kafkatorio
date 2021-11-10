package dev.adamko.factoriowebmap.archetype

import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension


plugins {
  id("dev.adamko.factoriowebmap.archetype.base")
  id("dev.adamko.factoriowebmap.archetype.node")
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
