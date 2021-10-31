package dev.adamko.gradle.pbandg.pattern

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinMultiplatformProjectConfiguration : Plugin<Project> {

  override fun apply(project: Project) {
    project.afterEvaluate {
      addSourceSets(this)
    }
  }

  private fun addSourceSets(project: Project) {

    project.extensions.findByType<KotlinMultiplatformExtension>()?.apply {
      project.logger.lifecycle("Kotlin Multiplatform source sets: ${sourceSets.joinToString { "${it.name} / ${it.kotlin.sourceDirectories.files.joinToString()}" }}")

      sourceSets.whenObjectAdded {
        this.name
      }
//
      val protoJvm = this.sourceSets.maybeCreate("protoJvm").apply {
        kotlin.srcDir(project.layout.buildDirectory.dir("pbAndG/generated-sources/java"))
        kotlin.srcDir(project.layout.buildDirectory.dir("pbAndG/generated-sources/kotlin"))
      }

      val protoTypescript = this.sourceSets.maybeCreate("protoJvm").apply {
        kotlin.srcDir(project.layout.buildDirectory.dir("pbAndG/generated-sources/typescript"))
      }

      this.sourceSets.findByName("jvmMain")?.apply {
//        protoJvm.dependsOn(this)
//        dependsOn(protoJvm)
        kotlin.srcDir(project.layout.buildDirectory.dir("pbAndG/generated-sources/java"))
        kotlin.srcDir(project.layout.buildDirectory.dir("pbAndG/generated-sources/kotlin"))
      }
      this.sourceSets.findByName("jsMain")?.apply {
//        protoTypescript.dependsOn(this)
//        dependsOn(protoTypescript)
        kotlin.srcDir(project.layout.buildDirectory.dir("pbAndG/generated-sources/typescript"))
      }

      project.logger.lifecycle("Kotlin Multiplatform source sets: ${sourceSets.joinToString { "${it.name} / ${it.kotlin.sourceDirectories.files.joinToString()}" }}")

//
//      targets        .forEach {
//        when (it.platformType){
//          KotlinPlatformType.jvm     -> TODO()
//          KotlinPlatformType.js      -> {
//          it.defaultConfigurationName
//          }
//       KotlinPlatformType.androidJvm -> {}
//          KotlinPlatformType.common  -> {}
//          KotlinPlatformType.native  -> {}
//        }
//      }

    }
  }
}
