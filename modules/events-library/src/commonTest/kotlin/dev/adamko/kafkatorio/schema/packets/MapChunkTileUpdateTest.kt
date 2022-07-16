package dev.adamko.kafkatorio.schema.packets

import dev.adamko.kafkatorio.library.LuaJsonList
import dev.adamko.kafkatorio.library.jsonMapperKafkatorio
import dev.adamko.kafkatorio.schema.common.ChunkSize
import dev.adamko.kafkatorio.schema.common.EventName
import dev.adamko.kafkatorio.schema.common.MapChunkPosition
import dev.adamko.kafkatorio.schema.common.MapTileDictionary
import dev.adamko.kafkatorio.schema.common.PrototypeId
import dev.adamko.kafkatorio.schema.common.SurfaceIndex
import dev.adamko.kafkatorio.schema.common.tick
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString

class MapChunkTileUpdateTest : FunSpec({

  context("MapChunkTileUpdate update") {
    //language=json
    val json = """
{
  "data": {
    "type": "kafkatorio.packet.keyed.MapChunkTileUpdate",
    "key": {
      "surfaceIndex": 1,
      "chunkPosition": [
        -6,
        -3,
        "CHUNK_032"
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
        "tile/water": 1,
        "tile/grass-4": 2,
        "tile/grass-3": 32
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
          MapChunkPosition(-6, -3, ChunkSize.CHUNK_032),
          SurfaceIndex(1u),
        ),
        events = mapOf(
          EventName("on_chunk_generated") to LuaJsonList(listOf(1u.tick))
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
