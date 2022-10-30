package dev.adamko.gradle.factorio

import javax.inject.Inject
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.create


abstract class FactorioModPlugin @Inject constructor(
  private val objects: ObjectFactory,
) : Plugin<Project> {

  override fun apply(target: Project) {
    target.createSettings()

    val factorioModConfiguration =
      target.configurations.register(FACTORIO_GRADLE_CONFIGURATION_NAME) {
        asConsumer()
        factorioModAttributes(objects)
      }
  }

  private fun Project.createSettings(): FactorioModSettings {
    return extensions.create<FactorioModSettings>(FACTORIO_GRADLE_EXTENSION_NAME).apply {

    }
  }

  companion object {
    const val FACTORIO_GRADLE_EXTENSION_NAME = "factorioMod"
    const val FACTORIO_GRADLE_CONFIGURATION_NAME = "factorioMod"

    const val TASK_GROUP = "factorioMod"

    /** Lifecycle task for publishing the mod locally */
    const val PUBLISH_MOD_LOCAL_TASK_NAME = "publishModLocal"

    const val PUBLISH_MOD_TASK_NAME = "publishMod"
  }
}
