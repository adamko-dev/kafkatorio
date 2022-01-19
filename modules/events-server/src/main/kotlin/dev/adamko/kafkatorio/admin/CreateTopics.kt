package dev.adamko.kafkatorio.admin

import dev.adamko.kafkatorio.events.schema.FactorioObjectData
import dev.adamko.kafkatorio.events.schema.KafkatorioPacket
import dev.adamko.kafkatorio.processor.KafkatorioTopology
import java.util.concurrent.TimeUnit
import mu.KotlinLogging
import org.apache.kafka.clients.admin.Admin
import org.apache.kafka.clients.admin.CreateTopicsResult
import org.apache.kafka.clients.admin.NewTopic

private val logger = KotlinLogging.logger {}

fun main() {
  CreateTopics.create()
}

object CreateTopics {

  private val kafkaAdmin: Admin =
    Admin.create(
      mapOf(
        "bootstrap.servers" to "http://localhost:9092",
        "client.id" to "warehouse.setup-admin",
      )
    )

  fun create() {

    KafkatorioPacket.PacketType.values().forEach { packetType ->

      val kafkatorioTopics = buildSet {
//        add(KafkatorioTopology.sourceTopic)
        when (packetType) {
          KafkatorioPacket.PacketType.EVENT      ->
            FactorioObjectData.ObjectName.values().forEach { objectName ->
              add("kafkatorio.${packetType.name}.${objectName.name}")
            }
          KafkatorioPacket.PacketType.CONFIG     ->
            add("kafkatorio.${packetType.name}.FactorioConfigurationUpdate")
          KafkatorioPacket.PacketType.PROTOTYPES ->
            add("kafkatorio.${packetType.name}.all")
        }
      }
      val result = kafkatorioTopics
        .run {
          logger.info("Creating $size topics")
          kafkaAdmin.createTopics { this }
        }

      // wait for all brokers to become aware of the created topics
      Thread.sleep(TimeUnit.SECONDS.toMillis(5))

      result.values().forEach {
        logger.info("Created topic: ${it.key} ${it.value.get()}")
      }

      Thread.sleep(TimeUnit.SECONDS.toMillis(5))

      logger.info("Listing topics")
      kafkaAdmin.listTopics()
        .listings()
        .get()
        .forEach {
          logger.info("Topic: $it")
        }
    }
  }

  private fun Admin.createTopics(
    numPartitions: Int = 1,
    replicationFactor: Short = 1,
    topicNames: () -> Collection<String>,
  ): CreateTopicsResult =
    createTopics(topicNames().distinct().map {
      NewTopic(it, numPartitions, replicationFactor)
    })
}