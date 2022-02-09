import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  idea
  `kotlin-dsl`
  kotlin("jvm") version "1.6.10"
  `project-report`
}


object Versions {
  const val jvmTarget = "11"
  const val kotlinTarget = "1.6"

  const val gradleNodePlugin = "3.2.0"
  const val gradleDockerComposePlugin = "0.15.0"

  const val kotlin = "1.6.10"
  const val kotlinXSerialization = "1.3.2"
  const val kvision = "5.8.1"
  const val semver = "1.1.2"

  const val kotest = "5.1.0"
}


dependencies {

  implementation(enforcedPlatform("org.jetbrains.kotlin:kotlin-bom:${Versions.kotlin}"))
  implementation("org.jetbrains.kotlin:kotlin-serialization")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")

  implementation(enforcedPlatform("org.jetbrains.kotlinx:kotlinx-serialization-bom:${Versions.kotlinXSerialization}"))
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-core")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")
//  implementation("com.charleskorn.kaml:kaml:0.37.0")

  implementation("com.github.node-gradle:gradle-node-plugin:${Versions.gradleNodePlugin}")

  implementation("io.kotest:kotest-framework-multiplatform-plugin-gradle:${Versions.kotest}")

  implementation("io.kvision:io.kvision.gradle.plugin:${Versions.kvision}")

  implementation("net.swiftzer.semver:semver:${Versions.semver}")

  // https://github.com/avast/gradle-docker-compose-plugin
  implementation("com.avast.gradle:gradle-docker-compose-plugin:${Versions.gradleDockerComposePlugin}")
}


tasks.withType<KotlinCompile>().configureEach {

  kotlinOptions {
    jvmTarget = Versions.jvmTarget
    apiVersion = Versions.kotlinTarget
    languageVersion = Versions.kotlinTarget
  }

  kotlinOptions.freeCompilerArgs += listOf(
    "-opt-in=kotlin.RequiresOptIn",
    "-opt-in=kotlin.ExperimentalStdlibApi",
    "-opt-in=kotlin.time.ExperimentalTime",
    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
    "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
  )
}

kotlin {
  jvmToolchain {
    (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(Versions.jvmTarget))
  }

  kotlinDslPluginOptions {
    jvmTarget.set(Versions.jvmTarget)
  }
}

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
  }
}
