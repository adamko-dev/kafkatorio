package dev.adamko.kafkatorio.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile

abstract class UpdatePackageJson : DefaultTask() {
  @get:Input
  val propertiesToCheck: MutableMap<String, String> = mutableMapOf()
  @get:OutputFile
  abstract val packageJsonFile: RegularFileProperty
}
