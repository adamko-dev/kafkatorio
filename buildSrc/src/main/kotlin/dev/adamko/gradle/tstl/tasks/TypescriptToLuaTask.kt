package dev.adamko.gradle.tstl.tasks

import com.github.gradle.node.npm.task.*
import org.gradle.api.file.*
import org.gradle.api.provider.*
import org.gradle.api.tasks.*
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs
import org.gradle.kotlin.dsl.*

abstract class TypescriptToLuaTask : NpmTask() {

  @get:Internal
  val intermediateOutputDir: RegularFileProperty = project.objects.fileProperty()
    .convention { temporaryDir }

  @get:Internal
  val intermediateOutputCanonicalPath: Provider<String> =
    intermediateOutputDir.map { it.asFile.canonicalPath }

  @get:Input
  val identifier: Property<String> = project.objects.property<String>().convention(name)

  @get:Internal
  val luaOutputDirConvention: Provider<Directory> =
    project.layout.buildDirectory.map { it.dir("typescriptToLua/${identifier.get()}") }

  @get:OutputDirectory
  val luaOutputDir: DirectoryProperty = project.objects.directoryProperty()
    .convention(luaOutputDirConvention)

  init {
    group = "typescriptToLua"

    execOverrides { standardOutput = System.out }

    super.dependsOn(project.tasks.withType<NpmSetupTask>())
    ignoreExitValue.set(false)
  }

  @TaskAction
  fun convert() {
    args.set(parseSpaceSeparatedArgs("-- --outDir ${intermediateOutputCanonicalPath.get()}"))

    project.delete(intermediateOutputDir.get())

    super.exec()

  }

}