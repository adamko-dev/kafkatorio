package dev.adamko.kafkatorio.extensions.kafkastreams
//
//
//import org.apache.kafka.streams.KeyValue
//import org.apache.kafka.streams.kstream.Grouped
//import org.apache.kafka.streams.kstream.KGroupedTable
//import org.apache.kafka.streams.kstream.KTable
//import org.apache.kafka.streams.kstream.KeyValueMapper
//
///** @see [KTable.mapValues] */
//fun <K, inV, outV> KTable<K, inV>.mapValues(
//  name: String,
//  mapper: (readOnlyKey: K, value: inV) -> outV
//): KTable<K, outV> =
//  mapValues(
//    { k, v -> mapper(k, v) },
//    withName(name)
//  )
//
///** @See [KTable.groupBy] */
//fun <inK, inV, outK, outV> KTable<inK, inV>.groupBy(
//  grouped: Grouped<outK, outV>? = null,
//  selector: KeyValueMapper<inK, inV, KeyValue<outK, outV>>
//): KGroupedTable<outK, outV> = when (grouped) {
//  null -> groupBy(selector)
//  else -> groupBy(selector, grouped)
//}
//
//
////
////<KR> KStream<KR, V> toStream(final KeyValueMapper<? super K, ? super V, ? extends KR> mapper,final Named named);
////<VO, VR> KTable<K, VR> join(final KTable<K, VO> other,final ValueJoiner<? super V, ? super VO, ? extends VR> joiner,final Named named,final Materialized<K, VR, KeyValueStore<Bytes, byte[]>> materialized);
////<VR, KO, VO> KTable<K, VR> join(final KTable<KO, VO> other,final Function<V, KO> foreignKeyExtractor,final ValueJoiner<V, VO, VR> joiner,final Named named,final Materialized<K, VR, KeyValueStore<Bytes, byte[]>> materialized);
////
////<VO, VR> KTable<K, VR> leftJoin(final KTable<K, VO> other,final ValueJoiner<? super V, ? super VO, ? extends VR> joiner,final Named named,final Materialized<K, VR, KeyValueStore<Bytes, byte[]>> materialized);
////
////<VO, VR> KTable<K, VR> outerJoin(final KTable<K, VO> other,final ValueJoiner<? super V, ? super VO, ? extends VR> joiner,final Named named,final Materialized<K, VR, KeyValueStore<Bytes, byte[]>> materialized);
////<VR, KO, VO> KTable<K, VR> leftJoin(final KTable<KO, VO> other,final Function<V, KO> foreignKeyExtractor,final ValueJoiner<V, VO, VR> joiner,final Named named,final Materialized<K, VR, KeyValueStore<Bytes, byte[]>> materialized);
////<VR> KTable<K, VR> mapValues(final ValueMapper<? super V, ? extends VR> mapper,final Named named,final Materialized<K, VR, KeyValueStore<Bytes, byte[]>> materialized);
////<VR> KTable<K, VR> mapValues(final ValueMapperWithKey<? super K, ? super V, ? extends VR> mapper,final Named named,final Materialized<K, VR, KeyValueStore<Bytes, byte[]>> materialized);
////<VR> KTable<K, VR> transformValues(final ValueTransformerWithKeySupplier<? super K, ? super V, ? extends VR> transformerSupplier,final Materialized<K, VR, KeyValueStore<Bytes, byte[]>> materialized,final Named named,final String... stateStoreNames);
////KStream<K, V> toStream(final Named named);
/////** materialized, named - optional */
////KTable<K, V> filter(final Predicate<? super K, ? super V> predicate,final Named named,final Materialized<K, V, KeyValueStore<Bytes, byte[]>> materialized);
/////** materialized, named - optional */
////KTable<K, V> filterNot(final Predicate<? super K, ? super V> predicate,final Named named,final Materialized<K, V, KeyValueStore<Bytes, byte[]>> materialized);
////KTable<K, V> suppress(final Suppressed<? super K> suppressed);
////String queryableStoreName();
