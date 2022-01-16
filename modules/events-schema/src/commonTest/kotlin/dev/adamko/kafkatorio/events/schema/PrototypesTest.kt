package dev.adamko.kafkatorio.events.schema

import io.kotest.core.spec.style.BehaviorSpec
import kotlinx.serialization.decodeFromString

class PrototypesTest : BehaviorSpec(
  {
    Given("Prototypes Json") {
      //Language=JSON
      val raw = """
      {
         "modVersion":"0.2.4",
         "packetType":"PROTOTYPES",
         "prototypes":[ 
            {
               "prototypeObjectName":"LuaTilePrototype",
               "name":"tile-unknown",
               "order":"z-a",
               "layer":0,
               "collisionMasks":{
                  
               },
               "mapColour":{
                  "red":0,
                  "green":0,
                  "blue":0,
                  "alpha":255
               },
               "canBeMined":false
            } 
         ]
      }
""".trimIndent()

      Then("expect can be parsed") {
        val data = jsonMapperKafkatorio.decodeFromString<KafkatorioPacket>(raw)
        println(data)
      }

    }
  }
) {
  companion object {
  }
}
