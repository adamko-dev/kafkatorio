package dev.adamko.kafkatorio.schema.common

import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


// TODO change all booleans to be optional, and default to 'false' (also in events-mod)


@Serializable
@SerialName("kafkatorio.entity.FactorioEntityData")
sealed interface FactorioEntityData {

  val protoId: PrototypeId

  val position: MapEntityPosition

  /** Non-specific entity data */
  @Serializable
  @SerialName("kafkatorio.entity.FactorioEntityDataStandard")
  data class Standard(
    override val protoId: PrototypeId,
    override val position: MapEntityPosition,

    val graphicsVariation: UByte? = null,
    val health: Float? = null,
    val isActive: Boolean = false,
    val isRotatable: Boolean = false,
    val lastUser: UInt? = null,
    val status: Status? = null,
    val unitNumber: UnitNumber? = null,
  ) : FactorioEntityData


  @Serializable
  @SerialName("kafkatorio.entity.FactorioEntityDataResource")
  data class Resource(
    override val protoId: PrototypeId,

    override val position: MapEntityPosition,
    val amount: UInt,
    /** Count of initial resource units contained. `null` if resource is not infinite. */
    val initialAmount: UInt? = null,
  ) : FactorioEntityData


  /**
   * [`https://lua-api.factorio.com/latest/defines.html#defines.entity_status`](https://lua-api.factorio.com/latest/defines.html#defines.entity_status)
   */
  @Suppress("unused") // not directly used at the moment - maybe later
  @Serializable
  @SerialName("kafkatorio.entity.FactorioEntityData.FactorioEntityStatus")
  enum class Status {
    WORKING,
    NORMAL,
    NO_POWER,
    LOW_POWER,
    NO_FUEL,
    DISABLED_BY_CONTROL_BEHAVIOR,
    OPENED_BY_CIRCUIT_NETWORK,
    CLOSED_BY_CIRCUIT_NETWORK,
    DISABLED_BY_SCRIPT,
    MARKED_FOR_DECONSTRUCTION,

    /** Used by generators and solar panels. */
    NOT_PLUGGED_IN_ELECTRIC_NETWORK,

    /** Used by power switches. */
    NETWORKS_CONNECTED,

    /** Used by power switches. */
    NETWORKS_DISCONNECTED,

    /** Used by accumulators. */
    CHARGING,
    /** Used by accumulators. */
    DISCHARGING,
    /** Used by accumulators. */
    FULLY_CHARGED,

    /** Used by logistic containers. */
    OUT_OF_LOGISTIC_NETWORK,

    /** Used by assembling machines. */
    NO_RECIPE,

    /** Used by furnaces. */
    NO_INGREDIENTS,

    /** USED BY BOILERS, fluid turrets and fluid energy sources: Boiler has no fluid to work with. */
    NO_INPUT_FLUID,

    /** Used by labs. */
    NO_RESEARCH_IN_PROGRESS,

    /** Used by mining drills. */
    NO_MINABLE_RESOURCES,

    /** Used by boilers and fluid turrets: Boiler still has some fluid but is about to run out. */
    LOW_INPUT_FLUID,

    /** Used by crafting machines. */
    FLUID_INGREDIENT_SHORTAGE,

    /**
     * Used by crafting machines, boilers, burner energy sources and reactors: Reactor/burner has
     * full burnt result inventory, boiler has full output fluid box.
     */
    FULL_OUTPUT,

    /** Used by crafting machines. */
    ITEM_INGREDIENT_SHORTAGE,

    /** Used by mining drills when the mining fluid is missing. */
    MISSING_REQUIRED_FLUID,

    /** Used by labs. */
    MISSING_SCIENCE_PACKS,

    /** Used by inserters. */
    WAITING_FOR_SOURCE_ITEMS,

    /** Used by inserters and mining drills. */
    WAITING_FOR_SPACE_IN_DESTINATION,

