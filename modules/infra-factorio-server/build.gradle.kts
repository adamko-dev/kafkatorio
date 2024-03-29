import dev.adamko.gradle.factorio.FactorioModPlugin

plugins {
  id("kafkatorio.conventions.base")
  id("dev.adamko.geedeecee")
  id("dev.adamko.factorio-mod-library")
  idea
}


geedeecee {
  srcDir.set(layout.projectDirectory.dir("src"))
}

val factorioServerDataDir: DirectoryProperty = objects.directoryProperty()
  .convention(geedeecee.srcDir.dir("factorio-server"))

dependencies {
  factorioMod(projects.modules.eventsMod)
}


val deployModToLocalServer by tasks.registering(Copy::class) {
  description = "Copy the mod to the Factorio Docker server"
  group = FactorioModPlugin.TASK_GROUP

  dependsOn(configurations.factorioMod)

//  from(
//    provider { factorioMod.incoming.artifacts.artifactFiles.files }
//  )
  from(configurations.factorioMod.map { it.incoming.artifactView { lenient(true) }.files })
  into(factorioServerDataDir.dir("mods"))

  doLast {
    logger.lifecycle("Copying mods ${source.files} into $destinationDir")
  }
}


//tasks.dockerComposeDown {
//  commandLine = parseSpaceSeparatedArgs(""" docker-compose stop """)
//}


tasks.dockerComposeUp {
  dependsOn(
    deployModToLocalServer,
    ":modules:infra-kafka-cluster:processRun",
  )
}


val kafkatorioServerToken: String? by project


tasks.dockerComposeEnvUpdate {
  envProperties {
    put("FACTORIO_VERSION", libs.versions.factorio)
    kafkatorioServerToken?.let { token ->
      put("KAFKATORIO_TOKEN", token)
    }
  }
}


tasks.register(FactorioModPlugin.PUBLISH_MOD_LOCAL_TASK_NAME) {
  group = FactorioModPlugin.TASK_GROUP
  dependsOn(deployModToLocalServer)
}


idea {
  module {
    excludeDirs.add(file("src/factorio-server"))
  }
}

val runFactorioServer by tasks.registering {
  group = rootProject.name

  dependsOn(tasks.dockerComposeUp)
}
