package dev.adamko.factoriowebmap.archetype

plugins {
  base
}

group = rootProject.group.toString() + "." + project.name
version = rootProject.version

val projectId by extra(rootProject.name + "-" + project.name)
val modBuildDir by extra(layout.buildDirectory.dir(projectId).get())

//group = "dev.adamko.factoriowebmap"
//version = "0.0.1"

