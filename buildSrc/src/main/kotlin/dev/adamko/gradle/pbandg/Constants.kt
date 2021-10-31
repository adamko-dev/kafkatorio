package dev.adamko.gradle.pbandg

import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.ProjectLayout
import org.gradle.api.provider.Provider

object Constants {
  const val PBG_SETTINGS_NAME = "pbAndG"
  const val PBG_TASK_GROUP = PBG_SETTINGS_NAME// "pb-and-g"
  const val PBG_LIBS_CONF_NAME = "protobufLibrary"
  const val PBG_TASK_COMPILE = "protobufCompile"

  val Project.pbAndGBuildDir: Provider<Directory>
    get() = layout.pbAndGBuildDir

  val ProjectLayout.pbAndGBuildDir: Provider<Directory>
    get() = buildDirectory.dir(PBG_TASK_GROUP)

}
