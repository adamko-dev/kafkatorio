package dev.adamko.kafkatorio.extensions.kafkastreams
//
//import dev.adamko.kafkatorio.extensions.kafkastreams.*
//import kotlin.experimental.ExperimentalTypeInference
//import org.apache.kafka.streams.KeyValue
//import org.apache.kafka.streams.kstream.KStream
//import org.apache.kafka.streams.kstream.Transformer
//import org.apache.kafka.streams.kstream.TransformerSupplier
//import org.apache.kafka.streams.processor.ProcessorContext
//
///** @see KStream.transform */
//inline fun <inK, inV, reified outK, reified outV> KStream<inK, inV>.transformKt(
//  crossinline block: BuildTransformer<inK, inV, outK, outV>
//): KStream<outK, outV> {
//
//  val transformer = object : Transformer<inK, inV, KeyValue<outK, outV>> {
//
//    private lateinit var scope: TransformerScope<inK, inV, outK, outV>
//
//    override fun init(procContext: ProcessorContext) {
//      scope = TransformerScope(procContext)
//      scope.block()
//    }
//
//    override fun close() {
//      scope.close()
//    }
//
//    override fun transform(key: inK, value: inV): KeyValue<outK, outV>? {
//      return scope.mapper(key, value)?.toKeyValue()
//    }
//  }
//
//  return transform({ transformer })
//}
//
///** @see KStream.transform */
//@OptIn(ExperimentalTypeInference::class)
//inline fun <inK, inV, reified outK, reified outV, outKV : KeyValue<outK, outV>> KStream<inK, inV>.transformKt2(
//  @BuilderInference block: Transformer<inK, inV, outKV>.() -> Unit
//): KStream<outK, outV> {
//
//  val transformer = object : Transformer<inK, inV, KeyValue<outK, outV>> {
//
//    private lateinit var scope: TransformerScope<inK, inV, outK, outV>
//
//    override fun init(procContext: ProcessorContext) {
//      scope = TransformerScope(procContext)
//    }
//
//    override fun close() {
//      scope.close()
//
//      sequence<Long> {
//
//      }
//    }
//
//    override fun transform(key: inK, value: inV): KeyValue<outK, outV>? {
//      return scope.mapper(key, value)?.toKeyValue()
//    }
//  }
//
//  return transform(TransformerSupplier { transformer })
//}
//
//typealias BuildTransformer<inK, inV, outK, outV> = TransformerScope<inK, inV, outK, outV>.() -> Unit
//
//typealias TransformerMapper<inK, inV, outK, outV> = (key: inK, value: inV) -> Pair<outK, outV>?
//typealias TransformerCloseHandler = (ProcessorContext) -> Unit
//
//open class TransformerScope<inK, inV, outK, outV>(
//  val processorContext: ProcessorContext,
//) : AutoCloseable {
//
//  private var closeHandler: TransformerCloseHandler = {}
//  lateinit var mapper: TransformerMapper<inK, inV, outK, outV>
//
//  fun handleClose(closeHandler: TransformerCloseHandler) {
//    this.closeHandler = closeHandler
//  }
//
//  fun mapEach(mapper: TransformerMapper<inK, inV, outK, outV>) {
//    this.mapper = mapper
//  }
//
//  override fun close() {
//    closeHandler(processorContext)
//  }
//}
