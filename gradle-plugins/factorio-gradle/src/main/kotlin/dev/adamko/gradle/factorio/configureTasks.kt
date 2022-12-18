package dev.adamko.gradle.factorio

import dev.adamko.gradle.factorio.tasks.AssembleFactorioModContents
import dev.adamko.gradle.factorio.tasks.GenerateFactorioModInfoTask
import dev.adamko.gradle.factorio.tasks.LaunchFactorioClientTask
import dev.adamko.gradle.factorio.tasks.LocalPublishFactorioModTask
import dev.adamko.gradle.factorio.tasks.PackageFactorioModTask
import org.gradle.kotlin.dsl.withType

internal fun FactorioModPlugin.PluginContext.configureTasks() {

  tasks.launchFactorioClient.configure {

  }
  tasks.assembleModContents.configure {
    modFiles.apply {
      from(settings.mainSources.typescript.destinationDirectory)
      from(tasks.generateModInfoJson)
      from(settings.mainSources.resources)
    }
  }
  tasks.packageMod.configure {
    from(tasks.assembleModContents)
  }
  tasks.publishModToLocalClient.configure {
    dependsOn(tasks.packageMod)
  }
  tasks.generateModInfoJson.configure {

  }


  project.tasks.withType<LocalPublishFactorioModTask>().configureEach {
    description = "Copy the mod to the Factorio client"
    group = FactorioModPlugin.TASK_GROUP

    onlyIf {
      require(it is LocalPublishFactorioModTask)
      it.clientModDirectory.asFile.get().exists()
    }

    modFiles.from(
      configurations.factorioMod.map {
        it.incoming
          .artifactView { lenient(true) }
          .files
      }
    )

    doLast {
      logger.lifecycle("Published Factorio Mod to '${clientModDirectory.asFile.get()}'")
    }
  }

  configureLaunchFactorioClientTasks()

  project.tasks.withType<GenerateFactorioModInfoTask>().configureEach {
    description = "Generate info.json (required for Factorio Mods)"
    group = FactorioModPlugin.TASK_GROUP

    modName.convention(settings.modName)
    modTitle.convention(settings.modTitle)
    modDescription.convention(settings.modDescription)
    modAuthor.convention(settings.modAuthor)
    modVersion.convention(settings.modVersion)
    factorioCompatibility.convention(settings.factorioCompatibility)
    modDependencies.convention(settings.modDependencies)
    infoJson.convention(
      layout.file(
        providers.provider {
          temporaryDir.resolve("info.json")
        }
      )
    )
  }

  project.tasks.withType<AssembleFactorioModContents>().configureEach {
    description = "Gather the mod files that will be packaged into the mod zip"
    group = FactorioModPlugin.TASK_GROUP

    modName.convention(settings.modName)
    modVersion.convention(settings.modVersion)

    destination.convention(layout.dir(providers.provider { temporaryDir }))
  }

  project.tasks.withType<PackageFactorioModTask>().configureEach {
    description = "Package the Factorio mod"
    group = FactorioModPlugin.TASK_GROUP

    archiveFileName.convention(settings.distributionZipName)

    includeEmptyDirs = false

    exclude {
      // exclude empty files
      it.file.run {
        isFile && useLines { lines -> lines.all { line -> line.isBlank() } }
      }
    }
  }

  project.tasks.withType<LocalPublishFactorioModTask>().configureEach {
    description = "Launch local Factorio Steam game client"
    group = FactorioModPlugin.TASK_GROUP

    modFiles.from(project.tasks.withType<PackageFactorioModTask>())
    clientModDirectory.convention(layout.dir(settings.localDev.clientModsDirectory.asFile))
  }
}


private fun FactorioModPlugin.PluginContext.configureLaunchFactorioClientTasks() {

  val localDev = settings.localDev

//  // convert to local props -> try to comply with Gradle config cache
//  val localDevCurrentOs = localDev.currentOs
//  val localDevWindowsSteamExe = localDev.windowsSteamExe
//  val localDevFactorioSteamId = localDev.factorioSteamId
//  val localDevServerConnectHost = localDev.serverConnectHost
//  val localDevClientModDirectory = localDev.clientModDirectory
//  val localDevMacApplicationSupportDir = localDev.macApplicationSupportDir
//  val localDevMacFactorioApp = localDev.macFactorioApp

  project.tasks.withType<LaunchFactorioClientTask>().configureEach {
    description = "Launch local Factorio Steam game client"
    group = FactorioModPlugin.TASK_GROUP

    currentOs.convention(localDev.currentOs)
    windowsSteamExe.convention(localDev.windowsSteamExe)
    factorioSteamId.convention(localDev.factorioSteamId)
    serverConnectHost.convention(localDev.serverConnectHost)
    clientModsPath.convention(localDev.clientModsDirectory.map { it.asFile.canonicalPath })
    macFactorioApp.convention(localDev.macFactorioApp)
  }
}
