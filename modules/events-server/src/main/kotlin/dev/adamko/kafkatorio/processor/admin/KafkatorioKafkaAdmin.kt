package dev.adamko.kafkatorio.processor.admin

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
        "client.id" to "kafkatorio.setup-admin",
      )
    )

  fun createKafkatorioTopics() {

    val currentTopics = currentTopics().map { it.name() }

    val kafkatorioTopics = allTopics() - currentTopics.toSet()

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
    createTopics(
      topicNames()
        .distinct()
        .map {
          NewTopic(it, numPartitions, replicationFactor)
//            .configs(mapOf("cleanup.policy" to "compact"))
        }
    )

  private fun currentTopics(): MutableCollection<TopicListing> {
    return kafkaAdmin.listTopics()
      .listings()
      .get()
  }

//  fun topicNameBase(packetType: KafkatorioPacketDataType): String =
//    "kafkatorio.${packetType.name}"
}
