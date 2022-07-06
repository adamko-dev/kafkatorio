package dev.adamko.kafkatorio.events.schema

import dev.adamko.kafkatorio.library.jsonMapperKafkatorio
import dev.adamko.kafkatorio.schema.common.Colour
import dev.adamko.kafkatorio.schema.common.FactorioPrototype
import dev.adamko.kafkatorio.schema.common.PrototypeId
import dev.adamko.kafkatorio.schema.packets.KafkatorioPacket
import dev.adamko.kafkatorio.schema.packets.PrototypesUpdate
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.inspectors.forOne
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.serialization.decodeFromString


class PrototypesTest : BehaviorSpec({
  Given("Prototypes Json") {
    // language=JSON
    val raw = """
{
  "modVersion": "0.2.4",
  "tick": 534,
  "data": {
    "type": "kafkatorio.packet.instant.PrototypesUpdate",
    "prototypes": [
      {
        "type": "kafkatorio.prototype.MapTile",
        "protoId": "tile/stone-path",
        "order": "a[artificial]-a[tier-1]-a[stone-path]",
        "layer": 60,
        "collisionMasks": [
          "ground-tile"
        ],
        "mapColour": [
          86,
          82,
          74,
          255
        ],
        "canBeMined": true
      }
    ]
  }
}
    """.trimIndent()

    Then("expect can be parsed") {
      val data = jsonMapperKafkatorio.decodeFromString<KafkatorioPacket>(raw)
      println(data)
    }

  }

  Given("Big prototypes json") {
    // language=JSON
    val raw = """
      {
        "data": {
          "prototypes": [
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/stone-path",
              "order": "a[artificial]-a[tier-1]-a[stone-path]",
              "layer": 60,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                86,
                82,
                74,
                255
              ],
              "canBeMined": true
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/concrete",
              "order": "a[artificial]-b[tier-2]-a[concrete]",
              "layer": 61,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                63,
                61,
                59,
                255
              ],
              "canBeMined": true
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/hazard-concrete-left",
              "order": "a[artificial]-b[tier-2]-b[hazard-concrete-left]",
              "layer": 62,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                176,
                142,
                39,
                255
              ],
              "canBeMined": true
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/hazard-concrete-right",
              "order": "a[artificial]-b[tier-2]-c[hazard-concrete-right]",
              "layer": 62,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                176,
                142,
                39,
                255
              ],
              "canBeMined": true
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/refined-concrete",
              "order": "a[artificial]-c[tier-3]-a[refined-concrete]",
              "layer": 64,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                49,
                48,
                45,
                255
              ],
              "canBeMined": true
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/refined-hazard-concrete-left",
              "order": "a[artificial]-c[tier-3]-b[refined-hazard-concrete-left]",
              "layer": 65,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                116,
                94,
                26,
                255
              ],
              "canBeMined": true
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/refined-hazard-concrete-right",
              "order": "a[artificial]-c[tier-3]-c[refined-hazard-concrete-right]",
              "layer": 65,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                116,
                94,
                26,
                255
              ],
              "canBeMined": true
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/landfill",
              "order": "a[artificial]-d[utility]-a[landfill]",
              "layer": 57,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                57,
                39,
                26,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/acid-refined-concrete",
              "order": "a[artificial]-e[color-concrete]-acid",
              "layer": 97,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                142,
                194,
                40,
                127
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/black-refined-concrete",
              "order": "a[artificial]-e[color-concrete]-black",
              "layer": 88,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                25,
                25,
                25,
                127
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/blue-refined-concrete",
              "order": "a[artificial]-e[color-concrete]-blue",
              "layer": 73,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                39,
                137,
                228,
                127
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/brown-refined-concrete",
              "order": "a[artificial]-e[color-concrete]-brown",
              "layer": 91,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                76,
                29,
                0,
                127
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/cyan-refined-concrete",
              "order": "a[artificial]-e[color-concrete]-cyan",
              "layer": 94,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                70,
                192,
                181,
                127
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/green-refined-concrete",
              "order": "a[artificial]-e[color-concrete]-green",
              "layer": 70,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                23,
                195,
                43,
                127
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/orange-refined-concrete",
              "order": "a[artificial]-e[color-concrete]-orange",
              "layer": 76,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                221,
                127,
                33,
                127
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/pink-refined-concrete",
              "order": "a[artificial]-e[color-concrete]-pink",
              "layer": 82,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                236,
                98,
                131,
                127
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/purple-refined-concrete",
              "order": "a[artificial]-e[color-concrete]-purple",
              "layer": 85,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                123,
                28,
                168,
                127
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/red-refined-concrete",
              "order": "a[artificial]-e[color-concrete]-red",
              "layer": 67,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                207,
                6,
                0,
                127
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/yellow-refined-concrete",
              "order": "a[artificial]-e[color-concrete]-yellow",
              "layer": 79,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                212,
                169,
                19,
                127
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/grass-1",
              "order": "b[natural]-a[grass]-a[grass-1]",
              "layer": 26,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                55,
                53,
                11,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/grass-2",
              "order": "b[natural]-a[grass]-b[grass-2]",
              "layer": 28,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                66,
                57,
                15,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/grass-3",
              "order": "b[natural]-a[grass]-c[grass-3]",
              "layer": 29,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                65,
                52,
                28,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/grass-4",
              "order": "b[natural]-a[grass]-d[grass-4]",
              "layer": 30,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                59,
                40,
                18,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/dry-dirt",
              "order": "b[natural]-b[dirt]-a[dry-dirt]",
              "layer": 18,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                94,
                66,
                37,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/dirt-1",
              "order": "b[natural]-b[dirt]-b[dirt-1]",
              "layer": 19,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                141,
                104,
                60,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/dirt-2",
              "order": "b[natural]-b[dirt]-c[dirt-2]",
              "layer": 20,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                136,
                96,
                59,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/dirt-3",
              "order": "b[natural]-b[dirt]-d[dirt-3]",
              "layer": 21,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                133,
                92,
                53,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/dirt-4",
              "order": "b[natural]-b[dirt]-e[dirt-4]",
              "layer": 22,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                103,
                72,
                43,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/dirt-5",
              "order": "b[natural]-b[dirt]-f[dirt-5]",
              "layer": 23,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                91,
                63,
                38,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/dirt-6",
              "order": "b[natural]-b[dirt]-g[dirt-6]",
              "layer": 24,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                80,
                55,
                31,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/dirt-7",
              "order": "b[natural]-b[dirt]-h[dirt-7]",
              "layer": 25,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                80,
                54,
                28,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/sand-1",
              "order": "b[natural]-c[sand]-a[sand-1]",
              "layer": 8,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                138,
                103,
                58,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/sand-2",
              "order": "b[natural]-c[sand]-b[sand-2]",
              "layer": 9,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                128,
                93,
                52,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/sand-3",
              "order": "b[natural]-c[sand]-c[sand-3]",
              "layer": 10,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                115,
                83,
                47,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/red-desert-0",
              "order": "b[natural]-d[red-desert]-a[red-desert-0]",
              "layer": 31,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                103,
                70,
                32,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/red-desert-1",
              "order": "b[natural]-d[red-desert]-b[red-desert-1]",
              "layer": 14,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                116,
                81,
                39,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/red-desert-2",
              "order": "b[natural]-d[red-desert]-c[red-desert-2]",
              "layer": 15,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                116,
                84,
                43,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/red-desert-3",
              "order": "b[natural]-d[red-desert]-d[red-desert-3]",
              "layer": 16,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                128,
                93,
                52,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/water",
              "order": "c[water]-a[water]",
              "layer": 3,
              "collisionMasks": [
                "doodad-layer",
                "item-layer",
                "player-layer",
                "resource-layer",
                "water-tile"
              ],
              "mapColour": [
                51,
                83,
                95,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/deepwater",
              "order": "c[water]-b[deep-water]",
              "layer": 3,
              "collisionMasks": [
                "doodad-layer",
                "item-layer",
                "player-layer",
                "resource-layer",
                "water-tile"
              ],
              "mapColour": [
                38,
                64,
                73,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/water-green",
              "order": "c[water]-c[water-green]",
              "layer": 3,
              "collisionMasks": [
                "doodad-layer",
                "item-layer",
                "player-layer",
                "resource-layer",
                "water-tile"
              ],
              "mapColour": [
                31,
                48,
                18,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/deepwater-green",
              "order": "c[water]-d[deepwater-green]",
              "layer": 3,
              "collisionMasks": [
                "doodad-layer",
                "item-layer",
                "player-layer",
                "resource-layer",
                "water-tile"
              ],
              "mapColour": [
                23,
                37,
                16,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/water-shallow",
              "order": "c[water]-e[water-shallow]",
              "layer": 6,
              "collisionMasks": [
                "item-layer",
                "object-layer",
                "resource-layer",
                "water-tile"
              ],
              "mapColour": [
                82,
                98,
                92,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/water-mud",
              "order": "c[water]-g[water-mud]",
              "layer": 7,
              "collisionMasks": [
                "item-layer",
                "object-layer",
                "resource-layer",
                "water-tile"
              ],
              "mapColour": [
                65,
                89,
                90,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/nuclear-ground",
              "order": "d[destruction]-a[nuclear]-a[nuclear-ground]",
              "layer": 33,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                48,
                40,
                35,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/water-wube",
              "order": "x[wube]-a[water-wube]",
              "layer": 2,
              "collisionMasks": [
                "doodad-layer",
                "item-layer",
                "player-layer",
                "resource-layer",
                "water-tile"
              ],
              "mapColour": [
                0,
                0,
                0,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/tile-unknown",
              "order": "z-a",
              "layer": 0,
              "collisionMasks": {},
              "mapColour": [
                0,
                0,
                0,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/out-of-map",
              "order": "z[other]-a[out-of-map]",
              "layer": 0,
              "collisionMasks": [
                "doodad-layer",
                "floor-layer",
                "ground-tile",
                "item-layer",
                "object-layer",
                "player-layer",
                "resource-layer",
                "water-tile"
              ],
              "mapColour": [
                0,
                0,
                0,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/lab-dark-1",
              "order": "z[other]-b[lab]-a[lab-dark-1]",
              "layer": 70,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                49,
                49,
                49,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/lab-dark-2",
              "order": "z[other]-b[lab]-b[lab-dark-2]",
              "layer": 70,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                0,
                0,
                0,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/lab-white",
              "order": "z[other]-b[lab]-c[lab-white]",
              "layer": 70,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                255,
                255,
                255,
                255
              ],
              "canBeMined": false
            },
            {
              "type": "kafkatorio.prototype.MapTile",
              "protoId": "tile/tutorial-grid",
              "order": "z[other]-c[tutorial]-a[tutorial-grid]",
              "layer": 55,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": [
                122,
                122,
                122,
                255
              ],
              "canBeMined": false
            }
          ],
          "type": "kafkatorio.packet.instant.PrototypesUpdate"
        },
        "modVersion": "0.6.1",
        "tick": 58468
      }
    """.trimIndent()

    Then("expect can be parsed") {
      val packet: KafkatorioPacket = jsonMapperKafkatorio.decodeFromString(raw)
//      println(packet)

      packet.data.shouldBeInstanceOf<PrototypesUpdate>()
        .prototypes
        .forOne {
          it.shouldBeInstanceOf<FactorioPrototype.MapTile>()
          it.protoId shouldBe PrototypeId("tile", "tutorial-grid")
          it.order shouldBe "z[other]-c[tutorial]-a[tutorial-grid]"
          it.layer shouldBe 55u
          it.collisionMasks shouldBe listOf("ground-tile")
          it.mapColour shouldBe Colour(122f, 122f, 122f, 255f)
          it.canBeMined shouldBe false
        }
    }
  }
})
