package dev.adamko.kafkatorio.schema.common

import dev.adamko.kafkatorio.library.jsonMapperKafkatorio
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
      [0.1, 0.4, 0.7, 0.6]
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
          [
            ${r.toFloat() / UByte.MAX_VALUE.toFloat()},
            ${g.toFloat() / UByte.MAX_VALUE.toFloat()},
            ${b.toFloat() / UByte.MAX_VALUE.toFloat()},
            ${a.toFloat() / UByte.MAX_VALUE.toFloat()}
          ]
        """.trimIndent()

        val colour: Colour = jsonMapperKafkatorio.decodeFromString(colourJson)
        val colourHex = colour.toHex()
        colourHex shouldBe ColourHex(r, g, b, a)
      }
    }
  }
})
