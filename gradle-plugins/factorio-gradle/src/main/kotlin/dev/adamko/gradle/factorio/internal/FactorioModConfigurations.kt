package dev.adamko.gradle.factorio.internal

import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering

/**
 * The Gradle [Configuration]s used to share Factorio mod files between subprojects.
 */
internal class FactorioModConfigurations(
  project: Project
) {
  private val objects: ObjectFactory = project.objects

  private val factorioMod: NamedDomainObjectProvider<Configuration> by project.configurations.registering {
    asDeclarable()
    factorioModAttributes(project.objects)
  }

  val factorioModResolver: NamedDomainObjectProvider<Configuration> by project.configurations.registering {
    asConsumer()
    extendsFrom(factorioMod.get())
    factorioModAttributes(project.objects)
  }

  val factorioModProvider: NamedDomainObjectProvider<Configuration> by project.configurations.registering {
    asProvider()
    factorioModAttributes(objects)
  }
}
