package dev.adamko.geedeecee

import javax.inject.Inject
import org.gradle.api.*
import org.gradle.api.file.ProjectLayout
import org.gradle.api.logging.*
import org.gradle.api.provider.*
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.*
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs


@Suppress("UnstableApiUsage")
abstract class GDCPlugin @Inject constructor(
  private val providers: ProviderFactory,
  private val layout: ProjectLayout,
) : Plugin<Project> {


  override fun apply(target: Project) {
    target.plugins.apply(LifecycleBasePlugin::class)

    val gdcSettings = target.createSettings()

    val dockerComposeEnvUpdate by target.tasks.registering(DockerEnvUpdateTask::class) {
      dotEnvFile.set(gdcSettings.srcDir.file(".env"))

      envProperties.convention(gdcSettings.dotEnv)
//      properties(
//        "COMPOSE_PROJECT_NAME" to gdcSettings.composeProjectName,
//        "KAFKATORIO_VERSION" to gdcSettings.composeProjectVersion,
//        "REGISTRY_HOST" to providers.gradleProperty("dockerContainerRegistryHost"),
//      )
    }

    val dockerComposeUp by target.tasks.registering(GDCCommandTask::class) {
      `docker-compose`("up -d")
    }
    val dockerComposeDown by target.tasks.registering(GDCCommandTask::class) {
      `docker-compose`("down")
    }
    val dockerComposeStop by target.tasks.registering(GDCCommandTask::class) {
      `docker-compose`("stop")
    }
    val dockerComposeBuild by target.tasks.registering(GDCCommandTask::class) {
      `docker-compose`("build")
    }
    val dockerComposePush by target.tasks.registering(GDCCommandTask::class) {
      dependsOn(dockerComposeBuild)
      `docker-compose`("push")
    }
    val dockerComposeRemove by target.tasks.registering(GDCCommandTask::class) {
      `docker-compose`("rm --stop -v -f")
    }

    dockerComposeUp.configure { dependsOn(dockerComposeBuild) }
    dockerComposeRemove.configure { dependsOn(dockerComposeDown) }

    target.tasks.withType<GDCCommandTask>().configureEach {
      workingDir.convention(gdcSettings.srcDir)
      dependsOn(dockerComposeEnvUpdate)
    }

    val assembleTasks = target.tasks.matching { LifecycleBasePlugin.ASSEMBLE_TASK_NAME == it.name }

    assembleTasks.configureEach {
      dependsOn(dockerComposeEnvUpdate)
    }

    dockerComposeUp.configure {
      dependsOn(assembleTasks)
    }

  }


  private fun Project.createSettings(): GDCSettings =
    extensions.create<GDCSettings>(GDC_EXTENSION_NAME).apply {
      composeProjectName.convention(providers.provider { project.name })
      composeProjectVersion.convention(providers.provider { project.version.toString() })
      containerRegistryHost.convention(providers.gradleProperty("dockerContainerRegistryHost"))
      srcDir.convention(layout.projectDirectory.dir("docker"))

      dotEnv.put("COMPOSE_PROJECT_NAME", composeProjectName)
      dotEnv.put("PROJECT_VERSION", composeProjectVersion)
      dotEnv.put("KAFKATORIO_VERSION", composeProjectVersion)
      dotEnv.put("REGISTRY_HOST", containerRegistryHost)

      enabled.convention(isDockerActive())
    }


  private fun isDockerActive(): Provider<Boolean> =
    providers.exec {
      commandLine = parseSpaceSeparatedArgs("docker info")
      isIgnoreExitValue = true
    }.result.map {
      it.exitValue == 0
    }


  companion object {
    const val GDC_EXTENSION_NAME = "geedeecee"
    const val GCD_TASK_GROUP = "docker-compose"
  }

}
