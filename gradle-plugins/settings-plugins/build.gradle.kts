import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `kotlin-dsl`
  kotlin("jvm") version embeddedKotlinVersion
}

val gradleJvmTarget = "11"

dependencies {
  // This project should only be providing simple plugins for configuring Gradle without
  // dependencies. Try to avoid setting dependencies here, else the Gradle script classpath might
  // get polluted and messy.
}

kotlin {
  jvmToolchain {
    (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(gradleJvmTarget))
  }
}

kotlinDslPluginOptions {
  jvmTarget.set(gradleJvmTarget)
}

tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions {
    jvmTarget = gradleJvmTarget
  }
}
