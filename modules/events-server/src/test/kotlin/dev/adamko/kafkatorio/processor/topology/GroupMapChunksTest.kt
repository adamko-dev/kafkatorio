package dev.adamko.kafkatorio.processor.topology

import dev.adamko.kafkatorio.processor.admin.TOPIC_GROUPED_MAP_CHUNKS_STATE
import dev.adamko.kafkatorio.processor.serdes.jsonMapper
import dev.adamko.kafkatorio.processor.serdes.kxsBinary
import dev.adamko.kafkatorio.schema.common.Colour
import dev.adamko.kafkatorio.schema.common.ColourHex
import dev.adamko.kafkatorio.schema.common.MapChunkPosition
import dev.adamko.kafkatorio.schema.common.MapTileDictionary
import dev.adamko.kafkatorio.schema.common.MapTileDictionary.PrototypeKey
import dev.adamko.kafkatorio.schema.common.MapTilePosition
import dev.adamko.kafkatorio.schema.common.PrototypeName
import dev.adamko.kafkatorio.schema.common.SurfaceIndex
import dev.adamko.kafkatorio.schema.common.Tick
import dev.adamko.kafkatorio.schema.packets.KafkatorioPacket
import dev.adamko.kafkatorio.schema.packets.MapChunkUpdate
import dev.adamko.kafkatorio.schema.packets.MapChunkUpdateKey
import dev.adamko.kafkatorio.schema.packets.PrototypesUpdate
import dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype
import dev.adamko.kotka.kxs.serde
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.TestInputTopic
import org.apache.kafka.streams.TestOutputTopic
import org.apache.kafka.streams.TopologyTestDriver


