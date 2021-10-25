package dev.adamko.factoriowebmap.archetype

plugins {
  base
}

group = "${rootProject.group}.${project.name}"
version = rootProject.version

val projectId by extra("fwm-${project.name}")
//val modBuildDir by extra(layout.buildDirectory.dir(projectId).get())

val licenseFile: RegularFile by extra(rootProject.layout.projectDirectory.file("LICENSE"))

val tokens: Map<String, String> by extra(
  mapOf(
    "project.version" to "$version",
  )
)


//group = "dev.adamko.factoriowebmap"
//version = "0.0.1"

