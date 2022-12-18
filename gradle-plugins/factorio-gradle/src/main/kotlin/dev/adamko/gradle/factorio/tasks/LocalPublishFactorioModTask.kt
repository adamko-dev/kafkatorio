package dev.adamko.gradle.factorio.tasks

import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.tasks.IgnoreEmptyDirectories
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction

abstract class LocalPublishFactorioModTask @Inject constructor(
  private val files: FileSystemOperations,
) : DefaultTask() {

  @get:OutputDirectory
  abstract val clientModDirectory: DirectoryProperty

  @get:InputFiles
  @get:SkipWhenEmpty
  @get:PathSensitive(PathSensitivity.NAME_ONLY)
  @get:IgnoreEmptyDirectories
  abstract val modFiles: ConfigurableFileCollection

  @TaskAction
  fun install() {
    logger.lifecycle("Copying mod from ${modFiles.files} to ${clientModDirectory.asFile.get()}")

    files.copy {
      from(modFiles) {
        include("*.zip")
      }
      into(clientModDirectory)
    }
  }
}
