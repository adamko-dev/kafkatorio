package dev.adamko.kafkatorio.schema.packets

import dev.adamko.kafkatorio.library.LuaJsonList
import dev.adamko.kafkatorio.library.jsonMapperKafkatorio
import dev.adamko.kafkatorio.schema.common.Colour
import dev.adamko.kafkatorio.schema.common.EntityIdentifiersData
import dev.adamko.kafkatorio.schema.common.EventName
import dev.adamko.kafkatorio.schema.common.ForceIndex
import dev.adamko.kafkatorio.schema.common.MapEntityPosition
import dev.adamko.kafkatorio.schema.common.PlayerIndex
import dev.adamko.kafkatorio.schema.common.PrototypeId
import dev.adamko.kafkatorio.schema.common.SurfaceIndex
import dev.adamko.kafkatorio.schema.common.Tick
import dev.adamko.kafkatorio.schema.common.UnitNumber
import dev.adamko.kafkatorio.schema.common.tick
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
          "type": "kafkatorio.packet.keyed.PlayerUpdate",
          "key": {"index": 1},
          "afkTime": 0,
          "chatColour": [1, 0.4, 0.6, 1],
          "colour": [0.8, 0.5, 0.2, 0.5],
          "events": {
            "on_player_changed_position": [7, 99],
            "on_player_joined_game": [14]
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
      val packet: KafkatorioPacket = jsonMapperKafkatorio.decodeFromString(json)

      packet shouldBe KafkatorioPacket(
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
          events = mapOf(
            EventName("on_player_changed_position") to LuaJsonList(listOf(7u.tick, 99u.tick)),
            EventName("on_player_joined_game") to LuaJsonList(listOf(14u.tick)),
          ),
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
      val v = KafkatorioPacket(
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
            "type": "kafkatorio.packet.keyed.PlayerUpdate",
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
           "type": "kafkatorio.packet.keyed.PlayerUpdate",
           "key": {
             "index": 1
           },
          "lastOnline": 227932,
          "onlineTime": 227888,
          "afkTime": 324,
          "isConnected": true,
          "diedCause": {
            "unitNumber": 66,
            "protoId": "unit/small-biter"
          },
          "events": {
            "on_player_died": [2]
          }
        }
      }
    """.trimIndent()

    test("decode") {
      val packet: KafkatorioPacket = jsonMapperKafkatorio.decodeFromString(json)

      packet shouldBe KafkatorioPacket(
        modVersion = "0.3.2",
        tick = 227934u.tick,
        data = PlayerUpdate(
          afkTime = 324u.tick,
          bannedReason = null,
          characterUnitNumber = null,
          chatColour = null,
          colour = null,
          diedCause = EntityIdentifiersData(
            unitNumber = UnitNumber(66u),
            protoId = PrototypeId("unit", "small-biter"),
          ),
          disconnectReason = null,
          events = mapOf(
            EventName("on_player_died") to LuaJsonList(listOf(2u.tick)),
          ),
          forceIndex = null,
          key = PlayerUpdateKey(PlayerIndex(1u)),
          isAdmin = null,
          isConnected = true,
          isRemoved = null,
          isShowOnMap = null,
          isSpectator = null,
          kickedReason = null,
          lastOnline = 227932u.tick,
          name = null,
          onlineTime = 227888u.tick,
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
          "type": "kafkatorio.packet.instant.SurfaceUpdate",
          "name": "nauvis",
          "index": 1,
          "daytime": 0.73
        },
        "modVersion": "0.3.2",
        "tick": 176000
      }
    """.trimIndent()

    val expected = KafkatorioPacket(
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
      val packet: KafkatorioPacket = jsonMapperKafkatorio.decodeFromString(json)
      packet shouldBe expected
    }
  }
})
