package dev.adamko.kafkatorio.task

import dev.adamko.kafkatorio.gradle.dropDirectory
import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.file.ProjectLayout
import org.gradle.api.internal.file.FileOperations
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

abstract class DownloadCLibTask @Inject constructor(
  private val objects: ObjectFactory,
  private val providers: ProviderFactory,
  private val layout: ProjectLayout,
  private val files: FileOperations,
  private val fileOps: FileSystemOperations,
) : DefaultTask() {

  @get:Input
  abstract val sourceUrl: Property<String>

//  @get:Input
//  abstract val libName: Property<String>

  @get:OutputDirectory
  abstract val output: DirectoryProperty

  init {
    group = "interop setup"
  }

  @TaskAction
  fun download() {
    val sourceUrl = sourceUrl.get()
//    val libFilename = libName.get() + ".zip"
//    val downloadedLib = temporaryDir.resolve(libFilename)

    ant.invokeMethod(
      "get", mapOf(
        "src" to sourceUrl,
        "dest" to temporaryDir,
        "verbose" to true,
      )
    )

    val downloadedLib = files.fileTree(temporaryDir).files.first()

    fileOps.sync {
      from(files.zipTree(downloadedLib)) {
        eachFile { relativePath = dropDirectory() }
        include("**/*.h")
        includeEmptyDirs = false
      }
      into(output)
    }
  }
}
