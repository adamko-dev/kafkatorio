package dev.adamko.kafkatorio.schema.common

import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Both a [name] and [type] are required to unique identify a [prototype][FactorioPrototype].
 *
 * @property[name] The name of the prototype, which is unique per specific Prototype [type]
 */
@Serializable
@JvmInline
@SerialName("kafkatorio.id.PrototypeId")
value class PrototypeId private constructor(
  private val typeNameConcat: String,
) {

  val type: Type get() = Type(typeNameConcat.substringBefore(delim, ""))
  val name: Name get() = Name(typeNameConcat.substringAfter(delim, ""))


  operator fun component1(): Type = type
  operator fun component2(): Name = name


  constructor(
    type: String,
    name: String,
  ) : this("${type}${delim}${name}")


  /** Denotes the class of the Prototype */
  @Serializable
  @JvmInline
  @SerialName("kafkatorio.id.PrototypeIdType")
  value class Type(private val id: String) {
    override fun toString(): String = id
  }


  /** A specific implementation of a [type]. Unique per prototype 'super class' */
  @Serializable
  @JvmInline
  @SerialName("kafkatorio.id.PrototypeIdName")
  value class Name(private val name: String) {
    override fun toString(): String = name
  }


  companion object {
    private const val delim = '/'
  }
}


