import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `kotlin-dsl`
  kotlin("jvm") version "1.5.31"
}

dependencies {

  val kotlinVersion = "1.5.31"

//  implementation(gradleApi())

//  implementation("org.gradle.kotlin:gradle-kotlin-dsl-plugins:${org.gradle.kotlin.dsl.support.expectedKotlinDslPluginsVersion}")
//  implementation("org.gradle.kotlin:gradle-kotlin-dsl-plugins:${kotlinVersion}")

  implementation(platform("org.jetbrains.kotlin:kotlin-bom:$kotlinVersion"))
  implementation("org.jetbrains.kotlin:kotlin-serialization")

  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")

  implementation("com.github.node-gradle:gradle-node-plugin:3.1.1")

}

tasks.withType<KotlinCompile>().configureEach {

  kotlinOptions {
    jvmTarget = "11"
  }

  kotlinOptions.freeCompilerArgs += listOf(
    "-Xopt-in=kotlin.OptIn",
    "-Xopt-in=kotlin.ExperimentalStdlibApi",
    "-Xopt-in=kotlin.time.ExperimentalTime",
    "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
  )
}