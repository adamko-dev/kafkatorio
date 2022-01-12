package dev.adamko.kafkatorio.extensions.kafkastreams

import dev.adamko.kafkatorio.extensions.kafkastreams.KTableExtensions.mapValues
import dev.adamko.kafkatorio.extensions.kafkastreams.KafkaStreamsExtensions.withName
import org.apache.kafka.streams.kstream.KTable

object KTableExtensions {

  /** @see [KTable.mapValues] */
  fun <K, inV, outV> KTable<K, inV>.mapValues(
    name: String,
    mapper: (readOnlyKey: K, value: inV) -> outV
  ): KTable<K, outV> =
    mapValues(
      { k, v -> mapper(k, v) },
      withName(name)
    )

}
