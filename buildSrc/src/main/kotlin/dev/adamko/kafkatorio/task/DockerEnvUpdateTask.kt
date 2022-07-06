package dev.adamko.kafkatorio.task

import dev.adamko.kafkatorio.gradle.DOCKER_COMPOSE_TASK_GROUP
import javax.inject.Inject
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.logging.LogLevel
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.WriteProperties


@CacheableTask
open class DockerEnvUpdateTask @Inject constructor(
  private val objects: ObjectFactory
) : WriteProperties() {

  @Internal
  val dotEnvFile: RegularFileProperty = objects.fileProperty()

  init {
    group = DOCKER_COMPOSE_TASK_GROUP
    super.setOutputFile(dotEnvFile)
    super.getLogging().captureStandardOutput(LogLevel.LIFECYCLE)
  }

}
