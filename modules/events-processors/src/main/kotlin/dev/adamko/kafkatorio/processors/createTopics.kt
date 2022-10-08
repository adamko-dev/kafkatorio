package dev.adamko.kafkatorio.processors

import dev.adamko.kafkatorio.processor.admin.KafkatorioKafkaAdmin
import dev.adamko.kafkatorio.processor.config.ApplicationProperties

fun createTopics(
  appProps: ApplicationProperties = ApplicationProperties.load(),
) {
  val admin = KafkatorioKafkaAdmin(appProps)
  admin.createKafkatorioTopics()
}
