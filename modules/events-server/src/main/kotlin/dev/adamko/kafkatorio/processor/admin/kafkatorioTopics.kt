package dev.adamko.kafkatorio.processor.admin

import dev.adamko.kafkatorio.schema2.ConfigurationUpdate
import dev.adamko.kafkatorio.schema2.ConsoleChatUpdate
import dev.adamko.kafkatorio.schema2.ConsoleCommandUpdate
import dev.adamko.kafkatorio.schema2.EntityUpdate
import dev.adamko.kafkatorio.schema2.KafkatorioPacketData2
import dev.adamko.kafkatorio.schema2.MapChunkUpdate
import dev.adamko.kafkatorio.schema2.PlayerUpdate
import dev.adamko.kafkatorio.schema2.PrototypesUpdate
import dev.adamko.kafkatorio.schema2.SurfaceUpdate
import kotlin.reflect.KClass


private const val DOMAIN = "kafkatorio"


const val TOPIC_SRC_SERVER_LOG = "$DOMAIN.src.server-log"
//  "factorio-server-log"
const val TOPIC_GROUPED_MAP_CHUNKS_STATE = "$DOMAIN.state.map-chunks.grouped"


fun allTopics(): Set<String> = buildSet {
  add(TOPIC_SRC_SERVER_LOG)
  add(TOPIC_GROUPED_MAP_CHUNKS_STATE)

  addAll(packetTopicNames.values)
}

//fun KafkatorioPacket2.topicName(): String = data.topicName()

//fun KafkatorioPacketData2.topicName(): String = "$DOMAIN.packet.${topicName}"


val KafkatorioPacketData2.topicName: String
  get() = when (this) {
    is ConfigurationUpdate  -> packetTopicNames.getValue(ConfigurationUpdate::class)
    is ConsoleChatUpdate    -> packetTopicNames.getValue(ConsoleChatUpdate::class)
    is ConsoleCommandUpdate -> packetTopicNames.getValue(ConsoleCommandUpdate::class)
    is PrototypesUpdate     -> packetTopicNames.getValue(PrototypesUpdate::class)
    is SurfaceUpdate        -> packetTopicNames.getValue(SurfaceUpdate::class)
    is EntityUpdate         -> packetTopicNames.getValue(EntityUpdate::class)
    is MapChunkUpdate       -> packetTopicNames.getValue(MapChunkUpdate::class)
    is PlayerUpdate         -> packetTopicNames.getValue(PlayerUpdate::class)
  }


private val packetTopicNames: Map<KClass<out KafkatorioPacketData2>, String> = mapOf(
  ConfigurationUpdate::class to "configuration",
  ConsoleChatUpdate::class to "console-chat",
  ConsoleCommandUpdate::class to "console-command",
  PrototypesUpdate::class to "prototypes",
  SurfaceUpdate::class to "surface",
  EntityUpdate::class to "entity",
  MapChunkUpdate::class to "map-chunk",
  PlayerUpdate::class to "player",
).mapValues { (_, v) ->
  "$DOMAIN.packet.$v"
}

val KClass<out KafkatorioPacketData2>.topicName: String
  get() = packetTopicNames.getValue(this)

//fun packetTopic(
//  dataType: KafkatorioKeyedPacketData.KafkatorioKeyedPacketDataType,
//): String = packetTopic(KafkatorioPacketType.KEYED, dataType.name.lowercase())
//
//private fun packetTopic(
//  packetType: KafkatorioPacketType,
//  dataType: String,
//): String {
//  return "$DOMAIN.packet.$packetType.$dataType"
//}

//sealed interface KafkatorioTopic {
//
//  val type: String
//  val entity: String
//  val description: String
//
//  companion object {
//    private const val DOMAIN = "kafkatorio"
//
//    val KafkatorioTopic.topic
//      get() = listOf(DOMAIN, type, entity, description).joinToString(".")
//
//  }
//
//  data class Event(
//    override val entity: String,
//    override val description: String,
//  ) : KafkatorioTopic {
//    override val type: String = "event"
//  }
//
//
//  data class State(
//    override val entity: String,
//    override val description: String,
//  ) : KafkatorioTopic {
//    override val type: String = "state"
//  }
//
//}
