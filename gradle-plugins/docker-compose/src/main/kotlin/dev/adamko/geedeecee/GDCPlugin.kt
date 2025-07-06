package dev.adamko.geedeecee

import dev.adamko.geedeecee.internal.isSuccess
import dev.adamko.geedeecee.internal.parseSpaceSeparatedArgs
import dev.adamko.geedeecee.tasks.DockerContextFilesPreparation
import dev.adamko.geedeecee.tasks.DockerEnvUpdateTask
import dev.adamko.geedeecee.tasks.GDCCommandTask
import javax.inject.Inject
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.ProjectLayout
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.kotlin.dsl.*
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.gradle.language.base.plugins.LifecycleBasePlugin.ASSEMBLE_TASK_NAME


abstract class GDCPlugin @Inject constructor(
  private val providers: ProviderFactory,
  private val layout: ProjectLayout,
) : Plugin<Project> {

  override fun apply(project: Project) {
    project.plugins.apply(LifecycleBasePlugin::class)

    val gdcSettings = createSettings(project)

    val gdcTasks = GdcTasks(project, gdcSettings, providers)

    configureLifecycleBasePlugin(project, gdcTasks)
  }


  private fun createSettings(project: Project): GDCSettings =
    project.extensions.create<GDCSettings>(GDC_EXTENSION_NAME).apply {
      composeProjectName.convention(providers.provider { project.rootProject.name })
      composeProjectVersion.convention(providers.provider { project.version.toString() })
      containerRegistryHost.convention(
        providers.gradleProperty("dockerContainerRegistryHost")
//          .orElse("localhost")
      )
      srcDir.convention(layout.projectDirectory.dir("docker"))
      dockerBuildContextDir.convention(srcDir.dir("build"))
      stateDir.convention(layout.buildDirectory.dir("geedeecee/state/"))

      dotEnv.convention("COMPOSE_PROJECT_NAME", composeProjectName)
      dotEnv.convention("APP_NAME", providers.provider { project.name })
      dotEnv.convention("APP_GROUP", providers.provider { project.group.toString() })
      dotEnv.convention("PROJECT_VERSION", composeProjectVersion)
      dotEnv.convention("KAFKATORIO_VERSION", composeProjectVersion)
      dotEnv.convention("REGISTRY_HOST", containerRegistryHost)

      dockerActive.convention(isDockerActive())
    }


  private fun configureLifecycleBasePlugin(
    project: Project,
    gdcTasks: GdcTasks,
  ) {
    val assembleTask = project.tasks.named(ASSEMBLE_TASK_NAME)

    assembleTask {
      dependsOn(gdcTasks.dockerComposeEnvUpdate)
    }

    gdcTasks.dockerComposeUp.configure {
      dependsOn(assembleTask)
    }
  }


  @Suppress("UnstableApiUsage")
  private fun isDockerActive(): Provider<Boolean> =
    providers.exec {
      commandLine = parseSpaceSeparatedArgs("docker info")
      isIgnoreExitValue = true
    }.result.isSuccess


  private class GdcTasks(
    project: Project,
    private val gdcSettings: GDCSettings,
    private val providers: ProviderFactory
  ) {

    val dockerComposeEnvUpdate by project.tasks.registering(DockerEnvUpdateTask::class) {
      dotEnvFile.set(gdcSettings.srcDir.file(".env"))

      envProperties.addConventions(gdcSettings.dotEnv)
    }

    val dockerContextPrepareFiles by project.tasks.registering(DockerContextFilesPreparation::class) {
      into(gdcSettings.dockerBuildContextDir)
      includeEmptyDirs = false
//      dockerContextDir.set(gdcSettings.dockerBuildContextDir)
//      copySpec.convention(target.copySpec())
    }

    val dockerComposeUp by project.tasks.registering(GDCCommandTask::class) {
      doNotTrackState("Always run - state is managed by Docker")
      `docker-compose`("up --detach")
    }

    val dockerComposeDown by project.tasks.registering(GDCCommandTask::class) {
      doNotTrackState("Always run - state is managed by Docker")
      `docker-compose`("down")
    }

    @Suppress("unused")
    private val dockerComposeStop by project.tasks.registering(GDCCommandTask::class) {
      doNotTrackState("Always run - state is managed by Docker")
      `docker-compose`("stop")
    }

    val dockerComposeBuild by project.tasks.registering(GDCCommandTask::class) {
      `docker-compose`("build")
      cacheable.convention(true)
    }

    @Suppress("unused")
    val dockerComposePush by project.tasks.registering(GDCCommandTask::class) {
      doNotTrackState("Always run - state is managed by Docker")
      dependsOn(dockerComposeBuild)
      `docker-compose`("push")
    }

    val dockerComposeRemove by project.tasks.registering(GDCCommandTask::class) {
      doNotTrackState("Always run - state is managed by Docker")
      `docker-compose`("rm --stop --volumes --force")
    }

    init {
      dockerComposeUp.configure { dependsOn(dockerComposeBuild) }
      dockerComposeRemove.configure { dependsOn(dockerComposeDown) }

//    target.tasks.withType<DockerComposeExec>().configureEach {
//      dependsOn(dockerComposeEnvUpdate)
//      dockerIsActive.set(isDockerActive())
//      stateFile.set(temporaryDir.resolve("docker-state.md5"))
//    }

      project.tasks.withType<GDCCommandTask>().configureEach {
        dependsOn(dockerComposeEnvUpdate)
        dependsOn(dockerContextPrepareFiles)

        workingDir.convention(gdcSettings.srcDir)
        workingDirFiles.from(workingDir)
        dockerComposeExecutable.convention("docker-compose")
        systemPath.convention(providers.environmentVariable("PATH"))
        dockerActive.convention(gdcSettings.dockerActive)

        val stateFileName = workingDir.map { workingDir ->
          val workingDirHashCode = workingDir.asFile.hashCode()
          "dc_state${name.hashCode()}${workingDirHashCode}.md5"
        }
        stateFile.convention(
          gdcSettings.stateDir.file(stateFileName)
        )
      }
    }
  }


  companion object {
    const val GDC_EXTENSION_NAME = "geedeecee"
    const val GCD_TASK_GROUP = "docker-compose"
  }
}
