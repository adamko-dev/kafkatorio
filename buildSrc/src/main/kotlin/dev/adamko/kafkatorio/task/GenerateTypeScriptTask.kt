package dev.adamko.kafkatorio.task

import javax.inject.Inject
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction


abstract class GenerateTypeScriptTask @Inject constructor(
  private val fileOps: FileSystemOperations,
) : JavaExec() {

  @get:OutputDirectory
  abstract val output: DirectoryProperty

  init {
    group = "kt2ts"
  }

  @TaskAction
  fun generate() {
    fileOps.delete { delete(temporaryDir) }
    temporaryDir.mkdirs()

    super.exec()

    fileOps.sync {
      from(temporaryDir)
      into(output)
      include("**/*.ts")
    }
  }
}
