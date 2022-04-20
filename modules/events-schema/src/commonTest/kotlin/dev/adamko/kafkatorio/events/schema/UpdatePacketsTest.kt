package dev.adamko.kafkatorio.events.schema

import dev.adamko.kafkatorio.schema.common.Colour
import dev.adamko.kafkatorio.schema.common.EntityIdentifiersData
import dev.adamko.kafkatorio.schema.common.ForceIndex
import dev.adamko.kafkatorio.schema.common.MapEntityPosition
import dev.adamko.kafkatorio.schema.common.PlayerIndex
import dev.adamko.kafkatorio.schema.common.SurfaceIndex
import dev.adamko.kafkatorio.schema.common.Tick
import dev.adamko.kafkatorio.schema.common.UnitNumber
import dev.adamko.kafkatorio.schema.jsonMapperKafkatorio
import dev.adamko.kafkatorio.schema2.KafkatorioPacket2
import dev.adamko.kafkatorio.schema2.PlayerUpdate
import dev.adamko.kafkatorio.schema2.PlayerUpdateKey
import dev.adamko.kafkatorio.schema2.SurfaceUpdate
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

class UpdatePacketsTest : FunSpec({

  context("player update packet") {
    // language=JSON
    val json = """
      {
        "tick": 66,
        "modVersion": "0.3.2",
        "data": {
          "type": "dev.adamko.kafkatorio.schema2.PlayerUpdate",
          "key": {"index": 1},
          "afkTime": 0,
          "chatColour": [1, 0.4, 0.6, 1],
          "colour": [0.8, 0.5, 0.2, 0.5],
          "eventCounts": {
            "on_player_changed_position": 7,
            "on_player_joined_game": 1
          },
          "forceIndex": 1,
          "isAdmin": true,
          "isConnected": true,
          "isShowOnMap": true,
          "isSpectator": false,
          "lastOnline": 0,
          "name": "fredthedeadhead",
          "onlineTime": 0,
          "position": [
            -58, 
            13
          ],
          "tag": ""
        }
      }
    """.trimIndent()

    test("decode") {
      val packet: KafkatorioPacket2 = jsonMapperKafkatorio.decodeFromString(json)

      packet shouldBe KafkatorioPacket2(
        modVersion = "0.3.2",
        tick = Tick(66u),
        data = PlayerUpdate(
          key = PlayerUpdateKey(PlayerIndex(1u)),
          afkTime = Tick(0u),
          bannedReason = null,
          characterUnitNumber = null,
          chatColour = Colour(red = 1f, green = 0.4f, blue = 0.6f, alpha = 1f),
          colour = Colour(red = 0.8f, green = 0.5f, blue = 0.2f, alpha = 0.5f),
          diedCause = null,
          disconnectReason = null,
          eventCounts = mapOf("on_player_changed_position" to 7u, "on_player_joined_game" to 1u),
          forceIndex = ForceIndex(1u),
          isAdmin = true,
          isConnected = true,
          isRemoved = null,
          isShowOnMap = true,
          isSpectator = false,
          kickedReason = null,
          lastOnline = Tick(0u),
          name = "fredthedeadhead",
          onlineTime = Tick(0u),
          position = MapEntityPosition(-58.0, 13.0),
          surfaceIndex = null,
          tag = "",
          ticksToRespawn = null,
        )
      )
    }

    test("encode") {
      val v = KafkatorioPacket2(
        modVersion = "0.3.2",
        tick = Tick(66u),
        data = PlayerUpdate(
          key = PlayerUpdateKey(PlayerIndex(1u)),
          characterUnitNumber = null,
          chatColour = null,
          colour = null,
          name = "fredthedeadhead",
          afkTime = Tick(0u),
          ticksToRespawn = null,
          forceIndex = null,
          isAdmin = null,
          isConnected = null,
          isShowOnMap = true,
          isSpectator = false,
          lastOnline = Tick(0u),
          onlineTime = Tick(0u),
          position = MapEntityPosition(-58.0, 13.0),
          surfaceIndex = null,
          tag = null,
          diedCause = null,
          bannedReason = null,
          kickedReason = null,
          disconnectReason = null,
          isRemoved = null,
        )
      )

      val string = jsonMapperKafkatorio.encodeToString(v)
      // language=JSON
      string shouldBe """
        {
          "modVersion": "0.3.2",
          "tick": 66,
          "data": {
            "type": "dev.adamko.kafkatorio.schema2.PlayerUpdate",
            "key": {
              "index": 1
            },
            "name": "fredthedeadhead",
            "afkTime": 0,
            "isShowOnMap": true,
            "isSpectator": false,
            "lastOnline": 0,
            "onlineTime": 0,
            "position": [
              -58.0,
              13.0
            ]
          }
        }
      """.trimIndent()
    }
  }

  context("player died") {
    // language=JSON
    val json = """
      {
        "tick": 227934,
        "modVersion": "0.3.2",
        "data": {
           "type": "dev.adamko.kafkatorio.schema2.PlayerUpdate",
           "key": {
             "index": 1
           },
          "lastOnline": 227932,
          "onlineTime": 227888,
          "afkTime": 324,
          "isConnected": true,
          "diedCause": {
            "unitNumber": 66,
            "name": "small-biter",
            "protoType": "unit"
          },
          "eventCounts": {
            "on_player_died": 2
          }
        }
      }
    """.trimIndent()

    test("decode") {
      val packet: KafkatorioPacket2 = jsonMapperKafkatorio.decodeFromString(json)

      packet shouldBe KafkatorioPacket2(
        modVersion = "0.3.2",
        tick = Tick(227934u),
        data = PlayerUpdate(
          afkTime = Tick(324u),
          bannedReason = null,
          characterUnitNumber = null,
          chatColour = null,
          colour = null,
          diedCause = EntityIdentifiersData(
            unitNumber = UnitNumber(66u),
            name = "small-biter",
            protoType = "unit",
          ),
          disconnectReason = null,
          eventCounts = mapOf("on_player_died" to 2u),
          forceIndex = null,
          key = PlayerUpdateKey(PlayerIndex(1u)),
          isAdmin = null,
          isConnected = true,
          isRemoved = null,
          isShowOnMap = null,
          isSpectator = null,
          kickedReason = null,
          lastOnline = Tick(227932u),
          name = null,
          onlineTime = Tick(227888u),
          position = null,
          surfaceIndex = null,
          tag = null,
          ticksToRespawn = null,
        )
      )
    }
  }

  context("surface update") {
    //language=json
    val json = """
      {
        "data": {
          "type": "dev.adamko.kafkatorio.schema2.SurfaceUpdate",
          "name": "nauvis",
          "index": 1,
          "daytime": 0.73
        },
        "modVersion": "0.3.2",
        "tick": 176000
      }
    """.trimIndent()

    val expected = KafkatorioPacket2(
      tick = Tick(176000u),
      modVersion = "0.3.2",
      data = SurfaceUpdate(
        name = "nauvis",
        index = SurfaceIndex(1u),
        daytime = 0.73,
      )
    )

    println(jsonMapperKafkatorio.encodeToString(expected))

    test("decode") {
      val packet: KafkatorioPacket2 = jsonMapperKafkatorio.decodeFromString(json)
      packet shouldBe expected
    }
  }
})
