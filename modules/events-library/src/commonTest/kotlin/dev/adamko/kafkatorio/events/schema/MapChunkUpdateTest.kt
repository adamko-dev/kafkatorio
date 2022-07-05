package dev.adamko.kafkatorio.events.schema

import dev.adamko.kafkatorio.library.jsonMapperKafkatorio
import dev.adamko.kafkatorio.schema.common.EventName
import dev.adamko.kafkatorio.schema.common.MapChunkPosition
import dev.adamko.kafkatorio.schema.common.MapTileDictionary
import dev.adamko.kafkatorio.schema.common.PrototypeId
import dev.adamko.kafkatorio.schema.common.SurfaceIndex
import dev.adamko.kafkatorio.schema.common.tick
import dev.adamko.kafkatorio.schema.packets.KafkatorioPacket
import dev.adamko.kafkatorio.schema.packets.MapChunkTileUpdate
import dev.adamko.kafkatorio.schema.packets.MapChunkTileUpdateKey
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString

class MapChunkUpdateTest : FunSpec({

  context("MapChunkUpdate update") {
    //language=json
    val json = """
{
  "data": {
    "type": "kafkatorio.packet.keyed.MapChunkUpdate",
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
    "events": {
      "on_chunk_generated": [1]
    }
  },
  "modVersion": "0.4.0",
  "tick": 36
}
    """.trimIndent()

    val expected = KafkatorioPacket(
      tick = 36u.tick,
      modVersion = "0.4.0",
      data = MapChunkTileUpdate(
        key = MapChunkTileUpdateKey(
          MapChunkPosition(-6, -3),
          SurfaceIndex(1u),
        ),
        events = mapOf(
          EventName("on_chunk_generated") to listOf(1u.tick)
        ),
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
            PrototypeId("tile", "water") to MapTileDictionary.PrototypeKey(1),
            PrototypeId("tile", "grass-4") to MapTileDictionary.PrototypeKey(2),
            PrototypeId("tile", "grass-3") to MapTileDictionary.PrototypeKey(32),
          )
        )
      )
    )

    test("decode") {
      val packet: KafkatorioPacket = jsonMapperKafkatorio.decodeFromString(json)
      packet shouldBe expected
    }
  }
})
