package dev.adamko.kafkatorio.schema.packets

import dev.adamko.kafkatorio.schema.common.ServerMapChunkId
import dev.adamko.kafkatorio.schema.common.TilePngFilename
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
sealed class EventServerPacket {


  @Serializable
  @SerialName("kafkatorio.event-server-packet.Kafkatorio")
  data class Kafkatorio(
    val packet: KafkatorioPacket
  ) : EventServerPacket()


  @Serializable
  @SerialName("kafkatorio.event-server-packet.ChunkTileSaved")
  data class ChunkTileSaved(
    val chunkId: ServerMapChunkId,
    val filename: TilePngFilename,
  ) : EventServerPacket()

}
