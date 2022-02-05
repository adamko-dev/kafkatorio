package dev.adamko.kafkatorio.processor.misc

import dev.adamko.kotka.extensions.component1
import dev.adamko.kotka.extensions.component2
import dev.adamko.kotka.extensions.state.allAsSequence
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.processor.api.Processor
import org.apache.kafka.streams.processor.api.ProcessorContext
import org.apache.kafka.streams.processor.api.ProcessorSupplier
import org.apache.kafka.streams.processor.api.Record
import org.apache.kafka.streams.state.StoreBuilder
import org.apache.kafka.streams.state.Stores
import org.apache.kafka.streams.state.TimestampedKeyValueStore
import org.apache.kafka.streams.state.ValueAndTimestamp


class DebounceProcessor<K, V>(
  private val inactivityDuration: Duration,
  private val recordsTimestampedStoreName: String,
  coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
) :
  Processor<K, V, K, V>,
  CoroutineScope {

  override var coroutineContext: CoroutineContext =
    SupervisorJob() +
        coroutineDispatcher +
        CoroutineName("DebounceProcessor")

  private lateinit var kafkaContext: ProcessorContext<K, V>
  private lateinit var recordsTimestampedStore: TimestampedKeyValueStore<K, V?>

  private val recordsFlow: MutableSharedFlow<DebounceTaskInitializer<K>> =
    MutableSharedFlow(
      replay = 0,
      extraBufferCapacity = Int.MAX_VALUE,
      onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

  override fun init(context: ProcessorContext<K, V>) {
    this.kafkaContext = context

    this.coroutineContext += CoroutineName("DebounceProcessor-${context.applicationId()}-${context.taskId()}")

    recordsTimestampedStore = kafkaContext.getStateStore(recordsTimestampedStoreName)

    // start collecting records - create a debounce-timer for each record
    recordsFlow
      .runningFold(mapOf<K, Job>()) { acc, (k, createTask) ->
        require(coroutineContext.isActive)

        // cancel any previously existing debounce task
        acc[k]?.cancel()

        // add a new debounce task
        when (createTask) {
          true -> acc.plus(k to createDebounceTask(k))
          false -> acc.minus(k)
        }

      }.launchIn(this)

    // re-initialise the flow from previously-stored records
    launch {
      recordsFlow.emitAll(
        recordsTimestampedStore
          .allAsSequence()
          .map { keyValue ->
            require(coroutineContext.isActive)
            DebounceTaskInitializer<K>(keyValue)
          }
          .asFlow()
      )
    }
  }

  private fun createDebounceTask(key: K): Job {
    return launch {
      delay(inactivityDuration)
      val (value, timestamp) = recordsTimestampedStore.get(key)
      if (coroutineContext.isActive) {
        kafkaContext.forward(Record(key, value, timestamp))
      }
    }
  }

  override fun process(record: Record<K, V?>) {
    launch {
      recordsTimestampedStore.put(
        record.key(),
        ValueAndTimestamp.make(record.value(), record.timestamp())
      )
      recordsFlow.emit(DebounceTaskInitializer(record))
    }
  }

  override fun close() {
    coroutineContext.cancelChildren(CancellationException("processor go sleepy bye bye"))
  }

}

internal data class DebounceTaskInitializer<K>(
  val key: K,
  val createTask: Boolean,
) {
  constructor(keyValue: KeyValue<K, *>) : this(keyValue.key, keyValue.value != null)
  constructor(record: Record<K, *>) : this(record.key(), record.value() != null)
}


fun <K, V> debounceProcessor(
  inactivityDuration: Duration,
  storeName: String,
  keySerde: Serde<K>,
  valueSerde: Serde<V>,
): ProcessorSupplier<K, V, K, V> =
  object : ProcessorSupplier<K, V, K, V> {

    override fun get(): Processor<K, V, K, V> = DebounceProcessor(inactivityDuration, storeName)

    override fun stores(): Set<StoreBuilder<*>> = setOf(
      Stores.timestampedKeyValueStoreBuilder(
        Stores.persistentTimestampedKeyValueStore(storeName),
        keySerde,
        valueSerde,
      )
    )

  }


//class SuppressProcessor<K, V>(
//  private val storeName: String,
//  private val tickDuration: Duration = 1.seconds,
//  private val debounceDuration: Duration,
//) : Processor<K, V, K, V> {
//
//  private lateinit var context: ProcessorContext<K, V>
//
//  private lateinit var stateStore: TimestampedKeyValueStore<K, V?>
//
//  override fun init(context: ProcessorContext<K, V>) {
//
//    this.context = context
//
//    stateStore = context.getStateStore(storeName) as TimestampedKeyValueStore<K, V?>
//
//    context.schedule(
//      tickDuration.toJavaDuration(),
//      PunctuationType.WALL_CLOCK_TIME,
//      ::punctuate,
//    )
//  }
//
//  override fun process(record: Record<K, V?>) {
//
//    val (key, newVal) = record
//
//    val storedVal: V? = stateStore[key]?.value()
//
//    val cachedVal = ValueAndTimestamp.make(
//      storedVal ?: newVal,
//      System.currentTimeMillis()
//    )
//
//    stateStore.put(key, cachedVal)
//
////    val newTimestamp = if (storedVal == null) System.currentTimeMillis() else storedTimestamp
////    stateStore.put(key, ValueAndTimestamp.make(storedVal, newTimestamp))
//  }
//
//  private fun punctuate(currentTimestamp: Long) {
//
//    stateStore.all().use { iterator ->
//      iterator.forEach { record: KeyValue<K, ValueAndTimestamp<V?>> ->
//
//        val (k, v, storedTimestamp) = record
//
//        if (currentTimestamp - storedTimestamp > debounceDuration.inWholeMilliseconds) {
//
//          context.forward(Record(k, v, storedTimestamp))
//
//          stateStore.delete(k)
//        }
//
//      }
//    }
//  }
//
//}
//
