package dev.adamko.kafkatorio.lang

import org.gradle.kotlin.dsl.kotlin
import com.github.gradle.node.npm.task.NpmTask
import com.github.gradle.node.task.NodeTask
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.getting
import org.jetbrains.kotlin.gradle.targets.js.dukat.DtsResolver
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.RootPackageJsonTask
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinPackageJsonTask
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs
import org.jetbrains.kotlin.gradle.targets.js.npm.NpmProject


plugins {
  kotlin("js")
  id("dev.adamko.kafkatorio.base")
  id("dev.adamko.kafkatorio.lang.node")
}

kotlin {
  js(IR) {
    binaries.executable()

    useCommonJs()
    nodejs()
  }
}

dependencies {
  val kotlinWrappersVersion = "0.0.1-pre.270-kotlin-1.6.0"
  implementation(
      project.dependencies.enforcedPlatform(
          "org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:${kotlinWrappersVersion}"
      )
  )
}

val rootPackageJson by rootProject.tasks.getting(RootPackageJsonTask::class)

//fun NpmTask.setNodeModulesPath(path: String): Unit =
//    environment.put("NODE_PATH", path)
//
//fun NpmTask.setNodeModulesPath(folder: File): Unit =
//    setNodeModulesPath(folder.normalize().absolutePath)

// https://github.com/node-gradle/gradle-node-plugin/issues/150
// https://github.com/lamba92/kotlingram/blob/master/examples/js-bot/build.gradle.kts

val nodePath: Directory by extra {
  val file = rootPackageJson.rootPackageJson.parentFile.normalize()
  logger.lifecycle("Kotlin/JS NODE_PATH: $file")
  project.layout.dir(project.provider { file }).get()
}

val nodeModulesDir: Directory by extra {
  val file = nodePath.dir(NpmProject.NODE_MODULES)
  logger.lifecycle("Kotlin/JS NODE_MODULES: $file")
  file
}
