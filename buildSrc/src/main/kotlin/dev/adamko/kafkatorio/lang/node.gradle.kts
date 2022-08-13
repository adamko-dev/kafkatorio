package dev.adamko.kafkatorio.lang

import com.github.gradle.node.npm.task.NpmInstallTask
import dev.adamko.kafkatorio.Versions
import dev.adamko.kafkatorio.task.UpdatePackageJson
import kotlinx.serialization.json.put


plugins {
  id("com.github.node-gradle.node")
  id("dev.adamko.kafkatorio.base")
}


node {
  download.set(true)
  version.set(Versions.node)

  distBaseUrl.set(null as String?) // set by dependencyResolutionManagement
}

val projectVersion: Provider<String> = providers.provider { "${project.version}" }

val updatePackageJson by tasks.registering(UpdatePackageJson::class) {
//  group = NodePlugin.NODE_GROUP
//  description = "Read the package.json file and update the version and name, based on the project."

  inputs.property("projectVersion", projectVersion)
//  propertiesToCheck.put("version", projectVersion)

  updateExpectedJson {
    put("version", projectVersion.get())
  }

  // check to see if the properties are already up-to-date
//  outputs.upToDateWhen(areJsonPropertiesUpToDate(packageJsonFile, propertiesToCheck))
//  onlyIf(!areJsonPropertiesUpToDate(packageJsonFile, propertiesToCheck))

//  doLast {
//    val packageJsonContent = packageJsonFile.get().asFile.readText()
//    val packageJson = jsonMapper.parseToJsonElement(packageJsonContent).jsonObject
//
//    val newJsonProps = propertiesToCheck.get().mapValues { (_, newVal) -> JsonPrimitive(newVal) }
//
//    val packageJsonUpdate = JsonObject(packageJson + newJsonProps)
//    val packageJsonContentUpdated =
//      jsonMapper
//        .encodeToString(packageJsonUpdate)
//        .suffixIfNot("\n")
//
//    logger.debug(packageJsonContentUpdated)
//
//    packageJsonFile.get().asFile.writer().useToRun {
//      write(packageJsonContentUpdated)
//    }
//
//  }
}

tasks.assemble { dependsOn(updatePackageJson) }

tasks.withType<NpmInstallTask>().configureEach {
  dependsOn(updatePackageJson)
}
