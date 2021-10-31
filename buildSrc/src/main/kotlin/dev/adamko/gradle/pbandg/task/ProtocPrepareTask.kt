package dev.adamko.gradle.pbandg.task


import dev.adamko.gradle.pbandg.settings.PBAndGSettings
import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType


abstract class ProtocPrepareTask : DefaultTask() {

  @Internal
  val protocPluginConfig: Provider<PBAndGSettings> = project.provider {
    project.extensions.getByType<PBAndGSettings>()
  }

  @get:OutputDirectory
  val outputDirectory: DirectoryProperty = project.objects.directoryProperty()
    .convention(protocPluginConfig.flatMap { it.protocWorkingDir })

  @Internal
  val protocOutput: RegularFileProperty = project.objects.fileProperty()

  @Internal
  val protocDependency: Provider<ExternalModuleDependency> =
    protocPluginConfig.flatMap { it.dependency }

  init {
    super.setGroup("protobuf")
    outputs.dir(outputDirectory)
  }

  @TaskAction
  fun action() {

    val protocDep: Configuration = project.configurations.create("protoc") {
      isVisible = false
      isCanBeConsumed = false
      isCanBeResolved = true
      isTransitive = false

      defaultDependencies {
        add(protocDependency.get())
      }
    }

    logger.lifecycle("Downloading protoc")
    val resolvedProtocDep = protocDep.singleFile
    logger.lifecycle("Downloaded $resolvedProtocDep")

    project.sync {
      from(resolvedProtocDep)
      into(outputDirectory)
      logger.lifecycle("Syncing from $resolvedProtocDep to ${outputDirectory.asFile.get().canonicalPath}")
      protocOutput.set(outputDirectory.asFileTree.singleFile)
    }
  }
}
