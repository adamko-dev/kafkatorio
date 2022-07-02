package dev.adamko.kafkatorio.schema.common

import dev.adamko.kafkatorio.library.kxsBinary
import io.kotest.assertions.asClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.maps.shouldContainExactly
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray

class MapChunkPositionBoundsTest : FunSpec({

  context("create bounds") {

    context("MapChunkPosition(0, 0)") {

      context("chunk size 32") {

        val chunkPos = MapChunkPosition(0, 0)
        val bounds = MapChunkPositionBounds(chunkPos, ChunkSize.CHUNK_032)

        test("leftTop should be 0,0") {
          bounds.leftTop.asClue {
            it.x shouldBeExactly 0
            it.y shouldBeExactly 0
          }
        }

        test("rightBottom should be 31,31") {
          bounds.rightBottom.asClue {
            it.x shouldBeExactly 31
            it.y shouldBeExactly 31
          }
        }
      }
    }

    context("chunk size 256") {

      val chunkPos = MapChunkPosition(0, 0)
      val bounds = MapChunkPositionBounds(chunkPos, ChunkSize.CHUNK_256)

      test("leftTop should be 0,0") {
        bounds.leftTop.asClue {
          it.x shouldBeExactly 0
          it.y shouldBeExactly 0
        }
      }

      test("rightBottom should be 255,255") {
        bounds.rightBottom.asClue {
          it.x shouldBeExactly 255
          it.y shouldBeExactly 255
        }
      }
    }
  }

  context("MapChunkPosition(-10, -10)") {

    context("chunk size 32") {

      val chunkPos = MapChunkPosition(-10, -10)
      val bounds = MapChunkPositionBounds(chunkPos, ChunkSize.CHUNK_032)

      test("leftTop should be -320,-320") {
        bounds.leftTop.asClue {
          it.x shouldBeExactly -320
          it.y shouldBeExactly -320
        }
      }

      test("rightBottom should be -289,-289") {
        bounds.rightBottom.asClue {
          it.x shouldBeExactly -289
          it.y shouldBeExactly -289
        }
      }
    }

    context("chunk size 256") {

      val chunkPos = MapChunkPosition(-10, -10)
      val bounds = MapChunkPositionBounds(chunkPos, ChunkSize.CHUNK_256)

      test("leftTop should be -2560") {
        bounds.leftTop.asClue {
          it.x shouldBeExactly -2560
          it.y shouldBeExactly -2560
        }
      }

      test("rightBottom should be (-2560 + 255)") {
        bounds.rightBottom.asClue {
          it.x shouldBeExactly (-2560 + 255)
          it.y shouldBeExactly (-2560 + 255)
        }
      }
    }
  }


  context("encode MapTileDictionary") {
    val actual = MapTileDictionary(
      tilesXY = mapOf(
        "-192" to mapOf("-65" to MapTileDictionary.PrototypeKey(2)),
        "161" to mapOf(
          "-96" to MapTileDictionary.PrototypeKey(1),
          "-95" to MapTileDictionary.PrototypeKey(2),
          "94" to MapTileDictionary.PrototypeKey(2),
          "93" to MapTileDictionary.PrototypeKey(32),
        ),

        // unknown prototype
        "44" to mapOf("1" to MapTileDictionary.PrototypeKey(44)),
      ),
      protos = mapOf(
        PrototypeName("water") to MapTileDictionary.PrototypeKey(1),
        PrototypeName("grass-4") to MapTileDictionary.PrototypeKey(2),
        PrototypeName("grass-3") to MapTileDictionary.PrototypeKey(32),

        PrototypeName("not-used") to MapTileDictionary.PrototypeKey(99),
      )
    )

    test("binary round trip") {
      val encoded: ByteArray = kxsBinary.encodeToByteArray(actual)
      val decoded: MapTileDictionary = kxsBinary.decodeFromByteArray(encoded)

      decoded.tilesXY shouldContainExactly actual.tilesXY
      decoded.protos shouldContainExactly actual.protos

      decoded.toMapTileList() shouldContainExactly actual.toMapTileList()
    }
  }
})
