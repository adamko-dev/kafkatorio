package kafkatorio.tasks

import kafkatorio.tasks.UpdatePackageJson.Companion.foldParseToJsonObject
import kafkatorio.tasks.UpdatePackageJson.Companion.parseToJsonObject
import kotlinx.serialization.json.JsonObject
import org.gradle.api.Task
import org.gradle.api.internal.specs.ExplainingSpec
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging


object JsonPropertiesUpToDateSpec : ExplainingSpec<Task> {

  private val logger: Logger = Logging.getLogger(this::class.java)

  override fun isSatisfiedBy(element: Task?): Boolean {
    val reason = whyUnsatisfied(element)

    if (reason != null) {
      logger.lifecycle("JsonProperties not up to date:\n$reason")
    }

    return reason == null
  }


  // Returns a description explaining why the task is outdated,
  // or null if the task is up to date
  override fun whyUnsatisfied(task: Task?): String? {
    require(task is UpdatePackageJson) { "$task is not a UpdatePackageJson task" }

    val current = task.packageJsonFile.asFile.orNull?.parseToJsonObject()
      ?: return "no package.json file"

    val updates = task.expectedJsonUpdates.orNull?.toList()?.foldParseToJsonObject()
      ?: return "no package.json updates"

    val expected = JsonObject(current + updates)

    if (current != expected) {
      return (current.keys + expected.keys)
        .asSequence()
        .distinct()
        .sorted()
        .mapNotNull {
          val currentVal = current[it]
          val expectedVal = expected[it]

          if (currentVal == expectedVal) {
            null
          } else {
            "'$it' - expected:$expectedVal, actual:$currentVal"
          }
        }.joinToString(separator = "\n\t", prefix = "package.json is not up to date.\n\t")
    }

    return null
  }
}
