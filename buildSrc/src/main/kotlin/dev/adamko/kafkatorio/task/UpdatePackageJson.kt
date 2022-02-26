package dev.adamko.kafkatorio.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile

abstract class UpdatePackageJson : DefaultTask() {

  @get:Input
  abstract val propertiesToCheck: MapProperty<String, String>

  @get:OutputFile
  abstract val packageJsonFile: RegularFileProperty

  init {
    super.doFirst {
      propertiesToCheck.disallowChanges()
      packageJsonFile.disallowChanges()
    }
  }
}
