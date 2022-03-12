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

  const val kotlin = "1.6.10"

  const val gradleNodePlugin = "3.2.1"
  const val gradleDockerComposePlugin = "0.15.1"

  const val kotlinXSerialization = "1.3.2"
  const val kvision = "5.8.2"
  const val semver = "1.1.2"
  const val http4k = "4.19.1.0"

  const val kotest = "5.1.0"
}


dependencies {

  implementation(enforcedPlatform("org.jetbrains.kotlin:kotlin-bom:${Versions.kotlin}"))
  implementation("org.jetbrains.kotlin:kotlin-serialization")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")

  implementation(platform("org.jetbrains.kotlinx:kotlinx-serialization-bom:${Versions.kotlinXSerialization}"))
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-core")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")
//  implementation("com.charleskorn.kaml:kaml:0.37.0")

  implementation("com.github.node-gradle:gradle-node-plugin:${Versions.gradleNodePlugin}")

  implementation("io.kotest:kotest-framework-multiplatform-plugin-gradle:${Versions.kotest}")

  implementation("io.kvision:io.kvision.gradle.plugin:${Versions.kvision}")

  implementation("net.swiftzer.semver:semver:${Versions.semver}")

  // https://github.com/avast/gradle-docker-compose-plugin
  implementation("com.avast.gradle:gradle-docker-compose-plugin:${Versions.gradleDockerComposePlugin}")

  implementation(platform("org.http4k:http4k-bom:${Versions.http4k}"))
  implementation("org.http4k:http4k-core")
  implementation("org.http4k:http4k-client-okhttp")


  // https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
  // https://youtrack.jetbrains.com/issue/IDEA-262280#focus=Comments-27-5397040.0-0
//  implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}


tasks.withType<KotlinCompile>().configureEach {

  kotlinOptions {
    jvmTarget = Versions.jvmTarget
    apiVersion = Versions.kotlinTarget
    languageVersion = Versions.kotlinTarget
  }

  kotlinOptions.freeCompilerArgs += listOf(
//    "-Xcontext-receivers",
    "-opt-in=kotlin.RequiresOptIn",
    "-opt-in=kotlin.ExperimentalStdlibApi",
    "-opt-in=kotlin.time.ExperimentalTime",
//    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
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
