package dev.adamko.gradle.pbandg

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


class ProtocPlugin : Plugin<Project> {

  override fun apply(project: Project) {

    val protocConfig =
      project.extensions.create("protocConfig", ProtocConfigExtension::class.java)

    val protocDeps = project.configurations.create("pbAndG") {
      isVisible = false
      isCanBeConsumed = false
      isCanBeResolved = true
      description = "protoc dependencies"

      defaultDependencies {
//        deps += project.dependencies.create("com.google.protobuf:protoc:3.19.0")
        add(protocConfig.dependencyProvider())
      }
    }

    val generatorsContainer =
      project.objects.domainObjectContainer(ProtocGenerationTask::class.java)
    protocConfig.extensions.add("generate", generatorsContainer)


    project.extensions.add("asd", project.provider {
      DefaultNativePlatform.getCurrentOperatingSystem().toFamilyName()
    })

    project.tasks.withType<ProtocGenerationTask> {
//      .getDataFiles()?.from(protocDeps)
    }

//    project.getPlugins().withType(org.gradle.api.plugins.JavaPlugin::class) { javaPlugin ->
//      val sourceSets: SourceSetContainer =
//        project.getExtensions().getByType(SourceSetContainer::class.java)
//      val main: SourceSet = sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
//      main.getJava().setSrcDirs(Arrays.asList("src"))
//    }

  }

}


abstract class ProtocConfigExtension(private val project: Project) : ExtensionAware {

  val protocVersion: Property<String> =
    project.objects.property(String::class).convention("3.9.2")

  val protocGroup: Property<String> =
    project.objects.property(String::class).convention("com.google.protobuf")

  val protocArtifactName: Property<String> =
    project.objects.property(String::class).convention("protoc")

  val protocArtifactExtension: Property<String> =
    project.objects.property(String::class).convention("exe")

  var osFamilyNameProvider: () -> String = {
    DefaultNativePlatform.getCurrentOperatingSystem().toFamilyName()
  }

  var architectureProviderMapper: (String) -> String = {
    it.replace("-", "_")
  }

  var architectureProvider: () -> String = {
    DefaultNativePlatform.getCurrentArchitecture()
      .name
      .let(architectureProviderMapper)
  }

  var classifierProvider: () -> String = {
    "${osFamilyNameProvider()}-${architectureProvider()}"
  }

  var dependencyProvider: () -> ExternalDependency = {
    project.dependencies.create(
      group = protocGroup.get(),
      name = protocArtifactName.get(),
      version = protocVersion.get(),
      classifier = classifierProvider(),
      ext = protocArtifactExtension.get(),
    )
  }

  val generators = project.objects.domainObjectContainer(ProtocGenerationConfig::class)


}

abstract class ProtocGenerationConfig

abstract class ProtocGenerationTask : DefaultTask() {

  @TaskAction
  fun generate() {
    logger.lifecycle("generating protoc...")
  }

}


//class ProtocDownloadExe : DefaultTask() {
//
////@Input
//val protocVersion = "3.9.2"
//val protocArtifactFileExt = "exe"
//
//
//  @TaskAction
//  fun download() {
//
//    val baseUri = project.repositories.mavenCentral().url
//
//    val os: OperatingSystem = DefaultNativePlatform.getCurrentOperatingSystem()
//    val arch: ArchitectureInternal = DefaultNativePlatform.getCurrentArchitecture()
//
//    val artifact = "protoc-$protocVersion-${os.name}-${arch.name}.$protocArtifactFileExt"
//
//    ant.invokeMethod(
//      "get", mapOf(
//        "src" to baseUri.resolve("/com/google/protobuf/protoc/3.9.2/protoc-3.9.2-linux-aarch_64.exe"),
//        "dest" to project.layout.buildDirectory.dir("protoc-artifact"),
//        "verbose" to true,
//      )
//    )
//
//
//  }
//
//}
