package dev.adamko.kafkatorio.processor.core.topology

import dev.adamko.kafkatorio.library.kxsBinary
import dev.adamko.kafkatorio.schema.common.ColourHex
import dev.adamko.kafkatorio.schema.common.FactorioServerId
import dev.adamko.kafkatorio.schema.common.ServerMapChunkId
import dev.adamko.kotka.kxs.serde
import io.kotest.core.spec.style.FunSpec
import kotlin.time.Duration
import kotlin.time.toJavaDuration
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.TestInputTopic
import org.apache.kafka.streams.TopologyTestDriver

class FactorioServerPacketsTest : FunSpec({


})

private class Scenario(
  streamsBuilder: StreamsBuilder = StreamsBuilder(),

  groupedMapChunksInputTopicName: String = "kafkatorio.state.map-chunk.terrain.colour",

  val serverId: FactorioServerId = FactorioServerId("test-server-id"),
) : AutoCloseable {

  init {
    factorioServerPacketStream(streamsBuilder)
  }

  private val testDriver: TopologyTestDriver = TopologyTestDriver(streamsBuilder.build())

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
