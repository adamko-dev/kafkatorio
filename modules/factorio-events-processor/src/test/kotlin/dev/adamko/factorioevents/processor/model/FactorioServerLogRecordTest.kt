package dev.adamko.factorioevents.processor.model

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.http4k.format.Jackson
import org.intellij.lang.annotations.Language

class FactorioServerLogRecordTest : FunSpec({

  test("player obj") {
    @Language("JSON")
    val example = """
      {
       "mod_version":"0.0.3",
       "tick":1256529,
       "event_type":"on_player_joined_game",
       "data":{
          "object_name":"LuaPlayer",
          "name":"fredthedeadhead",
          "character_unit_number":1,
          "associated_characters_unit_numbers":{
             
          },
          "position":{
             "x":3.48828125,
             "y":23.43359375
          }
       }
      }
  """.trimIndent()
    val record: FactorioServerLogRecord<*> = Jackson.asA(example)

    record shouldBe null
  }

  test("surface obj") {
    @Language("JSON")
    val example = """
      {
         "mod_version":"0.0.3",
         "tick":1256529,
         "event_type":"on_player_joined_game",
         "data":{
            "object_name":"LuaEntity",
            "name":"character",
            "type":"character",
            "active":true,
            "health":250,
            "surface_index":1,
            "unit_number":1,
            "position":{
               "x":3.48828125,
               "y":23.43359375
            }
         }
      }
  """.trimIndent()
    val record: FactorioServerLogRecord<LuaSurfaceData> = Jackson.asA(example)

    record shouldNotBe null

    val str = Jackson.asFormatString(record)
    str.shouldEqualJson(example)
  }

})
