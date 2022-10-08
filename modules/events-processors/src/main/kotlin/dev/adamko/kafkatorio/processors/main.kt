package dev.adamko.kafkatorio.processors

import dev.adamko.kafkatorio.processor.config.TOPIC_MAP_CHUNK_BUILDING_COLOURED_032_UPDATES
import dev.adamko.kafkatorio.processor.config.TOPIC_MAP_CHUNK_BUILDING_COLOURED_STATE
import dev.adamko.kafkatorio.processor.config.TOPIC_MAP_CHUNK_RESOURCE_COLOURED_032_UPDATES
import dev.adamko.kafkatorio.processor.config.TOPIC_MAP_CHUNK_RESOURCE_COLOURED_STATE
import dev.adamko.kafkatorio.schema.common.ServerMapTileLayer
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


suspend fun main(): Unit = coroutineScope {

  createTopics()

  launch {
    splitPackets()
  }

  launch {
    groupTilesMapChunks()
  }

  launch {
    groupEntitiesMapChunks(
      layer = ServerMapTileLayer.BUILDING,
      colouredUpdatesStreamTopic = TOPIC_MAP_CHUNK_BUILDING_COLOURED_032_UPDATES,
      colouredChunkStateTopic = TOPIC_MAP_CHUNK_BUILDING_COLOURED_STATE,
    )
  }

  launch {
    groupEntitiesMapChunks(
      layer = ServerMapTileLayer.RESOURCE,
      colouredUpdatesStreamTopic = TOPIC_MAP_CHUNK_RESOURCE_COLOURED_032_UPDATES,
      colouredChunkStateTopic = TOPIC_MAP_CHUNK_RESOURCE_COLOURED_STATE,
    )
  }
}
