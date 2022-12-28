package dev.adamko.gradle.factorio

import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.gradle.plugins.ide.idea.model.IdeaModel
//import org.gradle.plugins.ide.idea.model.Module as IdeaModule

internal fun FactorioModPlugin.PluginContext.configureIdea() {
  project.plugins.withType<IdeaPlugin>().configureEach {

    project.extensions.configure<IdeaModel> {
      module {
        // scopes["MAIN"] = mapOf("plus" to listOf(configurations.factorioMod.get()))

        sourceDirs.plusAssign(settings.mainSources.typescript.srcDirs)
        sourceDirs.plusAssign(settings.mainSources.lua.srcDirs)
        sourceDirs.plusAssign(settings.generatedSources.lua.srcDirs)

        testSources.from(settings.testSources.typescript)
        testSources.from(settings.testSources.lua)

        resourceDirs.plusAssign(settings.mainSources.resources.srcDirs)
        testResources.from(settings.testSources.resources)

        generatedSourceDirs.plusAssign(settings.generatedSources.lua.srcDirs)
        generatedSourceDirs.plusAssign(settings.generatedSources.typescript.srcDirs)

//        iml {
//          whenMerged {
//            require(this is IdeaModule)
//
//          }
//        }
      }
    }
  }
}
