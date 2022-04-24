package dev.adamko.kafkatorio.processor.misc

import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.Test
import io.mockk.every
import io.mockk.mockk
import kotlin.time.Duration.Companion.minutes
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.state.KeyValueIterator
import org.apache.kafka.streams.state.TimestampedKeyValueStore

class DebounceProcessorTest : FunSpec({


}) {
  @Test
  fun blah() {


//    val topology = Topology()
//
//    topology.addProcessor(
//      "blah",
//      debounceProcessor(
//        1.minutes,
//        "asdasda",
//        Serdes.String(),
//        Serdes.String()
//      )
//    )


//    val streamsBuilder = StreamsBuilder()
//
//    streamsBuilder
//      .stream<String, String>("input-topic")
//      .process(
//        debounceProcessor(
//          1.minutes,
//          "input-topic-debounce-store",
//          Serdes.String(),
//          Serdes.String(),
//        ),
//      )

  }
}
