import dev.adamko.kafkatorio.gradle.asConsumer
import dev.adamko.kafkatorio.gradle.factorioModAttributes
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

plugins {
  id("dev.adamko.kafkatorio.infra.docker-compose")
}

val dockerSrcDir: Directory by extra
val factorioServerDataDir: Directory = dockerSrcDir.dir("factorio-server")

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

  dependsOn(factorioMod)

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

tasks.dockerEnv {
  properties("FACTORIO_VERSION" to libs.versions.factorio.get())
}

tasks.build { dependsOn(deployModToServer) }


idea {
  module {
    excludeDirs.add(file("src/factorio-server"))
  }
}
