package kafkatorio.conventions.lang

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  id("kafkatorio.conventions.base")
  kotlin("jvm")
}

//val projectKotlinTarget = "1.7"
val projectJvmTarget = "11"


dependencies {
  testImplementation("org.junit.jupiter:junit-jupiter")

  testImplementation("io.kotest:kotest-runner-junit5")
  testImplementation("io.kotest:kotest-assertions-core")
  testImplementation("io.kotest:kotest-property")
  testImplementation("io.kotest:kotest-assertions-json")

  testImplementation("io.mockk:mockk")
}

kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(projectJvmTarget))
  }

  compilerOptions {
    jvmTarget = JvmTarget.fromTarget(projectJvmTarget)
//    apiVersion = org.jetbrains.kotlin.gradle.dsl.KotlinVersion.fromVersion(projectKotlinTarget)
//    languageVersion = org.jetbrains.kotlin.gradle.dsl.KotlinVersion.fromVersion(projectKotlinTarget)

    optIn.addAll(
      //"kotlin.RequiresOptIn",
      "kotlin.ExperimentalStdlibApi",
      "kotlin.time.ExperimentalTime",
      //"kotlinx.coroutines.ExperimentalCoroutinesApi",
      //"kotlinx.serialization.ExperimentalSerializationApi",
    )
  }
}


tasks.compileTestKotlin {
  compilerOptions {
    optIn.addAll(
      "io.kotest.common.ExperimentalKotest",
    )
  }
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
}


plugins.withType<ApplicationPlugin>().configureEach {
  project.extensions.getByType<JavaApplication>().apply {
    applicationDefaultJvmArgs += listOf("-Dkotlinx.coroutines.debug")
  }
}
