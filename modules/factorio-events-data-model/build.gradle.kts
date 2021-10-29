import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalDependency
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.domainObjectContainer
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.withType
import org.gradle.kotlin.dsl.property
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

plugins {
  id("dev.adamko.factoriowebmap.archetype.base")
  `kotlin-dsl`
}

val projectId: String by project.extra
val buildDir: Directory = layout.buildDirectory.dir(projectId).get()

dependencies {
}


class ProtocPlugin : Plugin<Project> {

  override fun apply(project: Project) {

    val protocConfig =
      project.extensions.create("protocPluginConfig", ProtocPluginConfig::class, project)

//    val protocDep: Configuration = project.configurations.create("protoc") {
//      isVisible = false
//      isCanBeConsumed = false
//      isCanBeResolved = true
//      isTransitive = false
//
//      defaultDependencies {
//        add(protocConfig.dependencyProvider())
//      }
//    }

    project.tasks.register<ProtocPrepareTask>("protocPrepare") {

    }

//
//    val prepareTask = project.tasks.register<Sync>("protocPrepare") {
//      group = "protoc"
//
//      val outputDir = project.rootProject.layout.projectDirectory.dir(".gradle/protoc")
//
//      outputs.dir(outputDir)
//
////      doLast {
//      val protoc: File = protocDep.resolve().single()
//      logger.lifecycle("protoc file $protoc")
//
//      from(protoc)
//      into(outputDir)
////      }
//    }
//
//    project.tasks.build { dependsOn(prepareTask) }
//
//    val generateTask = project.tasks.create<Exec>("protocGenerate") {
//      dependsOn(prepareTask)
//    }

//    val protocDeps = project.configurations.create("pbAndG") {
//      this.let { config ->
//
//        config.isVisible = false
//        config.isCanBeConsumed = false
//        config.isCanBeResolved = true
//        config.description = "protoc dependencies"
//
//        config.defaultDependencies {
//          add(protocConfig.dependencyProvider())
//        }
//      }
//
//      val generatorsContainer =
//        project.objects.domainObjectContainer(ProtocGenerationTask::class)
//      protocConfig.extensions.add("protocGenerate", generatorsContainer)
//
//
//      project.extensions.add("testingExtensionThingy", project.provider {
//        DefaultNativePlatform.getCurrentOperatingSystem().toFamilyName()
//      })
//
//      project.tasks.withType<ProtocGenerationTask> {
//      }
//
//    }
//
  }
}

abstract class ProtocPluginConfig(private val project: Project) : java.io.Serializable {

  val protocVersion: Property<String> =
    project.objects.property(String::class).convention("3.9.2")

  val protocGroup: Property<String> =
    project.objects.property(String::class).convention("com.google.protobuf")

  val protocArtifactName: Property<String> =
    project.objects.property(String::class).convention("protoc")

  val protocArtifactExtension: Property<String> =
    project.objects.property(String::class).convention("exe")

  /**
   * Should return one of
   *
   * * `linux`
   * * `osx`
   * * `windows`
   */
  var osFamilyNameProvider: () -> String = {
    DefaultNativePlatform.getCurrentOperatingSystem().toFamilyName()
  }

  /**
   * Should return one of
   *
   * * `aarch_64`
   * * `ppcle_64`
   * * `x86_32`
   * * `x86_64`
   */
  var architectureProvider: () -> String = {
    DefaultNativePlatform.getCurrentArchitecture()
      .name
      .replace("-", "_")
  }

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
  var classifierProvider: () -> String = {
    "${osFamilyNameProvider()}-${architectureProvider()}"
  }

  var dependencyProvider: () -> ExternalModuleDependency = {
    project.dependencies.create(
      group = protocGroup.get(),
      name = protocArtifactName.get(),
      version = protocVersion.get(),
      classifier = classifierProvider(),
      ext = protocArtifactExtension.get(),
    )
  }

//  val generators = project.objects.domainObjectContainer(ProtocGenerationConfig::class)
}

abstract class ProtocGenerationConfig

abstract class ProtocPrepareTask : DefaultTask() {

  @get:OutputDirectory
  val outputDirectory: DirectoryProperty = project.objects.directoryProperty()
    .convention(project.rootProject.layout.projectDirectory.dir(".gradle/protoc"))

  @get:OutputFile
  val protocOutput: RegularFileProperty = project.objects.fileProperty()
    .convention { outputDirectory.asFileTree.singleFile }

  @Internal
  val protocPluginConfig: Provider<ProtocPluginConfig> = project.provider {
    project.extensions.getByType<ProtocPluginConfig>()
  }

  @Internal
  val protocDependency: Provider<ExternalModuleDependency> = protocPluginConfig.map {
    it.dependencyProvider()
  }

  init {
    super.setGroup("protoc")
  }


  @TaskAction
  fun prepare() {

    val protocDep: Configuration = project.configurations.create("protoc") {
      isVisible = false
      isCanBeConsumed = false
      isCanBeResolved = true
      isTransitive = false

      defaultDependencies {
        add(protocDependency.get())
      }
    }

    val resolvedProtocDep = protocDep.resolve()

    require(resolvedProtocDep.singleOrNull() != null) {
      "Expected to download a single protoc.exe, but got ${resolvedProtocDep.size}: ${resolvedProtocDep.joinToString()}"
    }

    project.sync {
      from(resolvedProtocDep)
      into(outputDirectory)
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

