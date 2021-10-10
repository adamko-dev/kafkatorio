import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

plugins {
  kotlin("multiplatform") version "1.5.31"
}

group = "dev.adamko.factoriowebmap"
version = "1.0-SNAPSHOT"

kotlin {

  //<editor-fold desc="Native">
  val hostOs = System.getProperty("os.name")
  val isMingwX64 = hostOs.startsWith("Windows")
  val nativeTarget = when {
    hostOs == "Mac OS X" -> macosX64("native")
    hostOs == "Linux"    -> linuxX64("native")
    isMingwX64           -> mingwX64("native")
    else                 -> throw GradleException(
        "Host OS '$hostOs' is not supported in Kotlin/Native.")
  }

  nativeTarget.apply {
    binaries {
      executable {
        entryPoint = "main"
      }
      sharedLib {
        baseName = if (isMingwX64) "libnative" else "native"
      }
    }
  }
  sourceSets {
    val nativeMain by getting {
      dependencies {
        implementation("com.soywiz.korlibs.luak:luak:2.4.3")
      }
    }
    val nativeTest by getting {
      dependencies {
        implementation(kotlin("test"))

      }
    }
  }
  //</editor-fold>

  //<editor-fold desc="JS">
  js(IR) {
    binaries.executable()
    browser {
      commonWebpackConfig {
        cssSupport.enabled = true
      }
    }

    useCommonJs()
    nodejs()

    compilations["main"].apply {
      packageJson {
        customField(
            "scripts",
            mapOf(
                "build" to "tstl",
                "dev" to "tstl --watch",
            )
        )
      }
    }

  }

  sourceSets {

    val jsMain by getting {
      kotlin.srcDir("externals02")
//      kotlin.srcDir("externals")

      dependencies {

        val kotlinWrappersVersion = "0.0.1-pre.254-kotlin-1.5.31"
        implementation(
            project.dependencies.enforcedPlatform(
                "org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:${kotlinWrappersVersion}"
            )
        )

        implementation("org.jetbrains.kotlin-wrappers:kotlin-react")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
        implementation(npm("react", "17.0.2"))
        implementation(npm("react-dom", "17.0.2"))

//        implementation("org.jetbrains.kotlinx:kotlinx-nodejs:0.0.7") {
//          because("https://github.com/Kotlin/kotlinx-nodejs")
//        }

        val generateExternals = false
        implementation(npm("typescript-to-lua", "1.0.1", generateExternals = generateExternals))
        implementation(npm("typed-factorio", "0.7.1", generateExternals = generateExternals))
        implementation(npm("lua-types", "2.11.0", generateExternals = generateExternals))
        implementation(npm("typescript", "4.4.3", generateExternals = generateExternals))
      }
    }

    val jsTest by getting {
      dependencies {
        implementation(kotlin("test-js"))
      }
    }
  }
  //</editor-fold>

}

configure<NodeJsRootExtension> {
  versions.dukat.version = "0.5.8-rc.4-dev.20211001"
//  versions.dukat.version = "0.5.8-rc.4"
}

tasks.wrapper {
  gradleVersion = "7.2"
  distributionType = Wrapper.DistributionType.ALL
}
