package dev.adamko.gradle.factorio

import javax.inject.Inject
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory
import org.gradle.kotlin.dsl.newInstance

abstract class FactorioModSettings @Inject constructor(
  internal val objects: ObjectFactory,
  internal val providers: ProviderFactory,
  internal val layout: ProjectLayout,
) {

  abstract val modName: Property<String>
  abstract val modTitle: Property<String>
  abstract val modDescription: Property<String>
  abstract val modAuthor: Property<String>
  abstract val modVersion: Property<String>
  abstract val modDependencies: ListProperty<String>

  // version of Factorio that the mod is compatible with (must only be "major.minor" - patch causes error)
  abstract val factorioCompatibility: Property<String>

  abstract val distributionZipName: Property<String>

  val mainSources: FactorioModSourceSet.WithResources = objects.newInstance("Main")
  val testSources: FactorioModSourceSet.WithResources = objects.newInstance("Test")
  val generatedSources: FactorioModSourceSet = objects.newInstance("Generated")


//  abstract val typescriptSrcDir: DirectoryProperty
//  abstract val resourcesDir: DirectoryProperty
//  abstract val modDataResourcesDir: DirectoryProperty
//  abstract val generatedLuaSrcDir: DirectoryProperty


  abstract val factorioServerDataDirectory: DirectoryProperty


  val localDev: LocalDev = objects.newInstance()

  fun localDev(configure: LocalDev.() -> Unit) {
    localDev.configure()
  }

  interface LocalDev {
    val currentOs: Property<OS>

    val windowsSteamExe: Property<String>

    val factorioSteamId: Property<String>

    val serverConnectHost: Property<String>

    val clientModsDirectory: RegularFileProperty

    val macApplicationSupportDir: RegularFileProperty

    val macFactorioApp: RegularFileProperty

    /** Supported local dev OSes */
    enum class OS { WINDOWS, MAC_OS, UNSUPPORTED }
  }
}
