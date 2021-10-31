package dev.adamko.gradle.pbandg.task

import dev.adamko.gradle.pbandg.Constants
import dev.adamko.gradle.pbandg.Constants.pbAndGBuildDir
import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.property

/** Download protobuf libs */
abstract class ProtobufPrepareLibrariesTask : DefaultTask() {

  @get:InputFiles
  val librarySources: Property<Configuration> = project.objects.property()

  @OutputDirectory
  val librariesDirectory: DirectoryProperty = project.objects.directoryProperty()
    .convention(project.pbAndGBuildDir.map { it.dir("libs") })

  init {
    group = Constants.PBG_TASK_GROUP
    description = "Download and extract protobuf libs"
  }

  @TaskAction
  fun doTask() {
    project.sync {
      includeEmptyDirs = false
      from(librarySources.map { conf -> conf.map { project.zipTree(it) } })
      into(librariesDirectory)
      include("**/*.proto")
    }
  }

}