package dev.adamko.kafkatorio.lang

import com.github.gradle.node.NodePlugin
import dev.adamko.kafkatorio.task.UpdatePackageJson
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.support.useToRun
import org.gradle.kotlin.dsl.withType
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


val packageJsonFile: RegularFile = layout.projectDirectory.file("src/main/typescript/package.json")

fun arePropertiesUpToDate(properties: () -> Map<String, String>): Spec<Task> =
  Spec<Task> {
    val packageJsonContent = packageJsonFile.asFile.readText()
    val packageJson = jsonMapper.parseToJsonElement(packageJsonContent).jsonObject

    properties().all { (key, expectedVal) ->
      val actualVal = packageJson[key]?.jsonPrimitive?.content
      val isUpToDate = expectedVal == actualVal
      if (!isUpToDate) {
        logger.lifecycle("package.json has outdated property $key=$actualVal (expected: $expectedVal)")
      }
      isUpToDate
    }
  }

val updatePackageJson by tasks.registering(UpdatePackageJson::class) {
  group = NodePlugin.NODE_GROUP

  group = project.name
  description = "Read the package.json file and update the version and name, based on the project."

  propertiesToCheck["version"] = "${project.version}"

  outputs.file(packageJsonFile)

  // check to see if the properties are already up-to-date
  onlyIf(arePropertiesUpToDate { propertiesToCheck })

  doLast {
    val packageJsonContent = packageJsonFile.asFile.readText()
    val packageJson = jsonMapper.parseToJsonElement(packageJsonContent).jsonObject

    val newJsonProps = propertiesToCheck.mapValues { (_, newVal) -> JsonPrimitive(newVal) }

    val packageJsonUpdate = JsonObject(packageJson + newJsonProps)
    val packageJsonContentUpdated =
      jsonMapper
        .encodeToString(packageJsonUpdate)
        .suffixIfNot("\n")

    packageJsonFile.asFile.writer().useToRun {
      write(packageJsonContentUpdated)
    }

  }
}
