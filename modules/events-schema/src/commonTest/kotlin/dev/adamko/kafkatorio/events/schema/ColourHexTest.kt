package dev.adamko.kafkatorio.events.schema

import dev.adamko.kafkatorio.schema.common.Colour
import dev.adamko.kafkatorio.schema.common.ColourHex
import dev.adamko.kafkatorio.schema.common.toHex
import dev.adamko.kafkatorio.schema.common.toPercentile
import dev.adamko.kafkatorio.schema.jsonMapperKafkatorio
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.floats.shouldBeWithinPercentageOf
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import kotlinx.serialization.decodeFromString

class ColourHexTest : BehaviorSpec({

  Given("colour json") {

    //Language=JSON
    val colourJson = """
      {
        "red": 0.1,
        "green": 0.4,
        "blue": 0.7,
        "alpha": 0.6
      }
    """.trimIndent()

    val colour: Colour = jsonMapperKafkatorio.decodeFromString(colourJson)

    Then("expect colour is decoded") {
      colour shouldBe Colour(
        0.1f,
        0.4f,
        0.7f,
        0.6f,
      )
    }

    Then("expect Colour can be converted to ColourHex and back") {
      val colourHex = colour.toHex()
      colourHex shouldBe ColourHex(
        25u,
        102u,
        178u,
        153u,
      )

      colourHex.toPercentile() should { back ->
        withClue(back) {

          val tolerance = 2.6 // 2.6%, because 255/100 = 2.55

          withClue("red") { back.red.shouldBeWithinPercentageOf(0.1f, tolerance) }
          withClue("green") { back.green.shouldBeWithinPercentageOf(0.4f, tolerance) }
          withClue("blue") { back.blue.shouldBeWithinPercentageOf(0.7f, tolerance) }
          withClue("alpha") { back.alpha.shouldBeWithinPercentageOf(0.6f, tolerance) }
        }
      }
    }
  }

  Given("any colour json") {
    Then("expect can always convert") {

      checkAll<UByte, UByte, UByte, UByte> { r, g, b, a ->

        //Language=JSON
        val colourJson = """
          {
            "red":   ${r.toFloat() / UByte.MAX_VALUE.toFloat()},
            "green": ${g.toFloat() / UByte.MAX_VALUE.toFloat()},
            "blue":  ${b.toFloat() / UByte.MAX_VALUE.toFloat()},
            "alpha": ${a.toFloat() / UByte.MAX_VALUE.toFloat()}
          }
        """.trimIndent()

        val colour: Colour = jsonMapperKafkatorio.decodeFromString(colourJson)
        val colourHex = colour.toHex()
        colourHex shouldBe ColourHex(r, g, b, a)

      }

    }
  }
})
