package dev.adamko.gradle.pbandg.pattern

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

class KotlinJvmProjectConfiguration : Plugin<Project> {

  override fun apply(project: Project) {
    addSourceSets(project)
  }

  private fun addSourceSets(project: Project) {

    project.beforeEvaluate {
      extensions.findByType<KotlinJvmProjectExtension>()?.apply {
        extensions.findByType<SourceSetContainer>()
          ?.named(SourceSet.MAIN_SOURCE_SET_NAME)
          ?.configure {
            java {
              srcDir(layout.buildDirectory.dir("pbAndG/generated-sources/java"))
              srcDir(layout.buildDirectory.dir("pbAndG/generated-sources/kotlin"))
            }
          }

      }

//      val srcSet = project.extensions.getByName<SourceSet>(SourceSet.MAIN_SOURCE_SET_NAME)
//
//      project.tasks
//        .withType<ProtobufCompileTask>()
//        .map { it.generatedSources.get() }
//        .forEach { generatedSourcesDir ->
//          project.logger.lifecycle("-------\nadding generated sources $generatedSourcesDir\n-------")
//
//          srcSet.java {
//            srcDir(generatedSourcesDir.dir("java"))
//            srcDir(generatedSourcesDir.dir("kotlin"))
//          }
//        }
    }
  }
}
