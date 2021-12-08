import dev.adamko.kafkatorio.gradle.asConsumer
import dev.adamko.kafkatorio.gradle.factorioModAttributes
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

plugins {
  id("dev.adamko.kafkatorio.infra.docker-compose")
}

val srcDir: Directory by extra
val factorioServerDataDir: Directory = srcDir.dir("factorio-server")

val factorioMod: Configuration by configurations.creating {
  asConsumer()
  factorioModAttributes(objects)
}

dependencies {
  factorioMod(projects.modules.eventsMod)
}

val deployModToServer by tasks.registering(Copy::class) {
  description = "Copy the mod to the Factorio Docker server"
  group = project.name

  from(
    provider { factorioMod.incoming.artifacts.artifactFiles.files }
  )
  into(factorioServerDataDir.dir("mods"))

  doLast {
    logger.lifecycle("Copying mods ${source.files} to $destinationDir")
  }
}

tasks.dockerDown {
  commandLine = parseSpaceSeparatedArgs(""" docker-compose stop """)
}

tasks.dockerUp {
  dependsOn(
    deployModToServer,
    ":modules:infra-kafka-cluster:processRun",
  )
}

tasks.updateEnv {
  properties("FACTORIO_VERSION" to libs.versions.factorio.get())
}

tasks.build { dependsOn(deployModToServer) }

//<editor-fold desc="Factorio server lifecycle tasks">
//val factorioServerStop = tasks.register<Exec>("factorioServerStop") {
//  group = project.name
//
//  mustRunAfter(deployModToServer)
//
//  workingDir(modInfraDir)
//  commandLine = parseSpaceSeparatedArgs("docker-compose stop factorio-server")
//}

//val factorioServerUp = tasks.register<Exec>("factorioServerUp") {
//  group = project.name
//
//  mustRunAfter(deployModToServer)
//  dependsOn(factorioServerStop)
//
//  workingDir(modInfraDir)
//  commandLine = parseSpaceSeparatedArgs("docker-compose up -d factorio-server")
//}

//val factorioServerRestart: Task by tasks.creating {
//  group = project.name
//
//  dependsOn(factorioServerStop, factorioServerUp)
//}
//</editor-fold>

//
//tasks.processRun {
//  description = "Build the mod, upload to Server and Client, and start both"
//  dependsOn(
//    deployModToServer,
//    factorioServerRestart,
//  )
//}
//
//tasks.processKill {
//  dependsOn(
//    factorioServerStop,
//  )
//}
//
