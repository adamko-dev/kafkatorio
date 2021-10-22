plugins {
  id("dev.adamko.factoriowebmap.archetype.kotlin-jvm")
}

description =
  """
    Receive raw Factorio Events from Kafka and process them into targeted topics or data formats. 
    
    Factorio events -> Mod -> Kafka -> ***Factorio Events Processor*** -> processed events
  """.trimIndent()

val projectId: String by project.extra
val buildDir: Directory = layout.buildDirectory.dir(projectId).get()


