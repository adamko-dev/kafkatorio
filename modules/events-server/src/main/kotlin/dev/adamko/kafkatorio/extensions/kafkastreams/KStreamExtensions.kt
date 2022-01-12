package dev.adamko.kafkatorio.extensions.kafkastreams

import dev.adamko.kafkatorio.extensions.kafkastreams.KStreamExtensions.flatMap
import dev.adamko.kafkatorio.extensions.kafkastreams.KStreamExtensions.groupBy
import dev.adamko.kafkatorio.extensions.kafkastreams.KStreamExtensions.map
import dev.adamko.kafkatorio.extensions.kafkastreams.KStreamExtensions.mapValues
import dev.adamko.kafkatorio.extensions.kafkastreams.KafkaStreamsExtensions.withName
import dev.adamko.kafkatorio.extensions.kafkastreams.KeyPairExtensions.kvPair
import dev.adamko.kafkatorio.extensions.kafkastreams.KeyPairExtensions.toKeyValue
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.kstream.Grouped
import org.apache.kafka.streams.kstream.KGroupedStream
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.Transformer
import org.apache.kafka.streams.processor.ProcessorContext

object KStreamExtensions {

//  /** @see KStream.map */
//  inline fun <inK, inV, reified outK, reified outV : TopicData<outK>> KStream<inK, inV>.map(
//    name: String,
//    crossinline mapper: (key: inK, value: inV) -> outV
//  ): KStream<outK, outV> =
//    map({ k, v -> mapper(k, v).asKeyValue() }, withName(name))

  /** @see KStream.map */
  inline fun <inK, inV, reified outK, reified outV> KStream<inK, inV>.map(
    name: String,
    crossinline mapper: (key: inK, value: inV) -> Pair<outK, outV>
  ): KStream<outK, outV> =
    map({ k, v -> mapper(k, v).toKeyValue() }, withName(name))

  /** Wrapper for [KStream.mapValues] */
  fun <K, inV, outV> KStream<K, inV>.mapValues(
    name: String,
    mapper: (key: K, value: inV) -> outV
  ): KStream<K, outV> =
    mapValues({ k, v -> mapper(k, v) }, withName(name))

//  /** @see KStream.flatMap */
//  inline fun <inK, reified outK, inV, reified outV : TopicData<outK>> KStream<inK, inV>.flatMap(
//    name: String,
//    crossinline mapper: (key: inK, value: inV) -> Iterable<outV>
//  ): KStream<outK, outV> = flatMap(
//    { k, v -> mapper(k, v).map { a: outV -> a.asKeyValue() } },
//    withName(name)
//  )

  /** @see KStream.flatMap */
  inline fun <reified inK, inV, reified outK, reified outV> KStream<inK, inV>.flatMap(
    name: String,
    crossinline mapper: (key: inK, value: inV) -> Iterable<Pair<outK, outV>>
  ): KStream<outK, outV> = flatMap(
    { k, v -> mapper(k, v).map { it.toKeyValue() } },
    withName(name)
  )

  /** @see KStream.groupBy */
  inline fun <reified inK, outK, reified V> KStream<inK, V>.groupBy(
    grouped: Grouped<outK, V>,
    crossinline keySelector: KeyValue<inK, V>.() -> outK
  ): KGroupedStream<outK, V> =
    groupBy(
      { k, v -> (k kvPair v).keySelector() },
      grouped
    )



//  @JvmName("flatMapKeyValuePairs")
//  inline fun <inK, reified outK, inV, reified outV> KStream<inK, inV>.flatMap(
//      name: String,
//      crossinline mapper: (key: inK, value: inV) -> Iterable<Pair<outK, outV>>
//  ): KStream<outK, outV> =
//      flatMap(
//          { k, v -> mapper(k, v).map { it.asKeyPair() } },
//          withName(name)
//      )

//  inline fun <reified K, inV, reified outV> KStream<K, inV>.mapValues(
//      name: String,
//      crossinline mapper: (readOnlyKey: K, value: inV) -> outV
//  ): KStream<K, outV> =
//      map({ k, v -> (k to mapper(v)).asKeyPair() }, KafkaStreamsExtensions.withName(name))

}


//open class TransformerKt<inK, inV, outK, outV>(
//) : Transformer<inK, inV, KeyValue<outK, outV>> {
//
//  val onClose: TransformerKt<inK, inV, outK, outV>.() -> Unit = {}
//  val transformer: TransformerKt<inK, inV, outK, outV>.(key: inK, value: inV) -> KeyValue<outK, outV>? =
//    {}
//
//  private lateinit var _context: ProcessorContext
//  val context: ProcessorContext?
//    get() = if (this::_context.isInitialized) _context else null
//
//  override fun init(context: ProcessorContext) {
//    this._context = context
//  }
//
//  override fun close() {
//    onClose()
//  }
//
//  override fun transform(key: inK, value: inV): KeyValue<outK, outV>? {
//    return transformer(key, value)
//  }
//}
