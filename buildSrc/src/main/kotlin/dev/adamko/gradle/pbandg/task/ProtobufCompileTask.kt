package dev.adamko.gradle.pbandg.task

import dev.adamko.gradle.pbandg.Constants
import dev.adamko.gradle.pbandg.Constants.pbAndGBuildDir
import dev.adamko.gradle.pbandg.task.options.ProtocOutput
import org.gradle.api.DefaultTask
import org.gradle.api.DomainObjectSet
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.property

abstract class ProtobufCompileTask : DefaultTask() {

  @get:InputFile
  abstract val protocExecutable: RegularFileProperty

  @get:OutputDirectory
  val generatedSources: DirectoryProperty = project.objects.directoryProperty()
    .convention(project.pbAndGBuildDir.map { it.dir("generated-sources") })

  @get:InputFiles
  abstract val protobufLibraryDirectories: ListProperty<Directory>

  @get:Input
  abstract val protocOutputs: DomainObjectSet<ProtocOutput>
//  = project.objects.domainObjectSet(ProtocOutput::class.java)

  @get:InputFile
  abstract val protoFile: RegularFileProperty

  @get:Input
  val cliParamProtoPath: Property<String> =
    project.objects.property<String>().convention("--proto_path=")

  init {
    group = Constants.PBG_TASK_GROUP
  }

  @TaskAction
  fun compile() {

    val workDir = temporaryDir
    project.delete(workDir)

    project.exec {
      executable(protocExecutable.asFile.get())
      workingDir(workDir)

      // prepare directories
      project.mkdir(workingDir)
      project.mkdir(generatedSources)

      val mapOutputsToOutputDir =
        protocOutputs.associateWith { workingDir.resolve(it.outputDirectoryName) }
      mapOutputsToOutputDir.values.forEach { project.mkdir(it) }

      // set args
      mapOutputsToOutputDir
        .map { (opt, dir) ->
          "${opt.cliParam}=${opt.protocOptions}:${dir}"
        }
        .also { args(it) }

      val protoPath = cliParamProtoPath.get()
      val libArgs = protobufLibraryDirectories.get().map { "$protoPath${it.asFile}" }
      args(libArgs)

      val targetProtoFile = protoFile.get().asFile
      args("$protoPath${targetProtoFile.parentFile}")
      args("$targetProtoFile")
    }

    // sync compiled files
    project.sync {
      from(workDir)
      into(generatedSources)
      includeEmptyDirs = false
    }

    // clean up temp-dir
    project.delete(workDir)
  }

}

/*

{
  group = "protobuf"
  dependsOn(protobufCompiler, protoLibsTask)

  standardOutput = System.out

  executable(protobufCompiler.singleFile)
  workingDir(temporaryDirFactory)

  val outDir = project.layout.buildDirectory.dir("protobuf/generated-sources")
  outputs.dir(outDir)

  val protoLibsDir: Provider<File> = protoLibsTask.map { it.destinationDir }

  val javaOut = workingDir.resolve("java")
  val kotlinOut = workingDir.resolve("kotlin")
  val protoFile by project.objects.fileProperty()
    .convention(project.layout.projectDirectory.file("src/main/proto/FactorioServerLogRecord.proto"))

  val protoFileParent = project.provider { protoFile.asFile.parentFile }

  val isLiteEnabled: Boolean by project.objects.property<Boolean>().convention(true)

  doFirst {
    project.delete(workingDir)

    project.mkdir(outDir)
    project.mkdir(javaOut)
    project.mkdir(kotlinOut)

    val liteOpt: String = when (isLiteEnabled) {
      true  -> "lite:"
      false -> ""
    }

    args(
      parseSpaceSeparatedArgs(
        """
                        --proto_path=${protoLibsDir.get()}
                        --proto_path=${protoFileParent.get()}
                        --java_out=$liteOpt$javaOut
                        --kotlin_out=$liteOpt$kotlinOut
                        $protoFile
                      """
      )
    )
  }

  doLast {
    project.sync {
      from(workingDir)
      into(outDir)
    }
    project.delete(workingDir)
  }
}

 */