package dev.adamko.gradle.factorio

import javax.inject.Inject
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.slf4j.LoggerFactory

/**
 * Configure a [Project] to produce components that will be used in a Factorio mod, but no tasks
 * that will create a Factorio mod.
 */
abstract class FactorioModLibraryPlugin @Inject constructor(
//  private val objects: ObjectFactory,
//  private val providers: ProviderFactory,
//  private val layout: ProjectLayout,
//  private val files: FileSystemOperations,
) : Plugin<Project> {

  internal val logger = LoggerFactory.getLogger(this::class.java)

  internal var factorioModConfigurations: FactorioModConfigurations? = null
    private set

  override fun apply(target: Project) {
    factorioModConfigurations = target.createConfigurations()
  }

  private fun Project.createConfigurations(): FactorioModConfigurations {
    return FactorioModConfigurations(
      factorioMod = project.configurations.register(CONFIGURATION_NAME__FACTORIO_MOD) {
        asConsumer()
        factorioModAttributes(objects)
      },

      factorioModProvider = project.configurations.register(
        CONFIGURATION_NAME__FACTORIO_MOD_PROVIDER
      ) {
        asProvider()
        factorioModAttributes(objects)
      },
    )
  }

  companion object {
    const val CONFIGURATION_NAME__FACTORIO_MOD = "factorioMod"
    const val CONFIGURATION_NAME__FACTORIO_MOD_PROVIDER = "factorioModProvider"
  }
}
