package dev.adamko.kafkatorio.lang

import org.jetbrains.kotlin.gradle.targets.js.npm.NpmProject
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.RootPackageJsonTask
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension


plugins {
  kotlin("js")
  id("dev.adamko.kafkatorio.base")
}

dependencies {
  val kotlinWrappersVersion = "0.0.1-pre.276-kotlin-1.6.0"
  implementation(
    project.dependencies.platform(
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

rootProject.extensions.configure<YarnRootExtension> {
  // kotlin-js adds a directory in the root-dir for the Yarn lock.
  // That's a bit annoying. It's a little neater if it's in the
  // gradle dir, next to the version-catalog.
  lockFileDirectory = rootProject.rootDir.resolve("gradle/kotlin-js-store")
}
