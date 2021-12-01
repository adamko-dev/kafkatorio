import dev.adamko.kafkatorio.gradle.asConsumer
import dev.adamko.kafkatorio.gradle.factorioModAttributes
import dev.adamko.isProcessRunning
import dev.adamko.not
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

plugins {
  id("dev.adamko.kafkatorio.process-runner")
}

val tokens: Map<String, String> by project.extra

val clientModsDirectory = file("""D:\Users\Adam\AppData\Roaming\Factorio\mods""")
val factorioGameId = "427520"
val steamExe = """C:\Program Files (x86)\Steam\steam.exe"""

val factorioMod: Configuration by configurations.creating {
  asConsumer()
  factorioModAttributes(objects)
}

dependencies {
  factorioMod(projects.modules.eventsMod)
}

//<editor-fold desc="Mod deployment tasks">
val deployModToClient by tasks.registering(Copy::class) {
  description = "Copy the mod to the Factorio client"
  group = project.name

  from(factorioMod)
  into(clientModsDirectory)

  doLast {
    logger.lifecycle("Copying mod from ${source.files} to $destinationDir")
  }
}
//</editor-fold>

//<editor-fold desc="Factorio client lifecycle tasks">
fun isFactorioRunning(): Spec<Task> = isProcessRunning("factorio.exe")

val clientLaunch by tasks.registering(Exec::class) {
  description = "Run Factorio Steam game client"
  group = project.name

  onlyIf(!isFactorioRunning())

  dependsOn(deployModToClient)
  mustRunAfter(clientKill)

  commandLine = parseSpaceSeparatedArgs(
//    """explorer "steam://rungameid/$factorioGameId// --mp-connect localhost/" """ // not working
//    """explorer "steam://run/$factorioGameId//--mp-connect localhost/" """ // works! But has annoying pop-up
    """ $steamExe -applaunch $factorioGameId --mp-connect localhost """
  )
  doFirst { logger.lifecycle("Launching factorio.exe") }
}

val clientKill by tasks.registering(Exec::class) {
  description = "Run Factorio Steam game client"
  group = project.name
  onlyIf(isFactorioRunning())
  commandLine = parseSpaceSeparatedArgs(""" taskkill /im factorio.exe """)
  doFirst { logger.lifecycle("Killing factorio.exe") }
}
//</editor-fold>

tasks.build { dependsOn(deployModToClient) }

tasks.processRun {
  dependsOn(
    deployModToClient,
    clientLaunch,
  )
}

tasks.processKill {
  dependsOn(clientKill)
}
