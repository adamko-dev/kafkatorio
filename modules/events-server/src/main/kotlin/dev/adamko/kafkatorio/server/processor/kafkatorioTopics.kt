package dev.adamko.kafkatorio.server.processor

import dev.adamko.kafkatorio.schema.packets.ConfigurationUpdate
import dev.adamko.kafkatorio.schema.packets.ConsoleChatUpdate
import dev.adamko.kafkatorio.schema.packets.ConsoleCommandUpdate
import dev.adamko.kafkatorio.schema.packets.EntityUpdate
import dev.adamko.kafkatorio.schema.packets.KafkatorioPacketData
import dev.adamko.kafkatorio.schema.packets.KafkatorioPacketDataError
import dev.adamko.kafkatorio.schema.packets.MapChunkEntityUpdate
import dev.adamko.kafkatorio.schema.packets.MapChunkResourceUpdate
import dev.adamko.kafkatorio.schema.packets.MapChunkTileUpdate
import dev.adamko.kafkatorio.schema.packets.PlayerUpdate
import dev.adamko.kafkatorio.schema.packets.PrototypesUpdate
import dev.adamko.kafkatorio.schema.packets.SurfaceUpdate
import kotlin.reflect.KClass


private const val DOMAIN = "kafkatorio"

const val TOPIC_SRC_SERVER_LOG = "$DOMAIN.src.server-log"

//const val TOPIC_GROUPED_MAP_CHUNKS_STATE = "$DOMAIN.state.map-chunks.grouped"

const val TOPIC_MAP_CHUNK_TERRAIN_COLOURED_032_UPDATES =
  "$DOMAIN.state-updates.map-chunk-032.terrain.colour"
const val TOPIC_MAP_CHUNK_RESOURCE_COLOURED_032_UPDATES =
  "$DOMAIN.state-updates.map-chunk-032.resource.colour"
const val TOPIC_MAP_CHUNK_BUILDING_COLOURED_032_UPDATES =
  "$DOMAIN.state-updates.map-chunk-032.building.colour"

const val TOPIC_MAP_CHUNK_TERRAIN_COLOURED_STATE = "$DOMAIN.state.map-chunk.terrain.colour"
const val TOPIC_MAP_CHUNK_RESOURCE_COLOURED_STATE = "$DOMAIN.state.map-chunk.resource.colour"
const val TOPIC_MAP_CHUNK_BUILDING_COLOURED_STATE = "$DOMAIN.state.map-chunk.building.colour"

//const val TOPIC_SUBDIVIDED_MAP_TILES = "$DOMAIN.map-tiles.subdivided"
//const val TOPIC_SUBDIVIDED_MAP_TILES_DEBOUNCED = "$TOPIC_SUBDIVIDED_MAP_TILES.debounced"


/** Broadcast these events out, via websocket */
const val TOPIC_BROADCAST_TO_WEBSOCKET = "$DOMAIN.broadcast.websocket"

fun allTopics(): Set<String> = buildSet {
  add(TOPIC_SRC_SERVER_LOG)

//  add(TOPIC_GROUPED_MAP_CHUNKS_STATE)

  add(TOPIC_MAP_CHUNK_TERRAIN_COLOURED_032_UPDATES)
  add(TOPIC_MAP_CHUNK_RESOURCE_COLOURED_032_UPDATES)
  add(TOPIC_MAP_CHUNK_BUILDING_COLOURED_032_UPDATES)

  add(TOPIC_MAP_CHUNK_TERRAIN_COLOURED_STATE)
  add(TOPIC_MAP_CHUNK_RESOURCE_COLOURED_STATE)
  add(TOPIC_MAP_CHUNK_BUILDING_COLOURED_STATE)

//  add(TOPIC_SUBDIVIDED_MAP_TILES)
//  add(TOPIC_SUBDIVIDED_MAP_TILES_DEBOUNCED)

  add(TOPIC_BROADCAST_TO_WEBSOCKET)

  addAll(packetTopicNames.values)
}

val KafkatorioPacketData.topicName: String
  get() = when (this) {
    is ConfigurationUpdate       -> packetTopicNames.getValue(ConfigurationUpdate::class)
    is ConsoleChatUpdate         -> packetTopicNames.getValue(ConsoleChatUpdate::class)
    is ConsoleCommandUpdate      -> packetTopicNames.getValue(ConsoleCommandUpdate::class)
    is PrototypesUpdate          -> packetTopicNames.getValue(PrototypesUpdate::class)
    is SurfaceUpdate             -> packetTopicNames.getValue(SurfaceUpdate::class)
    is EntityUpdate              -> packetTopicNames.getValue(EntityUpdate::class)
    is MapChunkTileUpdate        -> packetTopicNames.getValue(MapChunkTileUpdate::class)
    is PlayerUpdate              -> packetTopicNames.getValue(PlayerUpdate::class)
    is KafkatorioPacketDataError -> packetTopicNames.getValue(KafkatorioPacketDataError::class)
    is MapChunkEntityUpdate      -> packetTopicNames.getValue(MapChunkEntityUpdate::class)
    is MapChunkResourceUpdate    -> packetTopicNames.getValue(MapChunkResourceUpdate::class)
  }


private val packetTopicNames: Map<KClass<out KafkatorioPacketData>, String> = mapOf(
  ConfigurationUpdate::class to "configuration",
  ConsoleChatUpdate::class to "console-chat",
  ConsoleCommandUpdate::class to "console-command",
  PrototypesUpdate::class to "prototypes",
  SurfaceUpdate::class to "surface",
  EntityUpdate::class to "entity",
  MapChunkTileUpdate::class to "map-chunk-tile",
  PlayerUpdate::class to "player",
  MapChunkEntityUpdate::class to "map-chunk-entity",
  MapChunkResourceUpdate::class to "map-chunk-resource",
  KafkatorioPacketDataError::class to "error",
).mapValues { (_, v) ->
  "$DOMAIN.packet.$v"
}


val KClass<out KafkatorioPacketData>.topicName: String
  get() = packetTopicNames.getValue(this)
