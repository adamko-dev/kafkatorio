package dev.adamko.gradle.factorio.tasks

import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.tasks.*
import org.gradle.api.tasks.PathSensitivity.NAME_ONLY

/**
 * Publish a packaged Factorio mod into a local directory.
 */
abstract class LocalPublishFactorioModTask @Inject constructor(
  private val files: FileSystemOperations,
) : DefaultTask() {

  /**
   * The mod directory of a locally installed Factorio client.
   */
  @get:OutputDirectory
  abstract val clientModDirectory: DirectoryProperty

  /**
   * Mod files. Should only contain `.zip` files (other files will be skipped).
   */
  @get:InputFiles
  @get:SkipWhenEmpty
  @get:PathSensitive(NAME_ONLY)
  @get:IgnoreEmptyDirectories
  abstract val modFiles: ConfigurableFileCollection

  @TaskAction
  fun install() {
    logger.lifecycle("Copying mod (${modFiles.files}) to ${clientModDirectory.asFile.get()}")

    files.copy {
      from(modFiles) {
        include("*.zip")
      }
      into(clientModDirectory)
    }
  }
}
