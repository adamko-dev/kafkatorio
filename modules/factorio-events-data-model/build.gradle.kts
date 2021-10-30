import java.util.function.Supplier
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.property
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform
import org.gradle.nativeplatform.platform.internal.DefaultOperatingSystem
import org.jetbrains.kotlin.incremental.isJavaFile
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

plugins {
  id("dev.adamko.factoriowebmap.archetype.base")
  `kotlin-dsl`
}

val projectId: String by project.extra
val buildDir: Directory = layout.buildDirectory.dir(projectId).get()


dependencies {
}


open class ProtocPlugin : Plugin<Project> {

  override fun apply(project: Project) {

    val protocConfig =
      project.extensions.create("protocPluginConfig", ProtocPluginConfig::class, project)

    val protobufCompiler: Configuration = project.configurations.create("protobufCompiler") {
      isVisible = false
      isCanBeConsumed = false
      isCanBeResolved = true
      isTransitive = false

      defaultDependencies {
        add(protocConfig.dependency.get())
      }
    }

    val protobufLibs: Configuration = project.configurations.create("protobufLib") {
      isVisible = true
      isCanBeConsumed = false
      isCanBeResolved = true
      isTransitive = false
    }

//    val protocPrepare = project.tasks.register<ProtocPrepareTask>("protocPrepare")

    project.dependencies {
      protobufLibs("com.google.protobuf:protobuf-javalite:3.19.1")
    }

    val libsTask = project.tasks.register<Sync>("protobufLibs") {
      group = "protobuf"

      includeEmptyDirs = false

      dependsOn(protobufLibs)

      val outDir = project.layout.buildDirectory.dir("proto/libs")

      protobufLibs
        .map { project.zipTree(it) }
        .forEach {
          logger.lifecycle("protoc lib - $it")
          from(it) {
            include("**/*.proto")
          }
        }

      into(outDir)
    }


    project.tasks.register<Exec>("protobufCompile") {
      group = "protobuf"
      dependsOn(protobufCompiler, libsTask)
      executable(protobufCompiler.singleFile)

      val protoLibsDir = project.layout.buildDirectory.dir("proto/libs")
      inputs.dir(protoLibsDir)

      standardOutput = System.out

      val outDir = project.layout.buildDirectory.dir("protoc/generated-sources")
      outputs.dir(outDir)

      workingDir(temporaryDirFactory)

      val javaOut = workingDir.resolve("proto/java")
      val kotlinOut = workingDir.resolve("proto/kotlin")
      val protoFile by project.objects.fileProperty()
        .convention(project.layout.projectDirectory.file("src/main/proto/FactorioServerLogRecord.proto"))
      val protoFileParent = project.provider { protoFile.asFile.parentFile }

      inputs.file(protoFile)

      args(
        parseSpaceSeparatedArgs(
          """
                      --proto_path=${protoFileParent.get()}
                      --proto_path=${protoLibsDir.get()}
                      --java_out=$javaOut
                      --kotlin_out=$kotlinOut
                      $protoFile
                    """
        )
      )
      doFirst {
        project.delete(workingDir)

        project.mkdir(javaOut)
        project.mkdir(kotlinOut)
      }

      doLast {
        project.sync {
          from(workingDir)
          into(outDir)
        }
        project.delete(workingDir)
      }

    }
  }
}

abstract class ProtocPluginConfig(private val project: Project) :
  java.io.Serializable { // TODO remove serializable?

  val protocWorkingDir: DirectoryProperty =
    project.objects.directoryProperty()
      .convention(project.rootProject.layout.projectDirectory.dir(".gradle/protoc"))

  val protocVersion: Property<String> =
    project.objects.property(String::class).convention("3.19.1")

  val protocGroup: Property<String> =
    project.objects.property(String::class).convention("com.google.protobuf")

  val protocArtifactName: Property<String> =
    project.objects.property(String::class).convention("protoc")

  val protocArtifactExtension: Property<String> =
    project.objects.property(String::class).convention("exe")

  val operatingSystemProvider: Property<Supplier<DefaultOperatingSystem>> =
    project.objects.property<Supplier<DefaultOperatingSystem>>()
      .convention { DefaultNativePlatform.getCurrentOperatingSystem() }

  /**
   * Should return one of
   *
   * * `linux`
   * * `osx`
   * * `windows`
   */
  val operatingSystemName: Property<String> =
    project.objects
      .property<String>()
      .convention(operatingSystemProvider.map { it.get().toFamilyName() })

  private val architectureProvider: Provider<Architecture> =
    project.provider { DefaultNativePlatform.getCurrentArchitecture() }

  /**
   * Should return one of
   *
   * * `aarch_64`
   * * `ppcle_64`
   * * `x86_32`
   * * `x86_64`
   */
  val architectureName: Property<String> =
    project.objects
      .property<String>()
      .convention(architectureProvider.map { it.name.replace("-", "_") })

  private val classifierProvider: Provider<String> =
    project.provider { "${operatingSystemName.get()}-${architectureName.get()}" }

  /**
   * Default: concatenate [osFamilyNameProvider] and [architectureProvider]
   *
   * Should return one of:
   *
   *  * `linux-aarch_64`
   *  * `linux-ppcle_64`
   *  * `linux-x86_64`
   *  * `osx-x86_32`
   *  * `osx-x86_64`
   *  * `windows-x86_32`
   */
  val classifier: Property<String> =
    project.objects
      .property<String>()
      .convention(classifierProvider)


  private val dependencyProvider: Provider<ExternalModuleDependency> =
    project.provider {
      project.dependencies.create(
        group = protocGroup.get(),
        name = protocArtifactName.get(),
        version = protocVersion.get(),
        classifier = classifierProvider.get(),
        ext = protocArtifactExtension.get(),
      )
    }

  val dependency: Property<ExternalModuleDependency> =
    project.objects
      .property<ExternalModuleDependency>()
      .convention(dependencyProvider)

}

abstract class ProtocPrepareTask : DefaultTask() {


  @Internal
  val protocPluginConfig: Provider<ProtocPluginConfig> = project.provider {
    project.extensions.getByType<ProtocPluginConfig>()
  }

  @get:OutputDirectory
  val outputDirectory: DirectoryProperty = project.objects.directoryProperty()
    .convention(protocPluginConfig.flatMap { it.protocWorkingDir })

  @Internal
  val protocOutput: RegularFileProperty = project.objects.fileProperty()

  @Internal
  val protocDependency: Provider<ExternalModuleDependency> =
    protocPluginConfig.flatMap { it.dependency }

  init {
    super.setGroup("protobuf")
    outputs.dir(outputDirectory)
  }

  @TaskAction
  fun action() {

    val protocDep: Configuration = project.configurations.create("protoc") {
      isVisible = false
      isCanBeConsumed = false
      isCanBeResolved = true
      isTransitive = false

      defaultDependencies {
        add(protocDependency.get())
      }
    }

    logger.lifecycle("Downloading protoc")
    val resolvedProtocDep = protocDep.singleFile
    logger.lifecycle("Downloaded $resolvedProtocDep")

    project.sync {
      from(resolvedProtocDep)
      into(outputDirectory)
      logger.lifecycle("Syncing from $resolvedProtocDep to ${outputDirectory.asFile.get().canonicalPath}")
      protocOutput.set(outputDirectory.asFileTree.singleFile)
    }
  }
}

abstract class ProtocGenerationTask : DefaultTask() {

  @TaskAction
  fun generate() {
    logger.lifecycle("generating protoc...")
  }

}


apply<ProtocPlugin>()

configure<ProtocPluginConfig> {
}

