package dev.adamko.kafkatorio.lang

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("dev.adamko.kafkatorio.base")
  kotlin("jvm")
}

val projectKotlinTarget = "1.6"
val projectJvmTarget = "11"


dependencies {

//  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

//  testImplementation(platform("org.junit:junit-bom:${Versions.junit}"))
  testImplementation("org.junit.jupiter:junit-jupiter")
//  testRuntimeOnly("org.junit.platform:junit-platform-launcher") {
//    because("Only needed to run tests in a version of IntelliJ IDEA that bundles older versions")
//  }

//  testImplementation(platform("io.kotest:kotest-bom:${Versions.kotest}"))
  testImplementation("io.kotest:kotest-runner-junit5")
  testImplementation("io.kotest:kotest-assertions-core")
  testImplementation("io.kotest:kotest-property")
  testImplementation("io.kotest:kotest-assertions-json")

  testImplementation("io.mockk:mockk")
}

tasks.withType<KotlinCompile>().configureEach {

  kotlinOptions {
    jvmTarget = projectJvmTarget
    apiVersion = projectKotlinTarget
    languageVersion = projectKotlinTarget
  }

  kotlinOptions.freeCompilerArgs += listOf(
//    "-Xcontext-receivers",
    "-opt-in=kotlin.RequiresOptIn",
    "-opt-in=kotlin.ExperimentalStdlibApi",
    "-opt-in=kotlin.time.ExperimentalTime",
//    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
//    "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
  )
}

tasks.compileTestKotlin {
  kotlinOptions.freeCompilerArgs += "-opt-in=io.kotest.common.ExperimentalKotest"
}

kotlin {
  jvmToolchain {
    (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(projectJvmTarget))
  }
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
}
