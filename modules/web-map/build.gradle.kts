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
//          static = mutableListOf("$buildDir/processedResources/frontend/main")
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

        kvision("kvision")
        kvision("kvision-bootstrap")
        kvision("kvision-bootstrap-css")
        kvision("kvision-bootstrap-icons")
        kvision("kvision-fontawesome")
        kvision("kvision-state")
        kvision("kvision-chart")
        kvision("kvision-maps")
        kvision("kvision-rest")
        kvision("kvision-redux")
        kvision("kvision-state-flow")
        kvision("kvision-routing-navigo-ng")
      }

      resources.srcDir("src/main/web")
    }


    test {
      dependencies {
        implementation(kotlin("test"))

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

geedeecee {
  srcDir.set(layout.projectDirectory.dir("docker"))
}

tasks.dockerComposeEnvUpdate {
  properties(
    "KAFKATORIO_VERSION" to project.version,
    "REGISTRY_HOST" to "dcr.adamko.dev:5000",
  )
}

tasks.assemble { dependsOn(tasks.dockerComposeEnvUpdate) }


val installThemeCss by tasks.registering(Copy::class) {
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


tasks.matching { it.name == "processResources" }.configureEach {
  dependsOn(installThemeCss)
}


val runWebMap by tasks.registering {
  group = rootProject.name

  dependsOn(tasks.matching { it.name == "browserDevelopmentRun" })
}
