package dev.adamko.gradle.factorio

import dev.adamko.gradle.factorio.internal.adding
import javax.inject.Inject
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory
import org.gradle.kotlin.dsl.newInstance

abstract class FactorioModSettings @Inject constructor(
  private val objects: ObjectFactory,
  private val providers: ProviderFactory,
  private val layout: ProjectLayout,
) : ExtensionAware {

  abstract val modName: Property<String>
  abstract val modTitle: Property<String>
  abstract val modDescription: Property<String>
  abstract val modAuthor: Property<String>
  abstract val modVersion: Property<String>
  abstract val modDependencies: ListProperty<String>

  /**
   * The version of Factorio that the mod is compatible with,
   * in the format `"$major.minor"`.
   *
   * Must only be "major.minor" - declaring a patch causes an error.
   */
  abstract val factorioCompatibility: Property<String>

  /**
   * The filename of the zip file containing the compiled mod.
   *
   * The mod zip must be named in the pattern of `${modName}_{modVersion}.zip`,
   * for example `test-mod-thing_0.0.1.zip`.
   */
  abstract val distributionZipName: Property<String>

  val mainSources: FactorioModSourceSet.WithResources = objects.newInstance("Main")
  val testSources: FactorioModSourceSet.WithResources = objects.newInstance("Test")
  val generatedSources: FactorioModSourceSet = objects.newInstance("Generated")


//  abstract val typescriptSrcDir: DirectoryProperty
//  abstract val resourcesDir: DirectoryProperty
//  abstract val modDataResourcesDir: DirectoryProperty
//  abstract val generatedLuaSrcDir: DirectoryProperty


  abstract val factorioServerDataDirectory: DirectoryProperty

  val localDev: LocalDev =
    extensions.adding("localDev", objects.newInstance())

  abstract class LocalDev : ExtensionAware {
    abstract val currentOs: Property<OS>

    /**
     * Location of Steam executable, `steam.exe`, on Windows.
     *
     * Used to launch Steam.
     */
    abstract val windowsSteamExe: Property<String>

    /**
     * The Steam ID of Factorio.
     *
     * Each Steam game has an ID.
     */
    abstract val factorioSteamId: Property<String>

    /**
     * Hostname of the local Factorio server (e.g. `localhost`).
     */
    abstract val serverConnectHost: Property<String>

    /**
     * The mod directory of a locally installed Factorio client.
     */
    abstract val clientModsDirectory: RegularFileProperty

    /**
     * `~/Library/Application Support/` directory.
     */
    abstract val macApplicationSupportDir: RegularFileProperty

    /**
     * The location of locally installed Factorio game client executable.
     */
    abstract val macFactorioApp: RegularFileProperty

    /** Supported local dev OSes */
    enum class OS {
      Windows,
      MacOS,
      Unsupported,
    }
  }
}
