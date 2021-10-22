import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
  id("dev.adamko.factoriowebmap.archetype.base")
//  kotlin("jvm")
  id("io.kvision") version  "5.4.1"
  kotlin("js")
  kotlin("plugin.serialization") version "1.5.31"
}

group = "${rootProject.group}.server"
version = rootProject.version


val webDir = file("src/main/web")


val kvisionVersion = "5.4.1"

kotlin {
  js {
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

  sourceSets["main"].dependencies {
    implementation("io.kvision:kvision:$kvisionVersion")
    implementation("io.kvision:kvision-bootstrap:$kvisionVersion")
    implementation("io.kvision:kvision-bootstrap-css:$kvisionVersion")
    implementation("io.kvision:kvision-i18n:$kvisionVersion")
  }
  sourceSets["test"].dependencies {
    implementation(kotlin("test-js"))
    implementation("io.kvision:kvision-testutils:$kvisionVersion")
  }
  sourceSets["main"].resources.srcDir(webDir)

}
