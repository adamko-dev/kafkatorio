package dev.adamko.kafkatorio.task

import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class DownloadFileTask @Inject constructor(
  private val objects: ObjectFactory,
  private val providers: ProviderFactory,
  private val layout: ProjectLayout,
) : DefaultTask() {

  @get:Input
  abstract val target: Property<String>

  @get:OutputFile
  abstract val downloadDest: RegularFileProperty

  @TaskAction
  fun download() {
    val downloadDest = downloadDest.get()

    ant.invokeMethod(
      "get", mapOf(
        "src" to target,
        "dest" to downloadDest,
        "verbose" to true,
      )
    )
  }
}
