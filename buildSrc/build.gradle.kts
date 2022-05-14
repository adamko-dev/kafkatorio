import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  idea
  `kotlin-dsl`
  kotlin("jvm") version "1.6.21"
  `project-report`
}

val gradleJvmTarget = "11"
val gradleKotlinTarget = "1.6"


dependencies {

  implementation(enforcedPlatform("org.jetbrains.kotlin:kotlin-bom:${libs.versions.kotlin.get()}"))
  implementation("org.jetbrains.kotlin:kotlin-serialization")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable")
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")

  implementation(platform("org.jetbrains.kotlinx:kotlinx-serialization-bom:${libs.versions.kotlinx.serialization.get()}"))
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-core")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")
//  implementation("com.charleskorn.kaml:kaml:0.37.0")

  implementation("com.github.node-gradle:gradle-node-plugin:${libs.versions.gradleNodePlugin.get()}")

  implementation("io.kotest:kotest-framework-multiplatform-plugin-gradle:${libs.versions.kotest.get()}")

  implementation("io.kvision:io.kvision.gradle.plugin:${libs.versions.kvision.get()}")

  implementation("net.swiftzer.semver:semver:${libs.versions.semver.get()}")

  // https://github.com/avast/gradle-docker-compose-plugin
//  implementation("com.avast.gradle:gradle-docker-compose-plugin:${Versions.gradleDockerComposePlugin}")

  implementation(platform("org.http4k:http4k-bom:${libs.versions.http4k.get()}"))
  implementation("org.http4k:http4k-core")
  implementation("org.http4k:http4k-client-okhttp")


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
    jvmTarget = gradleJvmTarget
    apiVersion = gradleKotlinTarget
    languageVersion = gradleKotlinTarget
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
    (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(gradleJvmTarget))
  }

  kotlinDslPluginOptions {
    jvmTarget.set(gradleJvmTarget)
  }
}

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
  }
}
