package dev.adamko.gradle.factorio

import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.artifacts.Configuration

internal data class FactorioModConfigurations(
    val factorioMod: NamedDomainObjectProvider<Configuration>,
    val factorioModProvider: NamedDomainObjectProvider<Configuration>,
  )
