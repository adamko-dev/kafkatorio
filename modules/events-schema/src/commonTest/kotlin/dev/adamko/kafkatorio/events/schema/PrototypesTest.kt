package dev.adamko.kafkatorio.events.schema

import dev.adamko.kafkatorio.schema.common.Colour
import dev.adamko.kafkatorio.schema.common.PrototypeName
import dev.adamko.kafkatorio.schema.jsonMapperKafkatorio
import dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2
import dev.adamko.kafkatorio.schema2.KafkatorioPacket2
import dev.adamko.kafkatorio.schema2.PrototypesUpdate
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
    "type": "dev.adamko.kafkatorio.schema2.PrototypesUpdate",
    "prototypes": [
      {
        "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
        "name": "tile-unknown",
        "order": "z-a",
        "layer": 0,
        "collisionMasks": {},
        "mapColour": {
          "red": 0,
          "green": 0,
          "blue": 0,
          "alpha": 255
        },
        "canBeMined": false
      }
    ]
  }
}
    """.trimIndent()

    Then("expect can be parsed") {
      val data = jsonMapperKafkatorio.decodeFromString<KafkatorioPacket2>(raw)
      println(data)
    }

  }

  Given("Big prototypes json") {
    // language=JSON
    val raw = """
      {
        "modVersion": "0.3.2",
        "tick": 534,
        "data": {
          "type": "dev.adamko.kafkatorio.schema2.PrototypesUpdate",
          "prototypes": [
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "stone-path",
              "order": "a[artificial]-a[tier-1]-a[stone-path]",
              "layer": 60,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 86,
                "green": 82,
                "blue": 74,
                "alpha": 255
              },
              "canBeMined": true
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "concrete",
              "order": "a[artificial]-b[tier-2]-a[concrete]",
              "layer": 61,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 63,
                "green": 61,
                "blue": 59,
                "alpha": 255
              },
              "canBeMined": true
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "hazard-concrete-left",
              "order": "a[artificial]-b[tier-2]-b[hazard-concrete-left]",
              "layer": 62,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 176,
                "green": 142,
                "blue": 39,
                "alpha": 255
              },
              "canBeMined": true
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "hazard-concrete-right",
              "order": "a[artificial]-b[tier-2]-c[hazard-concrete-right]",
              "layer": 62,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 176,
                "green": 142,
                "blue": 39,
                "alpha": 255
              },
              "canBeMined": true
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "refined-concrete",
              "order": "a[artificial]-c[tier-3]-a[refined-concrete]",
              "layer": 64,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 49,
                "green": 48,
                "blue": 45,
                "alpha": 255
              },
              "canBeMined": true
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "refined-hazard-concrete-left",
              "order": "a[artificial]-c[tier-3]-b[refined-hazard-concrete-left]",
              "layer": 65,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 116,
                "green": 94,
                "blue": 26,
                "alpha": 255
              },
              "canBeMined": true
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "refined-hazard-concrete-right",
              "order": "a[artificial]-c[tier-3]-c[refined-hazard-concrete-right]",
              "layer": 65,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 116,
                "green": 94,
                "blue": 26,
                "alpha": 255
              },
              "canBeMined": true
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "landfill",
              "order": "a[artificial]-d[utility]-a[landfill]",
              "layer": 57,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 57,
                "green": 39,
                "blue": 26,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "acid-refined-concrete",
              "order": "a[artificial]-e[color-concrete]-acid",
              "layer": 97,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 142,
                "green": 194,
                "blue": 40,
                "alpha": 127
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "black-refined-concrete",
              "order": "a[artificial]-e[color-concrete]-black",
              "layer": 88,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 25,
                "green": 25,
                "blue": 25,
                "alpha": 127
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "blue-refined-concrete",
              "order": "a[artificial]-e[color-concrete]-blue",
              "layer": 73,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 39,
                "green": 137,
                "blue": 228,
                "alpha": 127
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "brown-refined-concrete",
              "order": "a[artificial]-e[color-concrete]-brown",
              "layer": 91,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 76,
                "green": 29,
                "blue": 0,
                "alpha": 127
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "cyan-refined-concrete",
              "order": "a[artificial]-e[color-concrete]-cyan",
              "layer": 94,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 70,
                "green": 192,
                "blue": 181,
                "alpha": 127
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "green-refined-concrete",
              "order": "a[artificial]-e[color-concrete]-green",
              "layer": 70,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 23,
                "green": 195,
                "blue": 43,
                "alpha": 127
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "orange-refined-concrete",
              "order": "a[artificial]-e[color-concrete]-orange",
              "layer": 76,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 221,
                "green": 127,
                "blue": 33,
                "alpha": 127
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "pink-refined-concrete",
              "order": "a[artificial]-e[color-concrete]-pink",
              "layer": 82,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 236,
                "green": 98,
                "blue": 131,
                "alpha": 127
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "purple-refined-concrete",
              "order": "a[artificial]-e[color-concrete]-purple",
              "layer": 85,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 123,
                "green": 28,
                "blue": 168,
                "alpha": 127
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "red-refined-concrete",
              "order": "a[artificial]-e[color-concrete]-red",
              "layer": 67,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 207,
                "green": 6,
                "blue": 0,
                "alpha": 127
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "yellow-refined-concrete",
              "order": "a[artificial]-e[color-concrete]-yellow",
              "layer": 79,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 212,
                "green": 169,
                "blue": 19,
                "alpha": 127
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "grass-1",
              "order": "b[natural]-a[grass]-a[grass-1]",
              "layer": 26,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 55,
                "green": 53,
                "blue": 11,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "grass-2",
              "order": "b[natural]-a[grass]-b[grass-2]",
              "layer": 28,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 66,
                "green": 57,
                "blue": 15,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "grass-3",
              "order": "b[natural]-a[grass]-c[grass-3]",
              "layer": 29,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 65,
                "green": 52,
                "blue": 28,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "grass-4",
              "order": "b[natural]-a[grass]-d[grass-4]",
              "layer": 30,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 59,
                "green": 40,
                "blue": 18,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "dry-dirt",
              "order": "b[natural]-b[dirt]-a[dry-dirt]",
              "layer": 18,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 94,
                "green": 66,
                "blue": 37,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "dirt-1",
              "order": "b[natural]-b[dirt]-b[dirt-1]",
              "layer": 19,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 141,
                "green": 104,
                "blue": 60,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "dirt-2",
              "order": "b[natural]-b[dirt]-c[dirt-2]",
              "layer": 20,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 136,
                "green": 96,
                "blue": 59,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "dirt-3",
              "order": "b[natural]-b[dirt]-d[dirt-3]",
              "layer": 21,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 133,
                "green": 92,
                "blue": 53,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "dirt-4",
              "order": "b[natural]-b[dirt]-e[dirt-4]",
              "layer": 22,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 103,
                "green": 72,
                "blue": 43,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "dirt-5",
              "order": "b[natural]-b[dirt]-f[dirt-5]",
              "layer": 23,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 91,
                "green": 63,
                "blue": 38,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "dirt-6",
              "order": "b[natural]-b[dirt]-g[dirt-6]",
              "layer": 24,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 80,
                "green": 55,
                "blue": 31,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "dirt-7",
              "order": "b[natural]-b[dirt]-h[dirt-7]",
              "layer": 25,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 80,
                "green": 54,
                "blue": 28,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "sand-1",
              "order": "b[natural]-c[sand]-a[sand-1]",
              "layer": 8,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 138,
                "green": 103,
                "blue": 58,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "sand-2",
              "order": "b[natural]-c[sand]-b[sand-2]",
              "layer": 9,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 128,
                "green": 93,
                "blue": 52,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "sand-3",
              "order": "b[natural]-c[sand]-c[sand-3]",
              "layer": 10,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 115,
                "green": 83,
                "blue": 47,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "red-desert-0",
              "order": "b[natural]-d[red-desert]-a[red-desert-0]",
              "layer": 31,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 103,
                "green": 70,
                "blue": 32,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "red-desert-1",
              "order": "b[natural]-d[red-desert]-b[red-desert-1]",
              "layer": 14,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 116,
                "green": 81,
                "blue": 39,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "red-desert-2",
              "order": "b[natural]-d[red-desert]-c[red-desert-2]",
              "layer": 15,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 116,
                "green": 84,
                "blue": 43,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "red-desert-3",
              "order": "b[natural]-d[red-desert]-d[red-desert-3]",
              "layer": 16,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 128,
                "green": 93,
                "blue": 52,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "water",
              "order": "c[water]-a[water]",
              "layer": 3,
              "collisionMasks": [
                "doodad-layer",
                "item-layer",
                "player-layer",
                "resource-layer",
                "water-tile"
              ],
              "mapColour": {
                "red": 51,
                "green": 83,
                "blue": 95,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "deepwater",
              "order": "c[water]-b[deep-water]",
              "layer": 3,
              "collisionMasks": [
                "doodad-layer",
                "item-layer",
                "player-layer",
                "resource-layer",
                "water-tile"
              ],
              "mapColour": {
                "red": 38,
                "green": 64,
                "blue": 73,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "water-green",
              "order": "c[water]-c[water-green]",
              "layer": 3,
              "collisionMasks": [
                "doodad-layer",
                "item-layer",
                "player-layer",
                "resource-layer",
                "water-tile"
              ],
              "mapColour": {
                "red": 31,
                "green": 48,
                "blue": 18,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "deepwater-green",
              "order": "c[water]-d[deepwater-green]",
              "layer": 3,
              "collisionMasks": [
                "doodad-layer",
                "item-layer",
                "player-layer",
                "resource-layer",
                "water-tile"
              ],
              "mapColour": {
                "red": 23,
                "green": 37,
                "blue": 16,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "water-shallow",
              "order": "c[water]-e[water-shallow]",
              "layer": 6,
              "collisionMasks": [
                "item-layer",
                "object-layer",
                "resource-layer",
                "water-tile"
              ],
              "mapColour": {
                "red": 82,
                "green": 98,
                "blue": 92,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "water-mud",
              "order": "c[water]-g[water-mud]",
              "layer": 7,
              "collisionMasks": [
                "item-layer",
                "object-layer",
                "resource-layer",
                "water-tile"
              ],
              "mapColour": {
                "red": 65,
                "green": 89,
                "blue": 90,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "nuclear-ground",
              "order": "d[destruction]-a[nuclear]-a[nuclear-ground]",
              "layer": 33,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 48,
                "green": 40,
                "blue": 35,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "water-wube",
              "order": "x[wube]-a[water-wube]",
              "layer": 2,
              "collisionMasks": [
                "doodad-layer",
                "item-layer",
                "player-layer",
                "resource-layer",
                "water-tile"
              ],
              "mapColour": {
                "red": 0,
                "green": 0,
                "blue": 0,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "tile-unknown",
              "order": "z-a",
              "layer": 0,
              "collisionMasks": {
              },
              "mapColour": {
                "red": 0,
                "green": 0,
                "blue": 0,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "out-of-map",
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
              "mapColour": {
                "red": 0,
                "green": 0,
                "blue": 0,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "lab-dark-1",
              "order": "z[other]-b[lab]-a[lab-dark-1]",
              "layer": 70,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 49,
                "green": 49,
                "blue": 49,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "lab-dark-2",
              "order": "z[other]-b[lab]-b[lab-dark-2]",
              "layer": 70,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 0,
                "green": 0,
                "blue": 0,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "lab-white",
              "order": "z[other]-b[lab]-c[lab-white]",
              "layer": 70,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 255,
                "green": 255,
                "blue": 255,
                "alpha": 255
              },
              "canBeMined": false
            },
            {
              "type": "dev.adamko.kafkatorio.schema.prototypes.FactorioPrototype2.MapTile",
              "name": "tutorial-grid",
              "order": "z[other]-c[tutorial]-a[tutorial-grid]",
              "layer": 55,
              "collisionMasks": [
                "ground-tile"
              ],
              "mapColour": {
                "red": 122,
                "green": 122,
                "blue": 122,
                "alpha": 255
              },
              "canBeMined": false
            }
          ]
        }
      }
    """.trimIndent()

    Then("expect can be parsed") {
      val packet: KafkatorioPacket2 = jsonMapperKafkatorio.decodeFromString(raw)
//      println(packet)

      packet.data.shouldBeInstanceOf<PrototypesUpdate>()
        .prototypes
        .forOne {
          it.shouldBeInstanceOf<FactorioPrototype2.MapTile>()
          it.name shouldBe PrototypeName("tutorial-grid")
          it.order shouldBe "z[other]-c[tutorial]-a[tutorial-grid]"
          it.layer shouldBe 55u
          it.collisionMasks shouldBe listOf("ground-tile")
          it.mapColour shouldBe Colour(122f, 122f, 122f, 255f)
          it.canBeMined shouldBe false
        }
    }
  }
})
