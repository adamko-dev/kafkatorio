package dev.adamko.kafkatorio.lang

import dev.adamko.kafkatorio.relocateKotlinJsStore
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension


plugins {
  id("dev.adamko.kafkatorio.base")
//  id("dev.adamko.kafkatorio.lang.node")
  kotlin("multiplatform")
}


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
