package dev.adamko.factorio_gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

abstract class FactorioModPlugin : Plugin<Project> {

  override fun apply(target: Project) {
    target.createSettings()
  }

  private fun Project.createSettings(): FactorioModSettings {
    return extensions.create<FactorioModSettings>(FACTORIO_GRADLE_EXTENSION_NAME).apply {

    }
  }

  companion object {
    const val FACTORIO_GRADLE_EXTENSION_NAME = "factorio"
  }
}
