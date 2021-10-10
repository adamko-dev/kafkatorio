@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

import kotlin.js.*
import org.khronos.webgl.*
import org.w3c.dom.*
import org.w3c.dom.events.*
import org.w3c.dom.parsing.*
import org.w3c.dom.svg.*
import org.w3c.dom.url.*
import org.w3c.fetch.*
import org.w3c.files.*
import org.w3c.notifications.*
import org.w3c.performance.*
import org.w3c.workers.*
import org.w3c.xhr.*
import defines.relative_gui_type
import defines.relative_gui_position
import tsstdlib.Record
import tsstdlib.Position
import defines.difficulty_settings.recipe_difficulty
import defines.difficulty_settings.technology_difficulty
import defines.circuit_connector_id
import defines.direction
import defines.wire_type
import defines.command
import defines.distraction
import defines.compound_command
import defines.inventory
import defines.riding.acceleration
import defines.riding.direction as _defines_riding_direction
import defines.control_behavior.mining_drill.resource_read_mode
import defines.control_behavior.inserter.hand_read_mode
import defines.control_behavior.transport_belt.content_read_mode

external interface DisplayResolution {
    var width: uint
    var height: uint
}

external interface PersonalLogisticParameters {
    var name: String?
        get() = definedExternally
        set(value) = definedExternally
    var min: uint?
        get() = definedExternally
        set(value) = definedExternally
    var max: uint?
        get() = definedExternally
        set(value) = definedExternally
}

typealias RealOrientation = float

external interface PositionTable {
    var x: int
    var y: int
}

external interface ChunkPositionTable {
    var x: int
    var y: int
}

external interface TilePositionTable {
    var x: int
    var y: int
}

external interface GuiLocationTable {
    var x: int
    var y: int
}

external interface ChunkPositionAndArea {
    var x: int
    var y: int
    var area: dynamic /* BoundingBoxTable | JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */
        get() = definedExternally
        set(value) = definedExternally
}

external interface EquipmentPoint {
    var x: uint
    var y: uint
}

