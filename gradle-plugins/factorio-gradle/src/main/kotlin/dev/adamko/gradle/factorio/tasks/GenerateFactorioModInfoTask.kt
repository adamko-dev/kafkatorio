package dev.adamko.gradle.factorio.tasks

import javax.inject.Inject
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class GenerateFactorioModInfoTask @Inject constructor(

) : DefaultTask() {

  /**
   * Mandatory field. The internal name of mod. The game accepts anything as a mod name, however the mod portal restricts mod names to only consist of alphanumeric characters, dashes and underscores. Note that the mod folder or mod zip file name has to contain the mod name, where the restrictions of the file system apply.
   *
   * The game accepts mod names with a maximum length of 100 characters. The mod portal only accepts mods with names that are longer than 3 characters and shorter than 50 characters.
   */
  @get:Input
  abstract val modName: Property<String>

  /**
   * Mandatory field. The display name of the mod, so it is not recommended to use someUgly_pRoGrAmMeR-name here. Can be overwritten with a locale entry in the mod-name category, using the internal mod name as the key.
   *
   * The game will reject a title field that is longer than 100 characters. However, this can be worked around by using the locale entry. The mod portal does not restrict mod title length.
   */
  @get:Input
  abstract val modTitle: Property<String>

  /**
   * Optional field. A short description of what your mod does. This is all that people get to see in-game. Can be overwritten with a locale entry in the mod-description category, using the internal mod name as the key.
   */
  @get:Input
  @get:Optional
  abstract val modDescription: Property<String>

  /**
   * Optional field. Where the mod can be found on the internet. Note that the in-game mod browser shows the mod portal link additionally to this field. Please don't put "None" here, it makes the field on the mod portal website look ugly. Just leave the field empty if the mod doesn't have a website/forum thread/discord.
   */
  @get:Input
  @get:Optional
  abstract val modHomepage: Property<String>

  /**
   * Mandatory field. The author of the mod. This field does not have restrictions, it can also be a list of authors etc. The mod portal ignores this field, it will simply display the uploader's name as the author.
   */
  @get:Input
  abstract val modAuthor: Property<String>

  /**
   * Mandatory field. Defines the version of the mod in the format `number.number.number` for `Major.Middle.Minor`, for example `0.6.4`. Each number can range from 0 to 65535.
   */
  @get:Input
  abstract val modVersion: Property<String>

  /**
   *  Optional field in the format "major.minor". The Factorio version that this mod supports. This can only be one Factorio version, not multiple. However, it includes all .sub versions. While the field is optional, usually mods are developed for versions higher than the default 0.12, so the field has to be added anyway.
   *
   *   Adding a sub part, e.g. "0.18.27" will make the mod portal reject the mod and the game act weirdly. That means this shouldn't be done; use only the major and minor components "major.minor", for example "1.0".
   *
   *   Mods with the factorio_version "0.18" can also be loaded in 1.0 and the mod portal will return them when queried for factorio_version 1.0 mods.
   */
  @get:Input
  @get:Optional
  abstract val factorioCompatibility: Property<String>

  /**
   * Optional field. Mods that this mod depends on or is incompatible with.
   * If this mod depends on another, the other mod will load first, see Data-Lifecycle.
   * An empty array allows get around the default and have no dependencies at all.
   *
   * Example:
   *
   * `"dependencies": ["mod-a", "? mod-c > 0.4.3", "! mod-g"]`
   *
   * Each dependency is a string that consists of up to three parts: `<prefix> internal-mod-name <equality-operator version>`, for example `? some-mod-everyone-loves >= 4.2.0`.
   *
   * The equality operator (`<`, `<=`, `=`, `>=` or `>`) combined with the version allows to define dependencies that require certain mod versions, but it is not required.
   *
   * Incompatibility does not support versions; if incompatibility is used, version is ignored.
   *
   * The possible prefixes are:
   * * `!` for incompatibility
   * * `?` for an optional dependency
   * * `(?)` for a hidden optional dependency
   * * `~` for a dependency that does not affect load order
   * * no prefix for a hard requirement for the other mod.
   */
  @get:Input
  @get:Optional
  abstract val modDependencies: ListProperty<String>

  @get:OutputFile
  abstract val infoJson: RegularFileProperty

  @TaskAction
  fun generate() {
    val infoJsonObject = buildJsonObject {
      put("name", modName.get())
      put("title", modTitle.get())
      put("version", modVersion.get())
      put("author", modAuthor.get())

      modDescription.orNull?.let { put("description", it) }
      modHomepage.orNull?.let { put("homepage", it) }
      factorioCompatibility.orNull?.let { put("factorio_version", it) }
      modDependencies.orNull?.let {
        putJsonArray("dependencies") { it.forEach(::add) }
      }
    }

    val infoJsonContent = encoder.encodeToString(infoJsonObject)
      .replace("[\n  ]", "[]")

    val infoJson = infoJson.get().asFile
    infoJson.parentFile.mkdirs()
    infoJson.writeText(infoJsonContent + "\n")
  }

  companion object {
    @OptIn(ExperimentalSerializationApi::class)
    private val encoder = Json {
      prettyPrint = true
      prettyPrintIndent = "  "
    }
  }
}
