package dev.adamko.kafkatorio.processor

import dev.adamko.kafkatorio.events.schema.FactorioEvent
import dev.adamko.kafkatorio.events.schema.FactorioObjectData
import dev.adamko.kafkatorio.events.schema.jsonMapper
import dev.adamko.kafkatorio.processor.config.ApplicationProperties
import java.time.Duration
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.modules.SerializersModule
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.serialization.Serializer
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.TopologyDescription
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.Produced
import org.http4k.format.ConfigurableKotlinxSerialization
import org.http4k.format.asConfigurable
import org.http4k.format.withStandardMappings

object KSX : ConfigurableKotlinxSerialization({
  ignoreUnknownKeys = true
  prettyPrint = true
  prettyPrintIndent = "  "
  asConfigurable().withStandardMappings().done()
  serializersModule = SerializersModule { }
})

object JsonSerdes : Serde<FactorioEvent<FactorioObjectData>> {
  override fun serializer() = Serializer<FactorioEvent<FactorioObjectData>> { _, message ->
    jsonMapper.encodeToString<FactorioEvent<FactorioObjectData>>(message).encodeToByteArray()
  }

  override fun deserializer() = Deserializer<FactorioEvent<FactorioObjectData>> { _, bytes ->
    jsonMapper.decodeFromString(bytes.decodeToString())
  }

}

fun main() {

  val appProps = ApplicationProperties()

  val builder = StreamsBuilder()

  builder.stream(
    "factorio-server-log",
    Consumed.with(Serdes.String(), Serdes.String())
  )
    .mapValues { readOnlyKey, value ->
      println("Mapping $readOnlyKey:$value")
      jsonMapper.decodeFromString<FactorioEvent<FactorioObjectData>>(value)
//      KSX.asA<FactorioEvent<FactorioObjectData>>(value)
    }
    .to(
      { key, value, recordContext -> value.data.objectName },
      Produced.with(Serdes.String(), JsonSerdes)
    )

  val topology = builder.build()
  val properties = appProps.kafkaConfig


  val streams = KafkaStreams(topology, properties)

  streams.setUncaughtExceptionHandler(StreamsExceptionHandler())

  Runtime.getRuntime().addShutdownHook(Thread { streams.close(Duration.ofSeconds(1)) })

//  streams.cleanUp()

  val description: TopologyDescription = topology.describe()
  println(description)

  streams.start()

}
