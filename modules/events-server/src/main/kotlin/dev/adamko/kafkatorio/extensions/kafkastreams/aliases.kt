package dev.adamko.kafkatorio.extensions.kafkastreams

import org.apache.kafka.streams.state.QueryableStoreType
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore
import org.apache.kafka.streams.state.ValueAndTimestamp

typealias TimestampedQueryStoreType<K, V> = QueryableStoreType<ReadOnlyKeyValueStore<K, ValueAndTimestamp<V>>>
