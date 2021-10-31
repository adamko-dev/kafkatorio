package dev.adamko.gradle.pbandg.settings


import java.util.function.Supplier
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.property
import org.gradle.nativeplatform.platform.Architecture
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform
import org.gradle.nativeplatform.platform.internal.DefaultOperatingSystem

abstract class PBAndGSettings(private val project: Project) {

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
   * Default: concatenate [operatingSystemName] and [architectureName]
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
