package dev.adamko.gradle.factorio.tasks

import dev.adamko.gradle.factorio.FactorioModSettings
import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import org.gradle.work.DisableCachingByDefault
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

@DisableCachingByDefault(because = "Not a cacheable task")
abstract class LaunchFactorioClientTask @Inject constructor(
  private val executor: ExecOperations
) : DefaultTask() {

  @get:Input
  abstract val currentOs: Property<FactorioModSettings.LocalDev.OS>

  @get:Input
  @get:Optional
  abstract val windowsSteamExe: Property<String>

  @get:Input
  @get:Optional
  abstract val factorioSteamId: Property<String>

  @get:Input
  @get:Optional
  abstract val serverConnectHost: Property<String>

  @get:Input
  @get:Optional
  abstract val clientModsPath: Property<String>

  @get:InputFile
  @get:Optional
  abstract val macFactorioApp: RegularFileProperty

  @get:Input
  @get:Optional
  abstract val launchFactorioCommand: Property<String>

  @TaskAction
  fun launch() {
    val launchCommand =
      launchFactorioCommand.orNull ?: when (currentOs.orNull) {
        FactorioModSettings.LocalDev.OS.WINDOWS           -> windowsCmd()
        FactorioModSettings.LocalDev.OS.MAC_OS            -> macOSCmd()
        null, FactorioModSettings.LocalDev.OS.UNSUPPORTED -> error("no can do, unsupported OS")
      }

    logger.lifecycle("Launching Factorio client\n > $launchCommand")

    executor.exec {
      commandLine = parseSpaceSeparatedArgs(launchCommand)
    }
  }

  private fun windowsCmd(): String {
    val steamExe = windowsSteamExe.orNull ?: error("missing Steam exe")
    val factorioId = factorioSteamId.orNull ?: error("missing Factorio Steam ID")
    val modsDirFlag = clientModsPath.map { """--mod-directory "$it"""" }.getOrElse("")
    val mpConnectFlag = serverConnectHost.map { "--mp-connect $it" }.getOrElse("")

//  commandLine = parseSpaceSeparatedArgs(
////    """explorer "steam://rungameid/$factorioGameId// --mp-connect localhost/" """ // not working
////    """explorer "steam://run/$factorioGameId//--mp-connect localhost/" """ // works! But has annoying pop-up
//    """ ${steamExe.asFile.orNull?.canonicalPath} -applaunch $factorioGameId --mp-connect localhost --mod-directory ${clientModsDirectory.asFile.orNull?.canonicalPath}  """
//  )

    return """
      $steamExe --applaunch $factorioId $modsDirFlag $mpConnectFlag
    """.trimIndent()
  }

  private fun macOSCmd(): String {
//    val factorioApp = macFactorioApp.orNull ?: error("missing Factorio app")

    val modsDirFlag = "" //clientModsPath.map { """--mod-directory "$it"""" }.getOrElse("")
    val mpConnectFlag = serverConnectHost.map { "-mp-connect $it" }.getOrElse("")

    return """
      /Applications/Steam.app/Contents/MacOS/steam_osx -applaunch 427520 $modsDirFlag $mpConnectFlag
    """.trimIndent()

//    return """
//      open -a "$factorioApp" --args $modsDirFlag $mpConnectFlag
//    """.trimIndent()
  }
}
