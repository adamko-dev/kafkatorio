package dev.adamko.kafkatorio.processor.serdes

import dev.adamko.kafkatorio.schema.common.MapTileDictionary
import dev.adamko.kafkatorio.schema.common.PrototypeName
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray

class MapTileDictionaryTest : FunSpec({

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
      decoded.shouldBeEqualToComparingFields(actual)
    }
  }
})
