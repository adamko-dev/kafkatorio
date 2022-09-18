package kafkatorio.tasks

import com.github.gradle.node.npm.task.NpmTask
import javax.inject.Inject
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.IgnoreEmptyDirectories
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction
import org.gradle.work.NormalizeLineEndings
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

@CacheableTask
abstract class TypescriptToLuaTask @Inject constructor(
  private val fs: FileSystemOperations
) : NpmTask() {

  @get:InputDirectory
  @get:SkipWhenEmpty
  @get:PathSensitive(PathSensitivity.RELATIVE)
  @get:NormalizeLineEndings
  @get:IgnoreEmptyDirectories
  abstract val sourceFiles: DirectoryProperty

  @get:OutputDirectory
  abstract val outputDirectory: DirectoryProperty

  init {
    super.setGroup(BasePlugin.BUILD_GROUP)
    super.setDescription("Convert Typescript To Lua")
    super.execOverrides { standardOutput = System.out }
    super.npmCommand.set(listOf("run", "build"))
    super.ignoreExitValue.set(false)
    super.args.set(parseSpaceSeparatedArgs("-- --outDir $temporaryDir"))
  }

  @TaskAction
  fun tstl() {
    fs.delete { delete(temporaryDir) }
    temporaryDir.mkdirs()

    super.exec()

    fs.sync {
      from(temporaryDir)
      into(outputDirectory)
    }
  }

}
