package dev.adamko.kafkatorio.gradle

import dev.adamko.kafkatorio.jsonMapper
import java.io.ByteArrayOutputStream
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.logging.LogLevel
import org.gradle.api.specs.Spec
import org.gradle.kotlin.dsl.invoke

operator fun <T> Spec<T>.not(): Spec<T> = Spec<T> { !this(it) }

fun Project.isProcessRunning(process: String, ignoreCase: Boolean = true): Spec<Task> =
  Spec<Task> {
    ByteArrayOutputStream().use { outputStream ->
      project.exec {
        commandLine("tasklist") // Windows only for now...
        standardOutput = outputStream
      }
      outputStream.toString()
        .contains(process, ignoreCase)
    }
  }

fun Project.areJsonPropertiesUpToDate(
  packageJsonFile: RegularFileProperty,
  properties: () -> Map<String, String>
): Spec<Task> = Spec<Task> {
  val packageJsonContent = packageJsonFile.get().asFile.readText()
  val packageJson = jsonMapper.parseToJsonElement(packageJsonContent).jsonObject

  properties().all { (key, expectedVal) ->
    val actualVal = packageJson[key]?.jsonPrimitive?.content
    (expectedVal == actualVal).also {
      logger.log(
        if (it) LogLevel.LIFECYCLE else LogLevel.INFO,
        "package.json has outdated property '$key' (expected: $expectedVal, actual: $actualVal)"
      )
    }
  }
}
