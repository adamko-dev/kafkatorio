package dev.adamko.kafkatorio.lang

import com.github.gradle.node.NodePlugin
import dev.adamko.kafkatorio.task.UpdatePackageJson
import dev.adamko.kafkatorio.gradle.not
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.gradle.kotlin.dsl.support.useToRun
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinMultiplatformPlugin
import org.jetbrains.kotlin.gradle.targets.js.KotlinJsPlugin
import org.jetbrains.kotlin.util.suffixIfNot


plugins {
  id("com.github.node-gradle.node")
  id("dev.adamko.kafkatorio.base")
}

node {
  download.set(true)
  version.set("14.18.0")

  distBaseUrl.set(null as String?) // set by dependencyResolutionManagement
}

project.plugins.withType<KotlinJsPlugin>()
project.plugins.withType<KotlinMultiplatformPlugin>()


val jsonMapper = Json {
  prettyPrint = true
  prettyPrintIndent = "  "
}


fun arePropertiesUpToDate(
  packageJsonFile: RegularFileProperty,
  properties: () -> Map<String, String>
): Spec<Task> = Spec<Task> {
  val packageJsonContent = packageJsonFile.get().asFile.readText()
  val packageJson = jsonMapper.parseToJsonElement(packageJsonContent).jsonObject

  properties().all { (key, expectedVal) ->
    val actualVal = packageJson[key]?.jsonPrimitive?.content
    (expectedVal == actualVal).also {
      if (!it)
        logger.lifecycle("package.json has outdated property '$key' (expected: $expectedVal, actual: $actualVal)")
    }
  }
}

val updatePackageJson by tasks.registering(UpdatePackageJson::class) {
  group = NodePlugin.NODE_GROUP
  description = "Read the package.json file and update the version and name, based on the project."

  propertiesToCheck["version"] = "${project.version}"

  // check to see if the properties are already up-to-date
  onlyIf(!arePropertiesUpToDate(packageJsonFile) { propertiesToCheck })

  doLast {
    val packageJsonContent = packageJsonFile.get().asFile.readText()
    val packageJson = jsonMapper.parseToJsonElement(packageJsonContent).jsonObject

    val newJsonProps = propertiesToCheck.mapValues { (_, newVal) -> JsonPrimitive(newVal) }

    val packageJsonUpdate = JsonObject(packageJson + newJsonProps)
    val packageJsonContentUpdated =
      jsonMapper
        .encodeToString(packageJsonUpdate)
        .suffixIfNot("\n")

    logger.lifecycle(packageJsonContentUpdated)

    packageJsonFile.get().asFile.writer().useToRun {
      write(packageJsonContentUpdated)
    }

  }
}
