import org.gradle.kotlin.dsl.support.expectedKotlinDslPluginsVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `kotlin-dsl`
}

val gradleJvmTarget = "11"

description = "Provide Gradle settings plugins, used to configure base Gradle behaviour"

dependencies {
  // This project should only be providing simple plugins for configuring Gradle without
  // dependencies. Try to avoid setting dependencies here, else the Gradle script classpath might
  // get polluted and messy.

  implementation("org.gradle.kotlin:gradle-kotlin-dsl-plugins:$expectedKotlinDslPluginsVersion")
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")

  // https://github.com/gradle/gradle/issues/22510
  implementation("org.jetbrains.kotlin:kotlin-sam-with-receiver:${libs.versions.kotlin.get()}")

  implementation(platform(libs.kotlin.bom))
  implementation("org.jetbrains.kotlin:kotlin-serialization:${libs.versions.kotlin.get()}")

  implementation(platform(libs.kotlinx.coroutines.bom))
}

kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(gradleJvmTarget))
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
