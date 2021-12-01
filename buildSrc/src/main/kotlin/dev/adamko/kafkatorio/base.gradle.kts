package dev.adamko.kafkatorio

import org.gradle.kotlin.dsl.base
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.idea

plugins {
  idea
  base
}

group = "${rootProject.group}.${project.name}"
version = rootProject.version

val licenseFile: RegularFile by extra(rootProject.layout.projectDirectory.file("LICENSE"))

val tokens: Map<String, String> by extra(
  mapOf(
    "project.version" to "$version",
  )
)

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
  }
}

//group = "dev.adamko.factoriowebmap"
//version = "0.0.1"

