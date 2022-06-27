import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension


plugins {
  dev.adamko.kafkatorio.lang.`kotlin-js`
//  dev.adamko.kafkatorio.lang.`kotlin-multiplatform`
  id("io.kvision")
  kotlin("plugin.serialization")
}


kvision {
  enableHiddenKotlinJsStore.set(false)
}


rootProject.extensions.configure<NodeJsRootExtension> {
  // https://github.com/rjaros/kvision/issues/410
  versions.webpackCli.version = "4.10.0"
  versions.webpackDevServer.version = "4.9.2"
}

rootProject.extensions.configure<YarnRootExtension> {
  resolution("http-proxy-middleware", "^2.0.6")
}

//rootProject.afterEvaluate {
//  extensions.configure<NodeJsRootExtension> {
//    // https://github.com/rjaros/kvision/issues/410
//    versions.webpackCli.version = "4.10.0"
//  }
//}

afterEvaluate {
  rootProject.extensions.configure<NodeJsRootExtension> {
    // https://github.com/rjaros/kvision/issues/410
    versions.webpackCli.version = "4.10.0"
    versions.webpackDevServer.version = "4.9.2"
  }

  rootProject.extensions.configure<YarnRootExtension> {
    resolution("http-proxy-middleware", "^2.0.6")
  }
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
            "/tiles" to mapOf(
              "target" to "http://localhost:12080",
              "secure" to false,
              "changeOrigin" to true,
            ),
            "/ws/foo" to mapOf(
              "target" to "ws://localhost:12080",
//              "secure" to false,
              "ws" to true,
//              "changeOrigin" to true,
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
//    compilations["main"].packageJson {
//      this.devDependencies["webpack-dev-server"] = "4.9.1"
//    }
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
        implementation(dependencies.platform(libs.kotlinx.coroutines.bom))
        implementation(dependencies.platform(libs.kotlin.jsWrappers.bom))

        implementation(libs.kotlinx.coroutines.core)

        implementation(projects.modules.eventsSchema)

        implementation(libs.kotlinx.html)

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

        implementation(devNpm("http-proxy-middleware", "^2.0.6"))
        implementation(dependencies.platform(devNpm("http-proxy-middleware", "^2.0.6")))

//        implementation(dependencies.platform(npm("follow-redirects", "^1.14.8")))
//        implementation(dependencies.platform(npm("nanoid", "^3.1.31")))
//        implementation(dependencies.platform(npm("minimist", "^1.2.6")))
//        implementation(dependencies.platform(npm("async", "^2.6.4")))
//        implementation(dependencies.platform(npm("node-forge", "^1.3.0")))
//        implementation(dependencies.platform(npm("socket.io", "^4.5.1")))
//        implementation(dependencies.platform(devNpm("http-proxy-middleware", "^3.0.0-beta.0")))
//        implementation(dependencies.platform(devNpm("webpack-dev-server", "4.9.1")))
      }

      resources.srcDir("src/main/web")
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

////// https://youtrack.jetbrains.com/issue/KT-42420
//afterEvaluate {
//  yarn {
////    resolution("mocha", "9.2.2")
////    resolution("follow-redirects", "1.14.8")
////    resolution("nanoid", "3.1.31")
////    resolution("minimist", "1.2.6")
////    resolution("async", "2.6.4")
////    resolution("node-forge", "1.3.0")
////    resolution("socket.io", "^4.5.1")
////    resolution("http-proxy-middleware", "^3.0.0-beta.0")
//    resolution("webpack-dev-server", "4.9.1")
//  }
//}


//evaluationDependsOn(rootProject.path)
//evaluationDependsOn(projects.modules.eventsSchema.dependencyProject.path)


//// stop warnings when building
//tasks.configureEach {
//  when (name) {
//    "productionExecutableCompileSync"          ->
//      dependsOn(taskProvider("compileProductionExecutableKotlinJs"))
//    "testTestDevelopmentExecutableCompileSync" ->
//      dependsOn(taskProvider("compileTestDevelopmentExecutableKotlinJs"))
////    "jsTestTestDevelopmentExecutableCompileSync"       ->
////      dependsOn(taskProvider("compileTestDevelopmentExecutableKotlinFrontend"))
////    "jsProductionExecutableCompileSync"                ->
////      dependsOn(taskProvider("compileProductionExecutableKotlinFrontend"))
//    "browserDevelopmentRun"                    ->
//      dependsOn(taskProvider("browserDevelopmentWebpack"))
//    "browserDevelopmentWebpack"                ->
//      dependsOn(taskProvider("assemble"))
//    "browserProductionWebpack"                 ->
//      dependsOn(taskProvider("developmentExecutableCompileSync"))
//  }
//}
