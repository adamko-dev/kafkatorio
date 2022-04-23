package dev.adamko.kafkatorio.task

import java.io.ByteArrayOutputStream
import org.gradle.api.Task
import org.gradle.api.internal.specs.ExplainingSpec
import org.gradle.api.tasks.Exec

/** Checks if a process is running. Windows only. */
class ProcessRunningSpec(
  private val process: String,
  private val ignoreCase: Boolean = true,
) : ExplainingSpec<Task> {

  override fun isSatisfiedBy(element: Task?): Boolean = whyUnsatisfied(element) == null

  override fun whyUnsatisfied(element: Task?): String? = with(element) {

    when (this) {
      null     -> "task is null"
      !is Exec -> "task ${this::class.simpleName} is not ${Exec::class.simpleName}"
      else     -> {
        return ByteArrayOutputStream().use { outputStream ->

          project.exec {
            commandLine("tasklist") // Windows only for now...
            standardOutput = outputStream
          }

          if (outputStream.toString().contains(process, ignoreCase)) {
            "found $process in tasklist"
          } else {
            null
          }
        }
      }
    }
  }
}
