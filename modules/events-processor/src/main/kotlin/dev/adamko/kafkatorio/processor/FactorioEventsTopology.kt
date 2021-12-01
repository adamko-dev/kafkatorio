package dev.adamko.kafkatorio.processor

import java.util.Properties
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder

class FactorioEventsTopology {

  fun build() {

    val topology = StreamsBuilder().build()


    val properties = Properties()

    val kafkaStreams = KafkaStreams(topology, properties)

  }

}