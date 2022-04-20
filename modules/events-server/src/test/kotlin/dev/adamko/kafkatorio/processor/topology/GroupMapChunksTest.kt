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
import dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2
import dev.adamko.kafkatorio.schema2.KafkatorioPacket2
import dev.adamko.kafkatorio.schema2.MapChunkUpdate
import dev.adamko.kafkatorio.schema2.MapChunkUpdateKey
import dev.adamko.kafkatorio.schema2.PrototypesUpdate
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

  test("basic test") {
    val streamsBuilder = StreamsBuilder()

    val prototypesTopicName = "kafkatorio.packet.prototypes"
    val mapChunkUpdatesTopicName = "kafkatorio.packet.map-chunk"

    val topology = groupMapChunks(streamsBuilder)

    val testDriver = TopologyTestDriver(topology)

    val prototypesTestInputTopic: TestInputTopic<FactorioServerId, KafkatorioPacket2> =
      testDriver.createInputTopic(
        prototypesTopicName,
        jsonMapper.serde<FactorioServerId>().serializer(),
        jsonMapper.serde<KafkatorioPacket2>().serializer(),
      )

    val serverId = FactorioServerId("test-server-id")

    prototypesTestInputTopic.pipeInput(
      serverId,
      KafkatorioPacket2(
        modVersion = "1.2.3",
        tick = Tick(22u),
        PrototypesUpdate(
          listOf(
            FactorioPrototype2.MapTile(
              PrototypeName("fake-proto"),
              layer = 1u,
              mapColour = Colour(0.3f, 0.2f, 0.1f, 1.0f),
              collisionMasks = listOf("fake-mask"),
              order = "fake-order",
              canBeMined = false,
            )
          )
        )
      )
    )

    val mapChunkUpdatesInputTopic: TestInputTopic<FactorioServerId, KafkatorioPacket2> =
      testDriver.createInputTopic(
        mapChunkUpdatesTopicName,
        jsonMapper.serde<FactorioServerId>().serializer(),
        jsonMapper.serde<KafkatorioPacket2>().serializer(),
      )

    mapChunkUpdatesInputTopic.pipeInput(
      serverId,
      KafkatorioPacket2(
        modVersion = "1.2.3",
        tick = Tick(44u),
        MapChunkUpdate(
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
      )
    )

    val outputTopic: TestOutputTopic<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> =
      testDriver
        .createOutputTopic(
          TOPIC_GROUPED_MAP_CHUNKS_STATE,
          kxsBinary.serde<ServerMapChunkId>().deserializer(),
          kxsBinary.serde<ServerMapChunkTiles<ColourHex>>().deserializer(),
        )

    val records = outputTopic.readRecordsToList()

    withClue(records) {
      records shouldHaveSize 1

      records.map { it.key to it.value }.first() should { (key, value) ->
        key.shouldNotBeNull()
        key shouldBeEqualToComparingFields ServerMapChunkId(
          serverId = serverId,
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
})
