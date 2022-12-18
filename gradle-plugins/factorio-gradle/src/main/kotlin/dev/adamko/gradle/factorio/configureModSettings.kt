package dev.adamko.gradle.factorio

import dev.adamko.gradle.factorio.FactorioModSettings.LocalDev.OS
import java.io.File
import org.gradle.api.internal.provider.Providers
import org.gradle.api.provider.ProviderFactory


internal fun FactorioModPlugin.PluginContext.configureModSettings() = with(settings) {
  modName.convention(providers.provider { project.name })
  modTitle.convention(modName)
  modDescription.convention(providers.provider { project.description ?: "" })
  modVersion.convention(providers.provider { project.version.toString() })

  distributionZipName.convention(providers.zip(modName, modVersion) { name, version ->
    "${name}_${version}.zip"
  })

  val srcBaseMain = layout.projectDirectory.dir("src/main")
  mainSources.lua.srcDir(srcBaseMain.dir("lua"))
  mainSources.typescript.srcDir(srcBaseMain.dir("typescript"))
  mainSources.resources.srcDir(srcBaseMain.dir("resources"))


  val srcBaseTest = layout.projectDirectory.dir("src/test")
  testSources.lua.srcDir(srcBaseTest.dir("lua"))
  testSources.typescript.srcDir(srcBaseTest.dir("typescript"))
  testSources.resources.srcDir(srcBaseTest.dir("resources"))


  val srcBaseGenerated = layout.buildDirectory.dir("generated-src").get()
  generatedSources.lua.srcDir(srcBaseGenerated.dir("lua"))
  generatedSources.typescript.srcDir(srcBaseGenerated.dir("typescript"))

  mainSources.typescript.destinationDirectory.convention(srcBaseGenerated.dir("lua"))

  configureLocalDev()
}


private fun FactorioModPlugin.PluginContext.configureLocalDev() = with(settings) {

  val userHome = providers.systemProperty("user.home")
    .map { home -> File(home) }


  localDev {

    currentOs.convention(providers.systemProperty("os.name").map { osName ->
      val currentOS = osName.lowercase()
      when {
        "windows" in currentOS -> OS.WINDOWS
        "mac" in currentOS     -> OS.MAC_OS
        else                   -> {
          logger.warn("cannot select OS for Factorio local dev - '${osName}' is unsupported")
          OS.UNSUPPORTED
        }
      }
    })

    windowsSteamExe.convention("""C:\Program Files (x86)\Steam\steam.exe""")
    factorioSteamId.convention("427520")
//    serverConnectHost.convention("localhost")

    macApplicationSupportDir.convention(layout.file(userHome.map { home ->
      home.resolve("""Library/Application Support/""")
    }))

    macFactorioApp.convention(layout.file(macApplicationSupportDir.map { appSupport ->
      appSupport.asFile.resolve("""Steam/steamapps/common/Factorio/factorio.app/Contents/MacOS/factorio""")
    }))

    clientModsDirectory.convention(layout.file(
      currentOs.flatMap { os ->
        when (os) {
          OS.WINDOWS -> userHome.map { home ->
            home.resolve("""AppData/Roaming/Factorio/mods""")
          }

          OS.MAC_OS -> macApplicationSupportDir.map { appSupport ->
            appSupport.asFile.resolve("""factorio/mods""")

          }

          OS.UNSUPPORTED -> {
            logger.warn("cannot automatically determine Factorio client mod directory, $os is unsupported")
            Providers.notDefined() // https://github.com/gradle/gradle/issues/12388
          }
        }
      }
    ))
  }
}


private fun ProviderFactory.factorioModProp(name: String) {
  environmentVariable("FACTORIO_MOD_$name")
    .orElse(gradleProperty("factorioMod_$name"))
}
