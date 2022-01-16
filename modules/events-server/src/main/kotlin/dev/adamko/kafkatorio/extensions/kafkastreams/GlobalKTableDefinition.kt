package dev.adamko.kafkatorio.extensions.kafkastreams

import org.apache.kafka.streams.state.QueryableStoreTypes.timestampedKeyValueStore

///**
// * * update topic [topicName]
// * * plus table-source topic [tableName]
// * * plus state store [globalStoreName]
// *
// * equals a GlobalTableDefinition
// */
//abstract class GlobalKTableDefinition<K, V>(
//    val tableName: String,
//    override val serdes: KeyValueSerdes<K, V>,
//    val globalStoreName: String = "$tableName-store",
//    /**
//     * The backing topic, from which this table will be filled.
//     *
//     * Updates to items can be sent or received with this topic.
//     */
//    final override val topicName: String = "$tableName-topic",
//    val storeType: TimestampedQueryStoreType<K, V> = timestampedKeyValueStore(),
//) : TopicDefinition<K, V> {
//  val topicNames: Set<String>
//
//  init {
//    require(tableName != topicName)
//    require(tableName != globalStoreName)
//    topicNames = setOf(tableName, topicName)
//  }
//}
