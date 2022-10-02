package dev.adamko.gradle.factorio

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property

interface FactorioModSettings {

  val factorioClientModsDirectory: DirectoryProperty

  val steamExe: RegularFileProperty

  val factorioGameSteamId: Property<String>

  val factorioServerDataDirectory: DirectoryProperty
}
