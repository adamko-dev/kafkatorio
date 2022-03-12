package dev.adamko.kafkatorio.processor.misc

import dev.adamko.kafkatorio.events.schema.MapChunkPosition
import dev.adamko.kafkatorio.events.schema.SurfaceIndex
import dev.adamko.kafkatorio.processor.serdes.kxsBinary
import dev.adamko.kafkatorio.processor.topology.ChunkSize
import dev.adamko.kafkatorio.processor.topology.FactorioServerId
import dev.adamko.kafkatorio.processor.topology.ServerMapChunkId
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray


class KxsBinaryTests : FunSpec({

  test("byte array") {
    val ba = byteArrayOf(
      0, 4, 116, 101, 115, 116, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 1, 0, 0, 0, 3
    )

    val result = kxsBinary.decodeFromByteArray(ServerMapChunkId.serializer(), ba)

    withClue(ba.toString(Charsets.UTF_8)) {

      result shouldBeEqualToComparingFields ServerMapChunkId(
        serverId = FactorioServerId("test"),
        chunkPosition = MapChunkPosition(1, 2),
        surfaceIndex = SurfaceIndex(1u),
        chunkSize = ChunkSize.CHUNK_064,
      )

    }
  }

  test("encoding") {
    val ba = kxsBinary.encodeToByteArray(
      ServerMapChunkId(
        serverId = FactorioServerId("test"),
        chunkPosition = MapChunkPosition(1, 2),
        surfaceIndex = SurfaceIndex(1u),
        chunkSize = ChunkSize.CHUNK_064,
      )
    )

    withClue(ba.toString(Charsets.UTF_8)) {
      ba shouldBe byteArrayOf(
        0, 4, 116, 101, 115, 116, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 1, 0, 0, 0, 3
      )
    }
  }

  context("inline value class") {
    val uuid = "fa858d73-6205-44cf-aeeb-6bbf3148c4a7"
    //@formatter:off
    val expectedBytes = byteArrayOf(0, 36, 102, 97, 56, 53, 56, 100, 55, 51, 45, 54, 50, 48, 53, 45, 52, 52, 99, 102, 45, 97, 101, 101, 98, 45, 54, 98, 98, 102, 51, 49, 52, 56, 99, 52, 97, 55)
    //@formatter:on

    test("encode") {
      val ba = kxsBinary.encodeToByteArray(FactorioServerId(uuid))

      withClue(ba.toString(Charsets.UTF_8)) {
        ba shouldBe expectedBytes
      }
    }
    test("decode") {
      val ba = kxsBinary.encodeToByteArray(FactorioServerId(uuid))
      val id: FactorioServerId = kxsBinary.decodeFromByteArray(ba)

      id shouldBeEqualToComparingFields FactorioServerId(uuid)
    }
  }

})
