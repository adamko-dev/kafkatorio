package dev.adamko.kafkatorio.task

import dev.adamko.kafkatorio.gradle.filesChecksum
import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.logging.LogLevel
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.property
import org.gradle.process.ExecOperations
import org.gradle.work.NormalizeLineEndings
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

@CacheableTask
abstract class DockerComposeExec @Inject constructor(
  private val executor: ExecOperations,
  private val objects: ObjectFactory,
  private val providers: ProviderFactory,
) : DefaultTask() {

  @get:InputDirectory
  @get:SkipWhenEmpty
  @get:PathSensitive(PathSensitivity.RELATIVE)
  @get:NormalizeLineEndings
  abstract val dockerComposeDir: DirectoryProperty

  @get:Input
  abstract val command: Property<String>

  @get:Input
  val dockerIsActive: Property<Boolean> = objects.property<Boolean>().convention(
    providers.exec {
      commandLine = parseSpaceSeparatedArgs("docker info")
      isIgnoreExitValue = true
    }.result.map {
      it.exitValue == 0
    }
  )

  @get:OutputFile
  val stateFile: RegularFileProperty = objects.fileProperty().convention {
    temporaryDir.resolve("docker-files.md5")
  }

  init {
    group = DOCKER_COMPOSE_GROUP
    logging.captureStandardOutput(LogLevel.LIFECYCLE)

    super.onlyIf {
      it is DockerComposeExec && it.dockerIsActive.getOrElse(false)
    }
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
