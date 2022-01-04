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
          "object_name": "LuaPlayer",
          "name": "fredthedeadhead",
          "character_unit_number": 1,
          "associated_characters_unit_numbers": {},
          "position": {
            "x": -3.5703125,
            "y": 29.75
          },
          "last_online": 123
        },
        "event_type": "on_player_joined_game",
        "mod_version": "0.0.4",
        "factorio_version": "1.1.48",
        "tick": 1278458
      }
    """.trimIndent()

    Then("parse") {
      val actual: FactorioEvent<FactorioObjectData> = jsonMapper.decodeFromString(json)

      val expected = FactorioEvent(
        data = PlayerData(
          name = "fredthedeadhead",
          characterUnitNumber = 1u,
          associatedCharactersUnitNumbers = emptyList(),
          position = PositionData(
            x = -3.5703125,
            y = 29.75
          ),
          colour = Colour(),
          chatColour = Colour(),
          lastOnline = 123u
        ),
        eventType = "on_player_joined_game",
        modVersion = "0.0.4",
        factorioVersion = "1.1.48",
        tick = 1278458u
      )

      actual shouldBeEqualToComparingFields expected

      expected.data.objectName shouldBe "LuaPlayer"
    }
  }

  Given("json surface") {
    @Language("JSON")
    val json = """
      {
        "data": {
          "object_name": "LuaSurface",
          "name": "nauvis",
          "index": 1,
          "daytime": 0.8390800000511436707029133685864508152008056640625
        },
        "event_type": "on_tick",
        "mod_version": "0.0.4",
        "factorio_version": "1.1.48",
        "tick": 1278480
      }
    """.trimIndent()
    Then("parse") {
      val actual: FactorioEvent<FactorioObjectData> = jsonMapper.decodeFromString(json)

      val expected = FactorioEvent(
        data = SurfaceData(
          name = "nauvis",
          index = 1u,
          daytime = 0.8390800000511436707029133685864508152008056640625
        ),
        eventType = "on_tick",
        modVersion = "0.0.4",
        factorioVersion = "1.1.48",
        tick = 1278480u
      )

      actual shouldBeEqualToComparingFields expected

      expected.data.objectName shouldBe "LuaSurface"
    }
  }

  Given("json entity") {
    @Language("JSON")
    val json = """
      {
        "data": {
          "object_name": "LuaEntity",
          "name": "character",
          "type": "character",
          "active": true,
          "health": 250,
          "health_ratio": 1,
          "surface_index": 1,
          "unit_number": 1,
          "position": {
            "x": -0.25,
            "y": 30.09375
          }
        },
        "event_type": "on_player_changed_position",
        "mod_version": "0.0.6",
        "factorio_version": "1.1.48",
        "tick": 1384507
      }
    """.trimIndent()
    Then("parse") {
      val actual: FactorioEvent<FactorioObjectData> = jsonMapper.decodeFromString(json)

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
            x = -0.25,
            y = 30.09375,
          )
        ),
        eventType = "on_player_changed_position",
        modVersion = "0.0.6",
        factorioVersion = "1.1.48",
        tick = 1384507u
      )

      actual shouldBeEqualToComparingFields expected

      expected.data.objectName shouldBe "LuaEntity"
    }
  }

})
