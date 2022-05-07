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
import dev.adamko.kafkatorio.schema.common.toHex
import dev.adamko.kafkatorio.schema.packets.KafkatorioPacket
import dev.adamko.kafkatorio.schema.packets.MapChunkUpdate
import dev.adamko.kafkatorio.schema.packets.MapChunkUpdateKey
import dev.adamko.kafkatorio.schema.packets.PrototypesUpdate
import dev.adamko.kafkatorio.schema.common.FactorioPrototype
import dev.adamko.kotka.kxs.serde
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainAnyOf
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import kotlinx.serialization.decodeFromString
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

  test("raw actual JSON messages") {

    val scenario = Scenario()

    val prototypesPacket: KafkatorioPacket = jsonMapper.decodeFromString(rawPrototypesJson)
    scenario.prototypesInputTopic.pipeInput(scenario.serverId, prototypesPacket)

    val mapChunkPacket: KafkatorioPacket = jsonMapper.decodeFromString(rawMapChunkUpdateJson)
    scenario.mapChunkInputTopic.pipeInput(scenario.serverId, mapChunkPacket)

    val records = scenario.mapTilesOutputTopic.readRecordsToList()

    records shouldHaveSize 1

    records.map { it.key to it.value }.first() should { (key, value) ->
      key.shouldNotBeNull()
      key shouldBeEqualToComparingFields ServerMapChunkId(
        serverId = FactorioServerId("test-server-id"),
        chunkPosition = MapChunkPosition(-1, -1),
        surfaceIndex = SurfaceIndex(1u),
        chunkSize = ChunkSize.CHUNK_512,
      )
      value.shouldNotBeNull()

      value.chunkId shouldBeEqualToComparingFields key

      value.map.values.toSet().shouldContainAnyOf(
        (prototypesPacket.data as PrototypesUpdate).prototypes
          .filterIsInstance<FactorioPrototype.MapTile>()
          .map {
            it.mapColour.toHex()
          }.toSet()
      )

      val top: Int = value.map.keys.minOf { it.y }
      val bottom = value.map.keys.maxOf { it.y }
      val left = value.map.keys.minOf { it.x }
      val right = value.map.keys.maxOf { it.x }

      val colours: MutableMap<ColourHex?, String> = mutableMapOf(null to " ")

      val mapRepresentation = buildString {
        (top..bottom).forEach { y ->
          (left..right).forEach { x ->
            val colour = value.map[MapTilePosition(x, y)]
            val a = colours.getOrPut(colour) { "${colours.size}" }
            append(a)
          }
          appendLine()
        }
      }
      println(mapRepresentation)


//        value shouldBeEqualToComparingFields ServerMapChunkTiles(
//          chunkId = key,
//          map = mapOf(
//            MapTilePosition(x=-224, y=-224) to ColourHex(red=91u, green=63u, blue=38u, alpha=255u),
//            MapTilePosition(x=-224, y=-223) to ColourHex(red=91u, green=63u, blue=38u, alpha=255u),
//            MapTilePosition(x=-224, y=-222) to ColourHex(red=91u, green=63u, blue=38u, alpha=255u),
//            MapTilePosition(x=-224, y=-221) to ColourHex(red=66u, green=57u, blue=15u, alpha=255u),
//            MapTilePosition(x=-224, y=-220) to ColourHex(red=66u, green=57u, blue=15u, alpha=255u),
//            MapTilePosition(x=-224, y=-219) to ColourHex(red=66u, green=57u, blue=15u, alpha=255u),
//            MapTilePosition(x=-224, y=-218) to ColourHex(red=66u, green=57u, blue=15u, alpha=255u),
//            MapTilePosition(x=-224, y=-217) to ColourHex(red=66u, green=57u, blue=15u, alpha=255u),
//            MapTilePosition(x=-224, y=-216) to ColourHex(red=66u, green=57u, blue=15u, alpha=255u),
//            MapTilePosition(x=-224, y=-215) to ColourHex(red=66u, green=57u, blue=15u, alpha=255u),
//          )
//        )
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

    val serverId: FactorioServerId = FactorioServerId("test-server-id"),
  ) {
    private val topology = groupMapChunks(streamsBuilder)
    private val testDriver: TopologyTestDriver = TopologyTestDriver(topology)

    val prototypesInputTopic: TestInputTopic<FactorioServerId, KafkatorioPacket> =
      testDriver.createInputTopic(
        prototypesTopicName,
        jsonMapper.serde<FactorioServerId>().serializer(),
        jsonMapper.serde<KafkatorioPacket>().serializer(),
      )

    val mapChunkInputTopic: TestInputTopic<FactorioServerId, KafkatorioPacket> =
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
      prototypesInputTopic.pipeInput(
        serverId,
        KafkatorioPacket(
          modVersion = "1.2.3",
          tick = Tick(22u),
          data = prototypeUpdate,
        )
      )
    }

    fun pipeMapChunkPacket(mapChunkUpdate: MapChunkUpdate) {
      mapChunkInputTopic.pipeInput(
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

    const val rawMapChunkUpdateJson = """
      {
      	"modVersion": "0.4.0",
      	"tick": 36,
      	"data": {
      		"type": "kafkatorio.packet.keyed.MapChunkUpdate",
      		"key": {
      			"chunkPosition": [
      				-7,
      				-7
      			],
      			"surfaceIndex": 1
      		},
      		"events": {
      			"on_chunk_generated": [1]
      		},
      		"tileDictionary": {
      			"tilesXY": {
      				"-224": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 1,
      					"-220": 1,
      					"-219": 1,
      					"-218": 1,
      					"-217": 1,
      					"-216": 1,
      					"-215": 1,
      					"-214": 1,
      					"-213": 1,
      					"-212": 1,
      					"-211": 1,
      					"-210": 1,
      					"-209": 1,
      					"-208": 1,
      					"-207": 2,
      					"-206": 2,
      					"-205": 2,
      					"-204": 2,
      					"-203": 2,
      					"-202": 2,
      					"-201": 2,
      					"-200": 2,
      					"-199": 2,
      					"-198": 2,
      					"-197": 2,
      					"-196": 2,
      					"-195": 2,
      					"-194": 2,
      					"-193": 2
      				},
      				"-223": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 1,
      					"-218": 1,
      					"-217": 1,
      					"-216": 1,
      					"-215": 1,
      					"-214": 1,
      					"-213": 1,
      					"-212": 1,
      					"-211": 1,
      					"-210": 1,
      					"-209": 1,
      					"-208": 1,
      					"-207": 2,
      					"-206": 2,
      					"-205": 2,
      					"-204": 2,
      					"-203": 2,
      					"-202": 2,
      					"-201": 2,
      					"-200": 2,
      					"-199": 2,
      					"-198": 2,
      					"-197": 2,
      					"-196": 2,
      					"-195": 2,
      					"-194": 2,
      					"-193": 2
      				},
      				"-222": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 0,
      					"-218": 1,
      					"-217": 1,
      					"-216": 1,
      					"-215": 1,
      					"-214": 1,
      					"-213": 1,
      					"-212": 1,
      					"-211": 1,
      					"-210": 1,
      					"-209": 1,
      					"-208": 1,
      					"-207": 2,
      					"-206": 2,
      					"-205": 2,
      					"-204": 2,
      					"-203": 2,
      					"-202": 2,
      					"-201": 2,
      					"-200": 2,
      					"-199": 2,
      					"-198": 2,
      					"-197": 2,
      					"-196": 2,
      					"-195": 2,
      					"-194": 2,
      					"-193": 2
      				},
      				"-221": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 0,
      					"-218": 0,
      					"-217": 1,
      					"-216": 1,
      					"-215": 1,
      					"-214": 1,
      					"-213": 1,
      					"-212": 1,
      					"-211": 1,
      					"-210": 1,
      					"-209": 1,
      					"-208": 1,
      					"-207": 2,
      					"-206": 2,
      					"-205": 2,
      					"-204": 2,
      					"-203": 2,
      					"-202": 2,
      					"-201": 2,
      					"-200": 2,
      					"-199": 2,
      					"-198": 2,
      					"-197": 2,
      					"-196": 2,
      					"-195": 2,
      					"-194": 2,
      					"-193": 1
      				},
      				"-220": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 0,
      					"-218": 0,
      					"-217": 0,
      					"-216": 1,
      					"-215": 1,
      					"-214": 1,
      					"-213": 1,
      					"-212": 1,
      					"-211": 1,
      					"-210": 1,
      					"-209": 1,
      					"-208": 1,
      					"-207": 1,
      					"-206": 2,
      					"-205": 2,
      					"-204": 2,
      					"-203": 2,
      					"-202": 2,
      					"-201": 2,
      					"-200": 2,
      					"-199": 2,
      					"-198": 2,
      					"-197": 2,
      					"-196": 2,
      					"-195": 2,
      					"-194": 2,
      					"-193": 2
      				},
      				"-219": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 0,
      					"-218": 0,
      					"-217": 0,
      					"-216": 0,
      					"-215": 0,
      					"-214": 0,
      					"-213": 0,
      					"-212": 0,
      					"-211": 0,
      					"-210": 0,
      					"-209": 1,
      					"-208": 1,
      					"-207": 1,
      					"-206": 2,
      					"-205": 2,
      					"-204": 2,
      					"-203": 2,
      					"-202": 2,
      					"-201": 2,
      					"-200": 2,
      					"-199": 2,
      					"-198": 2,
      					"-197": 2,
      					"-196": 2,
      					"-195": 2,
      					"-194": 2,
      					"-193": 2
      				},
      				"-218": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 0,
      					"-218": 0,
      					"-217": 0,
      					"-216": 0,
      					"-215": 0,
      					"-214": 0,
      					"-213": 0,
      					"-212": 0,
      					"-211": 0,
      					"-210": 0,
      					"-209": 1,
      					"-208": 1,
      					"-207": 1,
      					"-206": 1,
      					"-205": 2,
      					"-204": 2,
      					"-203": 2,
      					"-202": 2,
      					"-201": 2,
      					"-200": 2,
      					"-199": 2,
      					"-198": 2,
      					"-197": 2,
      					"-196": 2,
      					"-195": 2,
      					"-194": 2,
      					"-193": 2
      				},
      				"-217": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 0,
      					"-218": 0,
      					"-217": 0,
      					"-216": 0,
      					"-215": 0,
      					"-214": 0,
      					"-213": 0,
      					"-212": 0,
      					"-211": 0,
      					"-210": 1,
      					"-209": 1,
      					"-208": 1,
      					"-207": 1,
      					"-206": 1,
      					"-205": 1,
      					"-204": 2,
      					"-203": 2,
      					"-202": 2,
      					"-201": 2,
      					"-200": 2,
      					"-199": 2,
      					"-198": 2,
      					"-197": 2,
      					"-196": 2,
      					"-195": 2,
      					"-194": 2,
      					"-193": 2
      				},
      				"-216": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 0,
      					"-218": 0,
      					"-217": 0,
      					"-216": 0,
      					"-215": 0,
      					"-214": 0,
      					"-213": 0,
      					"-212": 0,
      					"-211": 1,
      					"-210": 1,
      					"-209": 1,
      					"-208": 1,
      					"-207": 1,
      					"-206": 1,
      					"-205": 1,
      					"-204": 2,
      					"-203": 2,
      					"-202": 2,
      					"-201": 2,
      					"-200": 2,
      					"-199": 2,
      					"-198": 2,
      					"-197": 2,
      					"-196": 2,
      					"-195": 2,
      					"-194": 2,
      					"-193": 2
      				},
      				"-215": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 0,
      					"-218": 0,
      					"-217": 0,
      					"-216": 0,
      					"-215": 0,
      					"-214": 0,
      					"-213": 0,
      					"-212": 0,
      					"-211": 1,
      					"-210": 1,
      					"-209": 1,
      					"-208": 1,
      					"-207": 1,
      					"-206": 1,
      					"-205": 1,
      					"-204": 2,
      					"-203": 2,
      					"-202": 2,
      					"-201": 2,
      					"-200": 2,
      					"-199": 2,
      					"-198": 2,
      					"-197": 2,
      					"-196": 2,
      					"-195": 2,
      					"-194": 2,
      					"-193": 2
      				},
      				"-214": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 0,
      					"-218": 0,
      					"-217": 0,
      					"-216": 0,
      					"-215": 0,
      					"-214": 0,
      					"-213": 0,
      					"-212": 0,
      					"-211": 1,
      					"-210": 1,
      					"-209": 1,
      					"-208": 1,
      					"-207": 1,
      					"-206": 1,
      					"-205": 1,
      					"-204": 1,
      					"-203": 2,
      					"-202": 2,
      					"-201": 2,
      					"-200": 2,
      					"-199": 2,
      					"-198": 2,
      					"-197": 2,
      					"-196": 2,
      					"-195": 2,
      					"-194": 2,
      					"-193": 2
      				},
      				"-213": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 0,
      					"-218": 0,
      					"-217": 0,
      					"-216": 0,
      					"-215": 0,
      					"-214": 0,
      					"-213": 0,
      					"-212": 0,
      					"-211": 1,
      					"-210": 1,
      					"-209": 1,
      					"-208": 1,
      					"-207": 1,
      					"-206": 1,
      					"-205": 1,
      					"-204": 1,
      					"-203": 1,
      					"-202": 2,
      					"-201": 2,
      					"-200": 2,
      					"-199": 2,
      					"-198": 2,
      					"-197": 2,
      					"-196": 2,
      					"-195": 2,
      					"-194": 2,
      					"-193": 1
      				},
      				"-212": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 0,
      					"-218": 0,
      					"-217": 0,
      					"-216": 0,
      					"-215": 0,
      					"-214": 0,
      					"-213": 0,
      					"-212": 0,
      					"-211": 0,
      					"-210": 1,
      					"-209": 1,
      					"-208": 1,
      					"-207": 1,
      					"-206": 1,
      					"-205": 1,
      					"-204": 1,
      					"-203": 1,
      					"-202": 1,
      					"-201": 1,
      					"-200": 2,
      					"-199": 2,
      					"-198": 0,
      					"-197": 0,
      					"-196": 2,
      					"-195": 2,
      					"-194": 2,
      					"-193": 1
      				},
      				"-211": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 0,
      					"-218": 0,
      					"-217": 0,
      					"-216": 0,
      					"-215": 0,
      					"-214": 0,
      					"-213": 0,
      					"-212": 0,
      					"-211": 0,
      					"-210": 0,
      					"-209": 1,
      					"-208": 1,
      					"-207": 1,
      					"-206": 1,
      					"-205": 1,
      					"-204": 1,
      					"-203": 1,
      					"-202": 1,
      					"-201": 1,
      					"-200": 1,
      					"-199": 2,
      					"-198": 0,
      					"-197": 0,
      					"-196": 2,
      					"-195": 1,
      					"-194": 1,
      					"-193": 1
      				},
      				"-210": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 0,
      					"-218": 0,
      					"-217": 0,
      					"-216": 0,
      					"-215": 0,
      					"-214": 0,
      					"-213": 0,
      					"-212": 0,
      					"-211": 0,
      					"-210": 0,
      					"-209": 1,
      					"-208": 1,
      					"-207": 1,
      					"-206": 0,
      					"-205": 0,
      					"-204": 1,
      					"-203": 1,
      					"-202": 1,
      					"-201": 1,
      					"-200": 1,
      					"-199": 1,
      					"-198": 2,
      					"-197": 2,
      					"-196": 1,
      					"-195": 1,
      					"-194": 1,
      					"-193": 1
      				},
      				"-209": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 0,
      					"-218": 0,
      					"-217": 0,
      					"-216": 0,
      					"-215": 0,
      					"-214": 0,
      					"-213": 0,
      					"-212": 0,
      					"-211": 0,
      					"-210": 0,
      					"-209": 0,
      					"-208": 1,
      					"-207": 1,
      					"-206": 0,
      					"-205": 0,
      					"-204": 0,
      					"-203": 1,
      					"-202": 1,
      					"-201": 1,
      					"-200": 1,
      					"-199": 1,
      					"-198": 2,
      					"-197": 2,
      					"-196": 1,
      					"-195": 1,
      					"-194": 1,
      					"-193": 1
      				},
      				"-208": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 0,
      					"-218": 0,
      					"-217": 0,
      					"-216": 0,
      					"-215": 0,
      					"-214": 0,
      					"-213": 0,
      					"-212": 0,
      					"-211": 0,
      					"-210": 0,
      					"-209": 0,
      					"-208": 1,
      					"-207": 1,
      					"-206": 0,
      					"-205": 0,
      					"-204": 0,
      					"-203": 1,
      					"-202": 1,
      					"-201": 1,
      					"-200": 1,
      					"-199": 2,
      					"-198": 2,
      					"-197": 2,
      					"-196": 1,
      					"-195": 1,
      					"-194": 1,
      					"-193": 1
      				},
      				"-207": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 0,
      					"-218": 0,
      					"-217": 0,
      					"-216": 0,
      					"-215": 0,
      					"-214": 0,
      					"-213": 0,
      					"-212": 0,
      					"-211": 0,
      					"-210": 0,
      					"-209": 1,
      					"-208": 1,
      					"-207": 1,
      					"-206": 1,
      					"-205": 1,
      					"-204": 1,
      					"-203": 1,
      					"-202": 1,
      					"-201": 1,
      					"-200": 1,
      					"-199": 2,
      					"-198": 2,
      					"-197": 2,
      					"-196": 1,
      					"-195": 1,
      					"-194": 1,
      					"-193": 1
      				},
      				"-206": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 0,
      					"-218": 0,
      					"-217": 0,
      					"-216": 0,
      					"-215": 0,
      					"-214": 0,
      					"-213": 1,
      					"-212": 1,
      					"-211": 1,
      					"-210": 1,
      					"-209": 1,
      					"-208": 1,
      					"-207": 1,
      					"-206": 1,
      					"-205": 1,
      					"-204": 1,
      					"-203": 1,
      					"-202": 1,
      					"-201": 1,
      					"-200": 2,
      					"-199": 2,
      					"-198": 2,
      					"-197": 1,
      					"-196": 1,
      					"-195": 1,
      					"-194": 1,
      					"-193": 1
      				},
      				"-205": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 0,
      					"-218": 0,
      					"-217": 0,
      					"-216": 0,
      					"-215": 0,
      					"-214": 1,
      					"-213": 1,
      					"-212": 1,
      					"-211": 1,
      					"-210": 1,
      					"-209": 1,
      					"-208": 1,
      					"-207": 1,
      					"-206": 1,
      					"-205": 1,
      					"-204": 1,
      					"-203": 1,
      					"-202": 1,
      					"-201": 1,
      					"-200": 2,
      					"-199": 2,
      					"-198": 2,
      					"-197": 1,
      					"-196": 1,
      					"-195": 1,
      					"-194": 1,
      					"-193": 2
      				},
      				"-204": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 0,
      					"-218": 0,
      					"-217": 0,
      					"-216": 1,
      					"-215": 1,
      					"-214": 1,
      					"-213": 1,
      					"-212": 1,
      					"-211": 1,
      					"-210": 1,
      					"-209": 1,
      					"-208": 1,
      					"-207": 1,
      					"-206": 1,
      					"-205": 1,
      					"-204": 1,
      					"-203": 1,
      					"-202": 1,
      					"-201": 2,
      					"-200": 2,
      					"-199": 2,
      					"-198": 2,
      					"-197": 1,
      					"-196": 1,
      					"-195": 1,
      					"-194": 1,
      					"-193": 2
      				},
      				"-203": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 0,
      					"-218": 0,
      					"-217": 1,
      					"-216": 1,
      					"-215": 1,
      					"-214": 1,
      					"-213": 1,
      					"-212": 1,
      					"-211": 1,
      					"-210": 1,
      					"-209": 1,
      					"-208": 1,
      					"-207": 1,
      					"-206": 1,
      					"-205": 1,
      					"-204": 1,
      					"-203": 1,
      					"-202": 1,
      					"-201": 2,
      					"-200": 2,
      					"-199": 2,
      					"-198": 2,
      					"-197": 2,
      					"-196": 2,
      					"-195": 1,
      					"-194": 2,
      					"-193": 2
      				},
      				"-202": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 0,
      					"-218": 0,
      					"-217": 1,
      					"-216": 1,
      					"-215": 1,
      					"-214": 1,
      					"-213": 1,
      					"-212": 1,
      					"-211": 1,
      					"-210": 1,
      					"-209": 1,
      					"-208": 1,
      					"-207": 1,
      					"-206": 1,
      					"-205": 1,
      					"-204": 1,
      					"-203": 1,
      					"-202": 1,
      					"-201": 2,
      					"-200": 2,
      					"-199": 2,
      					"-198": 2,
      					"-197": 2,
      					"-196": 2,
      					"-195": 2,
      					"-194": 2,
      					"-193": 2
      				},
      				"-201": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 0,
      					"-218": 3,
      					"-217": 1,
      					"-216": 1,
      					"-215": 1,
      					"-214": 1,
      					"-213": 1,
      					"-212": 1,
      					"-211": 1,
      					"-210": 1,
      					"-209": 1,
      					"-208": 1,
      					"-207": 1,
      					"-206": 1,
      					"-205": 1,
      					"-204": 1,
      					"-203": 1,
      					"-202": 2,
      					"-201": 2,
      					"-200": 2,
      					"-199": 2,
      					"-198": 2,
      					"-197": 2,
      					"-196": 2,
      					"-195": 2,
      					"-194": 2,
      					"-193": 2
      				},
      				"-200": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 3,
      					"-218": 3,
      					"-217": 3,
      					"-216": 1,
      					"-215": 1,
      					"-214": 1,
      					"-213": 1,
      					"-212": 1,
      					"-211": 1,
      					"-210": 1,
      					"-209": 1,
      					"-208": 1,
      					"-207": 1,
      					"-206": 1,
      					"-205": 1,
      					"-204": 1,
      					"-203": 2,
      					"-202": 2,
      					"-201": 2,
      					"-200": 2,
      					"-199": 2,
      					"-198": 2,
      					"-197": 2,
      					"-196": 2,
      					"-195": 2,
      					"-194": 2,
      					"-193": 2
      				},
      				"-199": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 3,
      					"-218": 3,
      					"-217": 3,
      					"-216": 1,
      					"-215": 1,
      					"-214": 1,
      					"-213": 1,
      					"-212": 1,
      					"-211": 1,
      					"-210": 1,
      					"-209": 1,
      					"-208": 1,
      					"-207": 1,
      					"-206": 1,
      					"-205": 1,
      					"-204": 2,
      					"-203": 2,
      					"-202": 2,
      					"-201": 2,
      					"-200": 2,
      					"-199": 2,
      					"-198": 2,
      					"-197": 2,
      					"-196": 2,
      					"-195": 2,
      					"-194": 2,
      					"-193": 2
      				},
      				"-198": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 3,
      					"-218": 3,
      					"-217": 3,
      					"-216": 1,
      					"-215": 1,
      					"-214": 1,
      					"-213": 1,
      					"-212": 1,
      					"-211": 1,
      					"-210": 1,
      					"-209": 1,
      					"-208": 1,
      					"-207": 1,
      					"-206": 1,
      					"-205": 1,
      					"-204": 2,
      					"-203": 2,
      					"-202": 2,
      					"-201": 2,
      					"-200": 2,
      					"-199": 2,
      					"-198": 2,
      					"-197": 2,
      					"-196": 2,
      					"-195": 2,
      					"-194": 2,
      					"-193": 2
      				},
      				"-197": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 3,
      					"-218": 3,
      					"-217": 0,
      					"-216": 0,
      					"-215": 1,
      					"-214": 1,
      					"-213": 1,
      					"-212": 1,
      					"-211": 1,
      					"-210": 1,
      					"-209": 1,
      					"-208": 1,
      					"-207": 1,
      					"-206": 1,
      					"-205": 2,
      					"-204": 2,
      					"-203": 2,
      					"-202": 2,
      					"-201": 2,
      					"-200": 2,
      					"-199": 2,
      					"-198": 2,
      					"-197": 2,
      					"-196": 2,
      					"-195": 2,
      					"-194": 2,
      					"-193": 2
      				},
      				"-196": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 0,
      					"-218": 0,
      					"-217": 0,
      					"-216": 0,
      					"-215": 1,
      					"-214": 1,
      					"-213": 1,
      					"-212": 2,
      					"-211": 2,
      					"-210": 2,
      					"-209": 1,
      					"-208": 1,
      					"-207": 1,
      					"-206": 1,
      					"-205": 2,
      					"-204": 2,
      					"-203": 2,
      					"-202": 2,
      					"-201": 2,
      					"-200": 2,
      					"-199": 2,
      					"-198": 2,
      					"-197": 2,
      					"-196": 2,
      					"-195": 2,
      					"-194": 2,
      					"-193": 2
      				},
      				"-195": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 0,
      					"-218": 0,
      					"-217": 0,
      					"-216": 0,
      					"-215": 0,
      					"-214": 1,
      					"-213": 1,
      					"-212": 2,
      					"-211": 2,
      					"-210": 2,
      					"-209": 2,
      					"-208": 2,
      					"-207": 2,
      					"-206": 1,
      					"-205": 2,
      					"-204": 2,
      					"-203": 2,
      					"-202": 2,
      					"-201": 2,
      					"-200": 2,
      					"-199": 2,
      					"-198": 2,
      					"-197": 2,
      					"-196": 2,
      					"-195": 2,
      					"-194": 2,
      					"-193": 2
      				},
      				"-194": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 0,
      					"-218": 0,
      					"-217": 0,
      					"-216": 0,
      					"-215": 0,
      					"-214": 1,
      					"-213": 2,
      					"-212": 2,
      					"-211": 2,
      					"-210": 2,
      					"-209": 2,
      					"-208": 2,
      					"-207": 2,
      					"-206": 2,
      					"-205": 2,
      					"-204": 2,
      					"-203": 2,
      					"-202": 2,
      					"-201": 2,
      					"-200": 2,
      					"-199": 2,
      					"-198": 2,
      					"-197": 2,
      					"-196": 2,
      					"-195": 2,
      					"-194": 2,
      					"-193": 2
      				},
      				"-193": {
      					"-224": 0,
      					"-223": 0,
      					"-222": 0,
      					"-221": 0,
      					"-220": 0,
      					"-219": 0,
      					"-218": 0,
      					"-217": 0,
      					"-216": 0,
      					"-215": 0,
      					"-214": 2,
      					"-213": 2,
      					"-212": 2,
      					"-211": 2,
      					"-210": 2,
      					"-209": 2,
      					"-208": 2,
      					"-207": 2,
      					"-206": 2,
      					"-205": 2,
      					"-204": 2,
      					"-203": 2,
      					"-202": 2,
      					"-201": 2,
      					"-200": 2,
      					"-199": 2,
      					"-198": 2,
      					"-197": 2,
      					"-196": 2,
      					"-195": 2,
      					"-194": 2,
      					"-193": 2
      				}
      			},
      			"protos": {
      				"dirt-5": 0,
      				"grass-2": 1,
      				"dirt-6": 2,
      				"red-desert-0": 3
      			}
      		}
      	}
      }
    """

    const val rawPrototypesJson = """
      {
      	"modVersion": "0.4.0",
      	"tick": 4352,
      	"data": {
      		"type": "kafkatorio.packet.instant.PrototypesUpdate",
      		"prototypes": [
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "stone-path",
      				"layer": 60,
      				"mapColour": [
      					86,
      					82,
      					74,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "a[artificial]-a[tier-1]-a[stone-path]",
      				"canBeMined": true
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "concrete",
      				"layer": 61,
      				"mapColour": [
      					63,
      					61,
      					59,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "a[artificial]-b[tier-2]-a[concrete]",
      				"canBeMined": true
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "hazard-concrete-left",
      				"layer": 62,
      				"mapColour": [
      					176,
      					142,
      					39,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "a[artificial]-b[tier-2]-b[hazard-concrete-left]",
      				"canBeMined": true
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "hazard-concrete-right",
      				"layer": 62,
      				"mapColour": [
      					176,
      					142,
      					39,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "a[artificial]-b[tier-2]-c[hazard-concrete-right]",
      				"canBeMined": true
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "refined-concrete",
      				"layer": 64,
      				"mapColour": [
      					49,
      					48,
      					45,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "a[artificial]-c[tier-3]-a[refined-concrete]",
      				"canBeMined": true
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "refined-hazard-concrete-left",
      				"layer": 65,
      				"mapColour": [
      					116,
      					94,
      					26,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "a[artificial]-c[tier-3]-b[refined-hazard-concrete-left]",
      				"canBeMined": true
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "refined-hazard-concrete-right",
      				"layer": 65,
      				"mapColour": [
      					116,
      					94,
      					26,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "a[artificial]-c[tier-3]-c[refined-hazard-concrete-right]",
      				"canBeMined": true
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "landfill",
      				"layer": 57,
      				"mapColour": [
      					57,
      					39,
      					26,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "a[artificial]-d[utility]-a[landfill]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "acid-refined-concrete",
      				"layer": 97,
      				"mapColour": [
      					142,
      					194,
      					40,
      					127
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "a[artificial]-e[color-concrete]-acid",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "black-refined-concrete",
      				"layer": 88,
      				"mapColour": [
      					25,
      					25,
      					25,
      					127
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "a[artificial]-e[color-concrete]-black",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "blue-refined-concrete",
      				"layer": 73,
      				"mapColour": [
      					39,
      					137,
      					228,
      					127
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "a[artificial]-e[color-concrete]-blue",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "brown-refined-concrete",
      				"layer": 91,
      				"mapColour": [
      					76,
      					29,
      					0,
      					127
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "a[artificial]-e[color-concrete]-brown",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "cyan-refined-concrete",
      				"layer": 94,
      				"mapColour": [
      					70,
      					192,
      					181,
      					127
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "a[artificial]-e[color-concrete]-cyan",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "green-refined-concrete",
      				"layer": 70,
      				"mapColour": [
      					23,
      					195,
      					43,
      					127
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "a[artificial]-e[color-concrete]-green",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "orange-refined-concrete",
      				"layer": 76,
      				"mapColour": [
      					221,
      					127,
      					33,
      					127
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "a[artificial]-e[color-concrete]-orange",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "pink-refined-concrete",
      				"layer": 82,
      				"mapColour": [
      					236,
      					98,
      					131,
      					127
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "a[artificial]-e[color-concrete]-pink",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "purple-refined-concrete",
      				"layer": 85,
      				"mapColour": [
      					123,
      					28,
      					168,
      					127
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "a[artificial]-e[color-concrete]-purple",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "red-refined-concrete",
      				"layer": 67,
      				"mapColour": [
      					207,
      					6,
      					0,
      					127
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "a[artificial]-e[color-concrete]-red",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "yellow-refined-concrete",
      				"layer": 79,
      				"mapColour": [
      					212,
      					169,
      					19,
      					127
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "a[artificial]-e[color-concrete]-yellow",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "grass-1",
      				"layer": 26,
      				"mapColour": [
      					55,
      					53,
      					11,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "b[natural]-a[grass]-a[grass-1]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "grass-2",
      				"layer": 28,
      				"mapColour": [
      					66,
      					57,
      					15,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "b[natural]-a[grass]-b[grass-2]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "grass-3",
      				"layer": 29,
      				"mapColour": [
      					65,
      					52,
      					28,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "b[natural]-a[grass]-c[grass-3]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "grass-4",
      				"layer": 30,
      				"mapColour": [
      					59,
      					40,
      					18,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "b[natural]-a[grass]-d[grass-4]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "dry-dirt",
      				"layer": 18,
      				"mapColour": [
      					94,
      					66,
      					37,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "b[natural]-b[dirt]-a[dry-dirt]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "dirt-1",
      				"layer": 19,
      				"mapColour": [
      					141,
      					104,
      					60,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "b[natural]-b[dirt]-b[dirt-1]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "dirt-2",
      				"layer": 20,
      				"mapColour": [
      					136,
      					96,
      					59,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "b[natural]-b[dirt]-c[dirt-2]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "dirt-3",
      				"layer": 21,
      				"mapColour": [
      					133,
      					92,
      					53,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "b[natural]-b[dirt]-d[dirt-3]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "dirt-4",
      				"layer": 22,
      				"mapColour": [
      					103,
      					72,
      					43,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "b[natural]-b[dirt]-e[dirt-4]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "dirt-5",
      				"layer": 23,
      				"mapColour": [
      					91,
      					63,
      					38,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "b[natural]-b[dirt]-f[dirt-5]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "dirt-6",
      				"layer": 24,
      				"mapColour": [
      					80,
      					55,
      					31,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "b[natural]-b[dirt]-g[dirt-6]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "dirt-7",
      				"layer": 25,
      				"mapColour": [
      					80,
      					54,
      					28,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "b[natural]-b[dirt]-h[dirt-7]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "sand-1",
      				"layer": 8,
      				"mapColour": [
      					138,
      					103,
      					58,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "b[natural]-c[sand]-a[sand-1]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "sand-2",
      				"layer": 9,
      				"mapColour": [
      					128,
      					93,
      					52,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "b[natural]-c[sand]-b[sand-2]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "sand-3",
      				"layer": 10,
      				"mapColour": [
      					115,
      					83,
      					47,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "b[natural]-c[sand]-c[sand-3]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "red-desert-0",
      				"layer": 31,
      				"mapColour": [
      					103,
      					70,
      					32,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "b[natural]-d[red-desert]-a[red-desert-0]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "red-desert-1",
      				"layer": 14,
      				"mapColour": [
      					116,
      					81,
      					39,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "b[natural]-d[red-desert]-b[red-desert-1]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "red-desert-2",
      				"layer": 15,
      				"mapColour": [
      					116,
      					84,
      					43,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "b[natural]-d[red-desert]-c[red-desert-2]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "red-desert-3",
      				"layer": 16,
      				"mapColour": [
      					128,
      					93,
      					52,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "b[natural]-d[red-desert]-d[red-desert-3]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "water",
      				"layer": 3,
      				"mapColour": [
      					51,
      					83,
      					95,
      					255
      				],
      				"collisionMasks": [
      					"doodad-layer",
      					"item-layer",
      					"player-layer",
      					"resource-layer",
      					"water-tile"
      				],
      				"order": "c[water]-a[water]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "deepwater",
      				"layer": 3,
      				"mapColour": [
      					38,
      					64,
      					73,
      					255
      				],
      				"collisionMasks": [
      					"doodad-layer",
      					"item-layer",
      					"player-layer",
      					"resource-layer",
      					"water-tile"
      				],
      				"order": "c[water]-b[deep-water]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "water-green",
      				"layer": 3,
      				"mapColour": [
      					31,
      					48,
      					18,
      					255
      				],
      				"collisionMasks": [
      					"doodad-layer",
      					"item-layer",
      					"player-layer",
      					"resource-layer",
      					"water-tile"
      				],
      				"order": "c[water]-c[water-green]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "deepwater-green",
      				"layer": 3,
      				"mapColour": [
      					23,
      					37,
      					16,
      					255
      				],
      				"collisionMasks": [
      					"doodad-layer",
      					"item-layer",
      					"player-layer",
      					"resource-layer",
      					"water-tile"
      				],
      				"order": "c[water]-d[deepwater-green]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "water-shallow",
      				"layer": 6,
      				"mapColour": [
      					82,
      					98,
      					92,
      					255
      				],
      				"collisionMasks": [
      					"item-layer",
      					"object-layer",
      					"resource-layer",
      					"water-tile"
      				],
      				"order": "c[water]-e[water-shallow]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "water-mud",
      				"layer": 7,
      				"mapColour": [
      					65,
      					89,
      					90,
      					255
      				],
      				"collisionMasks": [
      					"item-layer",
      					"object-layer",
      					"resource-layer",
      					"water-tile"
      				],
      				"order": "c[water]-g[water-mud]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "nuclear-ground",
      				"layer": 33,
      				"mapColour": [
      					48,
      					40,
      					35,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "d[destruction]-a[nuclear]-a[nuclear-ground]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "water-wube",
      				"layer": 2,
      				"mapColour": [
      					0,
      					0,
      					0,
      					255
      				],
      				"collisionMasks": [
      					"doodad-layer",
      					"item-layer",
      					"player-layer",
      					"resource-layer",
      					"water-tile"
      				],
      				"order": "x[wube]-a[water-wube]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "tile-unknown",
      				"layer": 0,
      				"mapColour": [
      					0,
      					0,
      					0,
      					255
      				],
      				"collisionMasks": {},
      				"order": "z-a",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "out-of-map",
      				"layer": 0,
      				"mapColour": [
      					0,
      					0,
      					0,
      					255
      				],
      				"collisionMasks": [
      					"doodad-layer",
      					"floor-layer",
      					"ground-tile",
      					"item-layer",
      					"object-layer",
      					"player-layer",
      					"resource-layer",
      					"water-tile"
      				],
      				"order": "z[other]-a[out-of-map]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "lab-dark-1",
      				"layer": 70,
      				"mapColour": [
      					49,
      					49,
      					49,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "z[other]-b[lab]-a[lab-dark-1]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "lab-dark-2",
      				"layer": 70,
      				"mapColour": [
      					0,
      					0,
      					0,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "z[other]-b[lab]-b[lab-dark-2]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "lab-white",
      				"layer": 70,
      				"mapColour": [
      					255,
      					255,
      					255,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "z[other]-b[lab]-c[lab-white]",
      				"canBeMined": false
      			},
      			{
      				"type": "kafkatorio.prototype.MapTile",
      				"name": "tutorial-grid",
      				"layer": 55,
      				"mapColour": [
      					122,
      					122,
      					122,
      					255
      				],
      				"collisionMasks": [
      					"ground-tile"
      				],
      				"order": "z[other]-c[tutorial]-a[tutorial-grid]",
      				"canBeMined": false
      			}
      		]
      	}
      }
    """
  }
}
