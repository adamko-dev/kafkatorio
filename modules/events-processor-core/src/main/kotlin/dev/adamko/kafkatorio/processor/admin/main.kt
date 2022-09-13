package dev.adamko.kafkatorio.processor.admin

import dev.adamko.kafkatorio.processor.config.ApplicationProperties


fun main() {
  val appProps = ApplicationProperties.load()
  val admin = KafkatorioKafkaAdmin(appProps)
  admin.createKafkatorioTopics()
}
