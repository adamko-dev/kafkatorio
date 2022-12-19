import dev.adamko.gradle.factorio.FactorioModPlugin
import kafkatorio.tasks.ProcessRunningSpec
import org.gradle.kotlin.dsl.support.serviceOf
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

plugins {
  id("kafkatorio.conventions.infra.process-runner")
  id("dev.adamko.factorio-mod")
}

description = "Manage the Factorio game client"

val clientModsDirectory: DirectoryProperty = objects.directoryProperty().apply {
  val modDir = File("""D:\Users\Adam\AppData\Roaming\Factorio\mods""")
  if (modDir.exists()) {
    set(modDir)
  }
}

dependencies {
  factorioMod(projects.modules.eventsMod)
}


fun ExecOperations.isFactorioRunning(): Spec<Task> = ProcessRunningSpec(this, "factorio.exe")


val clientKill by tasks.registering(Exec::class) {
  description = "Stop the local Factorio Steam game client"
  group = FactorioModPlugin.TASK_GROUP

  onlyIf(serviceOf<ExecOperations>().isFactorioRunning())

  commandLine = parseSpaceSeparatedArgs(""" taskkill /im factorio.exe """)
  doFirst { logger.lifecycle("Killing factorio.exe") }
}


tasks.register(FactorioModPlugin.PUBLISH_MOD_LOCAL_TASK_NAME) {
  group = FactorioModPlugin.TASK_GROUP
//  dependsOn(deployModToLocalClient)
}


tasks.processRun {
  dependsOn(
//    deployModToLocalClient,
    tasks.launchFactorioClient,
  )
}

tasks.processKill {
  dependsOn(clientKill)
}


val runFactorioClient by tasks.registering {
  group = rootProject.name

  dependsOn(tasks.processRun)
}
