package dev.adamko.gradle.factorio

import org.gradle.api.distribution.DistributionContainer
import org.gradle.api.distribution.plugins.DistributionPlugin
import org.gradle.api.tasks.bundling.Zip
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named

internal fun FactorioModPlugin.PluginContext.configureDistribution() {

  project.plugins.apply(DistributionPlugin::class)

  val distributions = project.extensions.getByType<DistributionContainer>()

  distributions.named(DistributionPlugin.MAIN_DISTRIBUTION_NAME) {

    distributionBaseName.set(settings.modName)

    contents {
      includeEmptyDirs = false

      from(settings.mainSources.resources) {
        include("**/**")
      }

      exclude {
        // exclude empty files
        it.file.run {
          isFile && useLines { lines -> lines.all { line -> line.isBlank() } }
        }
      }
    }
  }

  val distZipTask = project.tasks.named<Zip>("distZip")

  configurations.factorioModProvider.configure {
    outgoing.artifact(distZipTask.flatMap { it.archiveFile })
  }

}
