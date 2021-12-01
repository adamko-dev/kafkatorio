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

@Suppress("UnstableApiUsage")
dependencies {

  implementation("org.apache.kafka:kafka-streams:3.0.0")

  implementation(projects.modules.eventsSchema)

//  implementation("com.ionspin.kotlin:bignum:0.3.3")

  implementation(enforcedPlatform(libs.kotlinx.serialization.bom))
  implementation(libs.kotlinx.serialization.core)
  implementation(libs.kotlinx.serialization.json)

  implementation(platform(libs.http4k.bom))
//  implementation(libs.http4k.core)
  implementation(libs.http4k.cloudnative)
//  implementation(libs.http4k.format.json)
  implementation(libs.http4k.format.yaml) {
    because("parsing yaml properties files")
  }

  implementation(libs.http4k.format.kotlinx)

  implementation(libs.bundles.logging)

}

application {
  mainClass.set("dev.adamko.factorioevents.processor.mainKt")
}
