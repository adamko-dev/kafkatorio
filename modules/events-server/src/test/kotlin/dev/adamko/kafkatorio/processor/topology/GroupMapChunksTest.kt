package dev.adamko.kafkatorio.processor.topology

import dev.adamko.kafkatorio.events.schema.Colour
import dev.adamko.kafkatorio.events.schema.ColourHex
import dev.adamko.kafkatorio.events.schema.FactorioEventUpdatePacket
import dev.adamko.kafkatorio.events.schema.FactorioPrototypes
import dev.adamko.kafkatorio.events.schema.MapChunkPosition
import dev.adamko.kafkatorio.events.schema.MapChunkUpdate
import dev.adamko.kafkatorio.events.schema.MapTileDictionary
import dev.adamko.kafkatorio.events.schema.MapTilePosition
import dev.adamko.kafkatorio.events.schema.MapTilePrototype
import dev.adamko.kafkatorio.events.schema.PrototypeKey
import dev.adamko.kafkatorio.events.schema.PrototypeName
import dev.adamko.kafkatorio.events.schema.SurfaceIndex
import dev.adamko.kafkatorio.events.schema.Tick
import dev.adamko.kafkatorio.processor.KafkatorioTopology
import dev.adamko.kafkatorio.processor.serdes.jsonMapper
import dev.adamko.kafkatorio.processor.serdes.kxsBinary
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

    val prototypesTopicName = "prototypes-topic"
    val mapChunkUpdatesTopicName = "map-chunk-updates-topic"

    val topology = groupMapChunks(
      StreamsBuilder(),
      prototypesTopicName,
      mapChunkUpdatesTopicName,
    )

    val testDriver = TopologyTestDriver(topology)

    val prototypesTestInputTopic: TestInputTopic<FactorioServerId, FactorioPrototypes> =
      testDriver.createInputTopic(
        prototypesTopicName,
        jsonMapper.serde<FactorioServerId>().serializer(),
        jsonMapper.serde<FactorioPrototypes>().serializer(),
      )

    val serverId = FactorioServerId("test-server-id")

    prototypesTestInputTopic.pipeInput(
      serverId,
      FactorioPrototypes(
        modVersion = "1.2.3",
        tick = Tick(456u),
        listOf(
          MapTilePrototype(
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

    val mapChunkUpdatesInputTopic: TestInputTopic<FactorioServerId, FactorioEventUpdatePacket> =
      testDriver.createInputTopic(
        mapChunkUpdatesTopicName,
        jsonMapper.serde<FactorioServerId>().serializer(),
        jsonMapper.serde<FactorioEventUpdatePacket>().serializer(),
      )
    mapChunkUpdatesInputTopic.pipeInput(
      serverId,
      FactorioEventUpdatePacket(
        modVersion = "1.2.3",
        tick = Tick(854u),
        update = MapChunkUpdate(
          MapChunkPosition(11, 22),
          SurfaceIndex(1u),
          tileDictionary = MapTileDictionary(
            tilesXY = mapOf(
              "1" to mapOf("2" to PrototypeKey("33"))
            ),
            protos = mapOf(PrototypeKey("33") to PrototypeName("fake-proto"))
          )
        )
      )
    )

    val outputTopic: TestOutputTopic<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> =
      testDriver
        .createOutputTopic(
          KafkatorioTopology.TOPIC_GROUPED_MAP_CHUNKS,
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