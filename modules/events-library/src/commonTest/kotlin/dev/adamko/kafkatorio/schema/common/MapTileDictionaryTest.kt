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
        PrototypeName("water") to MapTileDictionary.PrototypeKey(1),
        PrototypeName("grass-4") to MapTileDictionary.PrototypeKey(2),
        PrototypeName("grass-3") to MapTileDictionary.PrototypeKey(32),

        PrototypeName("not-used") to MapTileDictionary.PrototypeKey(99),
      )
    )

    test("toMapTileList") {
      actual.toMapTileList() shouldContainAll listOf(
        MapTile(-192, -65, PrototypeName("grass-4")),

        MapTile(161, -96, PrototypeName("water")),
        MapTile(161, -95, PrototypeName("grass-4")),
        MapTile(161, 94, PrototypeName("grass-4")),
        MapTile(161, 93, PrototypeName("grass-3")),
      )
    }
  }
})
