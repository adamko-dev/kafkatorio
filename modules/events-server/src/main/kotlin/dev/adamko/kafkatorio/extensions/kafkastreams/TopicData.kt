package dev.adamko.kafkatorio.extensions.kafkastreams

import org.apache.kafka.streams.KeyValue

interface TopicData<K> {

  val topicKey: K

  companion object {
    fun <K, T : TopicData<K>> T.toPair(): Pair<K, T> = topicKey to this
    fun <K, T : TopicData<K>> T.toKeyValue(): KeyValue<K, T> = KeyValue.pair(topicKey, this)

    operator fun <K, T : TopicData<K>> T.component1(): K = topicKey
    operator fun <K, T : TopicData<K>> T.component2(): T = this

  }
}
