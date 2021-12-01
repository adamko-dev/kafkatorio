package dev.adamko.kafkatorio

plugins {
  idea
  base
}

group = "${rootProject.group}.${project.name}"
version = rootProject.version

val licenseFile: RegularFile by extra(rootProject.layout.projectDirectory.file("LICENSE"))

val tokens: MutableMap<String, String> by extra(
  mutableMapOf(
    "project.version" to "$version",
  )
)

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
  }
}

tasks.withType<WriteProperties>().configureEach {
  encoding = Charsets.UTF_8.name()
  comment = " Do not edit manually. This file was created with task '$name'"
}
