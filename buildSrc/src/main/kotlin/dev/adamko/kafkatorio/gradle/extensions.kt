package dev.adamko.kafkatorio.gradle

import dev.adamko.kafkatorio.jsonMapper
import java.io.ByteArrayOutputStream
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.specs.Spec
import org.gradle.kotlin.dsl.invoke
import org.gradle.plugins.ide.idea.model.IdeaModule


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
  properties: MapProperty<String, String>
): Spec<Task> = Spec<Task> {
  val packageJsonContent = packageJsonFile.get().asFile.readText()
  val packageJson = jsonMapper.parseToJsonElement(packageJsonContent).jsonObject

  properties.get().all { (key, expectedVal) ->
    val actualVal = packageJson[key]?.jsonPrimitive?.content
    val isUpToDate = expectedVal == actualVal

    if (isUpToDate) {
      logger.info("package.json property is up to date, '$key = $actualVal'")
    } else {
      logger.lifecycle(
        "package.json has outdated property '$key' (expected: $expectedVal, actual: $actualVal)"
      )
    }

    isUpToDate
  }
}


/** exclude generated Gradle code, so it doesn't clog up search results */
fun IdeaModule.excludeGeneratedGradleDsl(layout: ProjectLayout) {
  excludeDirs.addAll(
    layout.files(
      "buildSrc/build/generated-sources/kotlin-dsl-accessors",
      "buildSrc/build/generated-sources/kotlin-dsl-accessors/kotlin",
      "buildSrc/build/generated-sources/kotlin-dsl-accessors/kotlin/gradle",
      "buildSrc/build/generated-sources/kotlin-dsl-external-plugin-spec-builders",
      "buildSrc/build/generated-sources/kotlin-dsl-external-plugin-spec-builders/kotlin",
      "buildSrc/build/generated-sources/kotlin-dsl-external-plugin-spec-builders/kotlin/gradle",
      "buildSrc/build/generated-sources/kotlin-dsl-plugins",
      "buildSrc/build/generated-sources/kotlin-dsl-plugins/kotlin",
      "buildSrc/build/generated-sources/kotlin-dsl-plugins/kotlin/dev",
      "buildSrc/build/pluginUnderTestMetadata",
    )
  )
}
