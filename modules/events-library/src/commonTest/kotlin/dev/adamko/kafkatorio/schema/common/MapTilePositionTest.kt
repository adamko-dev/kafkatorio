package dev.adamko.kafkatorio.schema.common

import dev.adamko.kafkatorio.library.minus
import dev.adamko.kafkatorio.library.plus
import dev.adamko.kafkatorio.schema.common.MapChunkPositionTest.Companion.mapTilePositionArb
import io.kotest.core.spec.style.FunSpec
import io.kotest.property.forAll

class MapTilePositionTest : FunSpec({

  context("comparison") {

    test("less than <") {
      forAll(
        mapTilePositionArb(),
        mapTilePositionArb(1..100),
      ) { tilePos, delta ->
        tilePos < (tilePos + delta)
      }
    }

    test("equal comparing to ==") {
      forAll(mapTilePositionArb()) { tilePos ->
        tilePos.compareTo(tilePos) == 0
      }
    }

    test("less than or equal to <=") {
      forAll(
        mapTilePositionArb(),
        mapTilePositionArb(0..1),
      ) { tilePos, delta ->
        tilePos <= (tilePos + delta)
      }
    }

    test("greater than or equal to >=") {
      forAll(
        mapTilePositionArb(),
        mapTilePositionArb(0..1),
      ) { tilePos, delta ->
        tilePos >= (tilePos - delta)
      }
    }

    test("greater than >") {
      forAll(
        mapTilePositionArb(),
        mapTilePositionArb(1..100),
      ) { tilePos, delta ->
        tilePos > (tilePos - delta)
      }
    }
  }
})
