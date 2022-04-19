package dev.adamko.kafkatorio.events.schema

import dev.adamko.kafkatorio.schema.common.MapChunkPosition
import dev.adamko.kafkatorio.schema.common.MapTileDictionary
import dev.adamko.kafkatorio.schema.common.PrototypeName
import dev.adamko.kafkatorio.schema.common.SurfaceIndex
import dev.adamko.kafkatorio.schema.common.Tick
import dev.adamko.kafkatorio.schema.jsonMapperKafkatorio
import dev.adamko.kafkatorio.schema2.KafkatorioPacket2
import dev.adamko.kafkatorio.schema2.MapChunkUpdate
import dev.adamko.kafkatorio.schema2.MapChunkUpdateKey
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlin.math.exp
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

class MapChunkUpdateTest : FunSpec({

  context("MapChunkUpdate update") {
    //language=json
    val json = """
{
  "data": {
    "type": "dev.adamko.kafkatorio.schema2.MapChunkUpdate",
    "key": {
      "surfaceIndex": 1,
      "chunkPosition": [
        -6,
        -3
      ]
    },
    "tileDictionary": {
      "tilesXY": {
        "-192": {
          "-65": 2
        },
        "161": {
          "-96": 1,
          "-95": 2,
          "94": 2,
          "93": 32
        }
      },
      "protos": {
        "water": 1,
        "grass-4": 2,
        "grass-3": 32
      }
    },
    "eventCounts": {
      "on_chunk_generated": 1
    }
  },
  "modVersion": "0.4.0",
  "tick": 36
}
    """.trimIndent()

    val expected = KafkatorioPacket2(
      tick = Tick(36u),
      modVersion = "0.4.0",
      data = MapChunkUpdate(
        key = MapChunkUpdateKey(
          MapChunkPosition(-6, -3),
          SurfaceIndex(1u),
        ),
        eventCounts = mapOf("on_chunk_generated" to 1u),
        tileDictionary = MapTileDictionary(
          tilesXY = mapOf(
            "-192" to mapOf("-65" to MapTileDictionary.PrototypeKey(2)),
            "161" to mapOf(
              "-96" to MapTileDictionary.PrototypeKey(1),
              "-95" to MapTileDictionary.PrototypeKey(2),
              "94" to MapTileDictionary.PrototypeKey(2),
              "93" to MapTileDictionary.PrototypeKey(32),
            ),
          ),
          protos = mapOf(
            PrototypeName("water") to MapTileDictionary.PrototypeKey(1),
            PrototypeName("grass-4") to MapTileDictionary.PrototypeKey(2),
            PrototypeName("grass-3") to MapTileDictionary.PrototypeKey(32),
          )
        )
      )
    )

    test("decode") {
      val packet: KafkatorioPacket2 = jsonMapperKafkatorio.decodeFromString(json)
      packet shouldBe expected
    }
  }
})
