package dev.adamko.geedeecee

import dev.adamko.geedeecee.tasks.DockerContextFilesPreparation
import dev.adamko.geedeecee.tasks.DockerEnvUpdateTask
import dev.adamko.geedeecee.tasks.GDCCommandTask
import javax.inject.Inject
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.withType
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs


@Suppress("UnstableApiUsage")
abstract class GDCPlugin @Inject constructor(
  private val providers: ProviderFactory,
) : Plugin<Project> {


  override fun apply(target: Project) {
    target.plugins.apply(LifecycleBasePlugin::class)

    val gdcSettings = target.createSettings()

    val dockerComposeEnvUpdate by target.tasks.registering(DockerEnvUpdateTask::class) {
      dotEnvFile.set(gdcSettings.srcDir.file(".env"))

      envProperties.putAll(gdcSettings.dotEnv)
//      properties(
//        "COMPOSE_PROJECT_NAME" to gdcSettings.composeProjectName,
//        "KAFKATORIO_VERSION" to gdcSettings.composeProjectVersion,
//        "REGISTRY_HOST" to providers.gradleProperty("dockerContainerRegistryHost"),
//      )
    }

    val dockerContextPrepareFiles by target.tasks.registering(DockerContextFilesPreparation::class) {
      into(gdcSettings.dockerBuildContextDir)
      includeEmptyDirs = false
//      dockerContextDir.set(gdcSettings.dockerBuildContextDir)
//      copySpec.convention(target.copySpec())
//      copySpec = Action {  }
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
      cacheable.set(true)
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

//    target.tasks.withType<DockerComposeExec>().configureEach {
//      dependsOn(dockerComposeEnvUpdate)
//      dockerIsActive.set(isDockerActive())
//      stateFile.set(temporaryDir.resolve("docker-state.md5"))
//    }

    target.tasks.withType<GDCCommandTask>().configureEach {
      dependsOn(dockerComposeEnvUpdate)
      dependsOn(dockerContextPrepareFiles)

      workingDir.convention(gdcSettings.srcDir)
      dockerActive.set(gdcSettings.dockerActive)
      stateFile.set(
        gdcSettings.stateDir.file(workingDir.map { "dc_state${name.hashCode()}${it.asFile.hashCode()}.md5" })
      )
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
      composeProjectName.convention(providers.provider { rootProject.name })
      composeProjectVersion.convention(providers.provider { project.version.toString() })
      containerRegistryHost.convention(providers.gradleProperty("dockerContainerRegistryHost"))
      srcDir.convention(layout.projectDirectory.dir("docker"))
      dockerBuildContextDir.convention(srcDir.dir("build"))
      stateDir.convention(layout.buildDirectory.dir("geedeecee/state/"))

      dotEnv.put("COMPOSE_PROJECT_NAME", composeProjectName)
      dotEnv.put("APP_NAME", providers.provider { project.name })
      dotEnv.put("APP_GROUP", providers.provider { project.group.toString() })
      dotEnv.put("PROJECT_VERSION", composeProjectVersion)
      dotEnv.put("KAFKATORIO_VERSION", composeProjectVersion)
      dotEnv.put("REGISTRY_HOST", containerRegistryHost)

      dockerActive.convention(isDockerActive())
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
