import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
  id("dev.adamko.kafkatorio.lang.kotlin-js")
  kotlin("plugin.serialization")
  id("io.kvision") version "5.6.1"
}

kotlin {
  js(IR) {
//    nodejs()
    browser {
      runTask {
        outputFileName = "main.bundle.js"
        sourceMaps = true
        devServer = KotlinWebpackConfig.DevServer(
          open = false,
          port = 3000,
          proxy = mutableMapOf(
            "/kv/*" to "http://localhost:8080",
            "/kvws/*" to mapOf("target" to "ws://localhost:8080", "ws" to true)
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
    binaries.executable()
  }

  sourceSets {

    all {
      languageSettings.optIn("kotlin.RequiresOptIn")
      languageSettings.optIn("kotlinx.serialization.ExperimentalSerializationApi")
    }

    main {
      dependencies {

        implementation(projects.modules.eventsSchema)

        implementation("org.jetbrains.kotlinx:kotlinx-nodejs:0.0.7")
        implementation("org.jetbrains.kotlinx:kotlinx-html:0.7.3")

        kvision("kvision")
        kvision("kvision-bootstrap")
        kvision("kvision-bootstrap-css")
        kvision("kvision-bootstrap-icons")
        kvision("kvision-fontawesome")
        kvision("kvision-state")
        kvision("kvision-chart")
        kvision("kvision-maps")
        kvision("kvision-redux")
        kvision("kvision-state-flow")

        implementation(libs.kotlinx.coroutines)
      }

      val webDir = file("src/main/web")
      resources.srcDir(webDir)
    }

    test {
      dependencies {
        implementation(kotlin("test-js"))

        kvision("kvision-testutils")
      }
    }
  }
}

fun KotlinDependencyHandler.kvision(
  module: String,
  version: Provider<String> = libs.versions.kvision,
  configure: ExternalModuleDependency.() -> Unit = {
    isChanging = true
  }
) {
  implementation("io.kvision:$module:${version.get()}", configure)
}