external interface GuiAnchor {
    var gui: relative_gui_type
    var position: relative_gui_position
    var type: String?
        get() = definedExternally
        set(value) = definedExternally
    var name: String?
        get() = definedExternally
        set(value) = definedExternally
    var names: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TabAndContent {
    var tab: dynamic /* ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers */
        get() = definedExternally
        set(value) = definedExternally
    var content: dynamic /* ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers */
        get() = definedExternally
        set(value) = definedExternally
}

external interface OldTileAndPosition {
    var old_tile: LuaTilePrototype
    var position: dynamic /* TilePositionTable | JsTuple<x, int, y, int> */
        get() = definedExternally
        set(value) = definedExternally
}

typealias Tags = Record<String, dynamic /* String? | Number? | Boolean? | table? */>

external interface SmokeSource {
    var name: String
    var frequency: double
    var offset: double
    var position: PositionTable?
        get() = definedExternally
        set(value) = definedExternally
    var north_position: PositionTable?
        get() = definedExternally
        set(value) = definedExternally
    var east_position: PositionTable?
        get() = definedExternally
        set(value) = definedExternally
    var south_position: PositionTable?
        get() = definedExternally
        set(value) = definedExternally
    var west_position: PositionTable?
        get() = definedExternally
        set(value) = definedExternally
    var deviation: Position?
        get() = definedExternally
        set(value) = definedExternally
    var starting_frame_speed: uint16
    var starting_frame_speed_deviation: double
    var starting_frame: uint16
    var starting_frame_deviation: double
    var slow_down_factor: uint8
    var height: float
    var height_deviation: float
    var starting_vertical_speed: float
    var starting_vertical_speed_deviation: float
    var vertical_speed_slowdown: float
}

external interface BoundingBoxTable {
    var left_top: Position
    var right_bottom: Position
    var orientation: RealOrientation?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ScriptArea {
    var area: dynamic /* BoundingBoxTable | JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */
        get() = definedExternally
        set(value) = definedExternally
    var name: String
    var color: dynamic /* ColorTable | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var id: uint
}

external interface ScriptPosition {
    var position: Position
    var name: String
    var color: dynamic /* ColorTable | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var id: uint
}

external interface ColorTable {
    var r: float?
        get() = definedExternally
        set(value) = definedExternally
    var g: float?
        get() = definedExternally
        set(value) = definedExternally
    var b: float?
        get() = definedExternally
        set(value) = definedExternally
    var a: float?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ColorModifierTable {
    var r: float?
        get() = definedExternally
        set(value) = definedExternally
    var g: float?
        get() = definedExternally
        set(value) = definedExternally
    var b: float?
        get() = definedExternally
        set(value) = definedExternally
    var a: float?
        get() = definedExternally
        set(value) = definedExternally
}

external interface CraftingQueueItem {
    var index: uint
    var recipe: String
    var count: uint
}

external interface Alert {
    var tick: uint
    var target: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
    var prototype: LuaEntityPrototype?
        get() = definedExternally
        set(value) = definedExternally
    var position: Position?
        get() = definedExternally
        set(value) = definedExternally
    var icon: SignalID?
        get() = definedExternally
        set(value) = definedExternally
    var message: dynamic /* JsTuple<String, Any> | String? | Number? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface ScriptRenderVertexTarget {
    var target: dynamic /* Position | LuaEntity */
        get() = definedExternally
        set(value) = definedExternally
    var target_offset: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
}

external interface PathfinderWaypoint {
    var position: Position
    var needs_destroy_to_reach: Boolean
}

external interface CutsceneWaypoint {
    var position: Position?
        get() = definedExternally
        set(value) = definedExternally
    var target: dynamic /* LuaEntity? | LuaUnitGroup? */
        get() = definedExternally
        set(value) = definedExternally
    var transition_time: uint
    var time_to_wait: uint
    var zoom: double?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Decorative {
    var name: String
    var position: Position
    var amount: uint8
}

external interface DecorativeResult {
    var position: dynamic /* TilePositionTable | JsTuple<x, int, y, int> */
        get() = definedExternally
        set(value) = definedExternally
    var decorative: LuaDecorativePrototype
    var amount: uint
}

external interface ChartTagSpec {
    var position: Position
    var icon: SignalID?
        get() = definedExternally
        set(value) = definedExternally
    var text: String?
        get() = definedExternally
        set(value) = definedExternally
    var last_user: dynamic /* uint? | String? | LuaPlayer? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface GameViewSettings {
    var show_controller_gui: Boolean
    var show_minimap: Boolean
    var show_research_info: Boolean
    var show_entity_info: Boolean
    var show_alert_gui: Boolean
    var update_entity_selection: Boolean
    var show_rail_block_visualisation: Boolean
    var show_side_menu: Boolean
    var show_map_view_options: Boolean
    var show_quickbar: Boolean
    var show_shortcut_bar: Boolean
}

external interface MapViewSettings {
    @nativeGetter
    operator fun get(key: String): Boolean?
    @nativeSetter
    operator fun set(key: String, value: Boolean?)
}

external interface PollutionMapSettings {
    var enabled: Boolean
    var diffusion_ratio: double
    var min_to_diffuse: double
    var aeging: double
    var expected_max_per_chunk: double
    var min_to_show_per_chunk: double
    var min_pollution_to_damage_trees: double
    var pollution_with_max_forest_damage: double
    var pollution_per_tree_damage: double
    var pollution_restored_per_tree_damage: double
    var max_pollution_to_restore_trees: double
    var enemy_attack_pollution_consumption_modifier: double
}

external interface EnemyEvolutionMapSettings {
    var enabled: Boolean
    var time_factor: double
    var destroy_factor: double
    var pollution_factor: double
}

external interface EnemyExpansionMapSettings {
    var enabled: Boolean
    var max_expansion_distance: uint
    var friendly_base_influence_radius: uint
    var enemy_building_influence_radius: uint
    var building_coefficient: double
    var other_base_coefficient: double
    var neighbouring_chunk_coefficient: double
    var neighbouring_base_chunk_coefficient: double
    var max_colliding_tiles_coefficient: double
    var settler_group_min_size: uint
    var settler_group_max_size: uint
    var min_expansion_cooldown: uint
    var max_expansion_cooldown: uint
}

external interface UnitGroupMapSettings {
    var min_group_gathering_time: uint
    var max_group_gathering_time: uint
    var max_wait_time_for_late_members: uint
    var min_group_radius: double
    var max_group_radius: double
    var max_member_speedup_when_behind: double
    var max_member_slowdown_when_ahead: double
    var max_group_slowdown_factor: double
    var max_group_member_fallback_factor: double
    var member_disown_distance: double
    var tick_tolerance_when_member_arrives: uint
    var max_gathering_unit_groups: uint
    var max_unit_group_size: uint
}

external interface SteeringMapSetting {
    var radius: double
    var separation_factor: double
    var separation_force: double
    var force_unit_fuzzy_goto_behavior: Boolean
}

external interface SteeringMapSettings {
    var default: SteeringMapSetting
    var moving: SteeringMapSetting
}

external interface PathFinderMapSettings {
    var fwd2bwd_ratio: uint
    var goal_pressure_ratio: double
    var max_steps_worked_per_tick: double
    var max_work_done_per_tick: uint
    var use_path_cache: Boolean
    var short_cache_size: uint
    var long_cache_size: uint
    var short_cache_min_cacheable_distance: double
    var short_cache_min_algo_steps_to_cache: uint
    var long_cache_min_cacheable_distance: double
    var cache_max_connect_to_cache_steps_multiplier: uint
    var cache_accept_path_start_distance_ratio: double
    var cache_accept_path_end_distance_ratio: double
    var negative_cache_accept_path_start_distance_ratio: double
    var negative_cache_accept_path_end_distance_ratio: double
    var cache_path_start_distance_rating_multiplier: double
    var cache_path_end_distance_rating_multiplier: double
    var stale_enemy_with_same_destination_collision_penalty: double
    var ignore_moving_enemy_collision_distance: double
    var enemy_with_different_destination_collision_penalty: double
    var general_entity_collision_penalty: double
    var general_entity_subsequent_collision_penalty: double
    var extended_collision_penalty: double
    var max_clients_to_accept_any_new_request: uint
    var max_clients_to_accept_short_new_request: uint
    var direct_distance_to_consider_short_request: uint
    var short_request_max_steps: uint
    var short_request_ratio: double
    var min_steps_to_check_path_find_termination: uint
    var start_to_goal_cost_multiplier_to_terminate_path_find: double
    var overload_levels: Array<uint>
    var overload_multipliers: Array<double>
    var negative_path_cache_delay_interval: uint
}

external interface MapSettings {
    var pollution: PollutionMapSettings
    var enemy_evolution: EnemyEvolutionMapSettings
    var enemy_expansion: EnemyExpansionMapSettings
    var unit_group: UnitGroupMapSettings
    var steering: SteeringMapSettings
    var path_finder: PathFinderMapSettings
    var max_failed_behavior_count: uint
}

external interface DifficultySettings {
    var recipe_difficulty: recipe_difficulty
    var technology_difficulty: technology_difficulty
    var technology_price_multiplier: double
    var research_queue_setting: String /* "after-victory" | "always" | "never" */
}

external interface MapExchangeStringData {
    var map_settings: MapSettings
    var map_gen_settings: MapGenSettings
}

external interface BlueprintItemIcon {
    var name: String
    var index: uint
}

external interface BlueprintSignalIcon {
    var signal: SignalID
    var index: uint
}

external interface BlueprintConnectionData {
    var entity_id: uint
    var circuit_id: circuit_connector_id?
        get() = definedExternally
        set(value) = definedExternally
}

external interface BlueprintConnectionPoint {
    var red: Array<BlueprintConnectionData>?
        get() = definedExternally
        set(value) = definedExternally
    var green: Array<BlueprintConnectionData>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface BlueprintCircuitConnection {
    @nativeGetter
    operator fun get(key: String): BlueprintConnectionPoint?
    @nativeSetter
    operator fun set(key: String, value: BlueprintConnectionPoint?)
}

external interface BlueprintEntity {
    var entity_number: uint
    var name: String
    var position: Position
    var direction: direction?
        get() = definedExternally
        set(value) = definedExternally
    var tags: Tags?
        get() = definedExternally
        set(value) = definedExternally
    var items: Record<String, uint>?
        get() = definedExternally
        set(value) = definedExternally
    var connections: BlueprintCircuitConnection?
        get() = definedExternally
        set(value) = definedExternally
    var control_behavior: BlueprintControlBehavior?
        get() = definedExternally
        set(value) = definedExternally
    var schedule: Array<TrainScheduleRecord>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Tile {
    var position: Position
    var name: String
}

external interface Fluid {
    var name: String
    var amount: double
    var temperature: double?
        get() = definedExternally
        set(value) = definedExternally
}

external interface BaseIngredient {
    var type: String /* "item" | "fluid" */
    var name: String
    var amount: double
    var catalyst_amount: dynamic /* uint? | double? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface FluidIngredient : BaseIngredient {
    override var type: String /* "fluid" */
    var minimum_temperature: double?
        get() = definedExternally
        set(value) = definedExternally
    var maximum_temperature: double?
        get() = definedExternally
        set(value) = definedExternally
}

external interface OtherIngredient : BaseIngredient {
    override var type: String /* "item" */
}

external interface BaseProduct {
    var type: String /* "item" | "fluid" */
    var name: String
    var amount: double?
        get() = definedExternally
        set(value) = definedExternally
    var amount_min: dynamic /* uint? | double? */
        get() = definedExternally
        set(value) = definedExternally
    var amount_max: dynamic /* uint? | double? */
        get() = definedExternally
        set(value) = definedExternally
    var probability: double?
        get() = definedExternally
        set(value) = definedExternally
    var catalyst_amount: dynamic /* uint? | double? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface FluidProduct : BaseProduct {
    override var type: String /* "fluid" */
    var temperature: double?
        get() = definedExternally
        set(value) = definedExternally
}

external interface OtherProduct : BaseProduct {
    override var type: String /* "item" */
}

external interface Loot {
    var item: String
    var probability: double
    var count_min: double
    var count_max: double
}

external interface BaseTechnologyModifier {
    var type: String /* "inserter-stack-size-bonus" | "stack-inserter-capacity-bonus" | "laboratory-speed" | "character-logistic-trash-slots" | "maximum-following-robots-count" | "worker-robot-speed" | "worker-robot-storage" | "ghost-time-to-live" | "turret-attack" | "ammo-damage" | "give-item" | "gun-speed" | "unlock-recipe" | "character-crafting-speed" | "character-mining-speed" | "character-running-speed" | "character-build-distance" | "character-item-drop-distance" | "character-reach-distance" | "character-resource-reach-distance" | "character-item-pickup-distance" | "character-loot-pickup-distance" | "character-inventory-slots-bonus" | "deconstruction-time-to-live" | "max-failed-attempts-per-tick-per-construction-queue" | "max-successful-attempts-per-tick-per-construction-queue" | "character-health-bonus" | "mining-drill-productivity-bonus" | "train-braking-force-bonus" | "zoom-to-world-enabled" | "zoom-to-world-ghost-building-enabled" | "zoom-to-world-blueprint-enabled" | "zoom-to-world-deconstruction-planner-enabled" | "zoom-to-world-upgrade-planner-enabled" | "zoom-to-world-selection-tool-enabled" | "worker-robot-battery" | "laboratory-productivity" | "follower-robot-lifetime" | "artillery-range" | "nothing" | "character-additional-mining-categories" | "character-logistic-requests" */
}

external interface GunSpeedTechnologyModifier : BaseTechnologyModifier {
    override var type: String /* "gun-speed" */
    var ammo_category: String
    var modifier: double
}

external interface AmmoDamageTechnologyModifier : BaseTechnologyModifier {
    override var type: String /* "ammo-damage" */
    var ammo_category: String
    var modifier: double
}

external interface GiveItemTechnologyModifier : BaseTechnologyModifier {
    override var type: String /* "give-item" */
    var item: String
    var count: uint?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TurretAttackTechnologyModifier : BaseTechnologyModifier {
    override var type: String /* "turret-attack" */
    var turret_id: String
    var modifier: double
}

external interface UnlockRecipeTechnologyModifier : BaseTechnologyModifier {
    override var type: String /* "unlock-recipe" */
    var recipe: String
}

external interface NothingTechnologyModifier : BaseTechnologyModifier {
    override var type: String /* "nothing" */
    var effect_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
}

external interface OtherTechnologyModifier : BaseTechnologyModifier {
    override var type: String /* "inserter-stack-size-bonus" | "stack-inserter-capacity-bonus" | "laboratory-speed" | "character-logistic-trash-slots" | "maximum-following-robots-count" | "worker-robot-speed" | "worker-robot-storage" | "ghost-time-to-live" | "character-crafting-speed" | "character-mining-speed" | "character-running-speed" | "character-build-distance" | "character-item-drop-distance" | "character-reach-distance" | "character-resource-reach-distance" | "character-item-pickup-distance" | "character-loot-pickup-distance" | "character-inventory-slots-bonus" | "deconstruction-time-to-live" | "max-failed-attempts-per-tick-per-construction-queue" | "max-successful-attempts-per-tick-per-construction-queue" | "character-health-bonus" | "mining-drill-productivity-bonus" | "train-braking-force-bonus" | "zoom-to-world-enabled" | "zoom-to-world-ghost-building-enabled" | "zoom-to-world-blueprint-enabled" | "zoom-to-world-deconstruction-planner-enabled" | "zoom-to-world-upgrade-planner-enabled" | "zoom-to-world-selection-tool-enabled" | "worker-robot-battery" | "laboratory-productivity" | "follower-robot-lifetime" | "artillery-range" | "character-additional-mining-categories" | "character-logistic-requests" */
    var modifier: double
}

external interface Offer {
    var price: Array<dynamic /* FluidIngredient | OtherIngredient */>
    var offer: dynamic /* GunSpeedTechnologyModifier | AmmoDamageTechnologyModifier | GiveItemTechnologyModifier | TurretAttackTechnologyModifier | UnlockRecipeTechnologyModifier | NothingTechnologyModifier | OtherTechnologyModifier */
        get() = definedExternally
        set(value) = definedExternally
}

external interface AutoplaceSpecification {
    var probability_expression: NoiseExpression
    var richness_expression: NoiseExpression
    var coverage: double
    var sharpness: double
    var max_probability: double
    var placement_density: uint
    var richness_base: double
    var richness_multiplier: double
    var richness_multiplier_distance_bonus: double
    var starting_area_size: uint
    var order: String
    var default_enabled: Boolean
    var peaks: Array<AutoplaceSpecificationPeak>?
        get() = definedExternally
        set(value) = definedExternally
    var control: String?
        get() = definedExternally
        set(value) = definedExternally
    var tile_restriction: Array<AutoplaceSpecificationRestriction>?
        get() = definedExternally
        set(value) = definedExternally
    var force: String
    var random_probability_penalty: double
}

external interface NoiseExpression {
    var type: String
}

external interface AutoplaceSpecificationPeak {
    var influence: double
    var max_influence: double
    var min_influence: double
    var richness_influence: double
    var noisePersistence: double
    var noise_layer: String?
        get() = definedExternally
        set(value) = definedExternally
    var noise_octaves_difference: double
    var water_optimal: double
    var water_range: double
    var water_max_range: double
    var water_top_property_limit: double
    var elevation_optimal: double
    var elevation_range: double
    var elevation_max_range: double
    var elevation_top_property_limit: double
    var temperature_optimal: double
    var temperature_range: double
    var temperature_max_range: double
    var temperature_top_property_limit: double
    var starting_area_weight_optimal: double
    var starting_area_weight_range: double
    var starting_area_weight_max_range: double
    var starting_area_weight_top_property_limit: double
    var tier_from_start_optimal: double
    var tier_from_start_range: double
    var tier_from_start_max_range: double
    var tier_from_start_top_property_limit: double
    var distance_optimal: double
    var distance_range: double
    var distance_max_range: double
    var distance_top_property_limit: double
    var aux_optimal: double
    var aux_range: double
    var aux_max_range: double
    var aux_top_property_limit: double
}

external interface AutoplaceSpecificationRestriction {
    var first: String?
        get() = definedExternally
        set(value) = definedExternally
    var second: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Resistance {
    var decrease: float
    var percent: float
}

external interface AutoplaceSetting {
    var frequency: dynamic /* Number | "none" | "very-low" | "very-small" | "very-poor" | "low" | "small" | "poor" | "normal" | "medium" | "regular" | "high" | "big" | "good" | "very-high" | "very-big" | "very-good" */
        get() = definedExternally
        set(value) = definedExternally
    var size: dynamic /* Number | "none" | "very-low" | "very-small" | "very-poor" | "low" | "small" | "poor" | "normal" | "medium" | "regular" | "high" | "big" | "good" | "very-high" | "very-big" | "very-good" */
        get() = definedExternally
        set(value) = definedExternally
    var richness: dynamic /* Number | "none" | "very-low" | "very-small" | "very-poor" | "low" | "small" | "poor" | "normal" | "medium" | "regular" | "high" | "big" | "good" | "very-high" | "very-big" | "very-good" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface AutoplaceSettings {
    var treat_missing_as_default: Boolean
    var settings: Record<String, AutoplaceSetting>
}

external interface AutoplaceControl {
    var frequency: dynamic /* Number | "none" | "very-low" | "very-small" | "very-poor" | "low" | "small" | "poor" | "normal" | "medium" | "regular" | "high" | "big" | "good" | "very-high" | "very-big" | "very-good" */
        get() = definedExternally
        set(value) = definedExternally
    var size: dynamic /* Number | "none" | "very-low" | "very-small" | "very-poor" | "low" | "small" | "poor" | "normal" | "medium" | "regular" | "high" | "big" | "good" | "very-high" | "very-big" | "very-good" */
        get() = definedExternally
        set(value) = definedExternally
    var richness: dynamic /* Number | "none" | "very-low" | "very-small" | "very-poor" | "low" | "small" | "poor" | "normal" | "medium" | "regular" | "high" | "big" | "good" | "very-high" | "very-big" | "very-good" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface CliffPlacementSettings {
    var name: String
    var cliff_elevation_0: float
    var cliff_elevation_interval: float
    var richness: dynamic /* Number | "none" | "very-low" | "very-small" | "very-poor" | "low" | "small" | "poor" | "normal" | "medium" | "regular" | "high" | "big" | "good" | "very-high" | "very-big" | "very-good" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface MapGenSettings {
    var terrain_segmentation: dynamic /* Number | "none" | "very-low" | "very-small" | "very-poor" | "low" | "small" | "poor" | "normal" | "medium" | "regular" | "high" | "big" | "good" | "very-high" | "very-big" | "very-good" */
        get() = definedExternally
        set(value) = definedExternally
    var water: dynamic /* Number | "none" | "very-low" | "very-small" | "very-poor" | "low" | "small" | "poor" | "normal" | "medium" | "regular" | "high" | "big" | "good" | "very-high" | "very-big" | "very-good" */
        get() = definedExternally
        set(value) = definedExternally
    var autoplace_controls: Record<String, AutoplaceControl>
    var default_enable_all_autoplace_controls: Boolean
    var autoplace_settings: Record<String, AutoplaceSettings>
    var cliff_settings: CliffPlacementSettings
    var seed: uint
    var width: uint
    var height: uint
    var starting_area: dynamic /* Number | "none" | "very-low" | "very-small" | "very-poor" | "low" | "small" | "poor" | "normal" | "medium" | "regular" | "high" | "big" | "good" | "very-high" | "very-big" | "very-good" */
        get() = definedExternally
        set(value) = definedExternally
    var starting_points: Array<Position>
    var peaceful_mode: Boolean
    var property_expression_names: Record<String, String>
}

external interface MapGenSettingsPartial {
    var terrain_segmentation: dynamic /* Number? | "none" | "very-low" | "very-small" | "very-poor" | "low" | "small" | "poor" | "normal" | "medium" | "regular" | "high" | "big" | "good" | "very-high" | "very-big" | "very-good" */
        get() = definedExternally
        set(value) = definedExternally
    var water: dynamic /* Number? | "none" | "very-low" | "very-small" | "very-poor" | "low" | "small" | "poor" | "normal" | "medium" | "regular" | "high" | "big" | "good" | "very-high" | "very-big" | "very-good" */
        get() = definedExternally
        set(value) = definedExternally
    var autoplace_controls: Record<String, AutoplaceControl>?
        get() = definedExternally
        set(value) = definedExternally
    var default_enable_all_autoplace_controls: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var autoplace_settings: Record<String, AutoplaceSettings>?
        get() = definedExternally
        set(value) = definedExternally
    var cliff_settings: CliffPlacementSettings?
        get() = definedExternally
        set(value) = definedExternally
    var seed: uint?
        get() = definedExternally
        set(value) = definedExternally
    var width: uint?
        get() = definedExternally
        set(value) = definedExternally
    var height: uint?
        get() = definedExternally
        set(value) = definedExternally
    var starting_area: dynamic /* Number? | "none" | "very-low" | "very-small" | "very-poor" | "low" | "small" | "poor" | "normal" | "medium" | "regular" | "high" | "big" | "good" | "very-high" | "very-big" | "very-good" */
        get() = definedExternally
        set(value) = definedExternally
    var starting_points: Array<Position>?
        get() = definedExternally
        set(value) = definedExternally
    var peaceful_mode: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var property_expression_names: Record<String, String>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface SignalID {
    var type: String /* "item" | "fluid" | "virtual" */
    var name: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Signal {
    var signal: SignalID
    var count: int
}

external interface UpgradeFilter {
    var type: String /* "item" | "entity" */
    var name: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface InfinityInventoryFilter {
    var name: String
    var count: uint?
        get() = definedExternally
        set(value) = definedExternally
    var mode: String? /* "at-least" | "at-most" | "exactly" */
        get() = definedExternally
        set(value) = definedExternally
    var index: uint
}

external interface InfinityPipeFilter {
    var name: String
    var percentage: double?
        get() = definedExternally
        set(value) = definedExternally
    var temperature: double?
        get() = definedExternally
        set(value) = definedExternally
    var mode: String? /* "at-least" | "at-most" | "exactly" | "add" | "remove" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface FluidBoxFilter {
    var name: String
    var minimum_temperature: double
    var maximum_temperature: double
}

external interface FluidBoxFilterSpec {
    var name: String
    var minimum_temperature: double?
        get() = definedExternally
        set(value) = definedExternally
    var maximum_temperature: double?
        get() = definedExternally
        set(value) = definedExternally
    var force: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface HeatSetting {
    var temperature: double?
        get() = definedExternally
        set(value) = definedExternally
    var mode: String? /* "at-least" | "at-most" | "exactly" | "add" | "remove" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface HeatConnection {
    var position: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var direction: direction
}

external interface FluidBoxConnection {
    var type: String /* "input" | "output" | "input-output" */
    var positions: Array<PositionTable>
    var max_underground_distance: uint?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ArithmeticCombinatorParameters {
    var first_signal: SignalID?
        get() = definedExternally
        set(value) = definedExternally
    var second_signal: SignalID?
        get() = definedExternally
        set(value) = definedExternally
    var first_constant: int?
        get() = definedExternally
        set(value) = definedExternally
    var second_constant: int?
        get() = definedExternally
        set(value) = definedExternally
    var operation: String? /* "*" | "/" | "+" | "-" | "%" | "^" | "<<" | ">>" | "AND" | "OR" | "XOR" */
        get() = definedExternally
        set(value) = definedExternally
    var output_signal: SignalID?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ConstantCombinatorParameters {
    var signal: SignalID
    var count: int
    var index: uint
}

external interface DeciderCombinatorParameters {
    var first_signal: SignalID?
        get() = definedExternally
        set(value) = definedExternally
    var second_signal: SignalID?
        get() = definedExternally
        set(value) = definedExternally
    var constant: uint?
        get() = definedExternally
        set(value) = definedExternally
    var comparator: String? /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
        get() = definedExternally
        set(value) = definedExternally
    var output_signal: SignalID?
        get() = definedExternally
        set(value) = definedExternally
    var copy_count_from_input: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface InserterCircuitConditions {
    var circuit: CircuitCondition?
        get() = definedExternally
        set(value) = definedExternally
    var logistics: CircuitCondition?
        get() = definedExternally
        set(value) = definedExternally
}

external interface CircuitCondition {
    var comparator: String? /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
        get() = definedExternally
        set(value) = definedExternally
    var first_signal: SignalID?
        get() = definedExternally
        set(value) = definedExternally
    var second_signal: SignalID?
        get() = definedExternally
        set(value) = definedExternally
    var constant: int?
        get() = definedExternally
        set(value) = definedExternally
}

external interface CircuitConditionDefinition {
    var condition: CircuitCondition
    var fulfilled: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface CircuitConnectionDefinition {
    var wire: wire_type
    var target_entity: LuaEntity
    var source_circuit_id: circuit_connector_id
    var target_circuit_id: circuit_connector_id
}

external interface WireConnectionDefinition {
    var wire: wire_type
    var target_entity: LuaEntity
    var source_circuit_id: circuit_connector_id?
        get() = definedExternally
        set(value) = definedExternally
    var target_circuit_id: circuit_connector_id?
        get() = definedExternally
        set(value) = definedExternally
    var source_wire_id: circuit_connector_id?
        get() = definedExternally
        set(value) = definedExternally
    var target_wire_id: circuit_connector_id?
        get() = definedExternally
        set(value) = definedExternally
}

external interface InventoryFilter {
    var index: uint
    var name: String
}

external interface PlaceAsTileResult {
    var result: LuaTilePrototype
    var condition_size: uint
    var condition: CollisionMask
}

external interface BaseCommand {
    var type: command
}

external interface AttackCommand : BaseCommand {
    var target: LuaEntity
    var distraction: distraction?
        get() = definedExternally
        set(value) = definedExternally
}

external interface GoToLocationCommand : BaseCommand {
    var destination: Position?
        get() = definedExternally
        set(value) = definedExternally
    var destination_entity: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
    var distraction: distraction?
        get() = definedExternally
        set(value) = definedExternally
    var pathfind_flags: PathfinderFlags?
        get() = definedExternally
        set(value) = definedExternally
    var radius: double?
        get() = definedExternally
        set(value) = definedExternally
}

external interface CompoundCommand : BaseCommand {
    var structure_type: compound_command
    var commands: Array<dynamic /* AttackCommand | GoToLocationCommand | CompoundCommand | GroupCommand | AttackAreaCommand | WanderCommand | StopCommand | FleeCommand | BuildBaseCommand */>
}

external interface GroupCommand : BaseCommand {
    var group: LuaUnitGroup
    var distraction: distraction?
        get() = definedExternally
        set(value) = definedExternally
    var use_group_distraction: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface AttackAreaCommand : BaseCommand {
    var destination: Position
    var radius: double
    var distraction: distraction?
        get() = definedExternally
        set(value) = definedExternally
}

external interface WanderCommand : BaseCommand {
    var distraction: distraction?
        get() = definedExternally
        set(value) = definedExternally
    var radius: double?
        get() = definedExternally
        set(value) = definedExternally
    var wander_in_group: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var ticks_to_wait: uint?
        get() = definedExternally
        set(value) = definedExternally
}

external interface StopCommand : BaseCommand {
    var distraction: distraction?
        get() = definedExternally
        set(value) = definedExternally
    var ticks_to_wait: uint?
        get() = definedExternally
        set(value) = definedExternally
}

external interface FleeCommand : BaseCommand {
    var from: LuaEntity
    var distraction: distraction?
        get() = definedExternally
        set(value) = definedExternally
}

external interface BuildBaseCommand : BaseCommand {
    var destination: Position
    var distraction: distraction?
        get() = definedExternally
        set(value) = definedExternally
    var ignore_planner: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface PathfinderFlags {
    var allow_destroy_friendly_entities: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var allow_paths_through_own_entities: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var cache: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var prefer_straight_paths: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var low_priority: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var no_break: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface UnitSpawnDefinition {
    var unit: String
    var spawn_points: Array<SpawnPointDefinition>
}

external interface SpawnPointDefinition {
    var evolution_factor: double
    var weight: double
}

external interface ItemStackDefinition {
    var name: String
    var count: uint?
        get() = definedExternally
        set(value) = definedExternally
    var health: float?
        get() = definedExternally
        set(value) = definedExternally
    var durability: double?
        get() = definedExternally
        set(value) = definedExternally
    var ammo: double?
        get() = definedExternally
        set(value) = definedExternally
    var tags: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface WaitCondition {
    var type: String /* "time" | "inactivity" | "full" | "empty" | "item_count" | "circuit" | "robots_inactive" | "fluid_count" | "passenger_present" | "passenger_not_present" */
    var compare_type: String /* "and" | "or" */
    var ticks: uint?
        get() = definedExternally
        set(value) = definedExternally
    var condition: CircuitCondition?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TrainScheduleRecord {
    var station: String?
        get() = definedExternally
        set(value) = definedExternally
    var rail: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
    var wait_conditions: Array<WaitCondition>?
        get() = definedExternally
        set(value) = definedExternally
    var temporary: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TrainSchedule {
    var current: uint
    var records: Array<TrainScheduleRecord>
}

external interface BaseGuiArrowSpecification {
    var type: String /* "nowhere" | "goal" | "entity_info" | "active_window" | "entity" | "position" | "crafting_queue" | "item_stack" */
}

external interface EntityGuiArrowSpecification : BaseGuiArrowSpecification {
    override var type: String /* "entity" */
    var entity: LuaEntity
}

external interface PositionGuiArrowSpecification : BaseGuiArrowSpecification {
    override var type: String /* "position" */
    var position: Position
}

external interface CraftingQueueGuiArrowSpecification : BaseGuiArrowSpecification {
    override var type: String /* "crafting_queue" */
    var crafting_queueindex: uint
}

external interface ItemStackGuiArrowSpecification : BaseGuiArrowSpecification {
    override var type: String /* "item_stack" */
    var inventory_index: inventory
    var item_stack_index: uint
    var source: String /* "player" | "target" | "player-quickbar" | "player-equipment-bar" */
}

external interface OtherGuiArrowSpecification : BaseGuiArrowSpecification {
    override var type: String /* "nowhere" | "goal" | "entity_info" | "active_window" */
}

external interface AmmoType {
    var action: Array<TriggerItem>?
        get() = definedExternally
        set(value) = definedExternally
    var target_type: String /* "entity" | "position" | "direction" */
    var clamp_position: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var category: String
    var energy_consumption: double?
        get() = definedExternally
        set(value) = definedExternally
}

external interface BeamTarget {
    var entity: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
    var position: Position?
        get() = definedExternally
        set(value) = definedExternally
}

external interface RidingState {
    var acceleration: acceleration
    var direction: _defines_riding_direction
}

external interface ModuleEffectValue {
    var bonus: float
}

external interface ModuleEffects {
    var consumption: ModuleEffectValue?
        get() = definedExternally
        set(value) = definedExternally
    var speed: ModuleEffectValue?
        get() = definedExternally
        set(value) = definedExternally
    var productivity: ModuleEffectValue?
        get() = definedExternally
        set(value) = definedExternally
    var pollution: ModuleEffectValue?
        get() = definedExternally
        set(value) = definedExternally
}

external interface EntityPrototypeFlags {
    @nativeGetter
    operator fun get(key: String): Boolean?
    @nativeSetter
    operator fun set(key: String, value: Boolean?)
    var hidden: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ItemPrototypeFlags {
    @nativeGetter
    operator fun get(key: String): Boolean?
    @nativeSetter
    operator fun set(key: String, value: Boolean?)
    var hidden: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

typealias CollisionMask = Any

typealias CollisionMaskWithFlags = Any

typealias TriggerTargetMask = Any

external interface TriggerEffectItem {
    var type: String /* "damage" | "create-entity" | "create-explosion" | "create-fire" | "create-smoke" | "create-trivial-smoke" | "create-particle" | "create-sticker" | "nested-result" | "play-sound" | "push-back" | "destroy-cliffs" | "show-explosion-on-chart" | "insert-item" | "script" */
    var repeat_count: uint
    var affects_target: Boolean
    var show_in_tooltip: Boolean
}

external interface TriggerDelivery {
    var type: String /* "instant" | "projectile" | "flame-thrower" | "beam" | "stream" | "artillery" */
    var source_effects: Array<TriggerEffectItem>
    var target_effects: Array<TriggerEffectItem>
}

external interface TriggerItem {
    var type: String /* "direct" | "area" | "line" | "cluster" */
    var action_delivery: Array<TriggerDelivery>?
        get() = definedExternally
        set(value) = definedExternally
    var entity_flags: EntityPrototypeFlags?
        get() = definedExternally
        set(value) = definedExternally
    var ignore_collision_condition: Boolean
    var collision_mask: CollisionMask
    var trigger_target_mask: TriggerTargetMask
    var force: String /* "all" | "enemy" | "ally" | "friend" | "not-friend" | "same" | "not-same" */
    var repeat_count: uint
}

external interface CircularParticleCreationSpecification {
    var name: String
    var direction: float
    var direction_deviation: float
    var speed: float
    var speed_deviation: float
    var starting_frame_speed: float
    var starting_frame_speed_deviation: float
    var height: float
    var height_deviation: float
    var vertical_speed: float
    var vertical_speed_deviation: float
    var center: PositionTable
    var creation_distance: double
    var creation_distance_orientation: double
    var use_source_position: Boolean
}

external interface AttackParameterFluid {
    var type: String
    var damage_modifier: double
}

external interface BaseAttackParameters {
    var type: String /* "projectile" | "stream" | "beam" */
    var range: float
    var min_range: float
    var range_mode: String /* "center-to-center" | "bounding-box-to-bounding-box" */
    var fire_penalty: float
    var rotate_penalty: float
    var health_penalty: float
    var min_attack_distance: float
    var turn_range: float
    var damage_modifier: float
    var ammo_consumption_modifier: float
    var cooldown: float
    var warmup: uint
    var movement_slow_down_factor: double
    var movement_slow_down_cooldown: float
    var ammo_type: AmmoType?
        get() = definedExternally
        set(value) = definedExternally
    var ammo_categories: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ProjectileAttackParameters : BaseAttackParameters {
    override var type: String /* "projectile" */
    var projectile_center: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var projectile_creation_distance: float
    var projectile_orientation_offset: float
    var shell_particle: CircularParticleCreationSpecification?
        get() = definedExternally
        set(value) = definedExternally
    var projectile_creation_parameters: Array<dynamic /* JsTuple<RealOrientation, dynamic> */>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface StreamAttackParameters : BaseAttackParameters {
    override var type: String /* "stream" */
    var gun_barrel_length: float
    var gun_center_shift: Record<String, dynamic /* typealias Vector = dynamic */>
    var fluid_consumption: float
    var fluids: Array<AttackParameterFluid>?
        get() = definedExternally
        set(value) = definedExternally
    var projectile_creation_parameters: Array<dynamic /* JsTuple<RealOrientation, dynamic> */>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface OtherAttackParameters : BaseAttackParameters {
    override var type: String /* "beam" */
}

external interface CapsuleAction {
    var type: String /* "throw" | "equipment-remote" | "use-on-self" */
    var attack_parameters: dynamic /* ProjectileAttackParameters? | StreamAttackParameters? | OtherAttackParameters? */
        get() = definedExternally
        set(value) = definedExternally
    var equipment: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface SelectionModeFlags {
    @nativeGetter
    operator fun get(key: String): Boolean?
    @nativeSetter
    operator fun set(key: String, value: Boolean?)
    var blueprint: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var deconstruct: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var items: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var trees: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var nothing: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var friend: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var enemy: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var upgrade: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface LogisticFilter {
    var index: uint
    var name: String
    var count: uint
}

external interface ModSetting {
    var value: dynamic /* uint | double | Boolean | String */
        get() = definedExternally
        set(value) = definedExternally
}

external interface ProgrammableSpeakerParameters {
    var playback_volume: double
    var playback_globally: Boolean
    var allow_polyphony: Boolean
}

external interface ProgrammableSpeakerAlertParameters {
    var show_alert: Boolean
    var show_on_map: Boolean
    var icon_signal_id: SignalID
    var alert_message: String
}

external interface ProgrammableSpeakerCircuitParameters {
    var signal_value_is_pitch: Boolean
    var instrument_id: uint
    var note_id: uint
}

external interface ProgrammableSpeakerInstrument {
    var name: String
    var notes: Array<String>
}

external interface EventData {
    var name: uint /* uint & `T$93`<EventData, Unit> */
    var tick: uint
    var mod_name: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface NthTickEventData {
    var tick: uint
    var nth_tick: uint
}

external interface ModChangeData {
    var old_version: String?
    var new_version: String?
}

external interface ConfigurationChangedData {
    var old_version: String?
        get() = definedExternally
        set(value) = definedExternally
    var new_version: String?
        get() = definedExternally
        set(value) = definedExternally
    var mod_changes: Record<String, ModChangeData>
    var mod_startup_settings_changed: Boolean
    var migration_applied: Boolean
}

external interface CustomCommandData {
    var name: String
    var tick: uint
    var player_index: uint?
        get() = definedExternally
        set(value) = definedExternally
    var parameter: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface SelectedPrototypeData {
    var base_type: String
    var derived_type: String
    var name: String
}

external interface ScriptRenderTarget {
    var entity: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
    var entity_offset: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var position: Position?
        get() = definedExternally
        set(value) = definedExternally
}

typealias MouseButtonFlagsTable = Any

typealias MouseButtonFlagsArray = Array<String /* "left" | "right" | "middle" | "button-4" | "button-5" | "button-6" | "button-7" | "button-8" | "button-9" | "left-and-right" */>

external interface ItemStackLocation {
    var inventory: inventory
    var slot: uint
}

external interface VehicleAutomaticTargetingParameters {
    var auto_target_without_gunner: Boolean
    var auto_target_with_gunner: Boolean
}

external interface BaseItemPrototypeFilter {
    var filter: String /* "tool" | "mergeable" | "item-with-inventory" | "selection-tool" | "item-with-label" | "has-rocket-launch-products" | "fuel" | "place-result" | "burnt-result" | "place-as-tile" | "placed-as-equipment-result" | "name" | "type" | "flag" | "subgroup" | "fuel-category" | "stack-size" | "default-request-amount" | "wire-count" | "fuel-value" | "fuel-acceleration-multiplier" | "fuel-top-speed-multiplier" | "fuel-emissions-multiplier" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface PlaceResultItemPrototypeFilter : BaseItemPrototypeFilter {
    override var filter: String /* "place-result" */
    var elem_filters: Array<dynamic /* NameEntityPrototypeFilter | TypeEntityPrototypeFilter | CollisionMaskEntityPrototypeFilter | FlagEntityPrototypeFilter | BuildBaseEvolutionRequirementEntityPrototypeFilter | SelectionPriorityEntityPrototypeFilter | EmissionsEntityPrototypeFilter | CraftingCategoryEntityPrototypeFilter | OtherEntityPrototypeFilter */>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface BurntResultItemPrototypeFilter : BaseItemPrototypeFilter {
    override var filter: String /* "burnt-result" */
    var elem_filters: Array<dynamic /* PlaceResultItemPrototypeFilter | BurntResultItemPrototypeFilter | PlaceAsTileItemPrototypeFilter | PlacedAsEquipmentResultItemPrototypeFilter | NameItemPrototypeFilter | TypeItemPrototypeFilter | FlagItemPrototypeFilter | SubgroupItemPrototypeFilter | FuelCategoryItemPrototypeFilter | StackSizeItemPrototypeFilter | DefaultRequestAmountItemPrototypeFilter | WireCountItemPrototypeFilter | FuelValueItemPrototypeFilter | FuelAccelerationMultiplierItemPrototypeFilter | FuelTopSpeedMultiplierItemPrototypeFilter | FuelEmissionsMultiplierItemPrototypeFilter | OtherItemPrototypeFilter */>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface PlaceAsTileItemPrototypeFilter : BaseItemPrototypeFilter {
    override var filter: String /* "place-as-tile" */
    var elem_filters: Array<dynamic /* CollisionMaskTilePrototypeFilter | WalkingSpeedModifierTilePrototypeFilter | VehicleFrictionModifierTilePrototypeFilter | DecorativeRemovalProbabilityTilePrototypeFilter | EmissionsTilePrototypeFilter | OtherTilePrototypeFilter */>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface PlacedAsEquipmentResultItemPrototypeFilter : BaseItemPrototypeFilter {
    override var filter: String /* "placed-as-equipment-result" */
    var elem_filters: Array<dynamic /* TypeEquipmentPrototypeFilter | OtherEquipmentPrototypeFilter */>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface NameItemPrototypeFilter : BaseItemPrototypeFilter {
    override var filter: String /* "name" */
    var name: dynamic /* String | Array<String> */
        get() = definedExternally
        set(value) = definedExternally
}

external interface TypeItemPrototypeFilter : BaseItemPrototypeFilter {
    override var filter: String /* "type" */
    var type: String
}

external interface FlagItemPrototypeFilter : BaseItemPrototypeFilter {
    override var filter: String /* "flag" */
    var flag: String
}

external interface SubgroupItemPrototypeFilter : BaseItemPrototypeFilter {
    override var filter: String /* "subgroup" */
    var subgroup: String
}

external interface FuelCategoryItemPrototypeFilter : BaseItemPrototypeFilter {
    @nativeGetter
    operator fun get(key: String): String?
    @nativeSetter
    operator fun set(key: String, value: String)
    override var filter: String /* "fuel-category" */
}

external interface StackSizeItemPrototypeFilter : BaseItemPrototypeFilter {
    override var filter: String /* "stack-size" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: uint
}

external interface DefaultRequestAmountItemPrototypeFilter : BaseItemPrototypeFilter {
    override var filter: String /* "default-request-amount" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: uint
}

external interface WireCountItemPrototypeFilter : BaseItemPrototypeFilter {
    override var filter: String /* "wire-count" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: uint
}

external interface FuelValueItemPrototypeFilter : BaseItemPrototypeFilter {
    override var filter: String /* "fuel-value" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: double
}

external interface FuelAccelerationMultiplierItemPrototypeFilter : BaseItemPrototypeFilter {
    override var filter: String /* "fuel-acceleration-multiplier" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: double
}

external interface FuelTopSpeedMultiplierItemPrototypeFilter : BaseItemPrototypeFilter {
    override var filter: String /* "fuel-top-speed-multiplier" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: double
}

external interface FuelEmissionsMultiplierItemPrototypeFilter : BaseItemPrototypeFilter {
    override var filter: String /* "fuel-emissions-multiplier" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: double
}

external interface OtherItemPrototypeFilter : BaseItemPrototypeFilter {
    override var filter: String /* "tool" | "mergeable" | "item-with-inventory" | "selection-tool" | "item-with-label" | "has-rocket-launch-products" | "fuel" */
}

external interface BaseModSettingPrototypeFilter {
    var filter: String /* "type" | "mod" | "setting-type" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TypeModSettingPrototypeFilter : BaseModSettingPrototypeFilter {
    override var filter: String /* "type" */
    var type: String
}

external interface ModModSettingPrototypeFilter : BaseModSettingPrototypeFilter {
    override var filter: String /* "mod" */
    var mod: String
}

external interface SettingTypeModSettingPrototypeFilter : BaseModSettingPrototypeFilter {
    override var filter: String /* "setting-type" */
    var type: String
}

external interface BaseTechnologyPrototypeFilter {
    var filter: String /* "enabled" | "hidden" | "upgrade" | "visible-when-disabled" | "has-effects" | "has-prerequisites" | "research-unit-ingredient" | "level" | "max-level" | "time" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ResearchUnitIngredientTechnologyPrototypeFilter : BaseTechnologyPrototypeFilter {
    override var filter: String /* "research-unit-ingredient" */
    var ingredient: String
}

external interface LevelTechnologyPrototypeFilter : BaseTechnologyPrototypeFilter {
    override var filter: String /* "level" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: uint
}

external interface MaxLevelTechnologyPrototypeFilter : BaseTechnologyPrototypeFilter {
    override var filter: String /* "max-level" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: uint
}

external interface TimeTechnologyPrototypeFilter : BaseTechnologyPrototypeFilter {
    override var filter: String /* "time" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: uint
}

external interface OtherTechnologyPrototypeFilter : BaseTechnologyPrototypeFilter {
    override var filter: String /* "enabled" | "hidden" | "upgrade" | "visible-when-disabled" | "has-effects" | "has-prerequisites" */
}

external interface BaseDecorativePrototypeFilter {
    var filter: String /* "decal" | "autoplace" | "collision-mask" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface CollisionMaskDecorativePrototypeFilter : BaseDecorativePrototypeFilter {
    override var filter: String /* "collision-mask" */
    var mask: dynamic /* CollisionMask | CollisionMaskWithFlags */
        get() = definedExternally
        set(value) = definedExternally
    var mask_mode: String /* "collides" | "layers-equals" */
}

external interface OtherDecorativePrototypeFilter : BaseDecorativePrototypeFilter {
    override var filter: String /* "decal" | "autoplace" */
}

external interface BaseAchievementPrototypeFilter {
    var filter: String /* "allowed-without-fight" | "type" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TypeAchievementPrototypeFilter : BaseAchievementPrototypeFilter {
    override var filter: String /* "type" */
    var type: String
}

external interface OtherAchievementPrototypeFilter : BaseAchievementPrototypeFilter {
    override var filter: String /* "allowed-without-fight" */
}

external interface BaseFluidPrototypeFilter {
    var filter: String /* "hidden" | "name" | "subgroup" | "default-temperature" | "max-temperature" | "heat-capacity" | "fuel-value" | "emissions-multiplier" | "gas-temperature" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface NameFluidPrototypeFilter : BaseFluidPrototypeFilter {
    override var filter: String /* "name" */
    var name: dynamic /* String | Array<String> */
        get() = definedExternally
        set(value) = definedExternally
}

external interface SubgroupFluidPrototypeFilter : BaseFluidPrototypeFilter {
    override var filter: String /* "subgroup" */
    var subgroup: String
}

external interface DefaultTemperatureFluidPrototypeFilter : BaseFluidPrototypeFilter {
    override var filter: String /* "default-temperature" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: double
}

external interface MaxTemperatureFluidPrototypeFilter : BaseFluidPrototypeFilter {
    override var filter: String /* "max-temperature" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: double
}

external interface HeatCapacityFluidPrototypeFilter : BaseFluidPrototypeFilter {
    override var filter: String /* "heat-capacity" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: double
}

external interface FuelValueFluidPrototypeFilter : BaseFluidPrototypeFilter {
    override var filter: String /* "fuel-value" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: double
}

external interface EmissionsMultiplierFluidPrototypeFilter : BaseFluidPrototypeFilter {
    override var filter: String /* "emissions-multiplier" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: double
}

external interface GasTemperatureFluidPrototypeFilter : BaseFluidPrototypeFilter {
    override var filter: String /* "gas-temperature" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: double
}

external interface OtherFluidPrototypeFilter : BaseFluidPrototypeFilter {
    override var filter: String /* "hidden" */
}

external interface BaseEquipmentPrototypeFilter {
    var filter: String /* "item-to-place" | "type" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TypeEquipmentPrototypeFilter : BaseEquipmentPrototypeFilter {
    override var filter: String /* "type" */
    var type: String
}

external interface OtherEquipmentPrototypeFilter : BaseEquipmentPrototypeFilter {
    override var filter: String /* "item-to-place" */
}

external interface BaseTilePrototypeFilter {
    var filter: String /* "minable" | "autoplace" | "blueprintable" | "item-to-place" | "collision-mask" | "walking-speed-modifier" | "vehicle-friction-modifier" | "decorative-removal-probability" | "emissions" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface CollisionMaskTilePrototypeFilter : BaseTilePrototypeFilter {
    override var filter: String /* "collision-mask" */
    var mask: dynamic /* CollisionMask | CollisionMaskWithFlags */
        get() = definedExternally
        set(value) = definedExternally
    var mask_mode: String /* "collides" | "layers-equals" */
}

external interface WalkingSpeedModifierTilePrototypeFilter : BaseTilePrototypeFilter {
    override var filter: String /* "walking-speed-modifier" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: double
}

external interface VehicleFrictionModifierTilePrototypeFilter : BaseTilePrototypeFilter {
    override var filter: String /* "vehicle-friction-modifier" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: double
}

external interface DecorativeRemovalProbabilityTilePrototypeFilter : BaseTilePrototypeFilter {
    override var filter: String /* "decorative-removal-probability" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: float
}

external interface EmissionsTilePrototypeFilter : BaseTilePrototypeFilter {
    override var filter: String /* "emissions" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: double
}

external interface OtherTilePrototypeFilter : BaseTilePrototypeFilter {
    override var filter: String /* "minable" | "autoplace" | "blueprintable" | "item-to-place" */
}

external interface BaseRecipePrototypeFilter {
    var filter: String /* "enabled" | "hidden" | "hidden-from-flow-stats" | "hidden-from-player-crafting" | "allow-as-intermediate" | "allow-intermediates" | "allow-decomposition" | "always-show-made-in" | "always-show-products" | "show-amount-in-title" | "has-ingredients" | "has-products" | "has-ingredient-item" | "has-ingredient-fluid" | "has-product-item" | "has-product-fluid" | "subgroup" | "category" | "energy" | "emissions-multiplier" | "request-paste-multiplier" | "overload-multiplier" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface HasIngredientItemRecipePrototypeFilter : BaseRecipePrototypeFilter {
    override var filter: String /* "has-ingredient-item" */
    var elem_filters: Array<dynamic /* PlaceResultItemPrototypeFilter | BurntResultItemPrototypeFilter | PlaceAsTileItemPrototypeFilter | PlacedAsEquipmentResultItemPrototypeFilter | NameItemPrototypeFilter | TypeItemPrototypeFilter | FlagItemPrototypeFilter | SubgroupItemPrototypeFilter | FuelCategoryItemPrototypeFilter | StackSizeItemPrototypeFilter | DefaultRequestAmountItemPrototypeFilter | WireCountItemPrototypeFilter | FuelValueItemPrototypeFilter | FuelAccelerationMultiplierItemPrototypeFilter | FuelTopSpeedMultiplierItemPrototypeFilter | FuelEmissionsMultiplierItemPrototypeFilter | OtherItemPrototypeFilter */>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface HasIngredientFluidRecipePrototypeFilter : BaseRecipePrototypeFilter {
    override var filter: String /* "has-ingredient-fluid" */
    var elem_filters: Array<dynamic /* NameFluidPrototypeFilter | SubgroupFluidPrototypeFilter | DefaultTemperatureFluidPrototypeFilter | MaxTemperatureFluidPrototypeFilter | HeatCapacityFluidPrototypeFilter | FuelValueFluidPrototypeFilter | EmissionsMultiplierFluidPrototypeFilter | GasTemperatureFluidPrototypeFilter | OtherFluidPrototypeFilter */>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface HasProductItemRecipePrototypeFilter : BaseRecipePrototypeFilter {
    override var filter: String /* "has-product-item" */
    var elem_filters: Array<dynamic /* PlaceResultItemPrototypeFilter | BurntResultItemPrototypeFilter | PlaceAsTileItemPrototypeFilter | PlacedAsEquipmentResultItemPrototypeFilter | NameItemPrototypeFilter | TypeItemPrototypeFilter | FlagItemPrototypeFilter | SubgroupItemPrototypeFilter | FuelCategoryItemPrototypeFilter | StackSizeItemPrototypeFilter | DefaultRequestAmountItemPrototypeFilter | WireCountItemPrototypeFilter | FuelValueItemPrototypeFilter | FuelAccelerationMultiplierItemPrototypeFilter | FuelTopSpeedMultiplierItemPrototypeFilter | FuelEmissionsMultiplierItemPrototypeFilter | OtherItemPrototypeFilter */>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface HasProductFluidRecipePrototypeFilter : BaseRecipePrototypeFilter {
    override var filter: String /* "has-product-fluid" */
    var elem_filters: Array<dynamic /* NameFluidPrototypeFilter | SubgroupFluidPrototypeFilter | DefaultTemperatureFluidPrototypeFilter | MaxTemperatureFluidPrototypeFilter | HeatCapacityFluidPrototypeFilter | FuelValueFluidPrototypeFilter | EmissionsMultiplierFluidPrototypeFilter | GasTemperatureFluidPrototypeFilter | OtherFluidPrototypeFilter */>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface SubgroupRecipePrototypeFilter : BaseRecipePrototypeFilter {
    override var filter: String /* "subgroup" */
    var subgroup: String
}

external interface CategoryRecipePrototypeFilter : BaseRecipePrototypeFilter {
    override var filter: String /* "category" */
    var category: String
}

external interface EnergyRecipePrototypeFilter : BaseRecipePrototypeFilter {
    override var filter: String /* "energy" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: double
}

external interface EmissionsMultiplierRecipePrototypeFilter : BaseRecipePrototypeFilter {
    override var filter: String /* "emissions-multiplier" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: double
}

external interface RequestPasteMultiplierRecipePrototypeFilter : BaseRecipePrototypeFilter {
    override var filter: String /* "request-paste-multiplier" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: uint
}

external interface OverloadMultiplierRecipePrototypeFilter : BaseRecipePrototypeFilter {
    override var filter: String /* "overload-multiplier" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: uint
}

external interface OtherRecipePrototypeFilter : BaseRecipePrototypeFilter {
    override var filter: String /* "enabled" | "hidden" | "hidden-from-flow-stats" | "hidden-from-player-crafting" | "allow-as-intermediate" | "allow-intermediates" | "allow-decomposition" | "always-show-made-in" | "always-show-products" | "show-amount-in-title" | "has-ingredients" | "has-products" */
}

external interface BaseEntityPrototypeFilter {
    var filter: String /* "flying-robot" | "robot-with-logistics-interface" | "rail" | "ghost" | "explosion" | "vehicle" | "crafting-machine" | "rolling-stock" | "turret" | "transport-belt-connectable" | "wall-connectable" | "buildable" | "placable-in-editor" | "clonable" | "selectable" | "hidden" | "entity-with-health" | "building" | "fast-replaceable" | "uses-direction" | "minable" | "circuit-connectable" | "autoplace" | "blueprintable" | "item-to-place" | "name" | "type" | "collision-mask" | "flag" | "build-base-evolution-requirement" | "selection-priority" | "emissions" | "crafting-category" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface NameEntityPrototypeFilter : BaseEntityPrototypeFilter {
    override var filter: String /* "name" */
    var name: dynamic /* String | Array<String> */
        get() = definedExternally
        set(value) = definedExternally
}

external interface TypeEntityPrototypeFilter : BaseEntityPrototypeFilter {
    override var filter: String /* "type" */
    var type: String
}

external interface CollisionMaskEntityPrototypeFilter : BaseEntityPrototypeFilter {
    override var filter: String /* "collision-mask" */
    var mask: dynamic /* CollisionMask | CollisionMaskWithFlags */
        get() = definedExternally
        set(value) = definedExternally
    var mask_mode: String /* "collides" | "layers-equals" */
}

external interface FlagEntityPrototypeFilter : BaseEntityPrototypeFilter {
    override var filter: String /* "flag" */
    var flag: String
}

external interface BuildBaseEvolutionRequirementEntityPrototypeFilter : BaseEntityPrototypeFilter {
    override var filter: String /* "build-base-evolution-requirement" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: double
}

external interface SelectionPriorityEntityPrototypeFilter : BaseEntityPrototypeFilter {
    override var filter: String /* "selection-priority" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: uint8
}

external interface EmissionsEntityPrototypeFilter : BaseEntityPrototypeFilter {
    override var filter: String /* "emissions" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: double
}

external interface CraftingCategoryEntityPrototypeFilter : BaseEntityPrototypeFilter {
    override var filter: String /* "crafting-category" */
    var crafting_category: String
}

external interface OtherEntityPrototypeFilter : BaseEntityPrototypeFilter {
    override var filter: String /* "flying-robot" | "robot-with-logistics-interface" | "rail" | "ghost" | "explosion" | "vehicle" | "crafting-machine" | "rolling-stock" | "turret" | "transport-belt-connectable" | "wall-connectable" | "buildable" | "placable-in-editor" | "clonable" | "selectable" | "hidden" | "entity-with-health" | "building" | "fast-replaceable" | "uses-direction" | "minable" | "circuit-connectable" | "autoplace" | "blueprintable" | "item-to-place" */
}

external interface BaseScriptRaisedReviveEventFilter {
    var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" | "type" | "name" | "ghost_type" | "ghost_name" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TypeScriptRaisedReviveEventFilter : BaseScriptRaisedReviveEventFilter {
    override var filter: String /* "type" */
    var type: String
}

external interface NameScriptRaisedReviveEventFilter : BaseScriptRaisedReviveEventFilter {
    override var filter: String /* "name" */
    var name: String
}

external interface GhostTypeScriptRaisedReviveEventFilter : BaseScriptRaisedReviveEventFilter {
    override var filter: String /* "ghost_type" */
    var type: String
}

external interface GhostNameScriptRaisedReviveEventFilter : BaseScriptRaisedReviveEventFilter {
    override var filter: String /* "ghost_name" */
    var name: String
}

external interface OtherScriptRaisedReviveEventFilter : BaseScriptRaisedReviveEventFilter {
    override var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" */
}

external interface BaseEntityDiedEventFilter {
    var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" | "type" | "name" | "ghost_type" | "ghost_name" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TypeEntityDiedEventFilter : BaseEntityDiedEventFilter {
    override var filter: String /* "type" */
    var type: String
}

external interface NameEntityDiedEventFilter : BaseEntityDiedEventFilter {
    override var filter: String /* "name" */
    var name: String
}

external interface GhostTypeEntityDiedEventFilter : BaseEntityDiedEventFilter {
    override var filter: String /* "ghost_type" */
    var type: String
}

external interface GhostNameEntityDiedEventFilter : BaseEntityDiedEventFilter {
    override var filter: String /* "ghost_name" */
    var name: String
}

external interface OtherEntityDiedEventFilter : BaseEntityDiedEventFilter {
    override var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" */
}

external interface BaseEntityMarkedForDeconstructionEventFilter {
    var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" | "type" | "name" | "ghost_type" | "ghost_name" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TypeEntityMarkedForDeconstructionEventFilter : BaseEntityMarkedForDeconstructionEventFilter {
    override var filter: String /* "type" */
    var type: String
}

external interface NameEntityMarkedForDeconstructionEventFilter : BaseEntityMarkedForDeconstructionEventFilter {
    override var filter: String /* "name" */
    var name: String
}

external interface GhostTypeEntityMarkedForDeconstructionEventFilter : BaseEntityMarkedForDeconstructionEventFilter {
    override var filter: String /* "ghost_type" */
    var type: String
}

external interface GhostNameEntityMarkedForDeconstructionEventFilter : BaseEntityMarkedForDeconstructionEventFilter {
    override var filter: String /* "ghost_name" */
    var name: String
}

external interface OtherEntityMarkedForDeconstructionEventFilter : BaseEntityMarkedForDeconstructionEventFilter {
    override var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" */
}

external interface BasePreGhostDeconstructedEventFilter {
    var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" | "type" | "name" | "ghost_type" | "ghost_name" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TypePreGhostDeconstructedEventFilter : BasePreGhostDeconstructedEventFilter {
    override var filter: String /* "type" */
    var type: String
}

external interface NamePreGhostDeconstructedEventFilter : BasePreGhostDeconstructedEventFilter {
    override var filter: String /* "name" */
    var name: String
}

external interface GhostTypePreGhostDeconstructedEventFilter : BasePreGhostDeconstructedEventFilter {
    override var filter: String /* "ghost_type" */
    var type: String
}

external interface GhostNamePreGhostDeconstructedEventFilter : BasePreGhostDeconstructedEventFilter {
    override var filter: String /* "ghost_name" */
    var name: String
}

external interface OtherPreGhostDeconstructedEventFilter : BasePreGhostDeconstructedEventFilter {
    override var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" */
}

external interface BaseScriptRaisedDestroyEventFilter {
    var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" | "type" | "name" | "ghost_type" | "ghost_name" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TypeScriptRaisedDestroyEventFilter : BaseScriptRaisedDestroyEventFilter {
    override var filter: String /* "type" */
    var type: String
}

external interface NameScriptRaisedDestroyEventFilter : BaseScriptRaisedDestroyEventFilter {
    override var filter: String /* "name" */
    var name: String
}

external interface GhostTypeScriptRaisedDestroyEventFilter : BaseScriptRaisedDestroyEventFilter {
    override var filter: String /* "ghost_type" */
    var type: String
}

external interface GhostNameScriptRaisedDestroyEventFilter : BaseScriptRaisedDestroyEventFilter {
    override var filter: String /* "ghost_name" */
    var name: String
}

external interface OtherScriptRaisedDestroyEventFilter : BaseScriptRaisedDestroyEventFilter {
    override var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" */
}

external interface BaseUpgradeCancelledEventFilter {
    var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" | "type" | "name" | "ghost_type" | "ghost_name" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TypeUpgradeCancelledEventFilter : BaseUpgradeCancelledEventFilter {
    override var filter: String /* "type" */
    var type: String
}

external interface NameUpgradeCancelledEventFilter : BaseUpgradeCancelledEventFilter {
    override var filter: String /* "name" */
    var name: String
}

external interface GhostTypeUpgradeCancelledEventFilter : BaseUpgradeCancelledEventFilter {
    override var filter: String /* "ghost_type" */
    var type: String
}

external interface GhostNameUpgradeCancelledEventFilter : BaseUpgradeCancelledEventFilter {
    override var filter: String /* "ghost_name" */
    var name: String
}

external interface OtherUpgradeCancelledEventFilter : BaseUpgradeCancelledEventFilter {
    override var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" */
}

external interface BasePlayerRepairedEntityEventFilter {
    var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" | "type" | "name" | "ghost_type" | "ghost_name" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TypePlayerRepairedEntityEventFilter : BasePlayerRepairedEntityEventFilter {
    override var filter: String /* "type" */
    var type: String
}

external interface NamePlayerRepairedEntityEventFilter : BasePlayerRepairedEntityEventFilter {
    override var filter: String /* "name" */
    var name: String
}

external interface GhostTypePlayerRepairedEntityEventFilter : BasePlayerRepairedEntityEventFilter {
    override var filter: String /* "ghost_type" */
    var type: String
}

external interface GhostNamePlayerRepairedEntityEventFilter : BasePlayerRepairedEntityEventFilter {
    override var filter: String /* "ghost_name" */
    var name: String
}

external interface OtherPlayerRepairedEntityEventFilter : BasePlayerRepairedEntityEventFilter {
    override var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" */
}

external interface BaseEntityMarkedForUpgradeEventFilter {
    var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" | "type" | "name" | "ghost_type" | "ghost_name" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TypeEntityMarkedForUpgradeEventFilter : BaseEntityMarkedForUpgradeEventFilter {
    override var filter: String /* "type" */
    var type: String
}

external interface NameEntityMarkedForUpgradeEventFilter : BaseEntityMarkedForUpgradeEventFilter {
    override var filter: String /* "name" */
    var name: String
}

external interface GhostTypeEntityMarkedForUpgradeEventFilter : BaseEntityMarkedForUpgradeEventFilter {
    override var filter: String /* "ghost_type" */
    var type: String
}

external interface GhostNameEntityMarkedForUpgradeEventFilter : BaseEntityMarkedForUpgradeEventFilter {
    override var filter: String /* "ghost_name" */
    var name: String
}

external interface OtherEntityMarkedForUpgradeEventFilter : BaseEntityMarkedForUpgradeEventFilter {
    override var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" */
}

external interface BasePostEntityDiedEventFilter {
    var filter: String /* "type" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TypePostEntityDiedEventFilter : BasePostEntityDiedEventFilter {
    override var filter: String /* "type" */
    var type: String
}

typealias LuaPostEntityDiedEventFilter = TypePostEntityDiedEventFilter

external interface BasePreRobotMinedEntityEventFilter {
    var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" | "type" | "name" | "ghost_type" | "ghost_name" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TypePreRobotMinedEntityEventFilter : BasePreRobotMinedEntityEventFilter {
    override var filter: String /* "type" */
    var type: String
}

external interface NamePreRobotMinedEntityEventFilter : BasePreRobotMinedEntityEventFilter {
    override var filter: String /* "name" */
    var name: String
}

external interface GhostTypePreRobotMinedEntityEventFilter : BasePreRobotMinedEntityEventFilter {
    override var filter: String /* "ghost_type" */
    var type: String
}

external interface GhostNamePreRobotMinedEntityEventFilter : BasePreRobotMinedEntityEventFilter {
    override var filter: String /* "ghost_name" */
    var name: String
}

external interface OtherPreRobotMinedEntityEventFilter : BasePreRobotMinedEntityEventFilter {
    override var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" */
}

external interface BaseEntityClonedEventFilter {
    var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" | "type" | "name" | "ghost_type" | "ghost_name" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TypeEntityClonedEventFilter : BaseEntityClonedEventFilter {
    override var filter: String /* "type" */
    var type: String
}

external interface NameEntityClonedEventFilter : BaseEntityClonedEventFilter {
    override var filter: String /* "name" */
    var name: String
}

external interface GhostTypeEntityClonedEventFilter : BaseEntityClonedEventFilter {
    override var filter: String /* "ghost_type" */
    var type: String
}

external interface GhostNameEntityClonedEventFilter : BaseEntityClonedEventFilter {
    override var filter: String /* "ghost_name" */
    var name: String
}

external interface OtherEntityClonedEventFilter : BaseEntityClonedEventFilter {
    override var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" */
}

external interface BaseScriptRaisedBuiltEventFilter {
    var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" | "type" | "name" | "ghost_type" | "ghost_name" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TypeScriptRaisedBuiltEventFilter : BaseScriptRaisedBuiltEventFilter {
    override var filter: String /* "type" */
    var type: String
}

external interface NameScriptRaisedBuiltEventFilter : BaseScriptRaisedBuiltEventFilter {
    override var filter: String /* "name" */
    var name: String
}

external interface GhostTypeScriptRaisedBuiltEventFilter : BaseScriptRaisedBuiltEventFilter {
    override var filter: String /* "ghost_type" */
    var type: String
}

external interface GhostNameScriptRaisedBuiltEventFilter : BaseScriptRaisedBuiltEventFilter {
    override var filter: String /* "ghost_name" */
    var name: String
}

external interface OtherScriptRaisedBuiltEventFilter : BaseScriptRaisedBuiltEventFilter {
    override var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" */
}

external interface BaseRobotMinedEntityEventFilter {
    var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" | "type" | "name" | "ghost_type" | "ghost_name" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TypeRobotMinedEntityEventFilter : BaseRobotMinedEntityEventFilter {
    override var filter: String /* "type" */
    var type: String
}

external interface NameRobotMinedEntityEventFilter : BaseRobotMinedEntityEventFilter {
    override var filter: String /* "name" */
    var name: String
}

external interface GhostTypeRobotMinedEntityEventFilter : BaseRobotMinedEntityEventFilter {
    override var filter: String /* "ghost_type" */
    var type: String
}

external interface GhostNameRobotMinedEntityEventFilter : BaseRobotMinedEntityEventFilter {
    override var filter: String /* "ghost_name" */
    var name: String
}

external interface OtherRobotMinedEntityEventFilter : BaseRobotMinedEntityEventFilter {
    override var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" */
}

external interface BasePrePlayerMinedEntityEventFilter {
    var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" | "type" | "name" | "ghost_type" | "ghost_name" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TypePrePlayerMinedEntityEventFilter : BasePrePlayerMinedEntityEventFilter {
    override var filter: String /* "type" */
    var type: String
}

external interface NamePrePlayerMinedEntityEventFilter : BasePrePlayerMinedEntityEventFilter {
    override var filter: String /* "name" */
    var name: String
}

external interface GhostTypePrePlayerMinedEntityEventFilter : BasePrePlayerMinedEntityEventFilter {
    override var filter: String /* "ghost_type" */
    var type: String
}

external interface GhostNamePrePlayerMinedEntityEventFilter : BasePrePlayerMinedEntityEventFilter {
    override var filter: String /* "ghost_name" */
    var name: String
}

external interface OtherPrePlayerMinedEntityEventFilter : BasePrePlayerMinedEntityEventFilter {
    override var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" */
}

external interface BaseRobotBuiltEntityEventFilter {
    var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" | "type" | "name" | "ghost_type" | "ghost_name" | "force" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TypeRobotBuiltEntityEventFilter : BaseRobotBuiltEntityEventFilter {
    override var filter: String /* "type" */
    var type: String
}

external interface NameRobotBuiltEntityEventFilter : BaseRobotBuiltEntityEventFilter {
    override var filter: String /* "name" */
    var name: String
}

external interface GhostTypeRobotBuiltEntityEventFilter : BaseRobotBuiltEntityEventFilter {
    override var filter: String /* "ghost_type" */
    var type: String
}

external interface GhostNameRobotBuiltEntityEventFilter : BaseRobotBuiltEntityEventFilter {
    override var filter: String /* "ghost_name" */
    var name: String
}

external interface ForceRobotBuiltEntityEventFilter : BaseRobotBuiltEntityEventFilter {
    override var filter: String /* "force" */
    var force: String
}

external interface OtherRobotBuiltEntityEventFilter : BaseRobotBuiltEntityEventFilter {
    override var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" */
}

external interface BaseEntityDeconstructionCancelledEventFilter {
    var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" | "type" | "name" | "ghost_type" | "ghost_name" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TypeEntityDeconstructionCancelledEventFilter : BaseEntityDeconstructionCancelledEventFilter {
    override var filter: String /* "type" */
    var type: String
}

external interface NameEntityDeconstructionCancelledEventFilter : BaseEntityDeconstructionCancelledEventFilter {
    override var filter: String /* "name" */
    var name: String
}

external interface GhostTypeEntityDeconstructionCancelledEventFilter : BaseEntityDeconstructionCancelledEventFilter {
    override var filter: String /* "ghost_type" */
    var type: String
}

external interface GhostNameEntityDeconstructionCancelledEventFilter : BaseEntityDeconstructionCancelledEventFilter {
    override var filter: String /* "ghost_name" */
    var name: String
}

external interface OtherEntityDeconstructionCancelledEventFilter : BaseEntityDeconstructionCancelledEventFilter {
    override var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" */
}

external interface BasePlayerBuiltEntityEventFilter {
    var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" | "type" | "name" | "ghost_type" | "ghost_name" | "force" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TypePlayerBuiltEntityEventFilter : BasePlayerBuiltEntityEventFilter {
    override var filter: String /* "type" */
    var type: String
}

external interface NamePlayerBuiltEntityEventFilter : BasePlayerBuiltEntityEventFilter {
    override var filter: String /* "name" */
    var name: String
}

external interface GhostTypePlayerBuiltEntityEventFilter : BasePlayerBuiltEntityEventFilter {
    override var filter: String /* "ghost_type" */
    var type: String
}

external interface GhostNamePlayerBuiltEntityEventFilter : BasePlayerBuiltEntityEventFilter {
    override var filter: String /* "ghost_name" */
    var name: String
}

external interface ForcePlayerBuiltEntityEventFilter : BasePlayerBuiltEntityEventFilter {
    override var filter: String /* "force" */
    var force: String
}

external interface OtherPlayerBuiltEntityEventFilter : BasePlayerBuiltEntityEventFilter {
    override var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" */
}

external interface BasePlayerMinedEntityEventFilter {
    var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" | "type" | "name" | "ghost_type" | "ghost_name" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TypePlayerMinedEntityEventFilter : BasePlayerMinedEntityEventFilter {
    override var filter: String /* "type" */
    var type: String
}

external interface NamePlayerMinedEntityEventFilter : BasePlayerMinedEntityEventFilter {
    override var filter: String /* "name" */
    var name: String
}

external interface GhostTypePlayerMinedEntityEventFilter : BasePlayerMinedEntityEventFilter {
    override var filter: String /* "ghost_type" */
    var type: String
}

external interface GhostNamePlayerMinedEntityEventFilter : BasePlayerMinedEntityEventFilter {
    override var filter: String /* "ghost_name" */
    var name: String
}

external interface OtherPlayerMinedEntityEventFilter : BasePlayerMinedEntityEventFilter {
    override var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" */
}

external interface BaseEntityDamagedEventFilter {
    var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" | "type" | "name" | "ghost_type" | "ghost_name" | "original-damage-amount" | "final-damage-amount" | "damage-type" | "final-health" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TypeEntityDamagedEventFilter : BaseEntityDamagedEventFilter {
    override var filter: String /* "type" */
    var type: String
}

external interface NameEntityDamagedEventFilter : BaseEntityDamagedEventFilter {
    override var filter: String /* "name" */
    var name: String
}

external interface GhostTypeEntityDamagedEventFilter : BaseEntityDamagedEventFilter {
    override var filter: String /* "ghost_type" */
    var type: String
}

external interface GhostNameEntityDamagedEventFilter : BaseEntityDamagedEventFilter {
    override var filter: String /* "ghost_name" */
    var name: String
}

external interface OriginalDamageAmountEntityDamagedEventFilter : BaseEntityDamagedEventFilter {
    override var filter: String /* "original-damage-amount" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: float
}

external interface FinalDamageAmountEntityDamagedEventFilter : BaseEntityDamagedEventFilter {
    override var filter: String /* "final-damage-amount" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: float
}

external interface DamageTypeEntityDamagedEventFilter : BaseEntityDamagedEventFilter {
    override var filter: String /* "damage-type" */
    var type: String
}

external interface FinalHealthEntityDamagedEventFilter : BaseEntityDamagedEventFilter {
    override var filter: String /* "final-health" */
    var comparison: String /* "=" | ">" | "<" | "≥" | ">=" | "≤" | "<=" | "≠" | "!=" */
    var value: float
}

external interface OtherEntityDamagedEventFilter : BaseEntityDamagedEventFilter {
    override var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" */
}

external interface BaseSectorScannedEventFilter {
    var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" | "type" | "name" | "ghost_type" | "ghost_name" */
    var mode: String? /* "or" | "and" */
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TypeSectorScannedEventFilter : BaseSectorScannedEventFilter {
    override var filter: String /* "type" */
    var type: String
}

external interface NameSectorScannedEventFilter : BaseSectorScannedEventFilter {
    override var filter: String /* "name" */
    var name: String
}

external interface GhostTypeSectorScannedEventFilter : BaseSectorScannedEventFilter {
    override var filter: String /* "ghost_type" */
    var type: String
}

external interface GhostNameSectorScannedEventFilter : BaseSectorScannedEventFilter {
    override var filter: String /* "ghost_name" */
    var name: String
}

external interface OtherSectorScannedEventFilter : BaseSectorScannedEventFilter {
    override var filter: String /* "ghost" | "rail" | "rail-signal" | "rolling-stock" | "robot-with-logistics-interface" | "vehicle" | "turret" | "crafting-machine" | "wall-connectable" | "transport-belt-connectable" | "circuit-network-connectable" */
}

external interface `T$87` {
    var enabled: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var diffusion_ratio: double?
        get() = definedExternally
        set(value) = definedExternally
    var ageing: double?
        get() = definedExternally
        set(value) = definedExternally
    var enemy_attack_pollution_consumption_modifier: double?
        get() = definedExternally
        set(value) = definedExternally
    var min_pollution_to_damage_trees: double?
        get() = definedExternally
        set(value) = definedExternally
    var pollution_restored_per_tree_damage: double?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$88` {
    var enabled: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var time_factor: double?
        get() = definedExternally
        set(value) = definedExternally
    var destroy_factor: double?
        get() = definedExternally
        set(value) = definedExternally
    var pollution_factor: double?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$89` {
    var enabled: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var max_expansion_distance: double?
        get() = definedExternally
        set(value) = definedExternally
    var settler_group_min_size: double?
        get() = definedExternally
        set(value) = definedExternally
    var settler_group_max_size: double?
        get() = definedExternally
        set(value) = definedExternally
    var min_expansion_cooldown: double?
        get() = definedExternally
        set(value) = definedExternally
    var max_expansion_cooldown: double?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$90` {
    var recipe_difficulty: recipe_difficulty?
        get() = definedExternally
        set(value) = definedExternally
    var technology_difficulty: technology_difficulty?
        get() = definedExternally
        set(value) = definedExternally
    var technology_price_multiplier: double?
        get() = definedExternally
        set(value) = definedExternally
    var research_queue_setting: String? /* "after-victory" | "always" | "never" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$91` {
    var pollution: `T$87`?
        get() = definedExternally
        set(value) = definedExternally
    var enemy_evolution: `T$88`?
        get() = definedExternally
        set(value) = definedExternally
    var enemy_expansion: `T$89`?
        get() = definedExternally
        set(value) = definedExternally
    var difficulty_settings: `T$90`?
        get() = definedExternally
        set(value) = definedExternally
}

external interface MapGenPreset {
    var order: String
    var default: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var basic_settings: MapGenSettingsPartial
    var advanced_settings: `T$91`
}

external interface BlueprintControlBehavior {
    var condition: CircuitCondition?
        get() = definedExternally
        set(value) = definedExternally
    var circuit_condition: CircuitCondition?
        get() = definedExternally
        set(value) = definedExternally
    var filters: Array<Signal>?
        get() = definedExternally
        set(value) = definedExternally
    var is_on: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var arithmetic_conditions: ArithmeticCombinatorParameters?
        get() = definedExternally
        set(value) = definedExternally
    var decider_conditions: DeciderCombinatorParameters?
        get() = definedExternally
        set(value) = definedExternally
    var circuit_enable_disable: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var circuit_read_resources: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var circuit_resource_read_mode: resource_read_mode?
        get() = definedExternally
        set(value) = definedExternally
    var read_stopped_train: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var train_stopped_signal: SignalID?
        get() = definedExternally
        set(value) = definedExternally
    var read_from_train: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var send_to_train: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var circuit_mode_of_operation: Number?
        get() = definedExternally
        set(value) = definedExternally
    var circuit_read_hand_contents: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var circuit_hand_read_mode: hand_read_mode?
        get() = definedExternally
        set(value) = definedExternally
    var circuit_set_stack_size: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var stack_control_input_signal: SignalID?
        get() = definedExternally
        set(value) = definedExternally
    var use_colors: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var read_robot_stats: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var read_logistics: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var available_logistic_output_signal: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var total_logistic_output_signal: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var available_construction_output_signal: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var total_construction_output_signal: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var circuit_contents_read_mode: content_read_mode?
        get() = definedExternally
        set(value) = definedExternally
    var output_signal: SignalID?
        get() = definedExternally
        set(value) = definedExternally
    var circuit_close_signal: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var circuit_read_signal: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var red_output_signal: SignalID?
        get() = definedExternally
        set(value) = definedExternally
    var orange_output_signal: SignalID?
        get() = definedExternally
        set(value) = definedExternally
    var green_output_signal: SignalID?
        get() = definedExternally
        set(value) = definedExternally
    var blue_output_signal: SignalID?
        get() = definedExternally
        set(value) = definedExternally
    var circuit_open_gate: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var circuit_read_sensor: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var circuit_parameters: ProgrammableSpeakerCircuitParameters?
        get() = definedExternally
        set(value) = definedExternally
}