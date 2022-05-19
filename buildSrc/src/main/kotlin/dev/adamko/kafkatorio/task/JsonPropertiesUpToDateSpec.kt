package dev.adamko.kafkatorio.task

import org.gradle.api.Task
import org.gradle.api.internal.specs.ExplainingSpec

object JsonPropertiesUpToDateSpec : ExplainingSpec<Task> {

  override fun isSatisfiedBy(element: Task?): Boolean = whyUnsatisfied(element) == null


  // Returns a description explaining why the task is outdated,
  // or null if the task is up to date
  override fun whyUnsatisfied(task: Task?): String? {
    require(task is UpdatePackageJson) { "$task is not a UpdatePackageJson task" }

    val current = task.currentPackageJson()
    val updated = task.updatedPackageJson(current)

    return if (current != updated) {
      (current.keys + updated.keys)
        .asSequence()
        .distinct()
        .sorted()
        .mapNotNull {
          val currentVal = current[it]
          val updatedVal = updated[it]

          if (currentVal == updatedVal) {
            null
          } else {
            "'$it' - expected:$updatedVal, actual:$currentVal"
          }
        }.joinToString(prefix = "package.json is not up to date. ")
    } else {
      null
    }
  }
}
