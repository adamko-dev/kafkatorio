package dev.adamko.gradle.factorio.tasks

import javax.inject.Inject
import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.internal.file.*
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*

abstract class AssembleFactorioModContents @Inject constructor(
  private val files: FileSystemOperations,
) : DefaultTask() {

  @get:Input
  abstract val modName: Property<String>

  @get:Input
  abstract val modVersion: Property<String>

  @get:InputFiles
  @get:SkipWhenEmpty
  @get:PathSensitive(PathSensitivity.NAME_ONLY)
  @get:IgnoreEmptyDirectories
  abstract val modFiles: ConfigurableFileCollection

  @get:OutputDirectory
  abstract val destination: DirectoryProperty

  @TaskAction
  fun assemble() {
    val modName = modName.get()
    val modVersion = modVersion.get()
    val modDirectory = "${modName}_${modVersion}"

    files.sync {
      from(modFiles)
      into(destination.dir(modDirectory))
    }
  }
}
