import dev.adamko.kafkatorio.task.FactorioModPublishTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  dev.adamko.kafkatorio.lang.`kotlin-jvm`
  kotlin("plugin.serialization")
  `java-gradle-plugin`
}


dependencies {
  implementation(platform(projects.modules.versionsPlatform))

  implementation(gradleKotlinDsl())

  implementation(projects.modules.eventsServer)
  implementation(projects.modules.eventsMod)

  implementation(libs.kotlinx.serialization.core)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.kotlinx.cli)

  implementation(libs.kotlinx.coroutines.core)

//  implementation(libs.bundles.http4k)
//  implementation(libs.http4k.format.kotlinx)

//  implementation(libs.skrapeit)

  implementation(libs.okio.core)

  implementation(libs.semver)

  implementation(libs.ktor.clientAuth)
  implementation(libs.ktor.clientContentNegotiation)
  implementation(libs.ktor.clientCore)
  implementation(libs.ktor.clientEncoding)
  implementation(libs.ktor.clientLogging)
  implementation(libs.ktor.clientCIO)
  implementation(libs.ktor.clientResources)
  implementation(libs.ktor.serializationKotlinxJson)
}

tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions.freeCompilerArgs += listOf(
    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
    "-opt-in=kotlinx.coroutines.FlowPreview",
  )
}

val factorioModPublish by tasks.registering(FactorioModPublishTask::class) {
  mainClass.set("dev.adamko.kafkatorio.ModUploaderKt")


}
