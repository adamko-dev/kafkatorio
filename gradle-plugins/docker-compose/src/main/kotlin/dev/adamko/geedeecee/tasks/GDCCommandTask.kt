package dev.adamko.geedeecee.tasks

import dev.adamko.geedeecee.GDCPlugin
import dev.adamko.geedeecee.internal.enabledIf
import dev.adamko.geedeecee.internal.filesChecksum
import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.logging.LogLevel
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import org.gradle.work.NormalizeLineEndings
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs


// look into using 'build context' for up-to-date check
// https://stackoverflow.com/questions/38946683/how-to-test-dockerignore-file
// https://github.com/pwaller/docker-show-context
// https://snippets.khromov.se/see-which-files-are-included-in-your-docker-build-context/
// or parsing Dockerfile
// https://github.com/keilerkonzept/dockerfile-json


@Suppress("UnstableApiUsage") // NormalizeLineEndings
abstract class GDCCommandTask @Inject constructor(
  private val executor: ExecOperations,
) : DefaultTask() {

  @get:InputDirectory
  @get:SkipWhenEmpty
  @get:PathSensitive(PathSensitivity.RELATIVE)
  @get:NormalizeLineEndings
  abstract val workingDir: DirectoryProperty

  @get:Input
  abstract val separatedArgs: ListProperty<String>

  @get:Input
  abstract val dockerActive: Property<Boolean>

  @get:Input
  @get:Optional
  abstract val cacheable: Property<Boolean>

  @get:OutputFile
  abstract val stateFile: RegularFileProperty

  init {
    group = GDCPlugin.GCD_TASK_GROUP
    description = "Run a docker-compose command."

    enabledIf { it.dockerActive.getOrElse(false) }

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

    if (cacheable.getOrElse(false)) {
      val inputChecksum = workingDir.get().filesChecksum()
      stateFile.get().asFile.writeText("$inputChecksum")
    }
  }

  /** Set the `docker-compose` argument */
  @Suppress("FunctionName")
  fun `docker-compose`(args: String) {
    separatedArgs.set(
      parseSpaceSeparatedArgs("docker-compose $args")
    )
  }
}
