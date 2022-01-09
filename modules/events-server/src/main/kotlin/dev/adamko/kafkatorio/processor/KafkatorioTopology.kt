package dev.adamko.kafkatorio.processor

import dev.adamko.kafkatorio.events.schema.FactorioConfigurationUpdate
import dev.adamko.kafkatorio.events.schema.FactorioEvent
import dev.adamko.kafkatorio.events.schema.KafkatorioPacket
import java.time.Duration
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.TopologyDescription
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.Produced

class KafkatorioTopology(
  private val websocketServer: WebsocketServer,
  private val builder: StreamsBuilder = StreamsBuilder(),
) {

  private val sourceTopic = "factorio-server-log"

  fun build() {

    builder.stream(
      sourceTopic,
      Consumed.with(Serdes.String(), Serdes.String())
    )
      .mapValues { readOnlyKey, value ->
        println("Mapping $readOnlyKey:$value")
        jsonMapper.decodeFromString<KafkatorioPacket>(value)
      }
      .peek { _, value ->
        websocketServer.sendMessage(jsonMapper.encodeToString(value))
      }
      .to(
        { _, value, _ ->
//        println("[$key] sending event:${value.eventType} to topic:${value.data.objectName()}")
          when (value) {
            is FactorioEvent<*>            ->
              "kafkatorio.${value.packetType.name}.${value.data.objectName.name}"
            is FactorioConfigurationUpdate ->
              "kafkatorio.${value.packetType.name}"
          }
        },
        Produced.with(Serdes.String(), KafkatorioPacketSerde)
      )

    val topology = builder.build()

    val streams = KafkaStreams(topology, appProps.kafkaConfig)

    streams.setUncaughtExceptionHandler(StreamsExceptionHandler())

    Runtime.getRuntime().addShutdownHook(Thread { streams.close(Duration.ofSeconds(1)) })

//  streams.cleanUp()

    val description: TopologyDescription = topology.describe()
    println(description)

    streams.start()
  }

}