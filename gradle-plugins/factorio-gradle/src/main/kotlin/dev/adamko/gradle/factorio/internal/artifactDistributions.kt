@file:Suppress("UnstableApiUsage")

package dev.adamko.gradle.factorio.internal

import org.gradle.api.artifacts.Configuration
import org.gradle.api.attributes.Bundling.*
import org.gradle.api.attributes.Category.CATEGORY_ATTRIBUTE
import org.gradle.api.attributes.Category.LIBRARY
import org.gradle.api.attributes.LibraryElements.*
import org.gradle.api.attributes.Usage.USAGE_ATTRIBUTE
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.named


internal fun Configuration.asDeclarable(visible: Boolean = false) {
  isVisible = visible
  isCanBeResolved = false
  isCanBeConsumed = false
  isCanBeDeclared = true
}

internal fun Configuration.asProvider(visible: Boolean = false) {
  isVisible = visible
  isCanBeResolved = false
  isCanBeConsumed = true
  isCanBeDeclared = false
}


internal fun Configuration.asConsumer(visible: Boolean = false) {
  isVisible = visible
  isCanBeResolved = true
  isCanBeConsumed = false
  isCanBeDeclared = false
}


fun Configuration.factorioModAttributes(objects: ObjectFactory): Configuration =
  attributes {
    attribute(USAGE_ATTRIBUTE, objects.named("dev.adamko.factorio"))
    attribute(CATEGORY_ATTRIBUTE, objects.named(LIBRARY))
    attribute(LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(CLASSES_AND_RESOURCES))
    attribute(BUNDLING_ATTRIBUTE, objects.named(EMBEDDED))
  }


fun Configuration.typescriptAttributes(objects: ObjectFactory): Configuration =
  attributes {
    attribute(USAGE_ATTRIBUTE, objects.named("dev.adamko.typescript"))
    attribute(CATEGORY_ATTRIBUTE, objects.named(LIBRARY))
    attribute(LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(OBJECTS))
    attribute(BUNDLING_ATTRIBUTE, objects.named(EXTERNAL))
  }
