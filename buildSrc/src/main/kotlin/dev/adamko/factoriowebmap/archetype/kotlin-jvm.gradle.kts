package dev.adamko.factoriowebmap.archetype

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("dev.adamko.factoriowebmap.archetype.base")
  kotlin("jvm")
}

kotlin {
}

dependencies {
  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
}

tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions.freeCompilerArgs += listOf(
      "-Xopt-in=kotlin.OptIn",
      "-Xopt-in=kotlin.ExperimentalStdlibApi",
      "-Xopt-in=kotlin.time.ExperimentalTime",
  )
}