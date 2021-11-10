import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  idea
  `kotlin-dsl`
  kotlin("jvm") version "1.5.31"
  `java-gradle-plugin`
}

gradlePlugin {
  plugins {
    create("tstl") {
      id = "dev.adamko.tstl"
      description =
        "Gradle plugin for TypescriptToLua. Requires plugin `com.github.node-gradle.node`."
      implementationClass = "dev.adamko.gradle.tstl.TstlPlugin"
    }
    create("json-kotlin-schema-gen") {
      id = "dev.adamko.json-kotlin-schema-gen"
      description =
        "Gradle plugin for 'json-kotlin-schema-codegen' - Code generation for JSON Schema (Draft 07) https://github.com/pwall567/json-kotlin-schema-codegen"
      implementationClass = "dev.adamko.gradle.jsonkotlinschemagen.JsonKotlinSchemaGenPlugin"
    }
  }
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

  implementation("net.pwall.json:json-kotlin-schema-codegen:0.53")
}

val projectJvmTarget = "11"

tasks.withType<KotlinCompile>().configureEach {

  kotlinOptions {
    jvmTarget = projectJvmTarget
    apiVersion = "1.5"
    languageVersion = "1.5"
  }

  kotlinOptions.freeCompilerArgs += listOf(
    "-Xopt-in=kotlin.OptIn",
    "-Xopt-in=kotlin.RequiresOptIn",
    "-Xopt-in=kotlin.ExperimentalStdlibApi",
    "-Xopt-in=kotlin.time.ExperimentalTime",
    "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
    "-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi",
  )
}


kotlin {
  jvmToolchain {
    (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(projectJvmTarget))
  }

  kotlinDslPluginOptions {
    jvmTarget.set(projectJvmTarget)
  }
}

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
  }
}
