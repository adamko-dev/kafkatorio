import io.kvision.gradle.KVisionPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig


plugins {
  id("kafkatorio.conventions.lang.kotlin-js")
  id("io.kvision")
  kotlin("plugin.serialization")
  id("dev.adamko.geedeecee")
}


kvision {
  enableHiddenKotlinJsStore.set(false)
}


kotlin {
  js(IR) {
    browser {
      binaries.executable()

      runTask {
        outputFileName = "main.bundle.js"
        sourceMaps = false
        devServer = KotlinWebpackConfig.DevServer(
          open = false,
          port = 3000,
          proxy = mutableMapOf(
            "/kafkatorio/data/servers" to mapOf(
              "target" to "http://localhost:12080",
              "secure" to false,
              "changeOrigin" to true,
            ),
            "/kafkatorio/ws" to mapOf(
              "target" to "ws://localhost:12080",
              "ws" to true,
            ),
          ),
          static = mutableListOf("$buildDir/processedResources/js/main")
        )
      }
      webpackTask {
        outputFileName = "main.bundle.js"
      }
      testTask {
        useKarma {
          useChromeHeadless()
        }
      }
    }
  }

  sourceSets {

    configureEach {
      languageSettings {
        optIn("kotlin.RequiresOptIn")
        optIn("kotlinx.serialization.ExperimentalSerializationApi")
      }
    }


    main {
      dependencies {
        implementation(dependencies.platform(projects.modules.versionsPlatform))

        implementation(libs.kotlinx.coroutines.core)

        implementation(projects.modules.eventsLibrary)

        implementation(libs.kotlinx.html)

        implementation(libs.kotlinx.nodejs)
        implementation(libs.kotlinx.html)

        implementation(kvision())
        implementation(kvision("bootstrap"))
        implementation(kvision("bootstrap-css"))
        implementation(kvision("bootstrap-icons"))
        implementation(kvision("fontawesome"))
        implementation(kvision("state"))
        implementation(kvision("chart"))
        implementation(kvision("maps"))
        implementation(kvision("rest"))
        implementation(kvision("redux"))
        implementation(kvision("state-flow"))
        implementation(kvision("routing-navigo-ng"))
      }

      resources.srcDir("src/main/web")
    }


    test {
      dependencies {
        implementation(kotlin("test"))

        implementation(kvision("testutils"))
        //        implementation(npm("karma", "^6.3.16"))
      }
    }
  }
}


@Suppress("UnusedReceiverParameter") // just for scoping - this function doesn't need to be used everywhere
fun KotlinDependencyHandler.kvision(
  module: String? = null,
  version: Provider<String> = libs.versions.kvision,
): String {
  val prefixedModule = if (module == null) "kvision" else "kvision-$module"
  return "io.kvision:$prefixedModule:${version.get()}"
}


val installBootstrapThemeCss by tasks.registering(Copy::class) {
  group = KVisionPlugin.KVISION_TASK_GROUP
  val bootswatchThemeMinCss = resources.text.fromUri(
    libs.versions.npm.bootswatch.map { bootswatch ->
      "https://cdn.jsdelivr.net/npm/bootswatch@$bootswatch/dist/darkly/bootstrap.min.css"
    }
  )
  from(bootswatchThemeMinCss) {
    rename { "bootstrap.min.css" }
  }
  into(layout.projectDirectory.dir("src/main/resources/css"))
}


tasks.withName("processResources").configureEach {
  dependsOn(installBootstrapThemeCss)
}


val runWebMap by tasks.registering {
  group = rootProject.name

  dependsOn(tasks.withName("browserDevelopmentRun"))
}


tasks.zip {
  dependsOn(tasks.withName("browserWebpack"))
  from(layout.buildDirectory.dir("distributions"))
}


tasks.dockerContextPrepareFiles {
  from(zipTree(tasks.zip.flatMap { it.archiveFile }))
}
