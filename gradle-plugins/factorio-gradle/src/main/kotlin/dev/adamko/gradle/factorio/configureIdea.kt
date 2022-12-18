package dev.adamko.gradle.factorio

import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.gradle.plugins.ide.idea.model.Module

internal fun FactorioModPlugin.PluginContext.configureIdea() {
  project.plugins.withType<IdeaPlugin>().configureEach {

//      project.afterEvaluate {
    project.extensions.configure<IdeaModel> {
      module {
//            this.scopes["MAIN"]?.plusAssign(mutableMapOf("plus" to listOf(project.configurations.create("main"))))
//
        sourceDirs.plusAssign(settings.mainSources.typescript.srcDirs)
        sourceDirs.plusAssign(settings.mainSources.lua.srcDirs)
        sourceDirs.plusAssign(settings.generatedSources.lua.srcDirs)

        testSources.from(settings.testSources.typescript)
        testSources.from(settings.testSources.lua)

        resourceDirs.plusAssign(settings.mainSources.resources.srcDirs)
        testResources.from(settings.testSources.resources)

        generatedSourceDirs.plusAssign(settings.generatedSources.lua.srcDirs)
        generatedSourceDirs.plusAssign(settings.generatedSources.typescript.srcDirs)

//          sourceDirs.plusAssign(
//            listOf(
//              settings.typescriptSrcDir.get().asFile,
//              settings.generatedLuaSrcDir.get().asFile,
//            )
//          )
////    sourceDirs = sourceDirs + file("src/main/lua")
//          resourceDirs = resourceDirs + settings.modDataResourcesDir
//          testSources.from(typescriptTestSrcDir)
////    testSourceDirs = testSourceDirs + typescriptTestSrcDir
//
//          generatedSourceDirs = generatedSourceDirs + generatedSrcDir
////    excludeDirs = excludeDirs + generatedSrcDir

        iml {
          whenMerged {
            require(this is Module)

//              sourceDirs.plusAssign(settings.mainSources.typescript.srcDirs)
//              sourceDirs.plusAssign(settings.mainSources.lua.srcDirs)
//              sourceDirs.plusAssign(settings.generatedSources.lua.srcDirs)
//
//              testSources.from(settings.testSources.typescript)
//              testSources.from(settings.testSources.lua)
//
//              resourceDirs.plusAssign(settings.mainSources.resources.srcDirs)
//              testResources.from(settings.testSources.resources)
//
//              generatedSourceDirs.plusAssign(settings.generatedSources.lua.srcDirs)
//              generatedSourceDirs.plusAssign(settings.generatedSources.typescript.srcDirs)
          }
        }
//          }
      }
    }
  }
}
