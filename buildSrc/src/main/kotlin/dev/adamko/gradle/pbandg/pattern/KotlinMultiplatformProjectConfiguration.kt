package dev.adamko.gradle.pbandg.pattern

import dev.adamko.gradle.pbandg.Constants.pbAndGBuildDir
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinMultiplatformProjectConfiguration : Plugin<Project> {

  override fun apply(project: Project) {
    project.afterEvaluate {
      addSourceSets(project)
    }
  }

  private fun addSourceSets(project: Project) {

    project.extensions.findByType<KotlinMultiplatformExtension>()?.apply {
      project.logger.lifecycle("Kotlin Multiplatform source sets: ${sourceSets.joinToString { "${it.name} / ${it.kotlin.sourceDirectories.files.joinToString()}" }}")

      val jvmProto = this.sourceSets.maybeCreate("jvmProto").apply {

        kotlin.srcDir(project.layout.pbAndGBuildDir.get().dir("generated-sources/java"))
        kotlin.srcDir(project.layout.pbAndGBuildDir.get().dir("generated-sources/kotlin"))

        dependsOn(sourceSets.maybeCreate("jvmMain"))
      }
//
      val tsProto = this.sourceSets.maybeCreate("tsProto").apply {

        kotlin.srcDir(project.layout.pbAndGBuildDir.get().dir("generated-sources/typescript"))

        dependsOn(sourceSets.maybeCreate("jsMain"))
      }

//      this.sourceSets.findByName("jvmMain")?.apply {
////        protoJvm.dependsOn(this)
////        dependsOn(protoJvm)
//        kotlin.srcDir(project.layout.buildDirectory.dir("pbAndG/generated-sources/java"))
//        kotlin.srcDir(project.layout.buildDirectory.dir("pbAndG/generated-sources/kotlin"))
//      }
//      this.sourceSets.findByName("jsMain")?.apply {
////        protoTypescript.dependsOn(this)
////        dependsOn(protoTypescript)
//        kotlin.srcDir(project.layout.buildDirectory.dir("pbAndG/generated-sources/typescript"))
//      }

      project.logger.lifecycle("Kotlin Multiplatform source sets: ${sourceSets.joinToString { "${it.name} / ${it.kotlin.sourceDirectories.files.joinToString()}" }}")

//
//      targets        .forEach {
//        when (it.platformType){
//          KotlinPlatformType.jvm     -> {}
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
