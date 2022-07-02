package dev.adamko.kafkatorio.schema.packets

import dev.adamko.kafkatorio.library.jsonMapperKafkatorio
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.serialization.decodeFromString

class EntityUpdateTest : FunSpec({

  context("EntityUpdate packet") {
    /*language=json*/
    val json = """
        {
        	"data": {
        		"type": "kafkatorio.packet.keyed.EntityUpdate",
        		"key": {
        			"name": "assembling-machine-2",
        			"protoType": "assembling-machine-2",
        			"unitNumber": 4622
        		},
        		"events": {
        			"on_built_entity": [
        				38023
        			]
        		},
        		"chunkPosition": [
        			-13.5,
        			4.5
        		],
        		"health": 350,
        		"isActive": true,
        		"isRotatable": true,
        		"lastUser": 1,
        		"prototype": "assembling-machine-2"
        	},
        	"modVersion": "0.4.0",
        	"tick": 38388
        }
      """.trimIndent()
    test("decode") {
      val entityUpdate: KafkatorioPacket = jsonMapperKafkatorio.decodeFromString(json)
      entityUpdate.data
        .shouldBeInstanceOf<EntityUpdate>()

//      entityUpdate.health.shouldNotBeNull().shouldBeExactly(350f)
    }

  }

})
