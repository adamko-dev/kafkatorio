package dev.adamko.gradle.pbandg.pattern

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.hasPlugin
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.ide.idea.GenerateIdeaModule
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.gradle.plugins.ide.idea.model.IdeaModel

/**
 * IntelliJ requires source dirs are configured first.
 *
 * https://github.com/google/protobuf-gradle-plugin/blob/master/src/main/groovy/com/google/protobuf/gradle/Utils.groovy#L128
 */
class IntelliJPattern : Plugin<Project> {

  fun isIdeaPluginPresent(project: Project) = project.plugins.hasPlugin(IdeaPlugin::class)

  override fun apply(project: Project) {

    if (!isIdeaPluginPresent(project)) {
      return
    }

    val genSrcDir = project.layout.buildDirectory.dir("protobuf/generated-sources")
    val ktGenSrc = genSrcDir.map { it.dir("kotlin") }
    val jGenSrc = genSrcDir.map { it.dir("java") }

    project.mkdir(ktGenSrc)
    project.mkdir(jGenSrc)

    project.plugins.withType<IdeaPlugin> {
      project.extensions.configure<IdeaModel> {
        this.module.generatedSourceDirs.add(ktGenSrc.get().asFile)
        this.module.generatedSourceDirs.add(jGenSrc.get().asFile)
      }
    }

    project.tasks.withType<GenerateIdeaModule> {
      doFirst {
        project.mkdir(ktGenSrc)
      }
    }

  }
}