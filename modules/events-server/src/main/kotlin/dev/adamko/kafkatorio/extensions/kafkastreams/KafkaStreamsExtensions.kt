package dev.adamko.kafkatorio.extensions.kafkastreams
//
//import org.apache.kafka.common.serialization.Serde
//import org.apache.kafka.streams.kstream.Grouped
//import org.apache.kafka.streams.kstream.Joined
//import org.apache.kafka.streams.kstream.Materialized
//import org.apache.kafka.streams.kstream.Named
//import org.apache.kafka.streams.kstream.Produced
//import org.apache.kafka.streams.kstream.Repartitioned
//import org.apache.kafka.streams.processor.StateStore
//
///** Helpful extensions & shortcuts for Kafka Streams */
//
//
///** @see [Named] */
//fun withName(name: String): Named = Named.`as`(name)
//
////  @Suppress("FunctionName")
////  inline fun <reified K, reified V, S : StateStore> MaterializedAs(
////      tableDefinition: GlobalTableDefinition<K, V>
////  ) = MaterializedAs<K, V, S>(
////      tableDefinition.globalStoreName,
////      tableDefinition.serdes.keySerde,
////      tableDefinition.serdes.valueSerde
////  )
//
///** @see Materialized */
//inline fun <reified K, reified V, S : StateStore> materializedAs(
//  name: String,
//  keySerde: Serde<K>,//= kxsJsonSerde(),
//  valueSerde: Serde<V>,//= kxsJsonSerde(),
//): Materialized<K, V, S> =
//  Materialized.`as`<K, V, S>(name)
//    .withKeySerde(keySerde)
//    .withValueSerde(valueSerde)
//
///** @see Repartitioned */
//inline fun <reified Key, reified Val> repartitionedAs(
//  name: String,
//  keySerde: Serde<Key>,
//  valueSerde: Serde<Val>,
//): Repartitioned<Key, Val> =
//  Repartitioned.`as`<Key, Val>(name)
//    .withKeySerde(keySerde)
//    .withValueSerde(valueSerde)
//
///** @see Produced */
//fun <K, V> producedAs(name: String): Produced<K, V> =
//  Produced.`as`(name)
//
///** @see Produced */
//inline fun <reified Key, reified Val> producedAs(
//  name: String,
//  keySerde: Serde<Key>,
//  valueSerde: Serde<Val>,
//): Produced<Key, Val> =
//  producedAs<Key, Val>(name)
//    .withKeySerde(keySerde)
//    .withValueSerde(valueSerde)
//
////  @Suppress("FunctionName")
////  fun <Key, Val, V_other> JoinedAs(name: String): Joined<Key, Val, V_other> =
////      Joined.`as`(name)
//
///** @see Joined */
//inline fun <reified Key, reified Val, reified V_other> joinedAs(
//  name: String,
//  keySerde: Serde<Key>,// = kxsJsonSerde(),
//  valueSerde: Serde<Val>,//= kxsJsonSerde(),
//  otherValueSerde: Serde<V_other>?,//= kxsJsonSerde(),
//): Joined<Key, Val, V_other> =
//  Joined.`as`<Key, Val, V_other>(name)
//    .withKeySerde(keySerde)
//    .withValueSerde(valueSerde)
//    .withOtherValueSerde(otherValueSerde)
//
///** @see [Grouped.`as`] */
//inline fun <reified Key, reified Val> groupedAs(
//  name: String,
//  keySerde: Serde<Key>?, //= jsonMapper.serde(),
//  valueSerde: Serde<Val>?, //= jsonMapper.serde(),
//): Grouped<Key, Val> =
//  Grouped.`as`<Key, Val>(name)
//    .withKeySerde(keySerde)
//    .withValueSerde(valueSerde)
//
////  fun <K, V> InteractiveQueryService.getQueryableStore(
////      tableDef: GlobalTableDefinition<K, V>
////  ): Optional<ReadOnlyKeyValueStore<K, ValueAndTimestamp<V>>> {
////    return getQueryableStore(tableDef.globalStoreName, tableDef.storeType)
////  }
