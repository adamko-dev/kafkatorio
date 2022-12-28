package dev.adamko.gradle.factorio

import javax.inject.Inject
import org.gradle.api.Named
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.model.ObjectFactory

abstract class FactorioModSourceSet @Inject constructor(
  private val named: String,
  objects: ObjectFactory,
) : Named {

  val typescript: SourceDirectorySet = objects
    .sourceDirectorySet("ts${named}", "TypeScript $named source files")
    .apply {
      include("**/*.d.ts", "**/*.ts")
    }

  val lua: SourceDirectorySet = objects
    .sourceDirectorySet("lua${named}", "Lua $named source files")
    .apply {
      include("**/*.lua")
    }

  override fun getName(): String = named

  abstract class WithResources @Inject constructor(
    type: String,
    objects: ObjectFactory,
  ) : FactorioModSourceSet(type, objects) {

    val resources: SourceDirectorySet = objects
      .sourceDirectorySet("resources${type}", "$type resources")
      .apply {
        exclude("**/*.d.ts", "**/*.ts", "**/*.lua")
      }
  }
}
