package dev.adamko.gradle.factorio

import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.internal.file.FileOperations
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

abstract class FactorioModInstallTask @Inject constructor(
  private val files: FileSystemOperations,
) : DefaultTask() {

  @get:Internal
  abstract val clientModDirectory: DirectoryProperty

  @get:InputFiles
  abstract val modFiles: ConfigurableFileCollection

  init {
    description = "Copy the mod to the Factorio client"
    group = FactorioModPlugin.TASK_GROUP

    super.onlyIf {
      require(it is FactorioModInstallTask)
      it.clientModDirectory.asFile.get().exists()
    }
  }

  @TaskAction
  fun install() {
    logger.lifecycle("Copying mod from $modFiles to $clientModDirectory")

    files.copy {
      from(modFiles)
      into(clientModDirectory)
    }
  }

}
