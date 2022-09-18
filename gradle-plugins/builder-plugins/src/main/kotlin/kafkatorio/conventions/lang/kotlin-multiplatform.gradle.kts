package kafkatorio.conventions.lang

import kafkatorio.conventions.relocateKotlinJsStore
//import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
//import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin
//import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension


plugins {
  id("kafkatorio.conventions.base")
//  id("dev.adamko.kafkatorio.lang.node")
  kotlin("multiplatform")
//  id("io.kotest.multiplatform") // disabled because of https://github.com/kotest/kotest/issues/3141
}


relocateKotlinJsStore()


//plugins.withType<NodeJsRootPlugin> {
//  configure<NodeJsRootExtension> {
//    nodeVersion = "16.0.0"
//  }
//}
//
//
//afterEvaluate {
//  rootProject.extensions.configure<NodeJsRootExtension> {
//    // https://github.com/rjaros/kvision/issues/410
//    versions.webpackCli.version = "4.10.0"
//    versions.webpackDevServer.version = "4.9.2"
//  }
//
//  rootProject.extensions.configure<YarnRootExtension> {
//    resolution("http-proxy-middleware", "^2.0.6")
//  }
//}
