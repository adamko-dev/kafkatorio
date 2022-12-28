package kafkatorio.conventions

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.gradle.kotlin.kotlin-dsl")
}

val gradleJvmTarget = 11

//kotlin {
//  jvmToolchain(gradleJvmTarget)
//}

kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(gradleJvmTarget))
  }
}

kotlinDslPluginOptions {
  jvmTarget.set("$gradleJvmTarget")
}

tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions {
    jvmTarget = "$gradleJvmTarget"
  }
}
