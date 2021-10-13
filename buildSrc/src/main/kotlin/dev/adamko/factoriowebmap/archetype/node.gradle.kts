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
  id("com.github.node-gradle.node")
  id("dev.adamko.factoriowebmap.archetype.base")
}

node {
  download.set(true)
  version.set("14.18.0")

  distBaseUrl.set(null as String?) // set by dependencyResolutionManagement
}

