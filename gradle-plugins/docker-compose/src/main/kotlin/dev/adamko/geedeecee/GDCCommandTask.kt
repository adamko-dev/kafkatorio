package dev.adamko.geedeecee

import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.logging.LogLevel
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

abstract class GDCCommandTask @Inject constructor(
  private val executor: ExecOperations
) : DefaultTask() {

  init {
    super.setGroup(GDCPlugin.GCD_TASK_GROUP)
    description = "Run a docker-compose command."

    logging.captureStandardOutput(LogLevel.LIFECYCLE)
  }

  @get:InputDirectory
  abstract val workingDir: DirectoryProperty

  @get:Input
  abstract val separatedArgs: ListProperty<String>

  @TaskAction
  fun dockerCompose() {
    val separatedArgs: List<String> = separatedArgs.get()

    executor.exec {
      workingDir(this@GDCCommandTask.workingDir)
      commandLine(separatedArgs)
    }
  }

  @Suppress("FunctionName")
  fun `docker-compose`(args: String) {
    separatedArgs.addAll(
      parseSpaceSeparatedArgs("docker-compose $args")
    )
  }
}
