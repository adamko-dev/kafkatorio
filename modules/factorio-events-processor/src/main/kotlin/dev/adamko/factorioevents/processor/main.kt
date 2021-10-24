package dev.adamko.factorioevents.processor

import dev.adamko.factorioevents.processor.config.ApplicationProperties
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder

fun main() {

  val appProps = ApplicationProperties()

  val topology = StreamsBuilder().build()

  val properties = appProps.kafkaConfig

  val streams = KafkaStreams(topology, properties)

  streams.setUncaughtExceptionHandler(StreamsExceptionHandler())

}