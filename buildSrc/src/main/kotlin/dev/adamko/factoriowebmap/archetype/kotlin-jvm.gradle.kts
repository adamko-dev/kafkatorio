package dev.adamko.factoriowebmap.archetype

plugins {
  id("dev.adamko.factoriowebmap.archetype.base")
  kotlin("jvm")
}

kotlin {
}

dependencies {
  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
}
