package dev.adamko.factoriowebmap.archetype

import org.gradle.kotlin.dsl.kotlin
import com.github.gradle.node.npm.task.NpmTask
import com.github.gradle.node.task.NodeTask
import org.jetbrains.kotlin.gradle.targets.js.dukat.DtsResolver
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.RootPackageJsonTask
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinPackageJsonTask
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs
import org.jetbrains.kotlin.gradle.targets.js.npm.NpmProject


plugins {
  kotlin("js")
  id("com.github.node-gradle.node")
  id("dev.adamko.factoriowebmap.archetype.base")
}

kotlin {
  js(IR) {

    binaries.executable()

    useCommonJs()
    nodejs()
//
////    compilations.all {
//    compilations["main"].apply {
//      packageJson {
//        customField(
//            "scripts",
//            mapOf(
//                "build" to "tstl",
//                "dev" to "tstl --watch",
//            )
//        )
//      }
//    }
  }
}

val rootPackageJson by rootProject.tasks.getting(RootPackageJsonTask::class)

//fun NpmTask.setNodeModulesPath(path: String): Unit =
//    environment.put("NODE_PATH", path)
//
//fun NpmTask.setNodeModulesPath(folder: File): Unit =
//    setNodeModulesPath(folder.normalize().absolutePath)


val nodePath: Directory by extra {
  val file = rootPackageJson.rootPackageJson.parentFile.normalize()
  logger.lifecycle("Found root NODE_PATH: $file")
  project.layout.dir(provider { file }).get()
}

val nodeModulesDir: Directory by extra {
  val file = nodePath.dir(NpmProject.NODE_MODULES)
  logger.lifecycle("Found root NODE_MODULES: $file")
  file
}

node {
  download.set(true)
  version.set("14.18.0")

  distBaseUrl.set(null as String?) // set by dependencyResolutionManagement

  nodeProjectDir.set(nodePath)
//  nodeProjectDir.set(packageJson.packageJson.parentFile.normalize())
}

