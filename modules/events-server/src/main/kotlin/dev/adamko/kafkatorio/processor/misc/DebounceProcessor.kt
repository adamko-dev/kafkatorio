package dev.adamko.kafkatorio.processor.misc

import dev.adamko.kotka.extensions.state.useAll
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.apache.kafka.common.serialization.Serde
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
) : Processor<K, V, K, V> {

  private var state: State<K, V> = Uninitialised()

  override fun init(kafkaContext: ProcessorContext<K, V>) {
    state = Active(kafkaContext)
  }

  override fun process(record: Record<K, V>) {
    when (val state = state) {
      is Active -> state.addRecord(record)
      else      -> error("state is not Active (actual: $state)")
    }
  }

  override fun close() {
    (state as? Active)?.close()
  }

  private sealed interface State<K, V>

  private class Uninitialised<K, V> : State<K, V>

  private class Closed<K, V> : State<K, V>

  private inner class Active(
    val kafkaContext: ProcessorContext<K, V>,
  ) : State<K, V>, CoroutineScope {
    override val coroutineContext: CoroutineContext =
      SupervisorJob() +
          Dispatchers.Unconfined +
          CoroutineName("DebounceProcessor")

    val recordsTimestampedStore: TimestampedKeyValueStore<K, V> =
      kafkaContext.getStateStore(recordsTimestampedStoreName)

    private val recordJobs = mutableMapOf<K, TimestampedJob>()

    init {
      recordsTimestampedStore.useAll {
        forEach { r ->
          val record = Record(r.key, r.value.value(), r.value.timestamp())
          addRecord(record)
        }
      }
    }

    fun addRecord(record: Record<K, V>) {
      recordJobs.compute(record.key()) { _, existing ->
        if (existing != null && existing.timestamp < record.timestamp()) {
          existing.job.cancel("replaced by newer record ${existing.timestamp} < ${record.timestamp()}")
          createTimestampedJob(record)
        } else {
          null
        }
      }
    }

    private fun createTimestampedJob(record: Record<K, V>): TimestampedJob {
      val job = launch {
        recordsTimestampedStore.put(
          record.key(),
          ValueAndTimestamp.make(record.value(), record.timestamp())
        )
        delay(inactivityDuration)
        if (isActive) {
          kafkaContext.forward(record)
          recordsTimestampedStore.delete(record.key())
        }
      }
      return TimestampedJob(job, record.timestamp())
    }

    fun close(): Closed<K, V> {
      coroutineContext.cancel()
      return Closed()
    }
  }

  private data class TimestampedJob(
    val job: Job,
    val timestamp: Long,
  )


  companion object {
    class Supplier<K, V>(
      private val inactivityDuration: Duration,
      private val storeName: String,
      private val keySerde: Serde<K>,
      private val valueSerde: Serde<V>,
    ) : ProcessorSupplier<K, V, K, V> {

      override fun get(): Processor<K, V, K, V> = DebounceProcessor(inactivityDuration, storeName)

      override fun stores(): Set<StoreBuilder<*>> = setOf(
        Stores.timestampedKeyValueStoreBuilder(
          Stores.persistentTimestampedKeyValueStore(storeName),
          keySerde,
          valueSerde,
        )
      )
    }
  }
}


//class DebounceProcessor<K, V>(
//  private val inactivityDuration: Duration,
//  private val recordsTimestampedStoreName: String,
//  coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
//) :
//  Processor<K, V, K, V>,
//  CoroutineScope {
//
//  override var coroutineContext: CoroutineContext =
//    SupervisorJob() +
//        coroutineDispatcher +
//        CoroutineName("DebounceProcessor")
//
//  private lateinit var kafkaContext: ProcessorContext<K, V>
//  private lateinit var recordsTimestampedStore: TimestampedKeyValueStore<K, V?>
//
//  private val recordsFlow: MutableSharedFlow<DebounceTaskInitializer<K>> =
//    MutableSharedFlow(
//      replay = 0,
//      extraBufferCapacity = Int.MAX_VALUE,
//      onBufferOverflow = BufferOverflow.DROP_OLDEST,
//    )
//
//  override fun init(context: ProcessorContext<K, V>) {
//    this.kafkaContext = context
//
//    this.coroutineContext += CoroutineName("DebounceProcessor-${context.applicationId()}-${context.taskId()}")
//
//    recordsTimestampedStore = kafkaContext.getStateStore(recordsTimestampedStoreName)
//
//    // start collecting records - create a debounce-timer for each record
//    recordsFlow
//      .runningFold(mapOf<K, Job>()) { acc, (k, createTask) ->
//        require(coroutineContext.isActive)
//
//        // cancel any previously existing debounce task
//        acc[k]?.cancel()
//
//        // add a new debounce task
//        when (createTask) {
//          true -> acc.plus(k to createDebounceTask(k))
//          false -> acc.minus(k)
//        }
//
//      }.launchIn(this)
//
//    // re-initialise the flow from previously-stored records
//    launch {
//      recordsFlow.emitAll(
//        recordsTimestampedStore
//          .allAsSequence()
//          .map { keyValue ->
//            require(coroutineContext.isActive)
//            DebounceTaskInitializer<K>(keyValue)
//          }
//          .asFlow()
//      )
//    }
//  }
//
//  private fun createDebounceTask(key: K): Job {
//    return launch {
//      delay(inactivityDuration)
//      val (value, timestamp) = recordsTimestampedStore.get(key)
//      if (coroutineContext.isActive) {
//        kafkaContext.forward(Record(key, value, timestamp))
//      }
//    }
//  }
//
//  override fun process(record: Record<K, V?>) {
//    launch {
//      recordsTimestampedStore.put(
//        record.key(),
//        ValueAndTimestamp.make(record.value(), record.timestamp())
//      )
//      recordsFlow.emit(DebounceTaskInitializer(record))
//    }
//  }
//
//  override fun close() {
//    coroutineContext.cancelChildren(CancellationException("processor go sleepy bye bye"))
//  }
//
//}
//
//internal data class DebounceTaskInitializer<K>(
//  val key: K,
//  val createTask: Boolean,
//) {
//  constructor(keyValue: KeyValue<K, *>) : this(keyValue.key, keyValue.value != null)
//  constructor(record: Record<K, *>) : this(record.key(), record.value() != null)
//}
//
//
//fun <K, V> debounceProcessor(
//  inactivityDuration: Duration,
//  storeName: String,
//  keySerde: Serde<K>,
//  valueSerde: Serde<V>,
//): ProcessorSupplier<K, V, K, V> =
//  object : ProcessorSupplier<K, V, K, V> {
//
//    override fun get(): Processor<K, V, K, V> = DebounceProcessor(inactivityDuration, storeName)
//
//    override fun stores(): Set<StoreBuilder<*>> = setOf(
//      Stores.timestampedKeyValueStoreBuilder(
//        Stores.persistentTimestampedKeyValueStore(storeName),
//        keySerde,
//        valueSerde,
//      )
//    )
//
//  }


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
