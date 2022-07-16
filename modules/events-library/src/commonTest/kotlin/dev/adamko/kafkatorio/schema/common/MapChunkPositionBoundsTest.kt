package dev.adamko.kafkatorio.schema.common

import dev.adamko.kafkatorio.library.kxsBinary
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray

class MapChunkPositionBoundsTest : FunSpec({

  context("create bounds") {

    context("MapChunkPosition(0, 0)") {

      context("chunk size 32") {

        val chunkPos = MapChunkPosition(0, 0, ChunkSize.CHUNK_032)

        test("leftTop should be 0,0") {
          chunkPos.bounds.leftTop.asClue {
            it.x shouldBeExactly 0
            it.y shouldBeExactly 0
          }
        }

        test("rightBottom should be 31,31") {
          chunkPos.bounds.rightBottom.asClue {
            it.x shouldBeExactly 31
            it.y shouldBeExactly 31
          }
        }
      }
    }

    context("chunk size 256") {

      val chunkPos = MapChunkPosition(0, 0, ChunkSize.CHUNK_256)

      test("leftTop should be 0,0") {
        chunkPos.bounds.leftTop.asClue {
          it.x shouldBeExactly 0
          it.y shouldBeExactly 0
        }
      }

      test("rightBottom should be 255,255") {
        chunkPos.bounds.rightBottom.asClue {
          it.x shouldBeExactly 255
          it.y shouldBeExactly 255
        }
      }
    }
  }

  context("MapChunkPosition(-10, -10)") {

    context("chunk size 32") {

      val chunkPos = MapChunkPosition(-10, -10, ChunkSize.CHUNK_032)

      test("leftTop should be -320,-320") {
        chunkPos.bounds.leftTop.asClue {
          it.x shouldBeExactly -320
          it.y shouldBeExactly -320
        }
      }

      test("rightBottom should be -289,-289") {
        chunkPos.bounds.rightBottom.asClue {
          it.x shouldBeExactly -289
          it.y shouldBeExactly -289
        }
      }
    }

    context("chunk size 256") {

      val chunkPos = MapChunkPosition(-10, -10, ChunkSize.CHUNK_256)

      test("leftTop should be -2560") {
        chunkPos.bounds.leftTop.asClue {
          it.x shouldBeExactly -2560
          it.y shouldBeExactly -2560
        }
      }

      test("rightBottom should be (-2560 + 255)") {
        chunkPos.bounds.rightBottom.asClue {
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
        PrototypeId("tile", "water") to MapTileDictionary.PrototypeKey(1),
        PrototypeId("tile", "grass-4") to MapTileDictionary.PrototypeKey(2),
        PrototypeId("tile", "grass-3") to MapTileDictionary.PrototypeKey(32),

        PrototypeId("tile", "not-used") to MapTileDictionary.PrototypeKey(99),
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

  context("verify map chunk 'contains'") {

    context("MapChunkPosition(1, 1)") {

      val chunkPos = MapChunkPosition(1, 1, ChunkSize.CHUNK_032) // from 32,32 to 63,63

      test("tile outside of chunk") {
        val outerTilePos = MapTilePosition(1, 1)

        withClue("$outerTilePos in ${chunkPos.bounds}") {
          (outerTilePos in chunkPos) shouldBe false
        }
      }

      test("tile inside of chunk") {
        val innerTilePos = MapTilePosition(34, 33)

        withClue("$innerTilePos in ${chunkPos.bounds}") {
          (innerTilePos in chunkPos) shouldBe true
        }
      }

      test("entity outside of chunk") {
        val outerEntityPos = MapEntityPosition(1.0, 1.0)

        withClue("$outerEntityPos in ${chunkPos.bounds}") {
          (outerEntityPos !in chunkPos) shouldBe true
          (outerEntityPos in chunkPos) shouldBe false
        }
      }

      test("entity inside of chunk") {
        val innerEntityPos = MapEntityPosition(34.0, 33.0)

        withClue("$innerEntityPos in ${chunkPos.bounds}") {
          (innerEntityPos !in chunkPos) shouldBe false
          (innerEntityPos in chunkPos) shouldBe true
        }
      }
    }
    context(" MapChunkPosition(9, -5)") {
      val chunkPos2 = MapChunkPosition(9, -5, ChunkSize.CHUNK_032)

      test("tile inside of chunk") {
        val innerTilePos = MapTilePosition(288, -160)

        withClue("$innerTilePos in ${chunkPos2.bounds}") {
          (innerTilePos in chunkPos2) shouldBe true
        }
      }
    }
  }
})
