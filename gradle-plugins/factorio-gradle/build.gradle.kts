plugins {
  id("kafkatorio.conventions.kotlin-dsl")
  id("kafkatorio.conventions.kotlin-serialization")
}


dependencies {
  implementation(platform(libs.kotlin.bom))

  implementation(platform(libs.kotlinx.coroutines.bom))
  implementation(libs.kotlinx.coroutines.core)

  implementation(platform(libs.kotlinxSerialization.bom))
  implementation(libs.kotlinxSerialization.core)
  implementation(libs.kotlinxSerialization.json)

  implementation(platform(libs.okio.bom))
  implementation(libs.okio.core)

  implementation(libs.kotlinx.cli)

  implementation(platform(libs.ktor.bom))
  implementation(libs.ktorClient.auth)
  implementation(libs.ktorClient.contentNegotiation)
  implementation(libs.ktorClient.core)
  implementation(libs.ktorClient.encoding)
  implementation(libs.ktorClient.logging)
  implementation(libs.ktorClient.cio)
  implementation(libs.ktorClient.resources)
  implementation(libs.ktorSerialization.kotlinxJson)

  implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:${libs.versions.kotlin.get()}")
}

gradlePlugin {
  plugins {
    create("factorioMod") {
      displayName = "Factorio Mod"
      id = "dev.adamko.factorio-mod"
      implementationClass = "dev.adamko.gradle.factorio.FactorioModPlugin"
    }
  }
  plugins {
    create("factorioModLibrary") {
      displayName = "Factorio Mod Library"
      id = "dev.adamko.factorio-mod-library"
      implementationClass = "dev.adamko.gradle.factorio.FactorioModLibraryPlugin"
    }
  }
}

kotlin {
  compilerOptions {
    optIn.addAll(
      "kotlin.ExperimentalStdlibApi",
      "kotlin.time.ExperimentalTime",
      "kotlinx.coroutines.ExperimentalCoroutinesApi",
    )
  }
}
