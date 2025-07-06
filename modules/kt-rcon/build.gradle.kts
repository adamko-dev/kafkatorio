plugins {
  id("kafkatorio.conventions.lang.kotlin-multiplatform")
}

kotlin {
  linuxX64()
  jvm()

  sourceSets {
    commonMain {
      dependencies {
        implementation(project.dependencies.platform(projects.modules.versionsPlatform))

        implementation(libs.kotlinx.coroutines.core)

//  implementation("io.ktor:ktor-network")
      }
    }

    linuxMain {
      languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
    }
  }
}
