package dev.adamko.kafkatorio.processor.misc;

import java.time.Duration;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.TimestampedKeyValueStore;
import org.apache.kafka.streams.state.ValueAndTimestamp;

/**
 * THIS PROCESSOR IS UNTESTED
 * <br>
 * This processor mirrors the source, but waits for an inactivity gap before forwarding records.
 * <br>
 * The suppression is key based. Newer values will replace previous values, and reset the inactivity
 * gap.
 */
public class SuppressProcessor<K, V> implements Processor<K, V, K, V> {

  private final String storeName;
  private final Duration debounceCheckInterval;
  private final long suppressTimeoutMillis;

  private TimestampedKeyValueStore<K, V> stateStore;
  private ProcessorContext<K, V> context;

  /**
   * @param storeName             The name of the {@link TimestampedKeyValueStore} which will hold
   *                              records while they are being debounced.
   * @param suppressTimeout       The duration of inactivity before records will be forwarded.
   * @param debounceCheckInterval How regularly all records will be checked to see if they are
   *                              eligible to be forwarded. The interval should be shorter than
   *                              {@code suppressTimeout}.
   */
  public SuppressProcessor(
      String storeName,
      Duration suppressTimeout,
      Duration debounceCheckInterval
  ) {
    this.storeName = storeName;
    this.suppressTimeoutMillis = suppressTimeout.toMillis();
    this.debounceCheckInterval = debounceCheckInterval;
  }

  @Override
  public void init(ProcessorContext<K, V> context) {
    this.context = context;

    stateStore = context.getStateStore(storeName);

    context.schedule(debounceCheckInterval, PunctuationType.WALL_CLOCK_TIME, this::punctuate);
  }

  @Override
  public void process(Record<K, V> record) {

    final var key = record.key();
    final var value = record.value();

    final var storedRecord = stateStore.get(key);

    final var isNewRecord = storedRecord == null;

    final var timestamp = isNewRecord ? System.currentTimeMillis() : storedRecord.timestamp();

    stateStore.put(key, ValueAndTimestamp.make(value, timestamp));
  }

  private void punctuate(long timestamp) {
    try (var iterator = stateStore.all()) {
      while (iterator.hasNext()) {
        KeyValue<K, ValueAndTimestamp<V>> storedRecord = iterator.next();
        if (timestamp - storedRecord.value.timestamp() > suppressTimeoutMillis) {

          final var record = new Record<>(
              storedRecord.key,
              storedRecord.value.value(),
              storedRecord.value.timestamp()
          );

          context.forward(record);
          stateStore.delete(storedRecord.key);
        }
      }
    }
  }
}
