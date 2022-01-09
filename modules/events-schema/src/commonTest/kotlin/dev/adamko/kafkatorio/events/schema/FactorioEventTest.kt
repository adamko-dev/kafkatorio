package dev.adamko.kafkatorio.events.schema

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

class FactorioEventTest : FunSpec({

  context("Given: json on_player_joined_game") {

    // language=JSON
    val json = """
       {
         "data": {
           "objectName": "LuaPlayer",
           "name": "fredthedeadhead",
           "characterUnitNumber": 1,
           "associatedCharactersUnitNumbers": {},
           "position": {
             "x": 30.6,
             "y": 84.1
           },
           "colour": {
             "red": 0.86,
             "green": 0.5,
             "blue": 0.12,
             "alpha": 0.5
           },
           "chatColour": {
             "red": 1,
             "green": 0.62,
             "blue": 0.25,
             "alpha": 1
           },
           "lastOnline": 2287061
         },
         "packetType": "EVENT",
         "eventType": "on_player_joined_game",
         "modVersion": "0.2.1",
         "tick": 2287072
       }
    """.trimIndent()
    context("When: decoded") {
      val actual: KafkatorioPacket = jsonMapperKafkatorio.decodeFromString(json)

      test("Then: parse") {

        val expected = FactorioEvent(
          data = PlayerData(
            name = "fredthedeadhead",
            characterUnitNumber = 1u,
            associatedCharactersUnitNumbers = emptyList(),
            position = MapEntityPosition(
              x = 30.6,
              y = 84.1,
            ),
            colour = Colour(
              red = 0.86f,
              green = 0.5f,
              blue = 0.12f,
              alpha = 0.5f
            ),
            chatColour = Colour(
              red = 1f,
              green = 0.62f,
              blue = 0.25f,
              alpha = 1f
            ),
            lastOnline = 2287061u
          ),
          eventType = "on_player_joined_game",
          modVersion = "0.2.1",
          tick = 2287072u
        )

        actual shouldBe expected

        expected.data.objectName shouldBe FactorioObjectData.ObjectName.LuaPlayer
      }

      test("Then: expect encode equals json") {
        val encoded = jsonMapperKafkatorio.encodeToString(actual)
        encoded.shouldEqualJson(json)
      }
    }
  }

  context("Given: json LuaSurface") {
    // language=JSON
    val json = """
      {
        "data": {
          "objectName": "LuaSurface",
          "name": "nauvis",
          "index": 1,
          "daytime": 0.745
        },
        "packetType": "EVENT",
        "eventType": "on_tick",
        "modVersion": "0.2.1",
        "tick": 2301240
      }
    """.trimIndent()

    context("When: decoded") {
      val actual: KafkatorioPacket = jsonMapperKafkatorio.decodeFromString(json)

      test("should equal obj") {

        val expected = FactorioEvent(
          data = SurfaceData(
            name = "nauvis",
            index = 1u,
            daytime = 0.745
          ),
          eventType = "on_tick",
          modVersion = "0.2.1",
          tick = 2301240u
        )

        actual shouldBe expected

        expected.data.objectName shouldBe FactorioObjectData.ObjectName.LuaSurface
      }

      test("Then: expect encode equals json") {
        val encoded = jsonMapperKafkatorio.encodeToString(actual)
        encoded.shouldEqualJson(json)
      }
    }
  }

  context("Given: LuaEntity on_player_changed_position") {
    // language=JSON
    val json = """
      {
        "data": {
          "objectName": "LuaEntity",
          "name": "character",
          "type": "character",
          "active": true,
          "health": 250,
          "healthRatio": 1,
          "surfaceIndex": 1,
          "unitNumber": 1,
          "position": {
            "x": 37.5,
            "y": 81.1
          }
        },
        "packetType": "EVENT",
        "eventType": "on_player_changed_position",
        "modVersion": "0.2.1",
        "tick": 2301231
      }
    """.trimIndent()
    context("When: decoded") {
      val actual: KafkatorioPacket = jsonMapperKafkatorio.decodeFromString(json)

      test("parse") {

        val expected = FactorioEvent(
          data = EntityData(
            name = "character",
            type = "character",
            active = true,
            health = 250.0,
            healthRatio = 1.0,
            surfaceIndex = 1,
            unitNumber = 1u,
            playerIndex = null,
            position = MapEntityPosition(
              x = 37.5,
              y = 81.1,
            )
          ),
          eventType = "on_player_changed_position",
          modVersion = "0.2.1",
          tick = 2301231u
        )

        actual shouldBe expected

        expected.data.objectName shouldBe FactorioObjectData.ObjectName.LuaEntity
      }

      test("Then: expect encode equals json") {
        val encoded = jsonMapperKafkatorio.encodeToString(actual)
        encoded.shouldEqualJson(json)
      }
    }

  }
})
