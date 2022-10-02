package dev.adamko.geedeecee

import javax.inject.Inject
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.logging.LogLevel
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.WriteProperties


@CacheableTask
open class DockerEnvUpdateTask @Inject constructor(
  objects: ObjectFactory
) : WriteProperties() {

  @Internal
  // internal and not abstract so that it can delegate to `super.outputFile` in `init {}`
  val dotEnvFile: RegularFileProperty = objects.fileProperty()

  init {
    group = GDCPlugin.GCD_TASK_GROUP
    super.setOutputFile(dotEnvFile)
    super.getLogging().captureStandardOutput(LogLevel.LIFECYCLE)
  }

}