//@Suppress("unused")
//enum class PrototypeTypeId(
//  val id: String
//) {
//  AmbientSound("ambient-sound"),
//  Animation("animation"),
//  EditorController("editor-controller"),
//  Font("font"),
//  GodController("god-controller"),
//  MapGenPresets("map-gen-presets"),
//  MapSettings("map-settings"),
//  MouseCursor("mouse-cursor"),
//  Sound("sound"),
//  SpectatorController("spectator-controller"),
//  Sprite("sprite"),
//  TileEffect("tile-effect"),
//  TipsAndTricksItemCategory("tips-and-tricks-item-category"),
//  TriggerTargetType("trigger-target-type"),
//  WindSound("wind-sound"),
//  Achievement("achievement"),
//  BuildEntityAchievement("build-entity-achievement"),
//  CombatRobotCountAchievement("combat-robot-count"),
//  ConstructWithRobotsAchievement("construct-with-robots-achievement"),
//  DeconstructWithRobotsAchievement("deconstruct-with-robots-achievement"),
//  DeliverByRobotsAchievement("deliver-by-robots-achievement"),
//  DontBuildEntityAchievement("dont-build-entity-achievement"),
//  DontCraftManuallyAchievement("dont-craft-manually-achievement"),
//  DontUseEntityInEnergyProductionAchievement("dont-use-entity-in-energy-production-achievement"),
//  FinishTheGameAchievement("finish-the-game-achievement"),
//  GroupAttackAchievement("group-attack-achievement"),
//  KillAchievement("kill-achievement"),
//  PlayerDamagedAchievement("player-damaged-achievement"),
//  ProduceAchievement("produce-achievement"),
//  ProducePerHourAchievement("produce-per-hour-achievement"),
//  ResearchAchievement("research-achievement"),
//  TrainPathAchievement("train-path-achievement"),
//  AmmoCategory("ammo-category"),
//  AutoplaceControl("autoplace-control"),
//  CustomInput("custom-input"),
//  DamageType("damage-type"),
//  Decorative("optimized-decorative"),
//  Entity("<abstract>"),
//  Arrow("arrow"),
//  ArtilleryFlare("artillery-flare"),
//  ArtilleryProjectile("artillery-projectile"),
//  Beam("beam"),
//  CharacterCorpse("character-corpse"),
//  Cliff("cliff"),
//  Corpse("corpse"),
//  RailRemnants("rail-remnants"),
//  DeconstructibleTileProxy("deconstructible-tile-proxy"),
//  EntityGhost("entity-ghost"),
//  /** for migration, cannot be used */
//  EntityParticle("particle"),
//  /** for migration, cannot be used */
//  LeafParticle("leaf-particle"),
//  EntityWithHealth("<abstract>"),
//  EntityWithOwner("<abstract>"),
//  Accumulator("accumulator"),
//  ArtilleryTurret("artillery-turret"),
//  Beacon("beacon"),
//  Boiler("boiler"),
//  BurnerGenerator("burner-generator"),
//  Character("character"),
//  Combinator("<abstract>"),
//  ArithmeticCombinator("arithmetic-combinator"),
//  DeciderCombinator("decider-combinator"),
//  ConstantCombinator("constant-combinator"),
//  Container("container"),
//  LogisticContainer("logistic-container"),
//  InfinityContainer("infinity-container"),
//  CraftingMachine("<abstract>"),
//  AssemblingMachine("assembling-machine"),
//  RocketSilo("rocket-silo"),
//  Furnace("furnace"),
//  ElectricEnergyInterface("electric-energy-interface"),
//  ElectricPole("electric-pole"),
//  EnemySpawner("unit-spawner"),
//  FlyingRobot("<abstract>"),
//  CombatRobot("combat-robot"),
//  RobotWithLogisticInterface("<abstract>"),
//  ConstructionRobot("construction-robot"),
//  LogisticRobot("logistic-robot"),
//  Gate("gate"),
//  Generator("generator"),
//  HeatInterface("heat-interface"),
//  HeatPipe("heat-pipe"),
//  Inserter("inserter"),
//  Lab("lab"),
//  Lamp("lamp"),
//  LandMine("land-mine"),
//  LinkedContainer("linked-container"),
//  Market("market"),
//  MiningDrill("mining-drill"),
//  OffshorePump("offshore-pump"),
//  Pipe("pipe"),
//  InfinityPipe("infinity-pipe"),
//  PipeToGround("pipe-to-ground"),
//  PlayerPort("player-port"),
//  PowerSwitch("power-switch"),
//  ProgrammableSpeaker("programmable-speaker"),
//  Pump("pump"),
//  Radar("radar"),
//  Rail("<abstract>"),
//  CurvedRail("curved-rail"),
//  StraightRail("straight-rail"),
//  RailSignalBase("<abstract>"),
//  RailChainSignal("rail-chain-signal"),
//  RailSignal("rail-signal"),
//  Reactor("reactor"),
//  Roboport("roboport"),
//  SimpleEntityWithOwner("simple-entity-with-owner"),
//  SimpleEntityWithForce("simple-entity-with-force"),
//  SolarPanel("solar-panel"),
//  StorageTank("storage-tank"),
//  TrainStop("train-stop"),
//  TransportBeltConnectable("<abstract>"),
//  LinkedBelt("linked-belt"),
//  Loader1x1("loader-1x1"),
//  Loader1x2("loader"),
//  Splitter("splitter"),
//  TransportBelt("transport-belt"),
//  UndergroundBelt("underground-belt"),
//  Turret("turret"),
//  AmmoTurret("ammo-turret"),
//  ElectricTurret("electric-turret"),
//  FluidTurret("fluid-turret"),
//  Unit("unit"),
//  Vehicle("<abstract>"),
//  Car("car"),
//  RollingStock("<abstract>"),
//  ArtilleryWagon("artillery-wagon"),
//  CargoWagon("cargo-wagon"),
//  FluidWagon("fluid-wagon"),
//  Locomotive("locomotive"),
//  SpiderVehicle("spider-vehicle"),
//  Wall("wall"),
//  Fish("fish"),
//  SimpleEntity("simple-entity"),
//  SpiderLeg("spider-leg"),
//  Tree("tree"),
//  Explosion("explosion"),
//  FlameThrowerExplosion("flame-thrower-explosion"),
//  FireFlame("fire"),
//  FluidStream("stream"),
//  FlyingText("flying-text"),
//  HighlightBoxEntity("highlight-box"),
//  ItemEntity("item-entity"),
//  ItemRequestProxy("item-request-proxy"),
//  ParticleSource("particle-source"),
//  Projectile("projectile"),
//  ResourceEntity("resource"),
//  RocketSiloRocket("rocket-silo-rocket"),
//  RocketSiloRocketShadow("rocket-silo-rocket-shadow"),
//  Smoke("<abstract>"),
//  /** for migration, cannot be used */
//  SimpleSmoke("smoke"),
//  SmokeWithTrigger("smoke-with-trigger"),
//  SpeechBubble("speech-bubble"),
//  Sticker("sticker"),
//  TileGhost("tile-ghost"),
//  Equipment("<abstract>"),
//  ActiveDefenseEquipment("active-defense-equipment"),
//  BatteryEquipment("battery-equipment"),
//  BeltImmunityEquipment("belt-immunity-equipment"),
//  EnergyShieldEquipment("energy-shield-equipment"),
//  GeneratorEquipment("generator-equipment"),
//  MovementBonusEquipment("movement-bonus-equipment"),
//  NightVisionEquipment("night-vision-equipment"),
//  RoboportEquipment("roboport-equipment"),
//  SolarPanelEquipment("solar-panel-equipment"),
//  EquipmentCategory("equipment-category"),
//  EquipmentGrid("equipment-grid"),
//  Fluid("fluid"),
//  FuelCategory("fuel-category"),
//  GuiStyle("gui-style"),
//  Item("item"),
//  AmmoItem("ammo"),
//  Capsule("capsule"),
//  Gun("gun"),
//  ItemWithEntityData("item-with-entity-data"),
//  ItemWithLabel("item-with-label"),
//  ItemWithInventory("item-with-inventory"),
//  BlueprintBook("blueprint-book"),
//  ItemWithTags("item-with-tags"),
//  SelectionTool("selection-tool"),
//  BlueprintItem("blueprint"),
//  CopyPasteTool("copy-paste-tool"),
//  DeconstructionItem("deconstruction-item"),
//  UpgradeItem("upgrade-item"),
//  Module("module"),
//  RailPlanner("rail-planner"),
//  SpidertronRemote("spidertron-remote"),
//  Tool("tool"),
//  Armor("armor"),
//  /** for migration, cannot be used */
//  MiningTool("mining-tool"),
//  RepairTool("repair-tool"),
//  ItemGroup("item-group"),
//  ItemSubGroup("item-subgroup"),
//  ModuleCategory("module-category"),
//  NamedNoiseExpression("noise-expression"),
//  NoiseLayer("noise-layer"),
//  Particle("optimized-particle"),
//  Recipe("recipe"),
//  RecipeCategory("recipe-category"),
//  ResourceCategory("resource-category"),
//  Shortcut("shortcut"),
//  Technology("technology"),
//  Tile("tile"),
//  TipsAndTricksItem("tips-and-tricks-item"),
//  TrivialSmoke("trivial-smoke"),
//  Tutorial("tutorial"),
//  UtilityConstants("utility-constants"),
//  UtilitySounds("utility-sounds"),
//  UtilitySprites("utility-sprites"),
//  VirtualSignal("virtual-signal"),
//  ;
//
//  fun named(protoName: String): PrototypeId = PrototypeId(this, protoName)
//
//  companion object {
//    val entries: Set<PrototypeTypeId> = values().toSet()
//  }
//
//}

