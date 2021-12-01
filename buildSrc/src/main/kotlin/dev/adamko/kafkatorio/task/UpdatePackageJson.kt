package dev.adamko.kafkatorio.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input

abstract class UpdatePackageJson : DefaultTask() {
  @get:Input
  val propertiesToCheck: MutableMap<String, String> = mutableMapOf()
}
