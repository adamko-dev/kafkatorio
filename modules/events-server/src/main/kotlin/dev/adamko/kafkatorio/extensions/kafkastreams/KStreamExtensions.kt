package dev.adamko.kafkatorio.extensions.kafkastreams
//
//import org.apache.kafka.streams.kstream.Grouped
//import org.apache.kafka.streams.kstream.KGroupedStream
//import org.apache.kafka.streams.kstream.KStream
//import org.apache.kafka.streams.kstream.Produced
//import org.apache.kafka.streams.processor.TopicNameExtractor
//
////  /** @see KStream.map */
////  inline fun <inK, inV, reified outK, reified outV : TopicData<outK>> KStream<inK, inV>.map(
////    name: String,
////    crossinline mapper: (key: inK, value: inV) -> outV
////  ): KStream<outK, outV> =
////    map({ k, v -> mapper(k, v).asKeyValue() }, withName(name))
//
///** @see KStream.map */
//inline fun <inK, inV, reified outK, reified outV> KStream<inK, inV>.map(
//  name: String,
//  crossinline mapper: (key: inK, value: inV) -> Pair<outK, outV>
//): KStream<outK, outV> =
//  map({ k, v -> mapper(k, v).toKeyValue() }, withName(name))
//
///** @see [KStream.mapValues] */
//inline fun <K, inV, outV> KStream<K, inV>.mapValues(
//  name: String,
//  crossinline mapper: (key: K, value: inV) -> outV
//): KStream<K, outV> = mapValues({ k, v -> mapper(k, v) }, withName(name))
//
////  /** @see KStream.flatMap */
////  inline fun <inK, reified outK, inV, reified outV : TopicData<outK>> KStream<inK, inV>.flatMap(
////    name: String,
////    crossinline mapper: (key: inK, value: inV) -> Iterable<outV>
////  ): KStream<outK, outV> = flatMap(
////    { k, v -> mapper(k, v).map { a: outV -> a.toKeyValue() } },
////    withName(name)
////  )
//
///** @see KStream.flatMap */
//inline fun <reified inK, inV, reified outK, reified outV> KStream<inK, inV>.flatMap(
//  name: String,
//  crossinline mapper: (key: inK, value: inV) -> Iterable<Pair<outK, outV>>
//): KStream<outK, outV> = flatMap(
//  { k, v -> mapper(k, v).map { it.toKeyValue() } },
//  withName(name)
//)
//
///** @see KStream.groupBy */
//fun <K, V, outK> KStream<K, V>.groupBy(
//  grouped: Grouped<outK, V>,
//  keySelector: (K, V) -> outK
//): KGroupedStream<outK, V> = groupBy(keySelector, grouped)
//
//fun <K, V> KStream<K, V>.to(
//  produced: Produced<K, V>,
//  topicNameExtractor: TopicNameExtractor<K, V>,
//) = to(topicNameExtractor, produced)
//
//fun <K, V> KStream<K, V>.filter(
//  name: String,
//  predicate: (K, V) -> Boolean,
//): KStream<K, V> = filter(predicate, withName(name))
//
//
//fun <K, V> KStream<K, V>.filterNot(
//  name: String,
//  predicate: (K, V) -> Boolean,
//): KStream<K, V> = filterNot(predicate, withName(name))
//
//
//fun <K, V> KStream<K, V>.merge(
//  name: String,
//  other: KStream<K, V>,
//): KStream<K, V> = merge(other, withName(name))
//
//
////  KStream<K, V> filter(final Predicate<? super K, ? super V> predicate, final Named named);
////  KStream<K, V> filterNot(final Predicate<? super K, ? super V> predicate, final Named named);
//
////  @JvmName("flatMapKeyValuePairs")
////  inline fun <inK, reified outK, inV, reified outV> KStream<inK, inV>.flatMap(
////      name: String,
////      crossinline mapper: (key: inK, value: inV) -> Iterable<Pair<outK, outV>>
////  ): KStream<outK, outV> =
////      flatMap(
////          { k, v -> mapper(k, v).map { it.asKeyPair() } },
////          withName(name)
////      )
//
////  inline fun <reified K, inV, reified outV> KStream<K, inV>.mapValues(
////      name: String,
////      crossinline mapper: (readOnlyKey: K, value: inV) -> outV
////  ): KStream<K, outV> =
////      map({ k, v -> (k to mapper(v)).asKeyPair() }, KafkaStreamsExtensions.withName(name))
//
//
////open class TransformerKt<inK, inV, outK, outV>(
////) : Transformer<inK, inV, KeyValue<outK, outV>> {
////
////  val onClose: TransformerKt<inK, inV, outK, outV>.() -> Unit = {}
////  val transformer: TransformerKt<inK, inV, outK, outV>.(key: inK, value: inV) -> KeyValue<outK, outV>? =
////    {}
////
////  private lateinit var _context: ProcessorContext
////  val context: ProcessorContext?
////    get() = if (this::_context.isInitialized) _context else null
////
////  override fun init(context: ProcessorContext) {
////    this._context = context
////  }
////
////  override fun close() {
////    onClose()
////  }
////
////  override fun transform(key: inK, value: inV): KeyValue<outK, outV>? {
////    return transformer(key, value)
////  }
////}
////
////
////  /**  one or the other or both */
////  KTable<K, V> toTable(final Named named, Materialized<K, V, KeyValueStore<Bytes, byte[]>> materialized);
////  <GK, GV, RV> KStream<K, RV> join(final GlobalKTable<GK, GV> globalTable, KeyValueMapper<? super K, ? super V, ? extends GK> keySelector, ValueJoinerWithKey<? super K, ? super V, ? super GV, ? extends RV> joiner, Named named);
////  <GK, GV, RV> KStream<K, RV> leftJoin(final GlobalKTable<GK, GV> globalTable, KeyValueMapper<? super K, ? super V, ? extends GK> keySelector, ValueJoinerWithKey<? super K, ? super V, ? super GV, ? extends RV> valueJoiner, Named named);
////  <K1, V1> KStream<K1, V1> flatTransform(final TransformerSupplier<? super K, ? super V, Iterable<KeyValue<K1, V1>>> transformerSupplier, Named named, String... stateStoreNames);
////  <K1, V1> KStream<K1, V1> flatTransform(final TransformerSupplier<? super K, ? super V, Iterable<KeyValue<K1, V1>>> transformerSupplier, String... stateStoreNames);
////  <K1, V1> KStream<K1, V1> transform(final TransformerSupplier<? super K, ? super V, KeyValue<K1, V1>> transformerSupplier, Named named, String... stateStoreNames);
////  <K1, V1> KStream<K1, V1> transform(final TransformerSupplier<? super K, ? super V, KeyValue<K1, V1>> transformerSupplier, String... stateStoreNames);
////  <KR, VR> KStream<KR, VR> flatMap(final KeyValueMapper<? super K, ? super V, ? extends Iterable<? extends KeyValue<? extends KR, ? extends VR>>> mapper, Named named);
////  <KR, VR> KStream<KR, VR> map(final KeyValueMapper<? super K, ? super V, ? extends KeyValue<? extends KR, ? extends VR>> mapper, Named named);
////  <KR> KGroupedStream<KR, V> groupBy(final KeyValueMapper<? super K, ? super V, KR> keySelector, Grouped<KR, V> grouped);
////  <KR> KStream<KR, V> selectKey(final KeyValueMapper<? super K, ? super V, ? extends KR> mapper, Named named);
////  <VO, VR> KStream<K, VR> join(final KStream<K, VO> otherStream, ValueJoinerWithKey<? super K, ? super V, ? super VO, ? extends VR> joiner, JoinWindows windows, StreamJoined<K, V, VO> streamJoined);
////  <VO, VR> KStream<K, VR> leftJoin(final KStream<K, VO> otherStream, ValueJoinerWithKey<? super K, ? super V, ? super VO, ? extends VR> joiner, JoinWindows windows, StreamJoined<K, V, VO> streamJoined);
////  <VO, VR> KStream<K, VR> outerJoin(final KStream<K, VO> otherStream, ValueJoinerWithKey<? super K, ? super V, ? super VO, ? extends VR> joiner, JoinWindows windows, StreamJoined<K, V, VO> streamJoined);
////  <VR> KStream<K, VR> flatMapValues(final ValueMapperWithKey<? super K, ? super V, ? extends Iterable<? extends VR>> mapper, Named named);
////  <VR> KStream<K, VR> flatTransformValues(final ValueTransformerWithKeySupplier<? super K, ? super V, Iterable<VR>> valueTransformerSupplier, Named named, String... stateStoreNames);
////  <VR> KStream<K, VR> mapValues(final ValueMapperWithKey<? super K, ? super V, ? extends VR> mapper, Named named);
////  <VR> KStream<K, VR> transformValues(final ValueTransformerWithKeySupplier<? super K, ? super V, ? extends VR> valueTransformerSupplier, Named named, String... stateStoreNames);
////  <VT, VR> KStream<K, VR> join(final KTable<K, VT> table, ValueJoinerWithKey<? super K, ? super V, ? super VT, ? extends VR> joiner, Joined<K, V, VT> joined);
////  <VT, VR> KStream<K, VR> leftJoin(final KTable<K, VT> table, ValueJoinerWithKey<? super K, ? super V, ? super VT, ? extends VR> joiner, Joined<K, V, VT> joined);
////  BranchedKStream<K, V> split(final Named named);
////  KGroupedStream<K, V> groupByKey(final Grouped<K, V> grouped);
////  KStream<K, V> merge(final KStream<K, V> stream, final Named named);
////  KStream<K, V> peek(final ForeachAction<? super K, ? super V> action, final Named named);
////  KStream<K, V> repartition(final Repartitioned<K, V> repartitioned);
////  void foreach(final ForeachAction<? super K, ? super V> action, final Named named);
////  void print(final Printed<K, V> printed);
////  void process(final ProcessorSupplier<? super K, ? super V, Void, Void> processorSupplier, Named named, String... stateStoreNames);
////  void to(final String topic, Produced<K, V> produced);
////  void to(final TopicNameExtractor<K, V> topicExtractor, Produced<K, V> produced);
////
