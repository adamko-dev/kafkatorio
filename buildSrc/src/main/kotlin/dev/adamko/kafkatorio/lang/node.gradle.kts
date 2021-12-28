package dev.adamko.kafkatorio.lang

import com.github.gradle.node.NodePlugin
import com.github.gradle.node.npm.task.NpmInstallTask
import dev.adamko.kafkatorio.gradle.areJsonPropertiesUpToDate
import dev.adamko.kafkatorio.gradle.not
import dev.adamko.kafkatorio.jsonMapper
import dev.adamko.kafkatorio.task.UpdatePackageJson
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import org.gradle.kotlin.dsl.support.useToRun
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

val updatePackageJson by tasks.registering(UpdatePackageJson::class) {
  group = NodePlugin.NODE_GROUP
  description = "Read the package.json file and update the version and name, based on the project."

  propertiesToCheck.put("version", "${project.version}")

  // check to see if the properties are already up-to-date
  onlyIf(!areJsonPropertiesUpToDate(packageJsonFile, propertiesToCheck))

  doLast {
    val packageJsonContent = packageJsonFile.get().asFile.readText()
    val packageJson = jsonMapper.parseToJsonElement(packageJsonContent).jsonObject

    val newJsonProps = propertiesToCheck.get().mapValues { (_, newVal) -> JsonPrimitive(newVal) }

    val packageJsonUpdate = JsonObject(packageJson + newJsonProps)
    val packageJsonContentUpdated =
      jsonMapper
        .encodeToString(packageJsonUpdate)
        .suffixIfNot("\n")

    logger.debug(packageJsonContentUpdated)

    packageJsonFile.get().asFile.writer().useToRun {
      write(packageJsonContentUpdated)
    }

  }
}

tasks.assemble { dependsOn(updatePackageJson) }
tasks.withType<NpmInstallTask> {
  dependsOn(updatePackageJson)
}
