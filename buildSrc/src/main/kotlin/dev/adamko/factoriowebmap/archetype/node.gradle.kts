package dev.adamko.factoriowebmap.archetype

import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinMultiplatformPlugin
import org.jetbrains.kotlin.gradle.targets.js.KotlinJsPlugin

plugins {
  id("com.github.node-gradle.node")
  id("dev.adamko.factoriowebmap.archetype.base")
}

node {
  download.set(true)
  version.set("14.18.0")

  distBaseUrl.set(null as String?) // set by dependencyResolutionManagement
}

project.plugins.withType<KotlinJsPlugin>()
project.plugins.withType<KotlinMultiplatformPlugin>()
