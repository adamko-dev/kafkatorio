package dev.adamko.kafkatorio.lang

import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("dev.adamko.kafkatorio.base")
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

  //</editor-fold>

  //<editor-fold desc="Test">

  val junitVersion = "5.8.2"
  testImplementation(platform("org.junit:junit-bom:$junitVersion"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher") {
    because("Only needed to run tests in a version of IntelliJ IDEA that bundles older versions")
  }

  val kotestVersion = "5.0.3"
  testImplementation(platform("io.kotest:kotest-bom:$kotestVersion"))
  testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
  testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
  testImplementation("io.kotest:kotest-property:$kotestVersion")
  testImplementation("io.kotest:kotest-assertions-json:$kotestVersion")

  testImplementation("io.mockk:mockk:1.12.0")

  //</editor-fold>

}

tasks.withType<KotlinCompile>().configureEach {

  kotlinOptions {
    jvmTarget = "11"
    apiVersion = "1.6"
    languageVersion = "1.6"
  }

  kotlinOptions.freeCompilerArgs += listOf(
    "-opt-in=kotlin.RequiresOptIn",
    "-opt-in=kotlin.ExperimentalStdlibApi",
    "-opt-in=kotlin.time.ExperimentalTime",
//    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
    "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
  )
}

tasks.compileTestKotlin {
  kotlinOptions.freeCompilerArgs += "-opt-in=io.kotest.common.ExperimentalKotest"
}

tasks.withType<Test> {
  useJUnitPlatform()
}
