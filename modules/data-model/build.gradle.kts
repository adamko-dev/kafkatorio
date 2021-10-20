plugins {
  id("dev.adamko.factoriowebmap.archetype.kotlin-js")
}

val projectId: String by project.extra
val buildDir: Directory = layout.buildDirectory.dir(projectId).get()
val nodeModulesDir: Directory by project.extra

