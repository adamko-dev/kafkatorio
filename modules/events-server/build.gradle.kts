import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  dev.adamko.kafkatorio.lang.`kotlin-jvm`
  kotlin("plugin.serialization")
  application
}

description = """
    Receive raw Factorio Events from Kafka and process them into targeted topics or data formats. 
    
    Factorio events -> Mod -> Kafka -> ***Factorio Events Processor*** -> processed events
  """.trimIndent()

val projectId: String by project.extra

@Suppress("UnstableApiUsage") // platform + version-catalog is incubating
dependencies {

  implementation("org.apache.kafka:kafka-streams:3.0.0")

  implementation(projects.modules.eventsSchema)

//  implementation("com.ionspin.kotlin:bignum:0.3.3")

  implementation(platform(libs.kotlinx.serialization.bom))
  implementation(libs.kotlinx.serialization.core)
  implementation(libs.kotlinx.serialization.json)
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf")

  implementation(libs.kotlinx.coroutines)

  implementation(platform(libs.http4k.bom))
  implementation(libs.bundles.http4k)
  implementation(libs.http4k.format.kotlinx)

  implementation(libs.bundles.logging)

  implementation("com.sksamuel.scrimage:scrimage-core:4.0.24")

  val kotkaVersion = "0.0.8"
  implementation("com.github.adamko-dev.kotka-streams:kotka-streams:$kotkaVersion") {
    isChanging = true
  }
//  val kotkaVersion = "main-SNAPSHOT"
//  implementation("dev.adamko.kotka:kotka-streams:$kotkaVersion") {
//    isChanging = true
//  }

}

application {
  mainClass.set("dev.adamko.kafkatorio.processor.EventProcessorKt")
}

tasks.withType<KotlinCompile> {
  kotlinOptions.freeCompilerArgs += listOf(
    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
  )
}
