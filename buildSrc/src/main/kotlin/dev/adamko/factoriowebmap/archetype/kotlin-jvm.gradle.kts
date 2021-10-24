package dev.adamko.factoriowebmap.archetype

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("dev.adamko.factoriowebmap.archetype.base")
  kotlin("jvm")
}

kotlin {
  jvmToolchain {
    (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(11))
  }
}

dependencies {

  //<editor-fold desc="Main">

  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
  implementation(kotlin("stdlib"))

  //</editor-fold>

  //<editor-fold desc="Test">

  val junitVersion = "5.8.1"
  testImplementation(enforcedPlatform("org.junit:junit-bom:$junitVersion"))
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")

  val kotestVersion = "4.6.3"
  testImplementation(enforcedPlatform("io.kotest:kotest-bom:$kotestVersion"))
  testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
  testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
  testImplementation("io.kotest:kotest-property-jvm:$kotestVersion")
  testImplementation("io.kotest:kotest-assertions-json:$kotestVersion")
  testImplementation("io.mockk:mockk:1.12.0")
  //</editor-fold>

}

tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions.freeCompilerArgs += listOf(
    "-Xopt-in=kotlin.OptIn",
    "-Xopt-in=kotlin.ExperimentalStdlibApi",
    "-Xopt-in=kotlin.time.ExperimentalTime",
    "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
  )
}

tasks.compileTestKotlin {
  kotlinOptions.freeCompilerArgs += "-Xopt-in=io.kotest.common.ExperimentalKotest"
}