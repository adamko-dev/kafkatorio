package dev.adamko.kafkatorio.schema.common

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainAll

class MapTileDictionaryTest : FunSpec({

  context("MapTileDictionary") {
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

    test("toMapTileList") {
      actual.toMapTileList() shouldContainAll listOf(
        MapTile(-192, -65, PrototypeId("tile", "grass-4")),

        MapTile(161, -96, PrototypeId("tile", "water")),
        MapTile(161, -95, PrototypeId("tile", "grass-4")),
        MapTile(161, 94, PrototypeId("tile", "grass-4")),
        MapTile(161, 93, PrototypeId("tile", "grass-3")),
      )
    }
  }
})
