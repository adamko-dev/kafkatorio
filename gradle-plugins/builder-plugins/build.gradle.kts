import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("kafkatorio.conventions.kotlin-dsl")
//  `kotlin-dsl`
//  kotlin("jvm") version embeddedKotlinVersion
}

dependencies {
  implementation(platform(libs.kotlin.bom))
  implementation("org.jetbrains.kotlin:kotlin-serialization")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable")
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")

  implementation(platform(libs.kotlinx.coroutines.bom))
  implementation(libs.kotlinx.coroutines.core)

  implementation(platform(libs.kotlinxSerialization.bom))
  implementation(libs.kotlinxSerialization.core)
  implementation(libs.kotlinxSerialization.json)

  implementation("com.github.node-gradle:gradle-node-plugin:${libs.versions.gradleNodePlugin.get()}")

  implementation(platform(libs.kotest.bom))
  implementation("io.kotest:kotest-framework-multiplatform-plugin-gradle")

  implementation("io.kvision:io.kvision.gradle.plugin:${libs.versions.kvision.get()}")

  implementation("net.swiftzer.semver:semver:${libs.versions.semver.get()}")

  implementation(platform(libs.okio.bom))
  implementation(libs.okio.core)

  implementation(libs.kotlinx.cli)
}

//val gradleJvmTarget = "11"
//
//kotlin {
//  jvmToolchain {
//    (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(gradleJvmTarget))
//  }
//}
//
//kotlinDslPluginOptions {
//  jvmTarget.set(gradleJvmTarget)
//}
//
//tasks.withType<KotlinCompile>().configureEach {
//  kotlinOptions {
//    jvmTarget = gradleJvmTarget
//  }
//}