    /** Used by the rocket silo. */
    PREPARING_ROCKET_FOR_LAUNCH,
    /** Used by the rocket silo. */
    WAITING_TO_LAUNCH_ROCKET,
    /** Used by the rocket silo. */
    LAUNCHING_ROCKET,

    /** Used by beacons. */
    NO_MODULES_TO_TRANSMIT,

    /** Used by roboports. */
    RECHARGING_AFTER_POWER_OUTAGE,

    /** Used by inserters targeting entity ghosts. */
    WAITING_FOR_TARGET_TO_BE_BUILT,
    /** Used by inserters targeting rails. */
    WAITING_FOR_TRAIN,

    /** Used by ammo turrets. */
    NO_AMMO,

    /** Used by heat energy sources. */
    LOW_TEMPERATURE,

    /** Used by constant combinators: Combinator is turned off via switch in GUI. */
    DISABLED,

    /** Used by lamps. */
    TURNED_OFF_DURING_DAYTIME,

    /** Used by rail signals. */
    NOT_CONNECTED_TO_RAIL,
    /** Used by rail signals. */
    CANT_DIVIDE_SEGMENTS,
  }

}


sealed interface FactorioEntityUpdateDictionary<T : Any>
  : Iterable<Pair<MapEntityPosition, T>> {

  val entitiesXY: Map<String, Map<String, T>>

  operator fun get(position: MapEntityPosition): T? =
    entitiesXY[position.x.toString()]?.get(position.y.toString())

  override fun iterator(): Iterator<Pair<MapEntityPosition, T>> =
    iterator {
      val invalid = mutableListOf<String>()
      entitiesXY.forEach { (xString, column) ->
        val x = xString.toDoubleOrNull()
        column.forEach { (yString, entity) ->
          val y = yString.toDoubleOrNull()
          if (x == null || y == null) {
            invalid += "$xString/$yString"
          } else {
            yield(MapEntityPosition(x, y) to entity)
          }
        }
      }

      if (invalid.isNotEmpty()) {
        println("WARNING FactorioEntityUpdateDictionary contained invalid x/y coords: ${invalid.joinToString()}")
      }
    }

  @Serializable
  @SerialName("kafkatorio.entity.FactorioEntityUpdateResourceAmountDictionary")
  @JvmInline
  value class ResourceAmounts(
    override val entitiesXY: Map<String, Map<String, UInt>> = emptyMap()
  ) : FactorioEntityUpdateDictionary<UInt>

  @Serializable
  @SerialName("kafkatorio.entity.FactorioEntityUpdateEntityDictionary")
  @JvmInline
  value class Entity(
    override val entitiesXY: Map<String, Map<String, FactorioEntityUpdateElement.Standard>>
  ) : FactorioEntityUpdateDictionary<FactorioEntityUpdateElement.Standard>

}


sealed interface FactorioEntityUpdateElement {

  @Serializable
  @SerialName("kafkatorio.entity.FactorioEntityUpdateElementStandard")
  data class Standard(
    val status: FactorioEntityData.Status? = null,

    val unitNumber: UnitNumber? = null,

    val graphicsVariation: UByte? = null,
    val health: Float? = null,
    val isActive: Boolean = false,
    val isRotatable: Boolean = false,
    val lastUser: UInt? = null,
  ) : FactorioEntityUpdateElement

}


/*
AssemblingMachine
Beam
Car
Character
CharacterCorpse
CraftingMachine
ElectricEnergyInterface
ElectricPole
EntityGhost
EntityWithHealth
EntityWithOwner
FlyingText
Furnace
Gate
Generator
Ghost
HeatInterface
HighlightBox
InfinityContainer
InfinityPipe
Inserter
ItemEntity
LandMine
LinkedBelt
Loader
Market
MiningDrill
ProgrammableSpeaker
Pump
Radar
Rail
RailChainSignal
RailSignal
Reactor
ResourceEntity
RocketSilo
SmokeWithTrigger
SpiderVehicle
Splitter
TrainStop
TransportBeltConnectable
TransportBeltToGround
Turret
Unit
Vehicle
 */
