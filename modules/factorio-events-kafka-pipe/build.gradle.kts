import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

plugins {
  id("dev.adamko.factoriowebmap.archetype.base")
}

description =
  """
    Send events from a Factorio server to a Kafka topic 
  """.trimIndent()

val projectId: String by project.extra
