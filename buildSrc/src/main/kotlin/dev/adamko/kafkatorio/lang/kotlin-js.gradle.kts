package dev.adamko.kafkatorio.lang

import dev.adamko.kafkatorio.Versions
import dev.adamko.kafkatorio.relocateKotlinJsStore
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin
import org.jetbrains.kotlin.gradle.targets.js.npm.NpmProject
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

//import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.RootPackageJsonTask


plugins {
  kotlin("js")
  id("dev.adamko.kafkatorio.base")
}

//dependencies {
//  implementation(
//    project.dependencies.platform(
//      "org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:${Versions.kotlinWrappers}"
//    )
//  )
//}

//val rootPackageJson by rootProject.tasks.getting(RootPackageJsonTask::class)

//fun NpmTask.setNodeModulesPath(path: String): Unit =
//    environment.put("NODE_PATH", path)
//
//fun NpmTask.setNodeModulesPath(folder: File): Unit =
//    setNodeModulesPath(folder.normalize().absolutePath)

// https://github.com/node-gradle/gradle-node-plugin/issues/150
// https://github.com/lamba92/kotlingram/blob/master/examples/js-bot/build.gradle.kts

//val nodePath: Directory by extra {
//  val file = rootPackageJson.rootPackageJson.parentFile.normalize()
//  logger.info("Kotlin/JS NODE_PATH: $file")
//  project.layout.dir(project.provider { file }).get()
//}

//val nodeModulesDir: Directory by extra {
//  val file = nodePath.dir(NpmProject.NODE_MODULES)
//  logger.info("Kotlin/JS NODE_MODULES: $file")
//  file
//}


relocateKotlinJsStore()


plugins.withType<NodeJsRootPlugin> {
  configure<NodeJsRootExtension> {
    nodeVersion = "16.0.0"
  }
}


afterEvaluate {
  rootProject.extensions.configure<NodeJsRootExtension> {
    // https://github.com/rjaros/kvision/issues/410
    versions.webpackCli.version = "4.10.0"
    versions.webpackDevServer.version = "4.9.2"
  }

  rootProject.extensions.configure<YarnRootExtension> {
    resolution("http-proxy-middleware", "^2.0.6")
  }
}