//@Suppress("unused")
//sealed class PrototypeType(
//  val id: String
//) {
//
//  object AmbientSound : PrototypeType("ambient-sound")
//  object Animation : PrototypeType("animation")
//  object EditorController : PrototypeType("editor-controller")
//  object Font : PrototypeType("font")
//  object GodController : PrototypeType("god-controller")
//  object MapGenPresets : PrototypeType("map-gen-presets")
//  object MapSettings : PrototypeType("map-settings")
//  object MouseCursor : PrototypeType("mouse-cursor")
//  object Sound : PrototypeType("sound")
//  object SpectatorController : PrototypeType("spectator-controller")
//  object Sprite : PrototypeType("sprite")
//  object TileEffect : PrototypeType("tile-effect")
//  object TipsAndTricksItemCategory : PrototypeType("tips-and-tricks-item-category")
//  object TriggerTargetType : PrototypeType("trigger-target-type")
//  object WindSound : PrototypeType("wind-sound")
//
//
//  object Achievement : PrototypeType("achievement")
//  object BuildEntityAchievement : PrototypeType("build-entity-achievement")
//  object CombatRobotCountAchievement : PrototypeType("combat-robot-count")
//  object ConstructWithRobotsAchievement : PrototypeType("construct-with-robots-achievement")
//  object DeconstructWithRobotsAchievement : PrototypeType("deconstruct-with-robots-achievement")
//  object DeliverByRobotsAchievement : PrototypeType("deliver-by-robots-achievement")
//  object DontBuildEntityAchievement : PrototypeType("dont-build-entity-achievement")
//  object DontCraftManuallyAchievement : PrototypeType("dont-craft-manually-achievement")
//  object DontUseEntityInEnergyProductionAchievement : PrototypeType("dont-use-entity-in-energy-production-achievement")
//
//
//  object FinishTheGameAchievement : PrototypeType("finish-the-game-achievement")
//  object GroupAttackAchievement : PrototypeType("group-attack-achievement")
//  object KillAchievement : PrototypeType("kill-achievement")
//  object PlayerDamagedAchievement : PrototypeType("player-damaged-achievement")
//  object ProduceAchievement : PrototypeType("produce-achievement")
//  object ProducePerHourAchievement : PrototypeType("produce-per-hour-achievement")
//  object ResearchAchievement : PrototypeType("research-achievement")
//  object TrainPathAchievement : PrototypeType("train-path-achievement")
//  object AmmoCategory : PrototypeType("ammo-category")
//  object AutoplaceControl : PrototypeType("autoplace-control")
//  object CustomInput : PrototypeType("custom-input")
//  object DamageType : PrototypeType("damage-type")
//  object Decorative : PrototypeType("optimized-decorative")
//
//
//  object Entity : PrototypeType("stract>")
//  object Arrow : PrototypeType("arrow")
//  object ArtilleryFlare : PrototypeType("artillery-flare")
//  object ArtilleryProjectile : PrototypeType("artillery-projectile")
//  object Beam : PrototypeType("beam")
//  object CharacterCorpse : PrototypeType("character-corpse")
//  object Cliff : PrototypeType("cliff")
//  object Corpse : PrototypeType("corpse")
//  object RailRemnants : PrototypeType("rail-remnants")
//  object DeconstructibleTileProxy : PrototypeType("deconstructible-tile-proxy")
//  object EntityGhost : PrototypeType("entity-ghost")
//  /** for migration, cannot be used */
//  object EntityParticle : PrototypeType("particle")
//  /** for migration, cannot be used */
//  object LeafParticle : PrototypeType("leaf-particle")
//
//
//  object EntityWithOwner : PrototypeType("stract>")
//  object Accumulator : PrototypeType("accumulator")
//  object ArtilleryTurret : PrototypeType("artillery-turret")
//  object Beacon : PrototypeType("beacon")
//  object Boiler : PrototypeType("boiler")
//  object BurnerGenerator : PrototypeType("burner-generator")
//  object Character : PrototypeType("character")
//
//
//  object Combinator : PrototypeType("stract>")
//  object ArithmeticCombinator : PrototypeType("arithmetic-combinator")
//  object DeciderCombinator : PrototypeType("decider-combinator")
//  object ConstantCombinator : PrototypeType("constant-combinator")
//  object Container : PrototypeType("container")
//  object LogisticContainer : PrototypeType("logistic-container")
//  object InfinityContainer : PrototypeType("infinity-container")
//
//
//  object CraftingMachine : PrototypeType("stract>")
//  object AssemblingMachine : PrototypeType("assembling-machine")
//  object RocketSilo : PrototypeType("rocket-silo")
//  object Furnace : PrototypeType("furnace")
//  object ElectricEnergyInterface : PrototypeType("electric-energy-interface")
//  object ElectricPole : PrototypeType("electric-pole")
//  object EnemySpawner : PrototypeType("unit-spawner")
//
//
//  object FlyingRobot : PrototypeType("stract>")
//  object CombatRobot : PrototypeType("combat-robot") , PrototypeAbstract.FlyingRobot
//
//
//  object RobotWithLogisticInterface : PrototypeType("stract>")
//  object ConstructionRobot : PrototypeType("construction-robot")
//  object LogisticRobot : PrototypeType("logistic-robot")
//  object Gate : PrototypeType("gate")
//  object Generator : PrototypeType("generator")
//  object HeatInterface : PrototypeType("heat-interface")
//  object HeatPipe : PrototypeType("heat-pipe")
//  object Inserter : PrototypeType("inserter")
//  object Lab : PrototypeType("lab")
//  object Lamp : PrototypeType("lamp")
//  object LandMine : PrototypeType("land-mine")
//  object LinkedContainer : PrototypeType("linked-container")
//  object Market : PrototypeType("market")
//  object MiningDrill : PrototypeType("mining-drill")
//  object OffshorePump : PrototypeType("offshore-pump")
//  object Pipe : PrototypeType("pipe")
//  object InfinityPipe : PrototypeType("infinity-pipe")
//  object PipeToGround : PrototypeType("pipe-to-ground")
//  object PlayerPort : PrototypeType("player-port")
//  object PowerSwitch : PrototypeType("power-switch")
//  object ProgrammableSpeaker : PrototypeType("programmable-speaker")
//  object Pump : PrototypeType("pump")
//  object Radar : PrototypeType("radar")
//
//  object CurvedRail : PrototypeType("curved-rail"), PrototypeAbstract.Rail
//  object StraightRail : PrototypeType("straight-rail"), PrototypeAbstract.Rail
//
//  object RailSignalBase : PrototypeType("stract>")
//  object RailChainSignal : PrototypeType("rail-chain-signal")
//  object RailSignal : PrototypeType("rail-signal")
//  object Reactor : PrototypeType("reactor")
//  object Roboport : PrototypeType("roboport")
//  object SimpleEntityWithOwner : PrototypeType("simple-entity-with-owner")
//  object SimpleEntityWithForce : PrototypeType("simple-entity-with-force")
//  object SolarPanel : PrototypeType("solar-panel")
//  object StorageTank : PrototypeType("storage-tank")
//  object TrainStop : PrototypeType("train-stop")
//
//
//  object TransportBeltConnectable : PrototypeType("stract>")
//  object LinkedBelt : PrototypeType("linked-belt")
//  object Loader1x1 : PrototypeType("loader-1x1")
//  object Loader1x2 : PrototypeType("loader")
//  object Splitter : PrototypeType("splitter")
//  object TransportBelt : PrototypeType("transport-belt")
//  object UndergroundBelt : PrototypeType("underground-belt")
//  object Turret : PrototypeType("turret")
//  object AmmoTurret : PrototypeType("ammo-turret")
//  object ElectricTurret : PrototypeType("electric-turret")
//  object FluidTurret : PrototypeType("fluid-turret")
//  object Unit : PrototypeType("unit")
//
//
//  object Car : PrototypeType("car>"), PrototypeAbstract.Vehicle
//  object SpiderVehicle : PrototypeType("spider-vehicle"), PrototypeAbstract.Vehicle
//
//
//  object ArtilleryWagon : PrototypeType("artillery-wagon"), PrototypeAbstract.RollingStock
//  object CargoWagon : PrototypeType("cargo-wagon"), PrototypeAbstract.RollingStock
//  object FluidWagon : PrototypeType("fluid-wagon"), PrototypeAbstract.RollingStock
//  object Locomotive : PrototypeType("locomotive"), PrototypeAbstract.RollingStock
//
//  object Wall : PrototypeType("wall")
//
//
//  object Fish : PrototypeType("fish"), PrototypeAbstract.EntityWithHealth
//  object SimpleEntity : PrototypeType("simple-entity"), PrototypeAbstract.EntityWithHealth
//  object SpiderLeg : PrototypeType("spider-leg"), PrototypeAbstract.EntityWithHealth
//  object Tree : PrototypeType("tree"), PrototypeAbstract.EntityWithHealth
//
//
//  object Explosion : PrototypeType("explosion"),
//  object FlameThrowerExplosion : PrototypeType("flame-thrower-explosion"),
//  object FireFlame : PrototypeType("fire"),
//  object FluidStream : PrototypeType("stream"),
//  object FlyingText : PrototypeType("flying-text"),
//  object HighlightBoxEntity : PrototypeType("highlight-box"),
//  object ItemEntity : PrototypeType("item-entity"),
//  object ItemRequestProxy : PrototypeType("item-request-proxy"),
//  object ParticleSource : PrototypeType("particle-source"),
//  object Projectile : PrototypeType("projectile"),
//  object ResourceEntity : PrototypeType("resource"),
//  object RocketSiloRocket : PrototypeType("rocket-silo-rocket"),
//  object RocketSiloRocketShadow : PrototypeType("rocket-silo-rocket-shadow"),
//
//  /** for migration, cannot be used */
//  object SimpleSmoke : PrototypeType("smoke"), PrototypeAbstract.Smoke
//  object SmokeWithTrigger : PrototypeType("smoke-with-trigger"), PrototypeAbstract.Smoke
//  object SpeechBubble : PrototypeType("speech-bubble"), PrototypeAbstract.Smoke
//  object Sticker : PrototypeType("sticker"), PrototypeAbstract.Smoke
//  object TileGhost : PrototypeType("tile-ghost"), PrototypeAbstract.Smoke
//
//  object Equipment : PrototypeType("abstract>")
//  object ActiveDefenseEquipment : PrototypeType("active-defense-equipment")
//  object BatteryEquipment : PrototypeType("battery-equipment")
//  object BeltImmunityEquipment : PrototypeType("belt-immunity-equipment")
//  object EnergyShieldEquipment : PrototypeType("energy-shield-equipment")
//  object GeneratorEquipment : PrototypeType("generator-equipment")
//  object MovementBonusEquipment : PrototypeType("movement-bonus-equipment")
//  object NightVisionEquipment : PrototypeType("night-vision-equipment")
//  object RoboportEquipment : PrototypeType("roboport-equipment")
//  object SolarPanelEquipment : PrototypeType("solar-panel-equipment")
//  object EquipmentCategory : PrototypeType("equipment-category")
//  object EquipmentGrid : PrototypeType("equipment-grid")
//  object Fluid : PrototypeType("fluid")
//  object FuelCategory : PrototypeType("fuel-category")
//  object GuiStyle : PrototypeType("gui-style")
//  object Item : PrototypeType("item")
//  object AmmoItem : PrototypeType("ammo")
//  object Capsule : PrototypeType("capsule")
//  object Gun : PrototypeType("gun")
//  object ItemWithEntityData : PrototypeType("item-with-entity-data")
//  object ItemWithLabel : PrototypeType("item-with-label")
//  object ItemWithInventory : PrototypeType("item-with-inventory")
//  object BlueprintBook : PrototypeType("blueprint-book")
//  object ItemWithTags : PrototypeType("item-with-tags")
//  object SelectionTool : PrototypeType("selection-tool")
//  object BlueprintItem : PrototypeType("blueprint")
//  object CopyPasteTool : PrototypeType("copy-paste-tool")
//  object DeconstructionItem : PrototypeType("deconstruction-item")
//  object UpgradeItem : PrototypeType("upgrade-item")
//  object Module : PrototypeType("module")
//  object RailPlanner : PrototypeType("rail-planner")
//  object SpidertronRemote : PrototypeType("spidertron-remote")
//  object Tool : PrototypeType("tool")
//  object Armor : PrototypeType("armor")
//  /** for migration, cannot be used */
//  object MiningTool : PrototypeType("mining-tool")
//  object RepairTool : PrototypeType("repair-tool")
//  object ItemGroup : PrototypeType("item-group")
//  object ItemSubGroup : PrototypeType("item-subgroup")
//  object ModuleCategory : PrototypeType("module-category")
//  object NamedNoiseExpression : PrototypeType("noise-expression")
//  object NoiseLayer : PrototypeType("noise-layer")
//  object Particle : PrototypeType("optimized-particle")
//  object Recipe : PrototypeType("recipe")
//  object RecipeCategory : PrototypeType("recipe-category")
//  object ResourceCategory : PrototypeType("resource-category")
//  object Shortcut : PrototypeType("shortcut")
//  object Technology : PrototypeType("technology")
//  object Tile : PrototypeType("tile")
//  object TipsAndTricksItem : PrototypeType("tips-and-tricks-item")
//  object TrivialSmoke : PrototypeType("trivial-smoke")
//  object Tutorial : PrototypeType("tutorial")
//  object UtilityConstants : PrototypeType("utility-constants")
//  object UtilitySounds : PrototypeType("utility-sounds")
//  object UtilitySprites : PrototypeType("utility-sprites")
//  object VirtualSignal : PrototypeType("virtual-signal")
//
//}
//
//@Suppress("unused")
//sealed interface PrototypeAbstract {
//  //@formatter:off
//  sealed interface PrototypeBase              : PrototypeAbstract
//  sealed interface Entity                     : PrototypeAbstract
//  sealed interface EntityWithHealth           : Entity
//
//  sealed interface EntityWithOwner            : EntityWithHealth
//
//  sealed interface Combinator                 : EntityWithOwner
//  sealed interface CraftingMachine            : EntityWithOwner
//  sealed interface FlyingRobot                : EntityWithOwner
//
//  sealed interface RobotWithLogisticInterface : FlyingRobot
//
//  sealed interface Rail                       : EntityWithOwner
//
//  sealed interface RailSignalBase             : EntityWithOwner
//  sealed interface TransportBeltConnectable   : EntityWithOwner
//  sealed interface Vehicle                    : EntityWithOwner
//  sealed interface RollingStock               : Vehicle
//  sealed interface Smoke                      : Entity
//  sealed interface Equipment                  : PrototypeAbstract
//  //@formatter:on
//}


