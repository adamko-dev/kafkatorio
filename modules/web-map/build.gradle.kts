import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
  id("dev.adamko.kafkatorio.lang.kotlin-js")
  kotlin("plugin.serialization")
//  kotlin("js") // version kotlinVersion
  id("io.kvision") version "5.6.1"
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
    val kvisionVersion: String = libs.versions.kvision.get()

    main {
      dependencies {

        implementation("org.jetbrains.kotlinx:kotlinx-nodejs:0.0.7")

        implementation("io.kvision:kvision:$kvisionVersion")
        implementation("io.kvision:kvision-bootstrap:$kvisionVersion")
        implementation("io.kvision:kvision-bootstrap-css:$kvisionVersion")
        implementation("io.kvision:kvision-bootstrap-icons:$kvisionVersion")
        implementation("io.kvision:kvision-fontawesome:$kvisionVersion")
        implementation("io.kvision:kvision-state:$kvisionVersion")
        implementation("io.kvision:kvision-chart:$kvisionVersion")

//        implementation(npm("kafkajs", "1.15.0"))
      }

      val webDir = file("src/main/web")
      resources.srcDir(webDir)
    }
    test {
      dependencies {
        implementation(kotlin("test-js"))
        implementation("io.kvision:kvision-testutils:$kvisionVersion")
      }
    }
  }
}
