package dev.adamko.kafkatorio.extensions.kafkastreams

//import dev.adamko.kafkatorio.extensions.kxs.KotlinxSerializationExtensions.serde
//import dev.adamko.kafkatorio.processor.jsonMapper
//import org.apache.kafka.common.serialization.Serde
//import org.apache.kafka.common.serialization.Serdes
//import org.apache.kafka.streams.kstream.Consumed
//import org.apache.kafka.streams.kstream.Produced
//import org.apache.kafka.streams.kstream.Repartitioned
//
///**
// * A wrapper for two [Serde]s - [one for the Key][keySerde], [another for the Value][valueSerde].
// */
//open class KeyValueSerdes<K, V>(
//  val keySerde: Serde<K>,
//  val valueSerde: Serde<V>,
//) {
//
//  fun consumer(name: String): Consumed<K, V> =
//    Consumed.with(keySerde, valueSerde).withName(name)
//
//  fun producer(name: String): Produced<K, V> =
//    Produced.with(keySerde, valueSerde).withName(name)
//
//  fun repartitioned(name: String): Repartitioned<K, V> =
//    Repartitioned.with(keySerde, valueSerde).withName(name)
//
//  companion object {
//    inline fun <reified K, reified V> kxsJson(): KeyValueSerdes<K, V> =
//      KeyValueSerdes(
//        jsonMapper.serde(),
//        jsonMapper.serde(),
//      )
//
//    fun string(): KeyValueSerdes<String, String> =
//      KeyValueSerdes<String, String>(
//        Serdes.String(),
//        Serdes.String(),
//      )
//  }
//}
