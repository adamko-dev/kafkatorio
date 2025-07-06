package dev.adamko.geedeecee.internal

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Task
import org.gradle.api.file.FileCopyDetails
import org.gradle.api.file.RelativePath
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.add

// move to common-lib

internal inline fun <reified T : Task> T.enabledIf(crossinline spec: (T) -> Boolean) {
  onlyIf {
    require(it is T)
    spec(it)
  }
}

/**
 * Add an extension to the [ExtensionContainer], and return the value.
 *
 * Adding an extension is especially useful for improving the DSL in build scripts when [T] is a
 * [NamedDomainObjectContainer].
 * Using an extension will allow Gradle to generate
 * [type-safe model accessors](https://docs.gradle.org/current/userguide/kotlin_dsl.html#kotdsl:accessor_applicability)
 * for added types.
 *
 * ([name] should match the property name. This has to be done manually. I tried using a
 * delegated-property provider, but then Gradle can't introspect the types properly, so it fails to
 * create accessors).
 */
internal inline fun <reified T : Any> ExtensionContainer.adding(
  name: String,
  value: T,
): T {
  add<T>(name, value)
  return value
}
