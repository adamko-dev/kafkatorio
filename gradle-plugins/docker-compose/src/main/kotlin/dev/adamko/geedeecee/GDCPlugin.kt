package dev.adamko.geedeecee

import javax.inject.Inject
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.ProjectLayout
import org.gradle.api.provider.ProviderFactory
import org.gradle.kotlin.dsl.create
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs


abstract class GDCPlugin @Inject constructor(
  private val providers: ProviderFactory,
  private val layout: ProjectLayout,
) : Plugin<Project> {


  override fun apply(target: Project) {
    target.createSettings()
  }


  private fun Project.createSettings(): GDCSettings =
    extensions.create<GDCSettings>(GDC_EXTENSION_NAME).apply {
      composeProjectName.convention(providers.provider { project.name })
      srcDir.convention(layout.projectDirectory.dir("docker"))

      dotEnv.put("COMPOSE_PROJECT_NAME", composeProjectName)
      dotEnv.put("PROJECT_VERSION", providers.provider { project.version.toString() })
      dotEnv.put("REGISTRY_HOST", providers.gradleProperty("dockerContainerRegistryHost"))

      enabled.convention(
        providers.exec {
          commandLine = parseSpaceSeparatedArgs("docker info")
          isIgnoreExitValue = true
        }.result.map {
          it.exitValue == 0
        }
      )
    }


  companion object {
    const val GDC_EXTENSION_NAME = "geedeecee"
    const val GCD_TASK_GROUP = "docker-compose"
  }

}
