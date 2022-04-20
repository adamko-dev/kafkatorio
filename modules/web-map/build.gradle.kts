import dev.adamko.kafkatorio.gradle.yarn
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig


plugins {
  dev.adamko.kafkatorio.lang.`kotlin-js`
  kotlin("plugin.serialization")
  id("io.kvision")
}

kotlin {
  js(IR) {
//    nodejs()
    browser {
      runTask {
        outputFileName = "main.bundle.js"
        sourceMaps = false
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
      languageSettings.apply {
        optIn("kotlin.RequiresOptIn")
        optIn("kotlinx.serialization.ExperimentalSerializationApi")
      }
    }

    main {
      dependencies {

        implementation(projects.modules.eventsSchema)

        implementation(libs.kotlinx.nodejs)
        implementation(libs.kotlinx.html)

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

        implementation(dependencies.platform(libs.kotlin.jsWrappers.bom))

//        implementation(dependencies.platform(npm("follow-redirects", "^1.14.8")))
//        implementation(dependencies.platform(npm("nanoid", "^3.1.31")))
//        implementation(dependencies.platform(npm("minimist", "^1.2.6")))
//        implementation(dependencies.platform(npm("async", "^2.6.4")))
//        implementation(dependencies.platform(npm("node-forge", "^1.3.0")))
      }

      val webDir = file("src/main/web")
      resources.srcDir(webDir)
    }

    test {
      dependencies {
        implementation(kotlin("test-js"))

        kvision("kvision-testutils")

//        implementation(npm("karma", "^6.3.16"))
      }
    }
  }
}

fun KotlinDependencyHandler.kvision(
  module: String,
  version: Provider<String> = libs.versions.kvision,
  configure: ExternalModuleDependency.() -> Unit = {
//    isChanging = true
  }
) {
  implementation("io.kvision:$module:${version.get()}", configure)
}

// https://youtrack.jetbrains.com/issue/KT-42420
afterEvaluate {
  yarn {
    resolution("mocha", "9.2.2")
    resolution("follow-redirects", "1.14.8")
    resolution("nanoid", "3.1.31")
    resolution("minimist", "1.2.6")
    resolution("async", "2.6.4")
    resolution("node-forge", "1.3.0")
  }
}
