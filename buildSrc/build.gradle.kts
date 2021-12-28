import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  idea
  `kotlin-dsl`
  kotlin("jvm") version "1.6.10"
  `project-report`
}

dependencies {

  val kotlinVersion = "1.6.10"
  implementation(enforcedPlatform("org.jetbrains.kotlin:kotlin-bom:$kotlinVersion"))
  implementation("org.jetbrains.kotlin:kotlin-serialization")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")

  val kotlinXSerializationVersion = "1.3.2"
  implementation(enforcedPlatform("org.jetbrains.kotlinx:kotlinx-serialization-bom:$kotlinXSerializationVersion"))
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-core")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")
//  implementation("com.charleskorn.kaml:kaml:0.37.0")

  implementation("com.github.node-gradle:gradle-node-plugin:3.1.1")

  implementation("net.swiftzer.semver:semver:1.1.2")

  // https://github.com/avast/gradle-docker-compose-plugin
  implementation("com.avast.gradle:gradle-docker-compose-plugin:0.14.12")
}

val projectJvmTarget = "11"

tasks.withType<KotlinCompile>().configureEach {

  kotlinOptions {
    jvmTarget = projectJvmTarget
    apiVersion = "1.6"
    languageVersion = "1.6"
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
    (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(projectJvmTarget))
  }

  kotlinDslPluginOptions {
    jvmTarget.set(projectJvmTarget)
  }
}

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
  }
}

//tasks.assemble { dependsOn(tasks.htmlDependencyReport) }
