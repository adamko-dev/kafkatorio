import dev.adamko.kafkatorio.factoriomod.FactorioMod
import kafkatorio.distributions.asConsumer
import kafkatorio.distributions.factorioModAttributes
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

plugins {
  id("kafkatorio.conventions.infra.docker-compose")
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


val deployModToLocalServer by tasks.registering(Copy::class) {
  description = "Copy the mod to the Factorio Docker server"
  group = FactorioMod.TASK_GROUP

  dependsOn(factorioMod)

  from(
    provider { factorioMod.incoming.artifacts.artifactFiles.files }
  )
  into(factorioServerDataDir.dir("mods"))

  doLast {
    logger.lifecycle("Copying mods ${source.files} into $destinationDir")
  }
}


tasks.dockerDown {
  commandLine = parseSpaceSeparatedArgs(""" docker-compose stop """)
}


tasks.dockerUp {
  dependsOn(
    deployModToLocalServer,
    ":modules:infra-kafka-cluster:processRun",
  )
}


val kafkatorioServerToken: String by project


tasks.dockerEnvUpdate {
  properties(
    "FACTORIO_VERSION" to libs.versions.factorio.get(),
    "KAFKATORIO_TOKEN" to kafkatorioServerToken,
  )
}


tasks.register(FactorioMod.PUBLISH_MOD_LOCAL_TASK_NAME) {
  group = FactorioMod.TASK_GROUP
  dependsOn(deployModToLocalServer)
}


idea {
  module {
    excludeDirs.add(file("src/factorio-server"))
  }
}

val runFactorioServer by tasks.registering {
  group = rootProject.name

  dependsOn(tasks.processRun)
}
