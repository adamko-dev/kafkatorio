package dev.adamko.kafkatorio.task

import dev.adamko.kafkatorio.gradle.filesChecksum
import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.logging.LogLevel
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import org.gradle.work.Incremental
import org.gradle.work.NormalizeLineEndings
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

@CacheableTask
abstract class DockerComposeExec @Inject constructor(
  private val executor: ExecOperations,
  private val objectFactory: ObjectFactory,
) : DefaultTask() {

  @get:InputDirectory
  @get:SkipWhenEmpty
  @get:PathSensitive(PathSensitivity.RELATIVE)
  @get:NormalizeLineEndings
  abstract val dockerComposeDir: DirectoryProperty

  @get:Input
  abstract val command: Property<String>

  @get:OutputFile
  val stateFile: RegularFileProperty = objectFactory.fileProperty().convention {
    temporaryDir.resolve("docker-files.md5")
  }

  init {
    group = DOCKER_COMPOSE_GROUP
    logging.captureStandardOutput(LogLevel.LIFECYCLE)
  }

  @TaskAction
  fun exec() {
    executor.exec {
      workingDir = dockerComposeDir.asFile.get()
      commandLine = parseSpaceSeparatedArgs(command.get())
    }

    val inputChecksum = dockerComposeDir.get().filesChecksum()
    stateFile.get().asFile.writeText("$inputChecksum")
  }

  companion object {
    const val DOCKER_COMPOSE_GROUP: String = "docker-compose"
  }
}
