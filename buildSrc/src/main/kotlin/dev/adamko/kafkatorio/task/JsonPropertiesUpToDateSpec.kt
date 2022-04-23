package dev.adamko.kafkatorio.task

import dev.adamko.kafkatorio.jsonMapper
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.gradle.api.Task
import org.gradle.api.internal.specs.ExplainingSpec

object JsonPropertiesUpToDateSpec : ExplainingSpec<Task> {

  override fun isSatisfiedBy(element: Task?): Boolean = whyUnsatisfied(element) == null


  // Returns a description explaining why the task is outdated,
  // or null if the task is up to date
  override fun whyUnsatisfied(element: Task?): String? = with(element) {
    when (this) {
      null                  -> "task is null"

      !is UpdatePackageJson ->
        "task ${this::class.simpleName} is not instance of ${UpdatePackageJson::class.simpleName}"

      else                  -> {

        val packageJsonContent = packageJsonFile.get().asFile.readText()
        val packageJson = jsonMapper.parseToJsonElement(packageJsonContent).jsonObject

        val outdatedProperties = propertiesToCheck.get().mapNotNull { (key, expectedVal) ->
          val actualVal = packageJson[key]?.jsonPrimitive?.content
          when {
            expectedVal == actualVal -> null
            else                     -> "'$key' (expected: $expectedVal, actual: $actualVal)"
          }
        }

        when {
          outdatedProperties.isEmpty() -> null
          outdatedProperties.size == 1 ->
            "package.json has outdated property: ${outdatedProperties.single()}"
          else                         ->
            "package.json has outdated properties: ${outdatedProperties.joinToString(", ")}"
        }
      }
    }
  }
}
