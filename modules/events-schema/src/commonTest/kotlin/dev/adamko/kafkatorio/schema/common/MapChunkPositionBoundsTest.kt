package dev.adamko.kafkatorio.schema.common

import io.kotest.assertions.asClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeExactly

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

})
