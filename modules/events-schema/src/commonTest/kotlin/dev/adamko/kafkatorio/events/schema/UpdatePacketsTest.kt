package dev.adamko.kafkatorio.events.schema

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
        "packetType": "UPDATE",
        "update": {
          "afkTime": 0,
          "chatColour": {
            "red": 1,
            "green": 0.4,
            "blue": 0.6,
            "alpha": 1
          },
          "colour": {
            "red": 0.8,
            "green": 0.5,
            "blue": 0.2,
            "alpha": 0.5
          },
          "eventCounts": {
            "on_player_changed_position": 7,
            "on_player_joined_game": 1
          },
          "forceIndex": 1,
          "index": 1,
          "isAdmin": true,
          "isConnected": true,
          "isShowOnMap": true,
          "isSpectator": false,
          "lastOnline": 0,
          "name": "fredthedeadhead",
          "onlineTime": 0,
          "position": {
            "y": 13,
            "x": -58
          },
          "tag": "",
          "updateType": "PLAYER"
        }
      }
    """.trimIndent()

    test("decode") {
      val packet: FactorioEventUpdatePacket = jsonMapperKafkatorio.decodeFromString(json)

      packet shouldBe FactorioEventUpdatePacket(
        modVersion = "0.3.2",
        tick = Tick(66u),
        update = PlayerUpdate(
          afkTime = Tick(0u),
          bannedReason = null,
          characterUnitNumber = null,
          chatColour = Colour(red = 1f, green = 0.4f, blue = 0.6f, alpha = 1f),
          colour =  Colour(red = 0.8f, green = 0.5f, blue = 0.2f, alpha = 0.5f),
          diedCause = null,
          disconnectReason = null,
          eventCounts = mapOf("on_player_changed_position" to 7u, "on_player_joined_game" to 1u),
          forceIndex = ForceIndex(1u),
          index = PlayerIndex(1u),
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
      val v = FactorioEventUpdatePacket(
        modVersion = "0.3.2",
        tick = Tick(66u),
        update = PlayerUpdate(
          index = PlayerIndex(1u),
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
          "update": {
            "index": 1,
            "name": "fredthedeadhead",
            "afkTime": 0,
            "isShowOnMap": true,
            "isSpectator": false,
            "lastOnline": 0,
            "onlineTime": 0,
            "position": {
              "x": -58.0,
              "y": 13.0
            },
            "updateType": "PLAYER"
          },
          "packetType": "UPDATE"
        }
      """.trimIndent()
    }
  }


})

/*
{"tick":66,"modVersion":"0.3.2","packetType":"UPDATE","update":{"index":1,"updateType":"PLAYER","position":{"y":13.5234375,"x":-58.9375},"eventCounts":{"on_player_changed_position":7,"on_player_joined_game":1},"isAdmin":true,"chatColour":{"red":1,"green":0.62999999523162841796875,"blue":0.259000003337860107421875,"alpha":1},"colour":{"red":0.869000017642974853515625,"green":0.5,"blue":0.12999999523162841796875,"alpha":0.5},"forceIndex":1,"name":"fredthedeadhead","isShowOnMap":true,"isSpectator":false,"tag":"","lastOnline":0,"onlineTime":0,"afkTime":0,"isConnected":true}}
 */
