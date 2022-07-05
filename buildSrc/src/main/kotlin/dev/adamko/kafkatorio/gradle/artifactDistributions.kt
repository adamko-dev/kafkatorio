package dev.adamko.kafkatorio.gradle

import org.gradle.api.artifacts.Configuration
import org.gradle.api.attributes.Bundling
import org.gradle.api.attributes.Bundling.BUNDLING_ATTRIBUTE
import org.gradle.api.attributes.Category
import org.gradle.api.attributes.Category.CATEGORY_ATTRIBUTE
import org.gradle.api.attributes.LibraryElements
import org.gradle.api.attributes.LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE
import org.gradle.api.attributes.Usage.USAGE_ATTRIBUTE
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.named


fun Configuration.asProvider() {
  isVisible = false
  isCanBeResolved = false
  isCanBeConsumed = true
}


fun Configuration.asConsumer() {
  isVisible = false
  isCanBeResolved = true
  isCanBeConsumed = false
}


fun Configuration.factorioModAttributes(objects: ObjectFactory): Configuration =
  attributes {
    attribute(USAGE_ATTRIBUTE, objects.named("factorio"))
    attribute(CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
    attribute(LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.CLASSES_AND_RESOURCES))
    attribute(BUNDLING_ATTRIBUTE, objects.named(Bundling.EMBEDDED))
  }


fun Configuration.typescriptAttributes(objects: ObjectFactory): Configuration =
  attributes {
    attribute(USAGE_ATTRIBUTE, objects.named("typescript"))
    attribute(CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
    attribute(LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.OBJECTS))
    attribute(BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
  }
