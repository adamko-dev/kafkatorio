import dev.adamko.kafkatorio.factoriomod.FactorioMod
import dev.adamko.kafkatorio.gradle.asConsumer
import dev.adamko.kafkatorio.gradle.factorioModAttributes
import dev.adamko.kafkatorio.gradle.not
import dev.adamko.kafkatorio.task.ProcessRunningSpec
import org.gradle.kotlin.dsl.support.serviceOf
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

plugins {
  dev.adamko.kafkatorio.infra.`process-runner`
}

description = "Start the Factorio game client"

val clientModsDirectory: DirectoryProperty = objects.directoryProperty().apply {
  val modDir = File("""D:\Users\Adam\AppData\Roaming\Factorio\mods""")
  if (modDir.exists()) {
    set(modDir)
  }
}
val factorioGameId = "427520"
val steamExe: RegularFileProperty = objects.fileProperty().apply {
  val steam = File("""C:\Program Files (x86)\Steam\steam.exe""")
  if (steam.exists()) {
    set(steam)
  }
}

val factorioMod: Configuration by configurations.creating {
  asConsumer()
  factorioModAttributes(objects)
}

dependencies {
  factorioMod(projects.modules.eventsMod)
}


//<editor-fold desc="Mod deployment tasks">
val deployModToLocalClient by tasks.registering(Copy::class) {
  description = "Copy the mod to the Factorio client"
  group = FactorioMod.TASK_GROUP

  val clientModsDirectory123 = clientModsDirectory

  onlyIf { clientModsDirectory123.orNull?.asFile?.exists() == true }

  from(factorioMod)
  into(clientModsDirectory)

  doLast {
    logger.lifecycle("Copying mod from ${source.files} to $destinationDir")
  }
}
//</editor-fold>


//<editor-fold desc="Factorio client lifecycle tasks">
fun ExecOperations.isFactorioRunning(): Spec<Task> = ProcessRunningSpec(this, "factorio.exe")


val clientLaunch by tasks.registering(Exec::class) {
  description = "Run local Factorio Steam game client"
  group = FactorioMod.TASK_GROUP

  val steamExe123 = steamExe
  val clientModsDirectory444 = clientModsDirectory

  onlyIf(!serviceOf<ExecOperations>().isFactorioRunning())
  onlyIf { steamExe123.orNull?.asFile?.exists() == true }
  onlyIf { clientModsDirectory444.orNull?.asFile?.exists() == true }

  dependsOn(deployModToLocalClient)
  mustRunAfter(clientKill, ":modules:infra-factorio-server:processRun")

  commandLine = parseSpaceSeparatedArgs(
//    """explorer "steam://rungameid/$factorioGameId// --mp-connect localhost/" """ // not working
//    """explorer "steam://run/$factorioGameId//--mp-connect localhost/" """ // works! But has annoying pop-up
    """ ${steamExe.asFile.orNull?.canonicalPath} -applaunch $factorioGameId --mp-connect localhost --mod-directory ${clientModsDirectory.asFile.orNull?.canonicalPath}  """
  )
  doFirst { logger.lifecycle("Launching factorio.exe") }
}

val clientKill by tasks.registering(Exec::class) {
  description = "Stop the local Factorio Steam game client"
  group = FactorioMod.TASK_GROUP

  onlyIf(serviceOf<ExecOperations>().isFactorioRunning())

  commandLine = parseSpaceSeparatedArgs(""" taskkill /im factorio.exe """)
  doFirst { logger.lifecycle("Killing factorio.exe") }
}
//</editor-fold>


tasks.register(FactorioMod.PUBLISH_MOD_LOCAL_TASK_NAME) {
  group = FactorioMod.TASK_GROUP
  dependsOn(deployModToLocalClient)
}


tasks.processRun {
  dependsOn(
    deployModToLocalClient,
    clientLaunch,
  )
}

tasks.processKill {
  dependsOn(clientKill)
}
