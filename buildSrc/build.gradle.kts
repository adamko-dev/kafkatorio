import kafkatorio.conventions.overrideKotlinLanguageVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("kafkatorio.conventions.kotlin-dsl")
//  idea
//  `kotlin-dsl`
//  kotlin("jvm") //version  "1.7.20" // embeddedKotlinVersion
  kotlin("plugin.serialization") version  embeddedKotlinVersion
//  kotlin("plugin.serialization") version "1.7.20"
}

//val gradleJvmTarget = "11"
//val gradleKotlinTarget = "1.6"


dependencies {
  implementation(platform(libs.kotlin.bom))
  implementation("org.jetbrains.kotlin:kotlin-serialization")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable")
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")

  implementation(platform(libs.kotlinx.coroutines.bom))
  implementation(libs.kotlinx.coroutines.core)

  implementation(platform(libs.kotlinxSerialization.bom))
  implementation(libs.kotlinxSerialization.core)
  implementation(libs.kotlinxSerialization.json)
//  implementation("com.charleskorn.kaml:kaml:0.37.0")

  implementation("com.github.node-gradle:gradle-node-plugin:${libs.versions.gradleNodePlugin.get()}")

  implementation(platform(libs.kotest.bom))
  implementation("io.kotest:kotest-framework-multiplatform-plugin-gradle")

  implementation("io.kvision:io.kvision.gradle.plugin:${libs.versions.kvision.get()}")

  implementation("net.swiftzer.semver:semver:${libs.versions.semver.get()}")

  implementation(platform(libs.okio.bom))
  implementation(libs.okio.core)

  implementation(gradleKotlinDsl())

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

  // https://github.com/avast/gradle-docker-compose-plugin
//  implementation("com.avast.gradle:gradle-docker-compose-plugin:${Versions.gradleDockerComposePlugin}")

  // https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
  // https://youtrack.jetbrains.com/issue/IDEA-262280#focus=Comments-27-5397040.0-0
//  implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}


//tasks.compileKotlin {
//  kotlinOptions {
//    // needed by IntelliJ? Even though below does the same?
//    languageVersion = "1.6"
//  }
//}

tasks.withType<KotlinCompile>().configureEach {

  kotlinOptions {
//    jvmTarget = gradleJvmTarget
//    apiVersion = gradleKotlinTarget
//    languageVersion = gradleKotlinTarget
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


overrideKotlinLanguageVersion("1.6")
//afterEvaluate {
//  tasks.withType<KotlinCompile>().configureEach {
//    kotlinOptions {
//      apiVersion = gradleKotlinTarget
//      languageVersion = gradleKotlinTarget
//    }
//  }
//}

//kotlin {
//  jvmToolchain {
//    (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(gradleJvmTarget))
//  }
//}
//
//kotlinDslPluginOptions {
//  jvmTarget.set(gradleJvmTarget)
//}

//idea {
//  module {
//    isDownloadSources = true
//    isDownloadJavadoc = true
//  }
//}
