package dev.adamko.kafkatorio.processor.admin

import dev.adamko.kafkatorio.schema.packets.ConfigurationUpdate
import dev.adamko.kafkatorio.schema.packets.ConsoleChatUpdate
import dev.adamko.kafkatorio.schema.packets.ConsoleCommandUpdate
import dev.adamko.kafkatorio.schema.packets.EntityUpdate
import dev.adamko.kafkatorio.schema.packets.KafkatorioPacketData
import dev.adamko.kafkatorio.schema.packets.MapChunkUpdate
import dev.adamko.kafkatorio.schema.packets.PlayerUpdate
import dev.adamko.kafkatorio.schema.packets.PrototypesUpdate
import dev.adamko.kafkatorio.schema.packets.SurfaceUpdate
import kotlin.reflect.KClass


private const val DOMAIN = "kafkatorio"

const val TOPIC_SRC_SERVER_LOG = "$DOMAIN.src.server-log"

const val TOPIC_GROUPED_MAP_CHUNKS_STATE = "$DOMAIN.state.map-chunks.grouped"

const val TOPIC_MAP_CHUNK_COLOURED_032_STATE = "$DOMAIN.state.map-chunk-032.colour"

const val TOPIC_MAP_CHUNK_COLOURED_STATE = "$DOMAIN.state.map-chunk.colour"

const val TOPIC_SUBDIVIDED_MAP_TILES = "$DOMAIN.map-tiles.subdivided"
const val TOPIC_SUBDIVIDED_MAP_TILES_DEBOUNCED = "$TOPIC_SUBDIVIDED_MAP_TILES.debounced"


fun allTopics(): Set<String> = buildSet {
  add(TOPIC_SRC_SERVER_LOG)

  add(TOPIC_GROUPED_MAP_CHUNKS_STATE)

  add(TOPIC_MAP_CHUNK_COLOURED_032_STATE)

  add(TOPIC_MAP_CHUNK_COLOURED_STATE)

  add(TOPIC_SUBDIVIDED_MAP_TILES)
  add(TOPIC_SUBDIVIDED_MAP_TILES_DEBOUNCED)

  addAll(packetTopicNames.values)
}

val KafkatorioPacketData.topicName: String
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


private val packetTopicNames: Map<KClass<out KafkatorioPacketData>, String> = mapOf(
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

val KClass<out KafkatorioPacketData>.topicName: String
  get() = packetTopicNames.getValue(this)
