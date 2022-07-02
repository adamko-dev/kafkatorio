package dev.adamko.kafkatorio.schema.common

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class MapChunkPositionTest : FunSpec({

  context("check MapChunkPosition.contains()") {

    context("MapChunkPosition(0, 0)") {

      val chunkPos = MapChunkPosition(0, 0)

      test("should contain tiles from (0,0) to (31,31)") {
        checkAll(mapTilePositionArb(0..31)) { tilePos ->
          (tilePos in chunkPos) shouldBe true
        }
      }

      test("should not contain tiles below (0,0)") {
        checkAll(
          mapTilePositionArb(-1000..-1)
        ) { tilePos ->
          (tilePos in chunkPos) shouldBe false
        }
      }

      test("should not contain tiles above (31,31)") {
        checkAll(
          mapTilePositionArb(32..100)
        ) { tilePos ->
          (tilePos in chunkPos) shouldBe false
        }
      }
    }
    context("MapChunkPosition(-10, -10)") {

      val chunkPos = MapChunkPosition(-10, -10)

      test("should contain tiles from (-320,-320) to (-289,-289)") {
        checkAll(mapTilePositionArb(-320..-289)) { tilePos ->
          (tilePos in chunkPos) shouldBe true
        }
      }

      test("should not contain tiles below (-320,-320)") {
        checkAll(
          mapTilePositionArb(-400..-321)
        ) { tilePos ->
          (tilePos in chunkPos) shouldBe false
        }
      }

      test("should not contain tiles above (-289,-289)") {
        checkAll(
          mapTilePositionArb(-288..100)
        ) { tilePos ->
          (tilePos in chunkPos) shouldBe false
        }
      }
    }
  }


}) {
  companion object {
    fun mapTilePositionArb(
      range: IntRange = -1000..1000
    ) = arbitrary {
      MapTilePosition(
        Arb.int(range).bind(),
        Arb.int(range).bind(),
      )
    }
  }
}
