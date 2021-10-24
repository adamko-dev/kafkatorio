
plugins {
  id("dev.adamko.factoriowebmap.archetype.kotlin-jvm")
  application
}

description =
  """
    Receive raw Factorio Events from Kafka and process them into targeted topics or data formats. 
    
    Factorio events -> Mod -> Kafka -> ***Factorio Events Processor*** -> processed events
  """.trimIndent()

val projectId: String by project.extra
val buildDir: Directory = layout.buildDirectory.dir(projectId).get()

@Suppress("UnstableApiUsage")
dependencies {

  implementation("org.apache.kafka:kafka-streams:3.0.0")

  implementation(platform(libs.http4k.bom))
  implementation(libs.http4k.core)
  implementation(libs.http4k.cloudnative)
  implementation(libs.http4k.format.json)
  implementation(libs.http4k.format.yaml)

  implementation(libs.bundles.logging)

}

application {
  mainClass.set("dev.adamko.factorioevents.processor.mainKt")
}
