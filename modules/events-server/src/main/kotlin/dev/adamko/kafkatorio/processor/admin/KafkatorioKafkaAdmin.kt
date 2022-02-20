package dev.adamko.kafkatorio.processor.admin

import dev.adamko.kafkatorio.events.schema.FactorioObjectData
import dev.adamko.kafkatorio.events.schema.KafkatorioPacket
import dev.adamko.kafkatorio.processor.KafkatorioTopology
import java.util.concurrent.TimeUnit
import mu.KotlinLogging
import org.apache.kafka.clients.admin.Admin
import org.apache.kafka.clients.admin.CreateTopicsResult
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.admin.TopicListing


object KafkatorioKafkaAdmin {

  private val logger = KotlinLogging.logger {}

  private val kafkaAdmin: Admin =
    Admin.create(
      mapOf(
        "bootstrap.servers" to "http://localhost:9092",
        "client.id" to "warehouse.setup-admin",
      )
    )

  fun createKafkatorioTopics() {

    val currentTopics = currentTopics().map { it.name() }

    val kafkatorioTopics = buildSet {
      KafkatorioPacket.PacketType.values.forEach { packetType ->

        add(KafkatorioTopology.TOPIC_GROUPED_MAP_CHUNKS)
        add(KafkatorioTopology.TOPIC_SRC_SERVER_LOG)

        val base = topicNameBase(packetType)

        when (packetType) {
          KafkatorioPacket.PacketType.EVENT      ->
            FactorioObjectData.ObjectName.values.forEach { objectName ->
              add("$base.${objectName.name}")
            }
          KafkatorioPacket.PacketType.CONFIG     -> add("$base.FactorioConfigurationUpdate")
          KafkatorioPacket.PacketType.PROTOTYPES -> add("$base.all")
          KafkatorioPacket.PacketType.UPDATE     -> add("$base.all")
        }


      }
    }.minus(currentTopics.toSet())

    if (kafkatorioTopics.isNotEmpty()) {

      logger.info("Creating ${kafkatorioTopics.size} topics")
      val result = kafkaAdmin.createTopics { kafkatorioTopics }

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
    numPartitions: Int = 3,
    replicationFactor: Short = 1,
    topicNames: () -> Collection<String>,
  ): CreateTopicsResult =
    createTopics(topicNames()
      .distinct()
      .map {
        NewTopic(it, numPartitions, replicationFactor)
      })

  private fun currentTopics(): MutableCollection<TopicListing> {
    return kafkaAdmin.listTopics()
      .listings()
      .get()
  }

  fun topicNameBase(packetType: KafkatorioPacket.PacketType): String =
    "kafkatorio.${packetType.name}"
}
