package dev.adamko.kafkatorio.extensions.kafkastreams
//
//import org.apache.kafka.common.utils.Bytes
//import org.apache.kafka.streams.kstream.KGroupedStream
//import org.apache.kafka.streams.kstream.KTable
//import org.apache.kafka.streams.kstream.Materialized
//import org.apache.kafka.streams.state.KeyValueStore
//
//
//fun <K> KGroupedStream<K, *>.count(
//  name: String? = null,
//  materialized: Materialized<K, Long, KeyValueStore<Bytes, ByteArray>>? = null,
//): KTable<K, Long> {
//  return when {
//    name != null && materialized != null ->      count(withName(name), materialized)
//    name == null && materialized != null ->      count(materialized)
//    name != null && materialized == null ->      count(withName(name))
//    else ->      count()
//  }
//}
//
//
//
////<VOut> CogroupedKStream<K, VOut> cogroup(final Aggregator<? super K, ? super V, VOut> aggregator);
////<VR> KTable<K, VR> aggregate(final Initializer<VR> initializer,final Aggregator<? super K, ? super V, VR> aggregator, final Named named, final Materialized<K, VR, KeyValueStore<Bytes, byte[]>> materialized);
////<W extends Window> TimeWindowedKStream<K, V> windowedBy(final Windows<W> windows);
////KTable<K, Long> count(final Materialized<K, Long, KeyValueStore<Bytes, byte[]>> materialized);
////KTable<K, V> reduce(final Reducer<V> reducer);
////KTable<K, V> reduce(final Reducer<V> reducer, final Named named, final Materialized<K, V, KeyValueStore<Bytes, byte[]>> materialized);
////KTable<K, V> reduce(final Reducer<V> reducer,final Materialized<K, V, KeyValueStore<Bytes, byte[]>> materialized);
////SessionWindowedKStream<K, V> windowedBy(final SessionWindows windows);
////TimeWindowedKStream<K, V> windowedBy(final SlidingWindows windows);
