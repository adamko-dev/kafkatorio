package kafkatorio.tasks

import java.io.ByteArrayOutputStream
import org.gradle.api.Task
import org.gradle.api.internal.specs.ExplainingSpec
import org.gradle.api.tasks.Exec
import org.gradle.process.ExecOperations

/** Checks if a process is running. Windows only. */
class ProcessRunningSpec(
  private val executor: ExecOperations,
  private val process: String,
  private val ignoreCase: Boolean = true,
) : ExplainingSpec<Task> {

  override fun isSatisfiedBy(element: Task?): Boolean = whyUnsatisfied(element) == null

  override fun whyUnsatisfied(element: Task?): String? {
    return when (element) {
      null     -> "task is null"
      !is Exec -> "task ${this::class.simpleName} is not ${Exec::class.simpleName}"
      else     -> {
        return ByteArrayOutputStream().use { outputStream ->

          executor.exec {
            commandLine("tasklist") // Windows only for now...
            standardOutput = outputStream
          }

          if (outputStream.toString().contains(process, ignoreCase)) {
            null
          } else {
            "found $process in tasklist"
          }
        }
      }
    }
  }
}
