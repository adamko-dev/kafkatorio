package dev.adamko.gradle.pbandg

import java.io.File
import java.util.function.Supplier
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ExternalDependency
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.Sync
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.domainObjectContainer
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.defaultDependencies
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.withType
import org.gradle.kotlin.dsl.property
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.register
import org.gradle.nativeplatform.platform.Architecture
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform
import org.gradle.nativeplatform.platform.internal.DefaultOperatingSystem
import org.gradle.plugins.ide.idea.GenerateIdeaModule
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs


open class ProtocPlugin : Plugin<Project> {

  override fun apply(project: Project) {

    configureIntelliJ(project)

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

    val protobufLib: Configuration = project.configurations.create("protobufLib") {
      isVisible = true
      isCanBeConsumed = false
      isCanBeResolved = true
      isTransitive = false
    }

    project.dependencies {
      protobufLib("com.google.protobuf:protobuf-javalite:3.19.1")
//      implementation("com.google.protobuf:protobuf-javalite:3.19.1")
//      implementation("com.google.protobuf:protobuf-kotlin-lite:3.19.1")
    }

    /** Download protobuf libs */
    val protoLibsTask = project.tasks.register<Sync>("protobufLibs") {
      group = "protobuf"
      description = "Download and extract protobuf libs"
      includeEmptyDirs = false
      dependsOn(protobufLib)

      val protoLibDir by project.objects.directoryProperty()
        .convention(project.layout.buildDirectory.dir("protobuf/libs"))
      into(protoLibDir)

      protobufLib
        .map { project.zipTree(it) }
        .forEach {
          logger.lifecycle("protoc lib - $it")
          from(it) {
            include("**/*.proto")
          }
        }
    }

    // TODO investigate builtBy and sourceDirectorySet?
//    project.objects.fileCollection().builtBy()
//    val pbSrcSet = project.objects.sourceDirectorySet("protobuf", "protobuf")


    /** Convert `.proto` files to Kotlin */
    val pbCompileTask = project.tasks.register<Exec>("protobufCompile") {
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

    project.tasks.assemble { dependsOn(pbCompileTask) }

  }

  /**
   * IntelliJ requires source dirs are configured first.
   *
   * https://github.com/google/protobuf-gradle-plugin/blob/master/src/main/groovy/com/google/protobuf/gradle/Utils.groovy#L128
   */
  private fun configureIntelliJ(project: Project) {

    val genSrcDir = project.layout.buildDirectory.dir("protobuf/generated-sources")
    val ktGenSrc = genSrcDir.map { it.dir("kotlin") }
    val jGenSrc = genSrcDir.map { it.dir("java") }

    project.mkdir(ktGenSrc)

    project.extensions.findByType<KotlinJvmProjectExtension>()?.apply {
      project.logger.lifecycle("-------\nadding generated sources $ktGenSrc\n-------")

      project.sourceSets.named("main") {
        this.java.srcDir(ktGenSrc)
        this.java.srcDir(jGenSrc)
      }

//          sourceSets.main.get().kotlin.srcDir(ktGenSrc)

//          sourceSets.main.configure {
//            this.kotlin.srcDir(ktGenSrc)
//          }
    }

    project.plugins.withType<IdeaPlugin> {
      project.extensions.configure<IdeaModel> {
        this.module.generatedSourceDirs.add(ktGenSrc.get().asFile)
        this.module.generatedSourceDirs.add(jGenSrc.get().asFile)
      }
    }

    project.tasks.withType<GenerateIdeaModule> {
      doFirst {
        project.mkdir(ktGenSrc)
      }
    }
  }
}

abstract class ProtocPluginConfig(private val project: Project) {

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
