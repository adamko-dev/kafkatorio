package dev.adamko.kafkatorio.extensions.kafkastreams;

/**
 * A definition for all topics that are created in Kafka.
 *
 * These can be used to create KTopics, KTables, or GlobalKTables.
 */
interface TopicDefinition<K, V> {

  /** unique kafka processor id */
  val pid: String
    get() = this::class.simpleName!!

  /**
   * Unique name for each [TopicDefinition]
   *
   * https://devshawn.com/blog/apache-kafka-topic-naming-conventions/
   *
   * https://www.xeotek.com/topic-naming-conventions-how-do-i-name-my-topics-5-recommendations-with-examples/
   *
   * Domain: warehouse
   * Operation: stock-management
   * Data type:
   *   - cdc=all instances of a specific object/thing/item,
   *   - cmd=command (mutation),
   *   - fct=fact (information (complete or partial)
   * about an event at specific point in time)
   * - sys=internal system topic - not meant for external use
   * - tbl=a global table store (must be paired with another topic)
   * Description: the data type
   *
   * Version: stored in message header
   *
   * ```
   * warehouse.stock-management.sts/cdc/cmd.warehouse-item
   * ```
   */
  val topicName: String

  val serdes: KeyValueSerdes<K, V>

}
