package dev.adamko.kafkatorio.server.processor.topology

import dev.adamko.kafkatorio.schema.common.FactorioServerId
import dev.adamko.kafkatorio.schema.common.Tick
import dev.adamko.kafkatorio.schema.packets.KafkatorioPacket
import dev.adamko.kafkatorio.schema.packets.KafkatorioPacketDataError
import dev.adamko.kafkatorio.server.config.jsonMapper
import dev.adamko.kafkatorio.server.processor.TOPIC_SRC_SERVER_LOG
import dev.adamko.kafkatorio.server.processor.topicName
import dev.adamko.kotka.extensions.component1
import dev.adamko.kotka.extensions.component2
import dev.adamko.kotka.extensions.consumedAs
import dev.adamko.kotka.extensions.producedAs
import dev.adamko.kotka.extensions.streams.mapValues
import dev.adamko.kotka.extensions.streams.to
import dev.adamko.kotka.kxs.serde
import java.util.zip.Inflater
import kotlinx.coroutines.CancellationException
import okio.Buffer
import okio.ByteString
import okio.ByteString.Companion.decodeBase64
import okio.buffer
import okio.inflate
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.KStream


fun factorioServerPacketStream(
  builder: StreamsBuilder = StreamsBuilder(),
): KStream<FactorioServerId, KafkatorioPacket> {

  return builder.stream(
    TOPIC_SRC_SERVER_LOG,
    consumedAs(
      "read-raw-packets-from-server",
      jsonMapper.serde(FactorioServerId.serializer()),
      Serdes.String(),
    )
  ).mapValues("decode-packets") { _: FactorioServerId, value: String? ->

    val packet = try {
      val decoded = decodeFactorioEncodedString(value!!)
      jsonMapper.decodeFromString(KafkatorioPacket.serializer(), decoded)
    } catch (e: Exception) {
      if (e is CancellationException) throw e

      KafkatorioPacket(
        modVersion = "unknown",
        tick = Tick(0u),
        data = KafkatorioPacketDataError(
          exception = e,
          rawValue = value,
        )
      )
    }

    if (packet.data is KafkatorioPacketDataError) {
      println("error parsing $TOPIC_SRC_SERVER_LOG message: $packet")
    }

    packet
  }
}


fun splitFactorioServerPacketStream(
  factorioServerPacketStream: KStream<FactorioServerId, KafkatorioPacket>
) {
  factorioServerPacketStream
    .to(
      producedAs(
        "split-server-log",
        jsonMapper.serde(),
        jsonMapper.serde(),
      )
    ) { (_: FactorioServerId, value: KafkatorioPacket) ->
//        println("[$key] sending event:${value.eventType} to topic:${value.data.objectName()}")
      value.data.topicName
    }
}


/**
 * Factorio `game.encode_string` zlib encodes a string, then base64's it. Here we do the reverse.
 */
private fun decodeFactorioEncodedString(
  source: String
): String {
  val bytes: ByteString = source.decodeBase64() ?: error("failed to base64 decode $source")

  return Buffer().write(bytes)
    .inflate(Inflater())
    .buffer()
    .readString(Charsets.UTF_8)
}
