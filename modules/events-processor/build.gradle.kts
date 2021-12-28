import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("dev.adamko.kafkatorio.lang.kotlin-jvm")
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

  implementation(libs.kotlinx.coroutines)

  implementation(platform(libs.http4k.bom))
  implementation(libs.bundles.http4k)

  implementation(libs.http4k.format.kotlinx)

  implementation(libs.bundles.logging)
}

application {
  mainClass.set("dev.adamko.factorioevents.processor.mainKt")
}

tasks.withType<KotlinCompile> {
  kotlinOptions.freeCompilerArgs += listOf(
    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
  )
}