class GroupMapChunksTest : FunSpec({

  test("proto, then map chunk") {

    val scenario = Scenario()

    scenario.pipePrototypePacket(
      PrototypesUpdate(listOf(testMockTile))
    )

    scenario.pipeMapChunkPacket(testMapChunkUpdate)

    val records = scenario.mapTilesOutputTopic.readRecordsToList()

    withClue(records) {
      records shouldHaveSize 1

      records.map { it.key to it.value }.first() should { (key, value) ->
        key.shouldNotBeNull()
        key shouldBeEqualToComparingFields ServerMapChunkId(
          serverId = FactorioServerId("test-server-id"),
          chunkPosition = MapChunkPosition(0, 0),
          surfaceIndex = SurfaceIndex(1u),
          chunkSize = ChunkSize.CHUNK_512,
        )
        value.shouldNotBeNull()
        value shouldBeEqualToComparingFields ServerMapChunkTiles(
          chunkId = key,
          map = mapOf(
            MapTilePosition(1, 2) to ColourHex(76u, 51u, 25u, 255u)
          )
        )
      }
    }
  }

  test("map chunk, then proto") {

    val scenario = Scenario()

    scenario.pipePrototypePacket(
      PrototypesUpdate(listOf(testMockTile.copy(mapColour = COLOUR_ONE)))
    )

    scenario.pipeMapChunkPacket(testMapChunkUpdate)

    var records = scenario.mapTilesOutputTopic.readRecordsToList()

    withClue(records) {
      records shouldHaveSize 1

      records.map { it.key to it.value }.first() should { (key, value) ->
        key.shouldNotBeNull()
        key shouldBeEqualToComparingFields ServerMapChunkId(
          serverId = FactorioServerId("test-server-id"),
          chunkPosition = MapChunkPosition(0, 0),
          surfaceIndex = SurfaceIndex(1u),
          chunkSize = ChunkSize.CHUNK_512,
        )
        value.shouldNotBeNull()
        value shouldBeEqualToComparingFields ServerMapChunkTiles(
          chunkId = key,
          map = mapOf(
            MapTilePosition(1, 2) to ColourHex(25u, 25u, 25u, 25u)
          )
        )
      }
    }

    scenario.pipePrototypePacket(
      PrototypesUpdate(listOf(testMockTile.copy(mapColour = COLOUR_TWO)))
    )

    records = scenario.mapTilesOutputTopic.readRecordsToList()

    withClue(records) {
      records shouldHaveSize 1

      records.map { it.key to it.value }.first() should { (key, value) ->
        key.shouldNotBeNull()
        key shouldBeEqualToComparingFields ServerMapChunkId(
          serverId = FactorioServerId("test-server-id"),
          chunkPosition = MapChunkPosition(0, 0),
          surfaceIndex = SurfaceIndex(1u),
          chunkSize = ChunkSize.CHUNK_512,
        )
        value.shouldNotBeNull()
        value shouldBeEqualToComparingFields ServerMapChunkTiles(
          chunkId = key,
          map = mapOf(
            MapTilePosition(1, 2) to ColourHex(51u, 51u, 51u, 51u)
          )
        )
      }
    }
  }
}) {

  class Scenario(
    streamsBuilder: StreamsBuilder = StreamsBuilder(),

    prototypesTopicName: String = "kafkatorio.packet.prototypes",
    mapChunkUpdatesTopicName: String = "kafkatorio.packet.map-chunk",

    private val serverId: FactorioServerId = FactorioServerId("test-server-id"),
  ) {
    private val topology = groupMapChunks(streamsBuilder)
    private val testDriver: TopologyTestDriver = TopologyTestDriver(topology)

    private val prototypesTestInputTopic: TestInputTopic<FactorioServerId, KafkatorioPacket> =
      testDriver.createInputTopic(
        prototypesTopicName,
        jsonMapper.serde<FactorioServerId>().serializer(),
        jsonMapper.serde<KafkatorioPacket>().serializer(),
      )

    private val mapChunkUpdatesInputTopic: TestInputTopic<FactorioServerId, KafkatorioPacket> =
      testDriver.createInputTopic(
        mapChunkUpdatesTopicName,
        jsonMapper.serde<FactorioServerId>().serializer(),
        jsonMapper.serde<KafkatorioPacket>().serializer(),
      )

    val mapTilesOutputTopic: TestOutputTopic<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> =
      testDriver.createOutputTopic(
          TOPIC_GROUPED_MAP_CHUNKS_STATE,
          kxsBinary.serde<ServerMapChunkId>().deserializer(),
          kxsBinary.serde<ServerMapChunkTiles<ColourHex>>().deserializer(),
        )

    fun pipePrototypePacket(prototypeUpdate: PrototypesUpdate) {
      prototypesTestInputTopic.pipeInput(
        serverId,
        KafkatorioPacket(
          modVersion = "1.2.3",
          tick = Tick(22u),
          data = prototypeUpdate,
        )
      )
    }

    fun pipeMapChunkPacket(mapChunkUpdate: MapChunkUpdate) {
      mapChunkUpdatesInputTopic.pipeInput(
        serverId,
        KafkatorioPacket(
          modVersion = "1.2.3",
          tick = Tick(44u),
          data = mapChunkUpdate
        )
      )
    }
  }

  companion object {
    val COLOUR_ONE = Colour(0.1f, 0.1f, 0.1f, 0.1f)
    val COLOUR_TWO = Colour(0.2f, 0.2f, 0.2f, 0.2f)

    val testMockTile = FactorioPrototype.MapTile(
      PrototypeName("fake-proto"),
      layer = 1u,
      mapColour = Colour(0.3f, 0.2f, 0.1f, 1.0f),
      collisionMasks = listOf("fake-mask"),
      order = "fake-order",
      canBeMined = false,
    )

    val testMapChunkUpdate = MapChunkUpdate(
      key = MapChunkUpdateKey(
        MapChunkPosition(11, 22),
        SurfaceIndex(1u),
      ),
      tileDictionary = MapTileDictionary(
        tilesXY = mapOf(
          "1" to mapOf("2" to PrototypeKey(33))
        ),
        protos = mapOf(PrototypeName("fake-proto") to PrototypeKey(33))
      )
    )
  }
}