/*


* [[Prototype/AmbientSound]] '''ambient-sound'''
* [[Prototype/Animation]] '''animation'''
* [[Prototype/EditorController]] '''editor-controller'''
* [[Prototype/Font]] '''font'''
* [[Prototype/GodController]] '''god-controller'''
* [[Prototype/MapGenPresets]] '''map-gen-presets'''
* [[Prototype/MapSettings]] '''map-settings'''
* [[Prototype/MouseCursor]] '''mouse-cursor'''
* [[Prototype/Sound]] '''sound'''
* [[Prototype/SpectatorController]] '''spectator-controller'''
* [[Prototype/Sprite]] '''sprite'''
* [[Prototype/TileEffect]] '''tile-effect'''
* [[Prototype/TipsAndTricksItemCategory]] '''tips-and-tricks-item-category'''
* [[Prototype/TriggerTargetType]] '''trigger-target-type'''
* [[Prototype/WindSound]] '''wind-sound'''
* [[PrototypeBase]] <abstract>
</div><div class="prototype-tree">
* [[Prototype/Achievement]] '''achievement'''
** [[Prototype/BuildEntityAchievement]] '''build-entity-achievement'''
** [[Prototype/CombatRobotCountAchievement]] '''combat-robot-count'''
** [[Prototype/ConstructWithRobotsAchievement]] '''construct-with-robots-achievement'''
** [[Prototype/DeconstructWithRobotsAchievement]] '''deconstruct-with-robots-achievement'''
** [[Prototype/DeliverByRobotsAchievement]] '''deliver-by-robots-achievement'''
** [[Prototype/DontBuildEntityAchievement]] '''dont-build-entity-achievement'''
** [[Prototype/DontCraftManuallyAchievement]] '''dont-craft-manually-achievement'''
** [[Prototype/DontUseEntityInEnergyProductionAchievement]] '''dont-use-entity-in-energy-production-achievement'''
** [[Prototype/FinishTheGameAchievement]] '''finish-the-game-achievement'''
** [[Prototype/GroupAttackAchievement]] '''group-attack-achievement'''
** [[Prototype/KillAchievement]] '''kill-achievement'''
** [[Prototype/PlayerDamagedAchievement]] '''player-damaged-achievement'''
** [[Prototype/ProduceAchievement]] '''produce-achievement'''
** [[Prototype/ProducePerHourAchievement]] '''produce-per-hour-achievement'''
** [[Prototype/ResearchAchievement]] '''research-achievement'''
** [[Prototype/TrainPathAchievement]] '''train-path-achievement'''
* [[Prototype/AmmoCategory]] '''ammo-category'''
* [[Prototype/AutoplaceControl]] '''autoplace-control'''
* [[Prototype/CustomInput]] '''custom-input'''
* [[Prototype/DamageType]] '''damage-type'''
* [[Prototype/Decorative]] '''optimized-decorative'''
* [[Prototype/Entity]] <abstract>
** [[Prototype/Arrow]] '''arrow'''
** [[Prototype/ArtilleryFlare]] '''artillery-flare'''
** [[Prototype/ArtilleryProjectile]] '''artillery-projectile'''
** [[Prototype/Beam]] '''beam'''
** [[Prototype/CharacterCorpse]] '''character-corpse'''
** [[Prototype/Cliff]] '''cliff'''
** [[Prototype/Corpse]] '''corpse'''
*** [[Prototype/RailRemnants]] '''rail-remnants'''
** [[Prototype/DeconstructibleTileProxy]] '''deconstructible-tile-proxy'''
** [[Prototype/EntityGhost]] '''entity-ghost'''
** [[Prototype/EntityParticle]] '''particle''' (for migration, cannot be used)
*** [[Prototype/LeafParticle]] '''leaf-particle''' (for migration, cannot be used)
** [[Prototype/EntityWithHealth]] <abstract>
*** [[Prototype/EntityWithOwner]] <abstract>
**** [[Prototype/Accumulator]] '''accumulator'''
**** [[Prototype/ArtilleryTurret]] '''artillery-turret'''
**** [[Prototype/Beacon]] '''beacon'''
**** [[Prototype/Boiler]] '''boiler'''
**** [[Prototype/BurnerGenerator]] '''burner-generator'''
**** [[Prototype/Character]] '''character'''
**** [[Prototype/Combinator]] <abstract>
***** [[Prototype/ArithmeticCombinator]] '''arithmetic-combinator'''
***** [[Prototype/DeciderCombinator]] '''decider-combinator'''
**** [[Prototype/ConstantCombinator]] '''constant-combinator'''
**** [[Prototype/Container]] '''container'''
***** [[Prototype/LogisticContainer]] '''logistic-container'''
****** [[Prototype/InfinityContainer]] '''infinity-container'''
**** [[Prototype/CraftingMachine]] <abstract>
***** [[Prototype/AssemblingMachine]] '''assembling-machine'''
****** [[Prototype/RocketSilo]] '''rocket-silo'''
***** [[Prototype/Furnace]] '''furnace'''
**** [[Prototype/ElectricEnergyInterface]] '''electric-energy-interface'''
**** [[Prototype/ElectricPole]] '''electric-pole'''
**** [[Prototype/EnemySpawner]] '''unit-spawner'''
**** [[Prototype/FlyingRobot]] <abstract>
***** [[Prototype/CombatRobot]] '''combat-robot'''
***** [[Prototype/RobotWithLogisticInterface]] <abstract>
****** [[Prototype/ConstructionRobot]] '''construction-robot'''
****** [[Prototype/LogisticRobot]] '''logistic-robot'''
**** [[Prototype/Gate]] '''gate'''
**** [[Prototype/Generator]] '''generator'''
**** [[Prototype/HeatInterface]] '''heat-interface'''
**** [[Prototype/HeatPipe]] '''heat-pipe'''
**** [[Prototype/Inserter]] '''inserter'''
**** [[Prototype/Lab]] '''lab'''
**** [[Prototype/Lamp]] '''lamp'''
**** [[Prototype/LandMine]] '''land-mine'''
**** [[Prototype/LinkedContainer]] '''linked-container'''
**** [[Prototype/Market]] '''market'''
**** [[Prototype/MiningDrill]] '''mining-drill'''
**** [[Prototype/OffshorePump]] '''offshore-pump'''
**** [[Prototype/Pipe]] '''pipe'''
***** [[Prototype/InfinityPipe]] '''infinity-pipe'''
**** [[Prototype/PipeToGround]] '''pipe-to-ground'''
**** [[Prototype/PlayerPort]] '''player-port'''
**** [[Prototype/PowerSwitch]] '''power-switch'''
**** [[Prototype/ProgrammableSpeaker]] '''programmable-speaker'''
**** [[Prototype/Pump]] '''pump'''
**** [[Prototype/Radar]] '''radar'''
**** [[Prototype/Rail]] <abstract>
***** [[Prototype/CurvedRail]] '''curved-rail'''
***** [[Prototype/StraightRail]] '''straight-rail'''
**** [[Prototype/RailSignalBase]] <abstract>
***** [[Prototype/RailChainSignal]] '''rail-chain-signal'''
***** [[Prototype/RailSignal]] '''rail-signal'''
**** [[Prototype/Reactor]] '''reactor'''
**** [[Prototype/Roboport]] '''roboport'''
**** [[Prototype/SimpleEntityWithOwner]] '''simple-entity-with-owner'''
***** [[Prototype/SimpleEntityWithForce]] '''simple-entity-with-force'''
**** [[Prototype/SolarPanel]] '''solar-panel'''
**** [[Prototype/StorageTank]] '''storage-tank'''
**** [[Prototype/TrainStop]] '''train-stop'''
**** [[Prototype/TransportBeltConnectable]] <abstract>
***** [[Prototype/LinkedBelt]] '''linked-belt'''
***** [[Prototype/Loader1x1]] '''loader-1x1'''
***** [[Prototype/Loader1x2]] '''loader'''
***** [[Prototype/Splitter]] '''splitter'''
***** [[Prototype/TransportBelt]] '''transport-belt'''
***** [[Prototype/UndergroundBelt]] '''underground-belt'''
**** [[Prototype/Turret]] '''turret'''
***** [[Prototype/AmmoTurret]] '''ammo-turret'''
***** [[Prototype/ElectricTurret]] '''electric-turret'''
***** [[Prototype/FluidTurret]] '''fluid-turret'''
**** [[Prototype/Unit]] '''unit'''
**** [[Prototype/Vehicle]] <abstract>
***** [[Prototype/Car]] '''car'''
***** [[Prototype/RollingStock]] <abstract>
****** [[Prototype/ArtilleryWagon]] '''artillery-wagon'''
****** [[Prototype/CargoWagon]] '''cargo-wagon'''
****** [[Prototype/FluidWagon]] '''fluid-wagon'''
****** [[Prototype/Locomotive]] '''locomotive'''
***** [[Prototype/SpiderVehicle]] '''spider-vehicle'''
**** [[Prototype/Wall]] '''wall'''
*** [[Prototype/Fish]] '''fish'''
*** [[Prototype/SimpleEntity]] '''simple-entity'''
*** [[Prototype/SpiderLeg]] '''spider-leg'''
*** [[Prototype/Tree]] '''tree'''
** [[Prototype/Explosion]] '''explosion'''
*** [[Prototype/FlameThrowerExplosion]] '''flame-thrower-explosion'''
** [[Prototype/FireFlame]] '''fire'''
** [[Prototype/FluidStream]] '''stream'''
** [[Prototype/FlyingText]] '''flying-text'''
** [[Prototype/HighlightBoxEntity]] '''highlight-box'''
** [[Prototype/ItemEntity]] '''item-entity'''
** [[Prototype/ItemRequestProxy]] '''item-request-proxy'''
** [[Prototype/ParticleSource]] '''particle-source'''
** [[Prototype/Projectile]] '''projectile'''
** [[Prototype/ResourceEntity]] '''resource'''
** [[Prototype/RocketSiloRocket]] '''rocket-silo-rocket'''
** [[Prototype/RocketSiloRocketShadow]] '''rocket-silo-rocket-shadow'''
** [[Prototype/Smoke]] <abstract>
*** [[Prototype/SimpleSmoke]] '''smoke''' (for migration, cannot be used)
*** [[Prototype/SmokeWithTrigger]] '''smoke-with-trigger'''
** [[Prototype/SpeechBubble]] '''speech-bubble'''
** [[Prototype/Sticker]] '''sticker'''
** [[Prototype/TileGhost]] '''tile-ghost'''
* [[Prototype/Equipment]] <abstract>
** [[Prototype/ActiveDefenseEquipment]] '''active-defense-equipment'''
** [[Prototype/BatteryEquipment]] '''battery-equipment'''
** [[Prototype/BeltImmunityEquipment]] '''belt-immunity-equipment'''
** [[Prototype/EnergyShieldEquipment]] '''energy-shield-equipment'''
** [[Prototype/GeneratorEquipment]] '''generator-equipment'''
** [[Prototype/MovementBonusEquipment]] '''movement-bonus-equipment'''
** [[Prototype/NightVisionEquipment]] '''night-vision-equipment'''
** [[Prototype/RoboportEquipment]] '''roboport-equipment'''
** [[Prototype/SolarPanelEquipment]] '''solar-panel-equipment'''
* [[Prototype/EquipmentCategory]] '''equipment-category'''
* [[Prototype/EquipmentGrid]] '''equipment-grid'''
* [[Prototype/Fluid]] '''fluid'''
* [[Prototype/FuelCategory]] '''fuel-category'''
* [[Prototype/GuiStyle]] '''gui-style'''
* [[Prototype/Item]] '''item'''
** [[Prototype/AmmoItem]] '''ammo'''
** [[Prototype/Capsule]] '''capsule'''
** [[Prototype/Gun]] '''gun'''
** [[Prototype/ItemWithEntityData]] '''item-with-entity-data'''
** [[Prototype/ItemWithLabel]] '''item-with-label'''
*** [[Prototype/ItemWithInventory]] '''item-with-inventory'''
**** [[Prototype/BlueprintBook]] '''blueprint-book'''
*** [[Prototype/ItemWithTags]] '''item-with-tags'''
*** [[Prototype/SelectionTool]] '''selection-tool'''
**** [[Prototype/BlueprintItem]] '''blueprint'''
**** [[Prototype/CopyPasteTool]] '''copy-paste-tool'''
**** [[Prototype/DeconstructionItem]] '''deconstruction-item'''
**** [[Prototype/UpgradeItem]] '''upgrade-item'''
** [[Prototype/Module]] '''module'''
** [[Prototype/RailPlanner]] '''rail-planner'''
** [[Prototype/SpidertronRemote]] '''spidertron-remote'''
** [[Prototype/Tool]] '''tool'''
*** [[Prototype/Armor]] '''armor'''
*** [[Prototype/MiningTool]] '''mining-tool''' (for migration, cannot be used)
*** [[Prototype/RepairTool]] '''repair-tool'''
* [[Prototype/ItemGroup]] '''item-group'''
* [[Prototype/ItemSubGroup]] '''item-subgroup'''
* [[Prototype/ModuleCategory]] '''module-category'''
* [[Prototype/NamedNoiseExpression]] '''noise-expression'''
* [[Prototype/NoiseLayer]] '''noise-layer'''
* [[Prototype/Particle]] '''optimized-particle'''
* [[Prototype/Recipe]] '''recipe'''
* [[Prototype/RecipeCategory]] '''recipe-category'''
* [[Prototype/ResourceCategory]] '''resource-category'''
* [[Prototype/Shortcut]] '''shortcut'''
* [[Prototype/Technology]] '''technology'''
* [[Prototype/Tile]] '''tile'''
* [[Prototype/TipsAndTricksItem]] '''tips-and-tricks-item'''
* [[Prototype/TrivialSmoke]] '''trivial-smoke'''
* [[Prototype/Tutorial]] '''tutorial'''
* [[Prototype/UtilityConstants]] '''utility-constants'''
* [[Prototype/UtilitySounds]] '''utility-sounds'''
* [[Prototype/UtilitySprites]] '''utility-sprites'''
* [[Prototype/VirtualSignal]] '''virtual-signal'''

 */
