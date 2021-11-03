plugins {
  id("dev.adamko.factoriowebmap.archetype.base")
  kotlin("multiplatform")
}

val projectId: String by project.extra
val buildDir: Directory = layout.buildDirectory.dir(projectId).get()

kotlin {

  //<editor-fold desc="Java">
  jvm {
    compilations.configureEach {
      kotlinOptions {
        jvmTarget = "11"
      }
    }
  }

  sourceSets {
    val jvmMain by getting {
      dependencies {
        api("com.google.protobuf:protobuf-javalite:3.19.1")
        api("com.google.protobuf:protobuf-kotlin-lite:3.19.1")
      }
    }
    val jvmTest by getting {
    }
  }

  //</editor-fold>

  //<editor-fold desc="JS">
  js(IR) {
    browser()
  }

  sourceSets {
    val jsMain by getting {
    }

    val jsTest by getting {
      dependencies {
        implementation(kotlin("test-js"))
      }
    }
  }
  //</editor-fold>

  sourceSets.all {
    languageSettings.apply {
      languageVersion = "1.5"
//      enableLanguageFeature("InlineClasses") // language feature name
      optIn("kotlin.OptIn")
      optIn("kotlin.ExperimentalStdlibApi")
      optIn("kotlin.time.ExperimentalTime")
      optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
      optIn("kotlin.js.ExperimentalJsExport")
//      progressiveMode = true // false by default
    }
  }
}
