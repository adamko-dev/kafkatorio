package dev.adamko.factoriowebmap.configurations

import org.gradle.api.artifacts.Configuration
import org.gradle.api.attributes.AttributeContainer
import org.gradle.api.attributes.Bundling
import org.gradle.api.attributes.Category
import org.gradle.api.attributes.LibraryElements
import org.gradle.api.attributes.Usage
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.named

fun Configuration.typescriptAttributes(objects: ObjectFactory) =
  attributes { typescript(objects) }

fun AttributeContainer.typescript(objects: ObjectFactory) {
  attribute(Usage.USAGE_ATTRIBUTE, objects.named("typescript"))
  attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
  attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.OBJECTS))
  attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
}
