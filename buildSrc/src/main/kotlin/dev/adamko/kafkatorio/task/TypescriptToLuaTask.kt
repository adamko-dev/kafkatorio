package dev.adamko.kafkatorio.task

import com.github.gradle.node.npm.task.NpmTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.SkipWhenEmpty
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

abstract class TypescriptToLuaTask : NpmTask() {

  @get:InputDirectory
  @get:SkipWhenEmpty
  @get:PathSensitive(PathSensitivity.RELATIVE)
  abstract val sourceFiles: DirectoryProperty

  @get:OutputFile
  abstract val outputDirectory: DirectoryProperty


  init {
    super.setGroup(BasePlugin.BUILD_GROUP)
    super.setDescription("Convert Typescript To Lua")
    super.execOverrides { standardOutput = System.out }
    super.npmCommand.set(listOf("run", "build"))
    super.args.set(parseSpaceSeparatedArgs("-- --outDir $temporaryDir"))
    super.ignoreExitValue.set(false)
    super.doFirst("cleanTemporaryDir") {
      project.delete(temporaryDir)
      project.mkdir(temporaryDir)
    }
    super.doLast("syncTstlOutput") {
      project.sync {
        from(temporaryDir)
        into(outputDirectory)
      }
    }
  }

}
