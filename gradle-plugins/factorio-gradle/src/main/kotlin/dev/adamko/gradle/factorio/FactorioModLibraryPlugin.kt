package dev.adamko.gradle.factorio

import dev.adamko.gradle.factorio.internal.FactorioModConfigurations
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

  override fun apply(project: Project) {
    val factorioModConfigurations = FactorioModConfigurations(project)
    project.extensions.add(
      "factorioModConfigurations",
      factorioModConfigurations,
    )
  }

  companion object
}
