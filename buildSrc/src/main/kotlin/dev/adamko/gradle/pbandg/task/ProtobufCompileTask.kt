package dev.adamko.gradle.pbandg.task

import dev.adamko.gradle.pbandg.Constants
import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.property

import dev.adamko.gradle.pbandg.Constants.pbAndGBuildDir

abstract class ProtobufCompileTask : DefaultTask() {

  @get:InputFile
  abstract val protocExecutable: RegularFileProperty

  @get:InputFiles
  abstract val protobufLibraryDirectories: ListProperty<Directory>

//  @get:Input
//  abstract val protocOutputs: DomainObjectSet<ProtocOutput>
//  = project.objects.domainObjectSet(ProtocOutput::class.java)

  @get:Input
  abstract val cliArgs: ListProperty<String>

  @get:InputFile
  abstract val protoFile: RegularFileProperty

  @get:Input
  val cliParamProtoPath: Property<String> =
    project.objects.property<String>().convention("--proto_path=")

  @get:OutputDirectory
  val outputDir: RegularFileProperty =
    project.objects.fileProperty().convention { temporaryDir }

//  @get:Internal
//  val outputPath: Provider<String> = outputDir.map { it.asFile.canonicalPath }

  init {
    group = Constants.PBG_TASK_GROUP
  }

  @TaskAction
  fun compile() {
    // prepare directories
    project.mkdir(outputDir)

    project.exec {
      executable(protocExecutable.asFile.get())
      workingDir(outputDir.asFile.get())

//      val mapOutputsToOutputDir =
//        protocOutputs.associateWith { outputDir.get().asFile.resolve(it.outputDirectoryName) }
//      mapOutputsToOutputDir.values.forEach { project.mkdir(it) }
//
//      // set args
//      mapOutputsToOutputDir
//        .map { (opt, dir) ->
//          val optionsSuffix = if (opt.protocOptions.isBlank()) "" else ":"
//          "${opt.cliParam}=${opt.protocOptions}$optionsSuffix$dir"
//        }
//        .also { args(it) }
      args(cliArgs.get())

      val protoPath = cliParamProtoPath.get()
      val libArgs = protobufLibraryDirectories.get().map { "$protoPath${it.asFile}" }
      args(libArgs)

      val targetProtoFile = protoFile.get().asFile
      args("$protoPath${targetProtoFile.parentFile}")
      args("$targetProtoFile")
    }

  }

}
