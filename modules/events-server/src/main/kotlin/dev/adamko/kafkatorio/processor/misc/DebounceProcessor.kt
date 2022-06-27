package dev.adamko.kafkatorio.processor.misc

import dev.adamko.kotka.extensions.component1
import dev.adamko.kotka.extensions.component2
import dev.adamko.kotka.extensions.component3
import dev.adamko.kotka.extensions.processor.component1
import dev.adamko.kotka.extensions.processor.component2
import dev.adamko.kotka.extensions.processor.component3
import dev.adamko.kotka.extensions.state.useAll
import kotlin.time.toJavaDuration
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.processor.Cancellable
import org.apache.kafka.streams.processor.PunctuationType
import org.apache.kafka.streams.processor.api.Processor
import org.apache.kafka.streams.processor.api.ProcessorContext
import org.apache.kafka.streams.processor.api.ProcessorSupplier
import org.apache.kafka.streams.processor.api.Record
import org.apache.kafka.streams.state.StoreBuilder
import org.apache.kafka.streams.state.Stores
import org.apache.kafka.streams.state.TimestampedKeyValueStore
import org.apache.kafka.streams.state.ValueAndTimestamp
import java.time.Duration as DurationJvm
import kotlin.time.Duration as DurationKt


class DebounceProcessor<K, V>(
  private val inactivityDuration: DurationKt,
  checkInterval: DurationKt,
  private val storeName: String,
) : Processor<K, V, K, V> {


  private val checkInterval: DurationJvm = checkInterval.toJavaDuration()

  private lateinit var state: State<K, V>


  override fun init(context: ProcessorContext<K, V>) {
    state = Active(context)
  }


  override fun process(record: Record<K, V>) {
    (state as Active).apply {
      val (key, value, _) = record

      val existing: ValueAndTimestamp<V>? = store.get(key)
      val newTimestamp = existing?.timestamp() ?: System.currentTimeMillis()

      store.put(key, ValueAndTimestamp.make(value, newTimestamp))
    }
  }


  override fun close() {
    (state as? Active)?.schedule?.cancel()
    state = Closed()
  }


  private sealed interface State<K, V>


  private class Closed<K, V> : State<K, V>


  private inner class Active(
    val context: ProcessorContext<K, V>
  ) : State<K, V> {
    val store: TimestampedKeyValueStore<K, V> = context.getStateStore(storeName)
    val schedule: Cancellable = context.schedule(
      checkInterval,
      PunctuationType.WALL_CLOCK_TIME,
      ::checkRecords
    )

    private fun checkRecords(now: Long) {
      store.useAll {
        forEach { record ->
          val (key, value, timestamp) = record
          if ((now - timestamp) > inactivityDuration.inWholeMilliseconds) {
            context.forward(Record(key, value, timestamp))
            store.delete(key)
          }
        }
      }
    }
  }

  companion object {

    fun <K, V> Topology.addDebounceProcessor(
      namePrefix: String,
      sourceTopic: String,
      sinkTopic: String,

      inactivityDuration: DurationKt,
      checkInterval: DurationKt = inactivityDuration / 10,
      keySerde: Serde<K>,
      valueSerde: Serde<V>,
      storeName: String = "$namePrefix.debounce-store",
    ): Topology {

      val sourceName = "$namePrefix.debounce-source"
      val processorName = "$namePrefix.debounce-processor"
      val sinkName = "$namePrefix.debounce-sink"

      return addSource(
        sourceName,
        keySerde.deserializer(),
        valueSerde.deserializer(),
        sourceTopic,
      ).addProcessor(
        processorName,
        Supplier(
          inactivityDuration = inactivityDuration,
          checkInterval = checkInterval,
          storeName = storeName,
          keySerde = keySerde,
          valueSerde = valueSerde,
        ),
        sourceName,
      ).addSink(
        sinkName,
        sinkTopic,
        keySerde.serializer(),
        valueSerde.serializer(),
        processorName,
      )
    }

    class Supplier<K, V>(
      private val inactivityDuration: DurationKt,
      private val checkInterval: DurationKt,
      private val storeName: String,
      private val keySerde: Serde<K>,
      private val valueSerde: Serde<V>,
    ) : ProcessorSupplier<K, V, K, V> {

      override fun get(): Processor<K, V, K, V> =
        DebounceProcessor(inactivityDuration, checkInterval, storeName)

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
