package dev.adamko.kafkatorio.schema.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@SerialName("kafkatorio.entity.FactorioEntityData")
sealed interface FactorioEntityData {

  val protoId: PrototypeId
  val status: Status?

  val position: MapEntityPosition

  /** Non-specific entity data */
  @Serializable
  @SerialName("kafkatorio.entity.FactorioEntityData.Standard")
  data class Standard(
    override val protoId: PrototypeId,
    override val status: Status? = null,

    override val position: MapEntityPosition,
    val graphicsVariation: UByte? = null,
    val health: Float? = null,
    val isActive: Boolean? = null,
    val isRotatable: Boolean? = null,
    val lastUser: UInt? = null,
    val localisedDescription: String? = null,
    val localisedName: String? = null,
  ) : FactorioEntityData


  @Serializable
  @SerialName("kafkatorio.entity.FactorioEntityData.Resource")
  data class Resource(
    override val protoId: PrototypeId,
    override val status: Status? = null,

    override val position: MapEntityPosition,
    val amount: UInt,
    /** Count of initial resource units contained. `null` if resource is not infinite. */
    val initialAmount: UInt? = null,
  ) : FactorioEntityData


  @Serializable
  @SerialName("kafkatorio.entity.FactorioEntityData.EntityStatus")
  enum class Status {
    CANT_DIVIDE_SEGMENTS,
    CHARGING,
    CLOSED_BY_CIRCUIT_NETWORK,
    DISABLED,
    DISABLED_BY_CONTROL_BEHAVIOR,
    DISABLED_BY_SCRIPT,
    DISCHARGING,
    FLUID_INGREDIENT_SHORTAGE,
    FULLY_CHARGED,
    FULL_OUTPUT,
    ITEM_INGREDIENT_SHORTAGE,
    LAUNCHING_ROCKET,
    LOW_INPUT_FLUID,
    LOW_POWER,
    LOW_TEMPERATURE,
    MARKED_FOR_DECONSTRUCTION,
    MISSING_REQUIRED_FLUID,
    MISSING_SCIENCE_PACKS,
    NETWORKS_CONNECTED,
    NETWORKS_DISCONNECTED,
    NORMAL,
    NOT_CONNECTED_TO_RAIL,
    NOT_PLUGGED_IN_ELECTRIC_NETWORK,
    NO_AMMO,
    NO_FUEL,
    NO_INGREDIENTS,
    NO_INPUT_FLUID,
    NO_MINABLE_RESOURCES,
    NO_MODULES_TO_TRANSMIT,
    NO_POWER,
    NO_RECIPE,
    NO_RESEARCH_IN_PROGRESS,
    OPENED_BY_CIRCUIT_NETWORK,
    OUT_OF_LOGISTIC_NETWORK,
    PREPARING_ROCKET_FOR_LAUNCH,
    RECHARGING_AFTER_POWER_OUTAGE,
    TURNED_OFF_DURING_DAYTIME,
    WAITING_FOR_SOURCE_ITEMS,
    WAITING_FOR_SPACE_IN_DESTINATION,
    WAITING_FOR_TARGET_TO_BE_BUILT,
    WAITING_FOR_TRAIN,
    WAITING_TO_LAUNCH_ROCKET,
    WORKING,
  }

}
/*
  enum entity_status {
    working,
    normal,
    no_power,
    low_power,
    no_fuel,
    disabled_by_control_behavior,
    opened_by_circuit_network,
    closed_by_circuit_network,
    disabled_by_script,
    marked_for_deconstruction,
    not_plugged_in_electric_network,
    networks_connected,
    networks_disconnected,
    charging,
    discharging,
    fully_charged,
    out_of_logistic_network,
    no_recipe,
    no_ingredients,
    no_input_fluid,
    no_research_in_progress,
    no_minable_resources,
    low_input_fluid,
    fluid_ingredient_shortage,
    full_output,
    item_ingredient_shortage,
    missing_required_fluid,
    missing_science_packs,
    waiting_for_source_items,
    waiting_for_space_in_destination,
    preparing_rocket_for_launch,
    waiting_to_launch_rocket,
    launching_rocket,
    no_modules_to_transmit,
    recharging_after_power_outage,
    waiting_for_target_to_be_built,
    waiting_for_train,
    no_ammo,
    low_temperature,
    disabled,
    turned_off_during_daytime,
    not_connected_to_rail,
    cant_divide_segments,
  }

 */

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
