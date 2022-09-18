import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `kotlin-dsl`
  kotlin("jvm") version embeddedKotlinVersion
}

dependencies {
  implementation(platform(libs.kotlin.bom))

  implementation(platform(libs.kotlinx.coroutines.bom))
  implementation(libs.kotlinx.coroutines.core)

  implementation(platform(libs.kotlinxSerialization.bom))
  implementation(libs.kotlinxSerialization.core)
  implementation(libs.kotlinxSerialization.json)

  implementation(platform(libs.okio.bom))
  implementation(libs.okio.core)

  implementation(libs.kotlinx.cli)

  implementation(platform(libs.ktor.bom))
  implementation(libs.ktorClient.auth)
  implementation(libs.ktorClient.contentNegotiation)
  implementation(libs.ktorClient.core)
  implementation(libs.ktorClient.encoding)
  implementation(libs.ktorClient.logging)
  implementation(libs.ktorClient.cio)
  implementation(libs.ktorClient.resources)
  implementation(libs.ktorSerialization.kotlinxJson)
}


val gradleJvmTarget = "11"

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
