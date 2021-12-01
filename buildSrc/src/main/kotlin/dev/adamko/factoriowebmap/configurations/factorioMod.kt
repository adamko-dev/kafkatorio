package dev.adamko.factoriowebmap.configurations

import org.gradle.api.artifacts.Configuration
import org.gradle.api.attributes.AttributeContainer
import org.gradle.api.attributes.Bundling
import org.gradle.api.attributes.Category
import org.gradle.api.attributes.LibraryElements
import org.gradle.api.attributes.Usage
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.named

fun Configuration.factorioModAttributes(objects: ObjectFactory) =
  attributes { factorioMod(objects) }

fun AttributeContainer.factorioMod(objects: ObjectFactory) {
  attribute(Usage.USAGE_ATTRIBUTE, objects.named("factorio"))
  attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
  attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.CLASSES_AND_RESOURCES))
  attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EMBEDDED))
}
