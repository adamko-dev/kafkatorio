package dev.adamko.kafkatorio.processor.core.topology

import dev.adamko.kafkatorio.library.iterateTiles
import dev.adamko.kafkatorio.library.kxsBinary
import dev.adamko.kafkatorio.library.toMapChunkPosition
import dev.adamko.kafkatorio.schema.common.ChunkSize
import dev.adamko.kafkatorio.schema.common.ColourHex
import dev.adamko.kafkatorio.schema.common.FactorioServerId
import dev.adamko.kafkatorio.schema.common.MapChunkPosition
import dev.adamko.kafkatorio.schema.common.MapTilePosition
import dev.adamko.kafkatorio.schema.common.ServerMapChunkId
import dev.adamko.kafkatorio.schema.common.ServerMapTileLayer
import dev.adamko.kafkatorio.schema.common.SurfaceIndex
import dev.adamko.kafkatorio.server.processor.topology.ServerMapChunkTiles
import dev.adamko.kafkatorio.server.processor.topology.saveMapTiles
import dev.adamko.kotka.kxs.serde
import io.kotest.core.spec.style.FunSpec
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.RandomSource
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.uByte
import io.kotest.property.checkAll
import java.nio.file.Path
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration
import kotlinx.coroutines.delay
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.TestInputTopic
import org.apache.kafka.streams.TopologyTestDriver


class SaveMapChunksTest : FunSpec({

  test("save tile") {

    Scenario().use { scenario ->

      val chunkTilesArb = serverMapChunkTilesArb(scenario.serverId)

      checkAll(
        config = PropTestConfig(iterations = 1),
        chunkTilesArb,
      ) { chunkTiles ->

        scenario.groupedMapChunksInputTopic.pipeInput(
          chunkTiles.chunkId,
          chunkTiles,
        )

        scenario += 1.minutes
        delay(5.seconds)
        scenario += 1.minutes
        // delay(5.seconds)
        scenario += 1.minutes
        // delay(5.seconds)
        scenario += 1.minutes
        // delay(5.seconds)

        println("file:///${scenario.outputDir.absolutePathString()}")
      }
    }
  }

}) {

  class Scenario(
    streamsBuilder: StreamsBuilder = StreamsBuilder(),

    groupedMapChunksInputTopicName: String = "kafkatorio.state.map-chunk.terrain.colour",

    val serverId: FactorioServerId = FactorioServerId("test-server-id"),
  ) : AutoCloseable {

    val outputDir: Path = run {
      val now = LocalTime.now().format(DateTimeFormatter.ISO_TIME).filter { it.isLetterOrDigit() }
      Path("build/test/save-map-chunks-${now}")
    }
    private val testDriver: TopologyTestDriver

    init {
      saveMapTiles(streamsBuilder, outputDir)
      testDriver = TopologyTestDriver(streamsBuilder.build())
    }

    val groupedMapChunksInputTopic: TestInputTopic<ServerMapChunkId, ServerMapChunkTiles<ColourHex>> =
      testDriver.createInputTopic(
        groupedMapChunksInputTopicName,
        kxsBinary.serde<ServerMapChunkId>().serializer(),
        kxsBinary.serde<ServerMapChunkTiles<ColourHex>>().serializer(),
      )

    operator fun plusAssign(wallClockTime: Duration) {
      testDriver.advanceWallClockTime(wallClockTime.toJavaDuration())
    }

    override fun close() {
      testDriver.close()
    }
  }

  companion object {

    private fun mapChunkPositionArb(
      xArb: Arb<Int> = Arb.int(-10..10),
      yArb: Arb<Int> = Arb.int(-10..10),
      chunkSize: ChunkSize = ChunkSize.MAX,
    ): Arb<MapChunkPosition> = arbitrary {
      MapChunkPosition(xArb.bind(), yArb.bind(), chunkSize)
    }

    private fun colourHexArb(
      redArb: Arb<UByte> = Arb.uByte(),
      greenArb: Arb<UByte> = Arb.uByte(),
      blueArb: Arb<UByte> = Arb.uByte(),
      alphaArb: Arb<UByte> = Arb.uByte(),
    ): Arb<ColourHex> = arbitrary {
      ColourHex(
        redArb.bind(),
        greenArb.bind(),
        blueArb.bind(),
        alphaArb.bind(),
      )
    }

    private fun serverMapChunkIdArb(
      serverId: FactorioServerId,
      chunkSize: ChunkSize = ChunkSize.MAX,
      chunkPosition: Arb<MapChunkPosition> = mapChunkPositionArb(chunkSize = chunkSize),
      surfaceIndex: SurfaceIndex = SurfaceIndex(1u),
    ): Arb<ServerMapChunkId> = arbitrary {
      ServerMapChunkId(
        serverId = serverId,
        layer = ServerMapTileLayer.RESOURCE,
        chunkPosition = chunkPosition.bind(),
        surfaceIndex = surfaceIndex,
      )
    }

    private fun tilesInChunkArb(
      chunkId: ServerMapChunkId
    ): Arb<Map<MapTilePosition, ColourHex>> = arbitrary {

      val tileColours = mutableMapOf<MapChunkPosition, ColourHex>()

      println("chunk size: ${chunkId.chunkSize} ${chunkId.chunkSize.lengthInTiles}")

      val map = chunkId.chunkPosition
        .iterateTiles()
        .asSequence()
        .map { tilePos ->
          val colour =
            tileColours.getOrPut(tilePos.toMapChunkPosition(ChunkSize.MIN)) {
              colourHexArb().sample(RandomSource.default()).value
            }
          tilePos to colour
        }
//        .take(100)
        .toMap()

      println(map.size)

      map
    }

    fun serverMapChunkTilesArb(
      serverId: FactorioServerId,
    ): Arb<ServerMapChunkTiles<ColourHex>> {

      val serverMapChunkId = serverMapChunkIdArb(serverId)

      return arbitrary {
        val chunkId = serverMapChunkId.bind()

        val tileColoursArb = tilesInChunkArb(chunkId)

        ServerMapChunkTiles(
          chunkId = chunkId,
          map = tileColoursArb.bind()
        )
      }
    }
  }
}
