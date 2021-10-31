package dev.adamko.gradle.pbandg

import dev.adamko.gradle.pbandg.pattern.KotlinJvmProjectConfiguration
import dev.adamko.gradle.pbandg.pattern.KotlinMultiplatformProjectConfiguration
import dev.adamko.gradle.pbandg.settings.PBAndGSettings
import dev.adamko.gradle.pbandg.task.ProtobufCompileTask
import dev.adamko.gradle.pbandg.task.ProtobufPrepareLibrariesTask
import dev.adamko.gradle.pbandg.task.options.ProtocOutput.JavaOutput
import dev.adamko.gradle.pbandg.task.options.ProtocOutput.KotlinOutput

//plugins {
//  `kotlin-dsl`
//}

plugins {
  base
}


// TODO investigate builtBy and sourceDirectorySet?
//    project.objects.fileCollection().builtBy()
//    val pbSrcSet = project.objects.sourceDirectorySet("protobuf", "protobuf")

//if (plugins.hasPlugin(IdeaPlugin::class)) {
//  IntelliJPattern().apply(project)
//}

plugins.withId("org.jetbrains.kotlin.jvm") {
  logger.info("Configuring Kotlin JVM plugin for PB&G")
  KotlinJvmProjectConfiguration().apply(project)
}

plugins.withId("org.jetbrains.kotlin.multiplatform") {
  logger.info("Configuring Kotlin Multiplatform plugin for PB&G")
  KotlinMultiplatformProjectConfiguration().apply(project)
}

val pbAndGSettings =
  project.extensions.create(Constants.PBG_SETTINGS_NAME, PBAndGSettings::class, project)

//val protocDep = ProtobufCompilerDependency(project, pbAndGSettings)
val protocDep = project.configurations.create("protobufCompiler") {
  this.description = "Define a single dependency that provides the protoc.exe for this system"
  isVisible = false
  isCanBeConsumed = false
  isCanBeResolved = true
  isTransitive = false

  defaultDependencies {
    add(pbAndGSettings.dependency.get())
  }
}

val protobufLibraryDependencies: Configuration =
  project.configurations.create(Constants.PBG_LIBS_CONF_NAME) {
    isVisible = true
    isCanBeConsumed = false
    isCanBeResolved = true
    isTransitive = false
  }


project.dependencies {
  protobufLibraryDependencies("com.google.protobuf:protobuf-javalite:3.19.1")
//      implementation("com.google.protobuf:protobuf-javalite:3.19.1")
//  implementation("com.google.protobuf:protobuf-kotlin-lite:3.19.1")
}

val protobufPrepareLibrariesTask =
  project.tasks.register<ProtobufPrepareLibrariesTask>("protobufPrepareLibraries") {
    librarySources.set(protobufLibraryDependencies)
  }

//project.dependencies {
//  protobufLibraryDependencies("com.google.protobuf:protobuf-javalite:3.19.1")
////      implementation("com.google.protobuf:protobuf-javalite:3.19.1")
////  implementation("com.google.protobuf:protobuf-kotlin-lite:3.19.1")
//}


/** Convert `.proto` files to Kotlin */
val pbCompileTask = project.tasks.register<ProtobufCompileTask>(Constants.PBG_TASK_COMPILE) {
  dependsOn(protocDep, protobufPrepareLibrariesTask)

  protobufLibraryDirectories.add(protobufPrepareLibrariesTask.flatMap { it.librariesDirectory })

  protocOutputs += KotlinOutput()
  protocOutputs += JavaOutput()

  val exe = protocDep.singleFile
  logger.lifecycle("protoc.exe: $exe")
  protocExecutable.set(exe)
}

project.tasks.assemble { dependsOn(pbCompileTask) }