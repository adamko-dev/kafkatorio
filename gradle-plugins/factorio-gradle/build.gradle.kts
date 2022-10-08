import kafkatorio.conventions.overrideKotlinLanguageVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("kafkatorio.conventions.kotlin-dsl")
  id("kafkatorio.conventions.kotlin-serialization")
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


//val gradleJvmTarget = 11
//
//kotlin {
//  jvmToolchain(gradleJvmTarget)
//}
//
//kotlinDslPluginOptions {
//  jvmTarget.set("$gradleJvmTarget")
//}
//
//tasks.withType<KotlinCompile>().configureEach {
//  kotlinOptions {
//    jvmTarget = "$gradleJvmTarget"
//  }
//}

gradlePlugin {
  plugins {
    create("factorioMod") {
      displayName = "Factorio Mod"
      id = "dev.adamko.factorio-mod"
      implementationClass = "dev.adamko.gradle.factorio.FactorioModPlugin"
    }
  }
}


overrideKotlinLanguageVersion("1.7")

//val gradleKotlinTarget = "1.7"
//
//afterEvaluate {
//  tasks.withType<KotlinCompile>().configureEach {
//    kotlinOptions {
//      apiVersion = gradleKotlinTarget
//      languageVersion = gradleKotlinTarget
//    }
//  }
//}


tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions.freeCompilerArgs += listOf(
    "-opt-in=kotlin.RequiresOptIn",
    "-opt-in=kotlin.ExperimentalStdlibApi",
    "-opt-in=kotlin.time.ExperimentalTime",
    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
  )
}
