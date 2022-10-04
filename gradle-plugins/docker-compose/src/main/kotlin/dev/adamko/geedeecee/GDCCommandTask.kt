package dev.adamko.geedeecee

import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.logging.LogLevel
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

abstract class GDCCommandTask @Inject constructor(
  private val executor: ExecOperations
) : DefaultTask() {

  @get:InputDirectory
  abstract val workingDir: DirectoryProperty

  @get:Input
  abstract val separatedArgs: ListProperty<String>

  @get:Input
  abstract val dockerActive: Property<Boolean>

  init {
    super.setGroup(GDCPlugin.GCD_TASK_GROUP)
    description = "Run a docker-compose command."

    enabledIf {
      it.dockerActive.orNull == true
    }

    logging.captureStandardOutput(LogLevel.LIFECYCLE)
  }

  @TaskAction
  fun dockerCompose() {
    val separatedArgs: List<String> = separatedArgs.get()

    executor.exec {
      environment("BUILDKIT_PROGRESS", "plain")
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

  companion object {
    private inline fun <reified T: Task> T.enabledIf(crossinline spec: (T) -> Boolean) {
      onlyIf {
        require(it is T)
        spec(it)
      }
    }
  }
}
