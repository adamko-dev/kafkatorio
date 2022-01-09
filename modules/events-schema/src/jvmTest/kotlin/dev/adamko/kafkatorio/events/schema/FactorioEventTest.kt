package dev.adamko.kafkatorio.events.schema

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString
import org.intellij.lang.annotations.Language

class FactorioEventTest : BehaviorSpec({

  Given("json on_player_joined_game") {

    @Language("JSON")
    val json = """
       {
         "data": {
           "objectName": "LuaPlayer",
           "name": "fredthedeadhead",
           "characterUnitNumber": 1,
           "associatedCharactersUnitNumbers": {},
           "position": {
             "type": "MAP",
             "x": 30.65625,
             "y": 84.07421875
           },
           "colour": {
             "red": 0.869000017642974853515625,
             "green": 0.5,
             "blue": 0.12999999523162841796875,
             "alpha": 0.5
           },
           "chatColour": {
             "red": 1,
             "green": 0.62999999523162841796875,
             "blue": 0.259000003337860107421875,
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

    Then("parse") {
      val actual: KafkatorioPacket = jsonMapper.decodeFromString(KafkatorioPacket.serializer(), json)

      val expected = FactorioEvent(
        data = PlayerData(
          name = "fredthedeadhead",
          characterUnitNumber = 1u,
          associatedCharactersUnitNumbers = emptyList(),
          position = PositionData(
            type = PositionType.MAP,
            x = 30.65625,
            y = 84.07421875,
          ),
          colour = Colour(
            red = 0.869000017642974853515625f,
            green = 0.5f,
            blue = 0.12999999523162841796875f,
            alpha = 0.5f
          ),
          chatColour = Colour(
            red = 1f,
            green = 0.62999999523162841796875f,
            blue = 0.259000003337860107421875f,
            alpha = 1f
          ),
          lastOnline = 2287061u
        ),
        eventType = "on_player_joined_game",
        modVersion = "0.2.1",
        tick = 2287072u
      )

      actual shouldBeEqualToComparingFields expected

      expected.data.objectName shouldBe FactorioObjectData.ObjectName.LuaPlayer
    }
  }

  Given("json surface") {
    @Language("JSON")
    val json = """
      {
        "data": {
          "objectName": "LuaSurface",
          "name": "nauvis",
          "index": 1,
          "daytime": 0.749480000092057618843455202295444905757904052734375
        },
        "packetType": "EVENT",
        "eventType": "on_tick",
        "modVersion": "0.2.1",
        "tick": 2301240
      }
    """.trimIndent()
    Then("parse") {
      val actual: KafkatorioPacket = jsonMapper.decodeFromString(json)

      val expected = FactorioEvent(
        data = SurfaceData(
          name = "nauvis",
          index = 1u,
          daytime = 0.749480000092057618843455202295444905757904052734375
        ),
        eventType = "on_tick",
        modVersion = "0.2.1",
        tick = 2301240u
      )

      actual shouldBeEqualToComparingFields expected

      expected.data.objectName shouldBe FactorioObjectData.ObjectName.LuaSurface
    }
  }

  Given("LuaEntity on_player_changed_position") {
    @Language("JSON")
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
            "type": "MAP",
            "x": 37.859375,
            "y": 81.14453125
          }
        },
        "packetType": "EVENT",
        "eventType": "on_player_changed_position",
        "modVersion": "0.2.1",
        "tick": 2301231
      }
    """.trimIndent()
    Then("parse") {
      val actual: KafkatorioPacket = jsonMapper.decodeFromString(json)

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
          position = PositionData(
            x = 37.859375,
            y = 81.14453125,
            type = PositionType.MAP,
          )
        ),
        eventType = "on_player_changed_position",
        modVersion = "0.2.1",
        tick = 2301231u
      )

      actual shouldBeEqualToComparingFields expected

      expected.data.objectName shouldBe FactorioObjectData.ObjectName.LuaEntity
    }
  }

})
