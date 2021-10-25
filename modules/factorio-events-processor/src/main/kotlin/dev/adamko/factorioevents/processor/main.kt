package dev.adamko.factorioevents.processor

import dev.adamko.factorioevents.processor.config.ApplicationProperties
import dev.adamko.factorioevents.processor.model.FactorioServerLogRecord
import dev.adamko.factorioevents.processor.model.LuaSurfaceData
import java.time.Duration
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Branched
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.Named
import org.apache.kafka.streams.processor.TopicNameExtractor
import org.http4k.format.Jackson

fun main() {

  val appProps = ApplicationProperties()

  val builder = StreamsBuilder()


  builder.stream(
    "factorio-server-log",
    Consumed.with(Serdes.String(), Serdes.String())
  )
    .mapValues { readOnlyKey, value ->
      println("Mapping $readOnlyKey:$value")
      Jackson.asA<FactorioServerLogRecord<*>>(value)
    }
    .to(TopicNameExtractor { key, value, recordContext ->
      value.data.objectName
    })

  val topology = builder.build()
  val properties = appProps.kafkaConfig

  val streams = KafkaStreams(topology, properties)

  streams.setUncaughtExceptionHandler(StreamsExceptionHandler())

  Runtime.getRuntime().addShutdownHook(Thread { streams.close(Duration.ofSeconds(10)) })

  streams.start()

}
