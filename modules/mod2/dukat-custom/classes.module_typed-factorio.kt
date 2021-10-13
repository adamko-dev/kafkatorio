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
import tsstdlib.Omit
import tsstdlib.Record
import defines.wire_type
import defines.circuit_connector_id
import tsstdlib.Position
import defines.inventory
import defines.direction
import defines.shooting
import defines.gui_type
import defines.control_behavior.type
import defines.rail_direction
import defines.rail_connection_direction
import defines.logistic_member_index
import defines.signal_state
import defines.chain_signal_state
import defines.entity_status
import defines.flow_precision_index
import defines.difficulty
import tsstdlib.Extract
import defines.control_behavior.inserter.circuit_mode_of_operation
import defines.control_behavior.inserter.hand_read_mode
import defines.deconstruction_item.entity_filter_mode
import defines.deconstruction_item.tile_filter_mode
import defines.deconstruction_item.tile_selection_mode
import defines.control_behavior.logistic_container.circuit_mode_of_operation as _defines_control_behavior_logistic_container_circuit_mode_of_operation
import defines.logistic_mode
import defines.control_behavior.mining_drill.resource_read_mode
import defines.input_action
import defines.controllers
import defines.alert_type
import defines.render_mode
import defines.rich_text_setting
import defines.build_check_type
import defines.chunk_generated_status
import defines.train_state
import defines.control_behavior.transport_belt.content_read_mode
import defines.group_state

external interface LuaAISettings {
    var allow_destroy_when_commands_fail: Boolean?
    var allow_try_return_to_spawner: Boolean?
    var do_separation: Boolean?
    var path_resolution_modifier: int8
    var valid: Boolean?
    var object_name: String /* "LuaAISettings" */
    fun help(): String
}

external interface LuaAccumulatorControlBehavior : LuaControlBehavior {
    var output_signal: SignalID
    var valid: Boolean?
    var object_name: String /* "LuaAccumulatorControlBehavior" */
    fun help(): String
}

external interface LuaAchievementPrototype {
    var name: String
    var order: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var allowed_without_fight: Boolean?
    var hidden: Boolean?
    var valid: Boolean?
    var object_name: String /* "LuaAchievementPrototype" */
    fun help(): String
}

external interface LuaAmmoCategoryPrototype {
    var name: String
    var order: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var bonus_gui_order: String
    var valid: Boolean?
    var object_name: String /* "LuaAmmoCategoryPrototype" */
    fun help(): String
}

external interface LuaArithmeticCombinatorControlBehavior : LuaCombinatorControlBehavior {
    var parameters: ArithmeticCombinatorParameters
    var valid: Boolean?
    var object_name: String /* "LuaArithmeticCombinatorControlBehavior" */
    fun help(): String
}

external interface LuaAutoplaceControlPrototype {
    var name: String
    var order: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var richness: Boolean?
    var control_order: String
    var category: String
    var valid: Boolean?
    var object_name: String /* "LuaAutoplaceControlPrototype" */
    fun help(): String
}

external interface `T$0` {
    var player_index: uint
    var message: String
}

external interface `T$1` {
    var item_stack: LuaItemStack
    var player_index: uint
    var recipe: LuaRecipe
}

external interface `T$2` {
    var player_index: uint
    var entity: LuaEntity
    var from_player: Boolean?
}

external interface `T$3` {
    var entity: LuaEntity
}

external interface `T$4` {
    var player_index: uint
    var market: LuaEntity
    var offer_index: uint
    var count: uint
}

external interface `T$5` {
    var entity: LuaEntity
    var tags: Tags?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$6` {
    var surface_index: uint
    var tiles: Array<Tile>
}

external interface `T$7` {
    var is_simulation: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var is_tutorial: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var campaign_name: String?
        get() = definedExternally
        set(value) = definedExternally
    var level_name: String
    var mod_name: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface LuaBootstrap {
    fun on_init(f: (() -> Unit)?)
    fun on_load(f: (() -> Unit)?)
    fun on_configuration_changed(f: ((param1: ConfigurationChangedData) -> Unit)?)
    fun <E : OnTickEvent> on_event(event: E, f: ((data: E) -> Unit)?, filters: Array<Any> = definedExternally)
    fun <E : OnTickEvent> on_event(event: E, f: ((data: E) -> Unit)?)
    fun <E : OnTickEvent> on_event(event: Array<E>, f: ((data: Any) -> Unit)?)
    fun on_event(event: String, f: ((data: CustomInputEvent) -> Unit)?)
    fun on_nth_tick(tick: uint?, f: ((param1: NthTickEventData) -> Unit)?)
    fun on_nth_tick(tick: Array<uint>?, f: ((param1: NthTickEventData) -> Unit)?)
    fun register_on_entity_destroyed(entity: LuaEntity): uint64
    fun generate_event_name(): uint /* uint & `T$93`<T, Unit> */
    fun <E : uint> get_event_handler(event: E): (data: Any) -> Unit
    fun get_event_handler(event: String): (data: CustomInputEvent) -> Unit
    fun get_event_order(): String
    fun <E : uint> set_event_filter(event: E, filters: Array<Any>?)
    fun <E : uint> get_event_filter(event: E): Array<Any>?
    fun <E> raise_event(event: E, data: Omit<Any, String /* "name" | "tick" | "mod_name" */>)
    fun raise_console_chat(params: `T$0`)
    fun raise_player_crafted_item(params: `T$1`)
    fun raise_player_fast_transferred(params: `T$2`)
    fun raise_biter_base_built(params: `T$3`)
    fun raise_market_item_purchased(params: `T$4`)
    fun raise_script_built(params: `T$3`)
    fun raise_script_destroy(params: `T$3`)
    fun raise_script_revive(params: `T$5`)
    fun raise_script_set_tiles(params: `T$6`)
    var mod_name: String
    var level: `T$7`
    var active_mods: Record<String, String>
    var object_name: String /* "LuaBootstrap" */
}

external interface LuaBurner {
    var owner: dynamic /* LuaEntity | LuaEquipment */
        get() = definedExternally
        set(value) = definedExternally
    var inventory: LuaInventory
    var burnt_result_inventory: LuaInventory
    var heat: double
    var heat_capacity: double
    var remaining_burning_fuel: double
    var currently_burning: LuaItemPrototype
    var fuel_categories: Record<String, Boolean>
    var valid: Boolean?
    var object_name: String /* "LuaBurner" */
    fun help(): String
}

external interface `T$8` {
    var minimum_intensity: float
    var maximum_intensity: float
    var derivation_change_frequency: float
    var derivation_change_deviation: float
    var border_fix_speed: float
    var minimum_light_size: float
    var light_intensity_to_size_coefficient: float
    var color: dynamic /* ColorTable | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
}

external interface LuaBurnerPrototype {
    var emissions: double
    var render_no_network_icon: Boolean?
    var render_no_power_icon: Boolean?
    var effectivity: double
    var fuel_inventory_size: uint
    var burnt_inventory_size: uint
    var smoke: Array<SmokeSource>
    var light_flicker: `T$8`
    var fuel_categories: Record<String, Boolean>
    var valid: Boolean?
    var object_name: String /* "LuaBurnerPrototype" */
    fun help(): String
}

external interface  LuaIterable<TValue, TState: Any?> :
    tsstdlib.Iterable<TValue>,
    LuaIterator<TValue, TState>
//    ,
//    LuaExtension<String>

external interface LuaChunkIterator : LuaIterable<ChunkPositionAndArea, Any?> {
    @nativeInvoke
    operator fun invoke(): ChunkPositionAndArea?
    var valid: Boolean?
    var object_name: String /* "LuaChunkIterator" */
    fun help(): String
}

external interface LuaCircuitNetwork {
    fun get_signal(signal: SignalID): int
    var entity: LuaEntity
    var wire_type: wire_type
    var circuit_connector_id: circuit_connector_id
    var signals: Array<Signal>?
    var network_id: uint
    var connected_circuit_count: uint
    var valid: Boolean?
    var object_name: String /* "LuaCircuitNetwork" */
    fun help(): String
}

external interface LuaCombinatorControlBehavior : LuaControlBehavior {
    fun get_signal_last_tick(signal: SignalID): int?
    var signals_last_tick: Array<Signal>
}

external interface LuaCommandProcessor {
    fun add_command(name: String, help: Any /* JsTuple<String, Any> */, _function: (param1: CustomCommandData) -> Unit)
    fun add_command(name: String, help: String, _function: (param1: CustomCommandData) -> Unit)
    fun add_command(name: String, help: Number, _function: (param1: CustomCommandData) -> Unit)
    fun remove_command(name: String): Boolean?
    var commands: Record<String, dynamic /* JsTuple<String, Any> | String | Number */>
    var game_commands: Record<String, dynamic /* JsTuple<String, Any> | String | Number */>
    var object_name: String /* "LuaCommandProcessor" */
}

external interface LuaConstantCombinatorControlBehavior : LuaControlBehavior {
    fun set_signal(index: uint, signal: Signal)
    fun get_signal(index: uint): Signal
    var parameters: Array<ConstantCombinatorParameters>
    var enabled: Boolean?
    var signals_count: uint
    var valid: Boolean?
    var object_name: String /* "LuaConstantCombinatorControlBehavior" */
    fun help(): String
}

external interface LuaContainerControlBehavior : LuaControlBehavior {
    var valid: Boolean?
    var object_name: String /* "LuaContainerControlBehavior" */
    fun help(): String
}

external interface BaseControlSetGuiArrow {
    var type: String /* "nowhere" | "goal" | "entity_info" | "active_window" | "entity" | "position" | "crafting_queue" | "item_stack" */
}

external interface EntityControlSetGuiArrow : BaseControlSetGuiArrow {
    override var type: String /* "entity" */
    var entity: LuaEntity
}

external interface PositionControlSetGuiArrow : BaseControlSetGuiArrow {
    override var type: String /* "position" */
    var position: Position
}

external interface CraftingQueueControlSetGuiArrow : BaseControlSetGuiArrow {
    override var type: String /* "crafting_queue" */
    var crafting_queueindex: uint
}

external interface ItemStackControlSetGuiArrow : BaseControlSetGuiArrow {
    override var type: String /* "item_stack" */
    var inventory_index: inventory
    var item_stack_index: uint
    var source: String /* "player" | "target" */
}

external interface OtherControlSetGuiArrow : BaseControlSetGuiArrow {
    override var type: String /* "nowhere" | "goal" | "entity_info" | "active_window" */
}

external interface `T$9` {
    var count: uint
    var recipe: dynamic /* String | LuaRecipe */
        get() = definedExternally
        set(value) = definedExternally
    var silent: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$10` {
    var index: uint
    var count: uint
}

external interface `T$11` {
    var walking: Boolean?
    var direction: direction
}

external interface `T$12` {
    var mining: Boolean?
    var position: Position?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$13` {
    var state: shooting
    var position: Position
}

external interface `T$14` {
    var repairing: Boolean?
    var position: Position
}

external interface LuaControl {
    fun get_inventory(inventory: inventory): LuaInventory?
    fun get_main_inventory(): LuaInventory?
    fun can_insert(items: String): Boolean?
    fun can_insert(items: ItemStackDefinition): Boolean?
    fun can_insert(items: LuaItemStack): Boolean?
    fun insert(items: String): uint
    fun insert(items: ItemStackDefinition): uint
    fun insert(items: LuaItemStack): uint
    fun set_gui_arrow(params: EntityControlSetGuiArrow)
    fun set_gui_arrow(params: PositionControlSetGuiArrow)
    fun set_gui_arrow(params: CraftingQueueControlSetGuiArrow)
    fun set_gui_arrow(params: ItemStackControlSetGuiArrow)
    fun set_gui_arrow(params: OtherControlSetGuiArrow)
    fun clear_gui_arrow()
    fun get_item_count(item: String = definedExternally): uint
    fun has_items_inside(): Boolean?
    fun can_reach_entity(entity: LuaEntity): Boolean?
    fun clear_items_inside()
    fun remove_item(items: String): uint
    fun remove_item(items: ItemStackDefinition): uint
    fun remove_item(items: LuaItemStack): uint
    fun teleport(position: Position, surface: uint = definedExternally): Boolean?
    fun teleport(position: Position): Boolean?
    fun teleport(position: Position, surface: String = definedExternally): Boolean?
    fun teleport(position: Position, surface: LuaSurface = definedExternally): Boolean?
    fun update_selected_entity(position: Position)
    fun clear_selected_entity()
    fun disable_flashlight()
    fun enable_flashlight()
    fun is_flashlight_enabled()
    fun get_craftable_count(recipe: String): uint
    fun get_craftable_count(recipe: LuaRecipe): uint
    fun begin_crafting(params: `T$9`): uint
    fun cancel_crafting(params: `T$10`)
    fun mine_entity(entity: LuaEntity, force: Boolean = definedExternally): Boolean?
    fun mine_tile(tile: LuaTile): Boolean?
    fun is_player(): Boolean?
    fun open_technology_gui(technology: String = definedExternally)
    fun open_technology_gui()
    fun open_technology_gui(technology: LuaTechnology = definedExternally)
    fun open_technology_gui(technology: LuaTechnologyPrototype = definedExternally)
    fun set_personal_logistic_slot(slot_index: uint, value: PersonalLogisticParameters): Boolean?
    fun set_vehicle_logistic_slot(slot_index: uint, value: PersonalLogisticParameters): Boolean?
    fun get_personal_logistic_slot(slot_index: uint): PersonalLogisticParameters
    fun get_vehicle_logistic_slot(slot_index: uint): PersonalLogisticParameters
    fun clear_personal_logistic_slot(slot_index: uint)
    fun clear_vehicle_logistic_slot(slot_index: uint)
    fun is_cursor_blueprint(): Boolean?
    fun get_blueprint_entities(): Array<BlueprintEntity>?
    fun is_cursor_empty(): Boolean?
    var surface: LuaSurface
    var position: PositionTable
    var vehicle: LuaEntity?
    var force: dynamic /* String | LuaForce */
        get() = definedExternally
        set(value) = definedExternally
    var selected: LuaEntity?
    var opened: dynamic /* LuaEntity? | LuaItemStack? | LuaEquipment? | LuaEquipmentGrid? | LuaPlayer? | ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers | defines.gui_type? */
        get() = definedExternally
        set(value) = definedExternally
    var crafting_queue_size: uint
    var crafting_queue_progress: double
    var walking_state: `T$11`
    var riding_state: RidingState
    var mining_state: `T$12`
    var shooting_state: `T$13`
    var picking_state: Boolean?
    var repair_state: `T$14`
    var cursor_stack: LuaItemStack?
    var cursor_ghost: dynamic /* LuaItemStack | LuaItemPrototype | String */
        get() = definedExternally
        set(value) = definedExternally
    var driving: Boolean?
    var crafting_queue: Array<CraftingQueueItem>
    var following_robots: Array<LuaEntity>
    var cheat_mode: Boolean?
    var character_crafting_speed_modifier: double
    var character_mining_speed_modifier: double
    var character_additional_mining_categories: Array<String>
    var character_running_speed_modifier: double
    var character_build_distance_bonus: uint
    var character_item_drop_distance_bonus: uint
    var character_reach_distance_bonus: uint
    var character_resource_reach_distance_bonus: uint
    var character_item_pickup_distance_bonus: uint
    var character_loot_pickup_distance_bonus: uint
    var character_inventory_slots_bonus: uint
    var character_trash_slot_count_bonus: uint
    var character_maximum_following_robot_count_bonus: uint
    var character_health_bonus: float
    var character_personal_logistic_requests_enabled: Boolean?
    var vehicle_logistic_requests_enabled: Boolean?
    var opened_gui_type: gui_type?
    var build_distance: uint
    var drop_item_distance: uint
    var reach_distance: uint
    var item_pickup_distance: double
    var loot_pickup_distance: double
    var resource_reach_distance: double
    var in_combat: Boolean?
    var character_running_speed: double
    var character_mining_progress: double
}

external interface LuaControlBehavior {
    fun get_circuit_network(wire: wire_type, circuit_connector: circuit_connector_id = definedExternally): LuaCircuitNetwork?
    var type: type
    var entity: LuaEntity
}

external interface LuaCustomChartTag {
    fun destroy()
    var icon: SignalID?
    var last_user: LuaPlayer
    var position: PositionTable
    var text: String
    var tag_number: uint
    var force: LuaForce
    var surface: LuaSurface
    var valid: Boolean?
    var object_name: String /* "LuaCustomChartTag" */
    fun help(): String
}

external interface LuaCustomInputPrototype {
    var name: String
    var order: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var key_sequence: String
    var alternative_key_sequence: String?
    var linked_game_control: String?
    var consuming: String /* "none" | "game-only" */
    var action: String
    var enabled: Boolean?
    var enabled_while_spectating: Boolean?
    var enabled_while_in_cutscene: Boolean?
    var include_selected_prototype: Boolean?
    var item_to_spawn: LuaItemPrototype?
    var valid: Boolean?
    var object_name: String /* "LuaCustomInputPrototype" */
    fun help(): String
}

typealias CustomTableIndex<K, V> = Any

external interface LuaCustomTableMembers {
    var length: () -> uint /* () -> uint & LuaExtension<String /* "__luaLengthMethodBrand" */> */
    var valid: Boolean?
    var object_name: String /* "LuaCustomTable" */
    fun help(): String
}

external interface LuaDamagePrototype {
    var name: String
    var order: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var hidden: Boolean?
    var valid: Boolean?
    var object_name: String /* "LuaDamagePrototype" */
    fun help(): String
}

external interface LuaDeciderCombinatorControlBehavior : LuaCombinatorControlBehavior {
    var parameters: DeciderCombinatorParameters
    var valid: Boolean?
    var object_name: String /* "LuaDeciderCombinatorControlBehavior" */
    fun help(): String
}

external interface LuaDecorativePrototype {
    var name: String
    var order: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var collision_box: BoundingBoxTable
    var collision_mask: CollisionMask
    var collision_mask_with_flags: CollisionMaskWithFlags
    var autoplace_specification: AutoplaceSpecification?
    var valid: Boolean?
    var object_name: String /* "LuaDecorativePrototype" */
    fun help(): String
}

external interface LuaElectricEnergySourcePrototype {
    var buffer_capacity: double
    var usage_priority: String
    var input_flow_limit: double
    var output_flow_limit: double
    var drain: double
    var emissions: double
    var render_no_network_icon: Boolean?
    var render_no_power_icon: Boolean?
    var valid: Boolean?
    var object_name: String /* "LuaElectricEnergySourcePrototype" */
    fun help(): String
}

external interface `T$15` {
    var do_cliff_correction: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var raise_destroy: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$16` {
    var force: dynamic /* String | LuaForce */
        get() = definedExternally
        set(value) = definedExternally
    var target: dynamic /* LuaEntity | LuaEntityPrototype | String */
        get() = definedExternally
        set(value) = definedExternally
    var player: dynamic /* uint? | String? | LuaPlayer? */
        get() = definedExternally
        set(value) = definedExternally
    var direction: direction?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$17` {
    var return_item_request_proxy: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var raise_revive: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$18` {
    var rail_direction: rail_direction
    var rail_connection_direction: rail_connection_direction
}

external interface `T$19` {
    var reverse: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var by_player: dynamic /* uint? | String? | LuaPlayer? */
        get() = definedExternally
        set(value) = definedExternally
    var spill_items: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var enable_looted: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var force: dynamic /* LuaForce? | String? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$20` {
    var position: Position
    var surface: LuaSurface?
        get() = definedExternally
        set(value) = definedExternally
    var force: dynamic /* String? | LuaForce? */
        get() = definedExternally
        set(value) = definedExternally
    var create_build_effect_smoke: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$21` {
    var name: String
    var amount: double
    var minimum_temperature: double?
        get() = definedExternally
        set(value) = definedExternally
    var maximum_temperature: double?
        get() = definedExternally
        set(value) = definedExternally
    var temperature: double?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$22` {
    var inventory: LuaInventory?
        get() = definedExternally
        set(value) = definedExternally
    var force: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var raise_destroyed: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var ignore_minable: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$23` {
    var red: Array<LuaEntity>
    var green: Array<LuaEntity>
}

external interface LuaEntity : LuaControl {
    fun get_output_inventory(): LuaInventory
    fun get_module_inventory(): LuaInventory?
    fun get_fuel_inventory(): LuaInventory?
    fun get_burnt_result_inventory(): LuaInventory?
    fun damage(damage: float, force: String, type: String = definedExternally, dealer: LuaEntity = definedExternally): float
    fun damage(damage: float, force: String): float
    fun damage(damage: float, force: String, type: String = definedExternally): float
    fun damage(damage: float, force: LuaForce, type: String = definedExternally, dealer: LuaEntity = definedExternally): float
    fun damage(damage: float, force: LuaForce): float
    fun damage(damage: float, force: LuaForce, type: String = definedExternally): float
    fun can_be_destroyed(): Boolean?
    fun destroy(params: `T$15` = definedExternally): Boolean?
    fun set_command(command: AttackCommand)
    fun set_command(command: GoToLocationCommand)
    fun set_command(command: CompoundCommand)
    fun set_command(command: GroupCommand)
    fun set_command(command: AttackAreaCommand)
    fun set_command(command: WanderCommand)
    fun set_command(command: StopCommand)
    fun set_command(command: FleeCommand)
    fun set_command(command: BuildBaseCommand)
    fun set_distraction_command(command: AttackCommand)
    fun set_distraction_command(command: GoToLocationCommand)
    fun set_distraction_command(command: CompoundCommand)
    fun set_distraction_command(command: GroupCommand)
    fun set_distraction_command(command: AttackAreaCommand)
    fun set_distraction_command(command: WanderCommand)
    fun set_distraction_command(command: StopCommand)
    fun set_distraction_command(command: FleeCommand)
    fun set_distraction_command(command: BuildBaseCommand)
    fun has_command(): Boolean?
    fun die(force: String = definedExternally, cause: LuaEntity = definedExternally): Boolean?
    fun die(): Boolean?
    fun die(force: String = definedExternally): Boolean?
    fun die(force: LuaForce = definedExternally, cause: LuaEntity = definedExternally): Boolean?
    fun die(force: LuaForce = definedExternally): Boolean?
    fun has_flag(flag: String): Boolean?
    fun ghost_has_flag(flag: String): Boolean?
    fun add_market_item(offer: Offer)
    fun remove_market_item(offer: uint): Boolean?
    fun get_market_items(): Array<Offer>
    fun clear_market_items()
    fun connect_neighbour(target: LuaEntity): Boolean?
    fun connect_neighbour(target: WireConnectionDefinition): Boolean?
    fun disconnect_neighbour(target: wire_type = definedExternally)
    fun disconnect_neighbour()
    fun disconnect_neighbour(target: LuaEntity = definedExternally)
    fun disconnect_neighbour(target: WireConnectionDefinition = definedExternally)
    fun order_deconstruction(force: String, player: uint = definedExternally): Boolean?
    fun order_deconstruction(force: String): Boolean?
    fun order_deconstruction(force: String, player: String = definedExternally): Boolean?
    fun order_deconstruction(force: String, player: LuaPlayer = definedExternally): Boolean?
    fun order_deconstruction(force: LuaForce, player: uint = definedExternally): Boolean?
    fun order_deconstruction(force: LuaForce): Boolean?
    fun order_deconstruction(force: LuaForce, player: String = definedExternally): Boolean?
    fun order_deconstruction(force: LuaForce, player: LuaPlayer = definedExternally): Boolean?
    fun cancel_deconstruction(force: String, player: uint = definedExternally)
    fun cancel_deconstruction(force: String)
    fun cancel_deconstruction(force: String, player: String = definedExternally)
    fun cancel_deconstruction(force: String, player: LuaPlayer = definedExternally)
    fun cancel_deconstruction(force: LuaForce, player: uint = definedExternally)
    fun cancel_deconstruction(force: LuaForce)
    fun cancel_deconstruction(force: LuaForce, player: String = definedExternally)
    fun cancel_deconstruction(force: LuaForce, player: LuaPlayer = definedExternally)
    fun to_be_deconstructed(): Boolean?
    fun order_upgrade(params: `T$16`): Boolean?
    fun cancel_upgrade(force: String, player: uint = definedExternally): Boolean?
    fun cancel_upgrade(force: String): Boolean?
    fun cancel_upgrade(force: String, player: String = definedExternally): Boolean?
    fun cancel_upgrade(force: String, player: LuaPlayer = definedExternally): Boolean?
    fun cancel_upgrade(force: LuaForce, player: uint = definedExternally): Boolean?
    fun cancel_upgrade(force: LuaForce): Boolean?
    fun cancel_upgrade(force: LuaForce, player: String = definedExternally): Boolean?
    fun cancel_upgrade(force: LuaForce, player: LuaPlayer = definedExternally): Boolean?
    fun to_be_upgraded(): Boolean?
    fun get_request_slot(slot: uint): dynamic /* String? | ItemStackDefinition? */
    fun set_request_slot(request: String, slot: uint): Boolean?
    fun set_request_slot(request: ItemStackDefinition, slot: uint): Boolean?
    fun set_request_slot(request: LuaItemStack, slot: uint): Boolean?
    fun clear_request_slot(slot: uint)
    fun is_crafting()
    fun is_opened(): Boolean?
    fun is_opening(): Boolean?
    fun is_closed(): Boolean?
    fun is_closing(): Boolean?
    fun request_to_open(force: String, extra_time: uint = definedExternally)
    fun request_to_open(force: String)
    fun request_to_open(force: LuaForce, extra_time: uint = definedExternally)
    fun request_to_open(force: LuaForce)
    fun request_to_close(force: String)
    fun request_to_close(force: LuaForce)
    fun get_transport_line(index: uint): LuaTransportLine
    fun get_max_transport_line_index(): uint
    fun launch_rocket(): Boolean?
    fun revive(params: `T$17` = definedExternally): dynamic /* JsTuple<Nothing?> | JsTuple<Record<String, uint>, LuaEntity, LuaEntity?> */
    fun silent_revive(params: `T$17` = definedExternally): dynamic /* JsTuple<Nothing?> | JsTuple<Record<String, uint>, LuaEntity, LuaEntity?> */
    fun get_connected_rail(params: `T$18`): LuaEntity
    fun get_connected_rails(): Array<LuaEntity>
    fun get_rail_segment_entity(direction: rail_direction, in_else_out: Boolean): LuaEntity?
    fun get_rail_segment_end(direction: rail_direction): dynamic /* JsTuple<LuaEntity, defines.rail_direction> */
    fun get_rail_segment_length(): double
    fun get_rail_segment_overlaps(): Array<LuaEntity>
    fun get_filter(slot_index: uint): String?
    fun set_filter(slot_index: uint, item: String)
    fun get_infinity_container_filter(index: uint): InfinityInventoryFilter?
    fun set_infinity_container_filter(index: uint, filter: InfinityInventoryFilter?)
    fun get_infinity_pipe_filter(): InfinityPipeFilter?
    fun set_infinity_pipe_filter(filter: InfinityPipeFilter?)
    fun get_heat_setting(): HeatSetting
    fun set_heat_setting(filter: HeatSetting)
    fun get_control_behavior(): LuaControlBehavior?
    fun get_or_create_control_behavior(): LuaControlBehavior?
    fun get_circuit_network(wire: wire_type, circuit_connector: circuit_connector_id = definedExternally): LuaCircuitNetwork?
    fun get_merged_signal(signal: SignalID, circuit_connector: circuit_connector_id = definedExternally): int
    fun get_merged_signals(circuit_connector: circuit_connector_id = definedExternally): Array<Signal>?
    fun supports_backer_name(): Boolean?
    fun copy_settings(entity: LuaEntity, by_player: uint = definedExternally): Record<String, uint>
    fun copy_settings(entity: LuaEntity): Record<String, uint>
    fun copy_settings(entity: LuaEntity, by_player: String = definedExternally): Record<String, uint>
    fun copy_settings(entity: LuaEntity, by_player: LuaPlayer = definedExternally): Record<String, uint>
    fun get_logistic_point(index: logistic_member_index = definedExternally): dynamic /* LuaLogisticPoint | Array<LuaLogisticPoint> */
    fun play_note(instrument: uint, note: uint): Boolean?
    fun connect_rolling_stock(direction: rail_direction): Boolean?
    fun disconnect_rolling_stock(direction: rail_direction): Boolean?
    fun update_connections()
    fun get_recipe(): LuaRecipe?
    fun set_recipe(recipe: String?): Record<String, uint>
    fun set_recipe(recipe: LuaRecipe?): Record<String, uint>
    fun rotate(params: `T$19` = definedExternally): Boolean?
    fun get_driver(): dynamic /* LuaEntity? | LuaPlayer? */
    fun set_driver(driver: LuaEntity?)
    fun set_driver(driver: uint?)
    fun set_driver(driver: String?)
    fun set_driver(driver: LuaPlayer?)
    fun get_passenger(): dynamic /* LuaEntity? | LuaPlayer? */
    fun set_passenger(passenger: LuaEntity)
    fun set_passenger(passenger: uint)
    fun set_passenger(passenger: String)
    fun set_passenger(passenger: LuaPlayer)
    fun is_connected_to_electric_network(): Boolean?
    fun get_train_stop_trains(): Array<LuaTrain>
    fun get_stopped_train(): LuaTrain?
    fun clone(params: `T$20`): LuaEntity?
    fun get_fluid_count(fluid: String = definedExternally): double
    fun get_fluid_contents(): Record<String, double>
    fun remove_fluid(params: `T$21`): double
    fun insert_fluid(fluid: Fluid): double
    fun clear_fluid_inside()
    fun get_beam_source(): BeamTarget
    fun set_beam_source(source: LuaEntity)
    fun set_beam_source(source: Position)
    fun get_beam_target(): BeamTarget
    fun set_beam_target(target: LuaEntity)
    fun set_beam_target(target: Position)
    fun get_radius(): double
    fun get_health_ratio(): float
    fun create_build_effect_smoke()
    fun release_from_spawner()
    fun toggle_equipment_movement_bonus()
    fun can_shoot(target: LuaEntity, position: Position): Boolean?
    fun start_fading_out()
    fun get_upgrade_target(): LuaEntityPrototype?
    fun get_upgrade_direction(): direction?
    fun get_damage_to_be_taken(): float
    fun deplete()
    fun mine(params: `T$22` = definedExternally): Boolean?
    fun spawn_decorations()
    fun can_wires_reach(entity: LuaEntity): Boolean?
    fun get_connected_rolling_stock(direction: rail_direction): LuaEntity
    fun is_registered_for_construction(): Boolean?
    fun is_registered_for_deconstruction(force: String): Boolean?
    fun is_registered_for_deconstruction(force: LuaForce): Boolean?
    fun is_registered_for_upgrade(): Boolean?
    fun is_registered_for_repair(): Boolean?
    fun add_autopilot_destination(position: Position)
    fun connect_linked_belts(neighbour: LuaEntity?)
    fun disconnect_linked_belts()
    var name: String /* "inserter" | "filter-inserter" */
    var ghost_name: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var ghost_localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var ghost_localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var type: String
    var ghost_type: String
    var active: Boolean?
    var destructible: Boolean?
    var minable: Boolean?
    var rotatable: Boolean?
    var operable: Boolean?
    var health: float?
    var direction: direction
    var supports_direction: Boolean?
    var orientation: RealOrientation
    var cliff_orientation: String /* "west-to-east" | "north-to-south" | "east-to-west" | "south-to-north" | "west-to-north" | "north-to-east" | "east-to-south" | "south-to-west" | "west-to-south" | "north-to-west" | "east-to-north" | "south-to-east" | "west-to-none" | "none-to-east" | "east-to-none" | "none-to-west" | "north-to-none" | "none-to-south" | "south-to-none" | "none-to-north" */
    var relative_turret_orientation: RealOrientation?
    var torso_orientation: RealOrientation
    var amount: uint
    var initial_amount: uint?
    var effectivity_modifier: float
    var consumption_modifier: float
    var friction_modifier: float
    var driver_is_gunner: Boolean?
    var vehicle_automatic_targeting_parameters: VehicleAutomaticTargetingParameters
    var speed: float
    var effective_speed: float
    var stack: LuaItemStack
    var prototype: LuaEntityPrototype
    var ghost_prototype: dynamic /* LuaEntityPrototype | LuaTilePrototype */
        get() = definedExternally
        set(value) = definedExternally
    var drop_position: Position
    var pickup_position: Position
    var drop_target: LuaEntity?
    var pickup_target: LuaEntity
    var selected_gun_index: uint?
    var energy: double
    var temperature: double?
    var previous_recipe: LuaRecipe?
    var held_stack: LuaItemStack
    var held_stack_position: PositionTable
    var train: LuaTrain?
    var neighbours: dynamic /* Record<String, Array<LuaEntity>>? | Array<Array<LuaEntity>>? | LuaEntity? */
        get() = definedExternally
        set(value) = definedExternally
    var belt_neighbours: Record<String, Array<LuaEntity>>
    var fluidbox: LuaFluidBox
    var backer_name: String?
    var entity_label: String?
    var time_to_live: uint
    var color: dynamic /* ColorTable? | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var text: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var signal_state: signal_state
    var chain_signal_state: chain_signal_state
    var to_be_looted: Boolean?
    var crafting_speed: double
    var crafting_progress: float
    var bonus_progress: double
    var productivity_bonus: double
    var pollution_bonus: double
    var speed_bonus: double
    var consumption_bonus: double
    var belt_to_ground_type: String /* "input" | "output" */
    var loader_type: String /* "input" | "output" */
    var rocket_parts: uint
    var logistic_network: LuaLogisticNetwork
    var logistic_cell: LuaLogisticCell?
    var item_requests: Record<String, uint>
    var player: LuaPlayer?
    var unit_group: LuaUnitGroup?
    var damage_dealt: double
    var kills: uint
    var last_user: LuaPlayer?
    var electric_buffer_size: double?
    var electric_input_flow_limit: double?
    var electric_output_flow_limit: double?
    var electric_drain: double?
    var electric_emissions: double?
    var unit_number: uint?
    var ghost_unit_number: uint?
    var mining_progress: double?
    var bonus_mining_progress: double?
    var power_production: double
    var power_usage: double
    var bounding_box: BoundingBoxTable
    var secondary_bounding_box: BoundingBoxTable?
    var selection_box: BoundingBoxTable
    var secondary_selection_box: BoundingBoxTable?
    var mining_target: LuaEntity?
    var circuit_connected_entities: `T$23`
    var circuit_connection_definitions: Array<CircuitConnectionDefinition>
    var request_slot_count: uint
    var filter_slot_count: uint
    var loader_container: LuaEntity
    var grid: LuaEquipmentGrid?
    var graphics_variation: uint8?
    var tree_color_index: uint8
    var tree_color_index_max: uint8
    var tree_stage_index: uint8
    var tree_stage_index_max: uint8
    var tree_gray_stage_index: uint8
    var tree_gray_stage_index_max: uint8
    var burner: LuaBurner?
    var shooting_target: LuaEntity?
    var proxy_target: LuaEntity?
    var stickers: Array<LuaEntity>?
    var sticked_to: LuaEntity
    var parameters: ProgrammableSpeakerParameters
    var alert_parameters: ProgrammableSpeakerAlertParameters
    var electric_network_statistics: LuaFlowStatistics
    var inserter_stack_size_override: uint
    var products_finished: uint
    var spawner: LuaEntity?
    var units: Array<LuaEntity>
    var power_switch_state: Boolean?
    var effects: ModuleEffects?
    var infinity_container_filters: Array<InfinityInventoryFilter>
    var remove_unfiltered_items: Boolean?
    var character_corpse_player_index: uint
    var character_corpse_tick_of_death: uint
    var character_corpse_death_cause: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var associated_player: LuaPlayer?
    var tick_of_last_attack: uint
    var tick_of_last_damage: uint
    var splitter_filter: LuaItemPrototype?
    var inserter_filter_mode: String /* "whitelist" | "blacklist" */
    var splitter_input_priority: String /* "left" | "none" | "right" */
    var splitter_output_priority: String /* "left" | "none" | "right" */
    var armed: Boolean?
    var recipe_locked: Boolean?
    var connected_rail: LuaEntity?
    var trains_in_block: uint
    var timeout: uint
    var neighbour_bonus: double
    var ai_settings: LuaAISettings
    var highlight_box_type: String
    var highlight_box_blink_interval: uint
    var status: entity_status?
    var enable_logistics_while_moving: Boolean?
    var render_player: LuaPlayer?
    var render_to_forces: Array<dynamic /* String | LuaForce */>?
    var pump_rail_target: LuaEntity?
    var moving: Boolean?
    var electric_network_id: uint?
    var allow_dispatching_robots: Boolean?
    var auto_launch: Boolean?
    var energy_generated_last_tick: double
    var storage_filter: LuaItemPrototype
    var request_from_buffers: Boolean?
    var corpse_expires: Boolean?
    var corpse_immune_to_entity_placement: Boolean?
    var tags: Tags?
    var command: dynamic /* AttackCommand? | GoToLocationCommand? | CompoundCommand? | GroupCommand? | AttackAreaCommand? | WanderCommand? | StopCommand? | FleeCommand? | BuildBaseCommand? */
        get() = definedExternally
        set(value) = definedExternally
    var distraction_command: dynamic /* AttackCommand? | GoToLocationCommand? | CompoundCommand? | GroupCommand? | AttackAreaCommand? | WanderCommand? | StopCommand? | FleeCommand? | BuildBaseCommand? */
        get() = definedExternally
        set(value) = definedExternally
    var time_to_next_effect: uint
    var autopilot_destination: Position?
    var autopilot_destinations: Array<PositionTable>
    var trains_count: uint?
    var trains_limit: uint
    var is_entity_with_force: Boolean?
    var is_entity_with_owner: Boolean?
    var is_entity_with_health: Boolean?
    var combat_robot_owner: LuaEntity
    var link_id: uint
    var follow_target: LuaEntity
    var follow_offset: Position?
    var linked_belt_type: String /* "input" | "output" */
    var linked_belt_neighbour: LuaEntity?
    var valid: Boolean?
    var object_name: String /* "LuaEntity" */
    fun help(): String
}

external interface `T$24` {
    var minable: Boolean?
    var mining_time: double
    var mining_particle: String?
        get() = definedExternally
        set(value) = definedExternally
    var products: Array<dynamic /* FluidProduct | OtherProduct */>?
        get() = definedExternally
        set(value) = definedExternally
    var fluid_amount: double?
        get() = definedExternally
        set(value) = definedExternally
    var required_fluid: String?
        get() = definedExternally
        set(value) = definedExternally
    var mining_trigger: Array<TriggerItem>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$25` {
    var min: double
    var max: double
}

external interface `T$26` {
    var smoke_name: String
    var offsets: Array<dynamic /* typealias Vector = dynamic */>
    var offset_deviation: dynamic /* BoundingBoxTable | JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */
        get() = definedExternally
        set(value) = definedExternally
    var initial_height: float
    var max_radius: float?
        get() = definedExternally
        set(value) = definedExternally
    var speed: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var speed_multiplier: float
    var speed_multiplier_deviation: float
    var starting_frame: float
    var starting_frame_deviation: float
    var starting_frame_speed: float
    var starting_frame_speed_deviation: float
    var speed_from_center: float
    var speed_from_center_deviation: float
}

external interface LuaEntityPrototype {
    fun has_flag(flag: String /* "not-rotatable" | "placeable-neutral" | "placeable-player" | "placeable-enemy" | "placeable-off-grid" | "player-creation" | "filter-directions" | "fast-replaceable-no-build-while-moving" | "breaths-air" | "not-repairable" | "not-on-map" | "not-deconstructable" | "not-blueprintable" | "hide-from-bonus-gui" | "hide-alt-info" | "fast-replaceable-no-cross-type-while-moving" | "no-gap-fill-while-building" | "not-flammable" | "no-automated-item-removal" | "no-automated-item-insertion" | "not-upgradable" */): Boolean?
    fun get_inventory_size(index: inventory): uint?
    var type: String
    var name: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var max_health: float
    var infinite_resource: Boolean?
    var minimum_resource_amount: uint?
    var normal_resource_amount: uint?
    var infinite_depletion_resource_amount: uint?
    var resource_category: String?
    var mineable_properties: `T$24`
    var items_to_place_this: Array<dynamic /* String | ItemStackDefinition */>?
    var collision_box: BoundingBoxTable
    var secondary_collision_box: BoundingBoxTable?
    var map_generator_bounding_box: BoundingBoxTable
    var selection_box: BoundingBoxTable
    var drawing_box: BoundingBoxTable
    var sticker_box: BoundingBoxTable
    var collision_mask: CollisionMask
    var collision_mask_with_flags: CollisionMaskWithFlags
    var default_collision_mask_with_flags: CollisionMaskWithFlags
    var order: String
    var group: LuaGroup
    var subgroup: LuaGroup
    var healing_per_tick: float
    var emissions_per_second: double
    var corpses: Record<String, LuaEntityPrototype>
    var selectable_in_game: Boolean?
    var selection_priority: uint
    var weight: double?
    var resistances: Record<String, Resistance>
    var fast_replaceable_group: String?
    var next_upgrade: LuaEntityPrototype?
    var loot: Array<Loot>?
    var repair_speed_modifier: uint?
    var turret_range: uint?
    var autoplace_specification: AutoplaceSpecification?
    var belt_speed: double?
    var result_units: Array<UnitSpawnDefinition>
    var attack_result: Array<TriggerItem>?
    var final_attack_result: Array<TriggerItem>?
    var attack_parameters: dynamic /* ProjectileAttackParameters? | StreamAttackParameters? | OtherAttackParameters? */
        get() = definedExternally
        set(value) = definedExternally
    var spawn_cooldown: `T$25`?
    var mining_drill_radius: double?
    var mining_speed: double?
    var logistic_mode: String /* "requester" | "active-provider" | "passive-provider" | "buffer" | "storage" | "none" */
    var max_underground_distance: uint8?
    var flags: EntityPrototypeFlags
    var remains_when_mined: Array<LuaEntityPrototype>
    var additional_pastable_entities: Array<LuaEntityPrototype>
    var allow_copy_paste: Boolean?
    var shooting_cursor_size: double
    var created_smoke: `T$26`?
    var created_effect: Array<TriggerItem>?
    var map_color: ColorTable?
    var friendly_map_color: ColorTable
    var enemy_map_color: ColorTable
    var build_base_evolution_requirement: double
    var instruments: Array<ProgrammableSpeakerInstrument>?
    var max_polyphony: uint?
    var module_inventory_size: uint?
    var ingredient_count: uint?
    var crafting_speed: double?
    var crafting_categories: Record<String, Boolean>
    var resource_categories: Record<String, Boolean>?
    var supply_area_distance: double?
    var max_wire_distance: double
    var max_circuit_wire_distance: double
    var energy_usage: double?
    var max_energy_usage: double
    var max_energy_production: double
    var effectivity: double?
    var consumption: double?
    var friction_force: double?
    var braking_force: double?
    var air_resistance: double?
    var tank_driving: Boolean?
    var rotation_speed: double?
    var turret_rotation_speed: double?
    var guns: Record<String, LuaItemPrototype>?
    var speed: double?
    var speed_multiplier_when_out_of_energy: float?
    var max_payload_size: uint?
    var draw_cargo: Boolean?
    var energy_per_move: double?
    var energy_per_tick: double?
    var max_energy: double?
    var min_to_charge: float?
    var max_to_charge: float?
    var burner_prototype: LuaBurnerPrototype?
    var electric_energy_source_prototype: LuaElectricEnergySourcePrototype?
    var heat_energy_source_prototype: LuaHeatEnergySourcePrototype?
    var fluid_energy_source_prototype: LuaFluidEnergySourcePrototype?
    var void_energy_source_prototype: LuaVoidEnergySourcePrototype?
    var building_grid_bit_shift: uint
    var fluid_usage_per_tick: double?
    var maximum_temperature: double?
    var target_temperature: double?
    var fluid: LuaFluidPrototype?
    var fluid_capacity: double
    var pumping_speed: double?
    var stack: Boolean?
    var allow_custom_vectors: Boolean?
    var allow_burner_leech: Boolean?
    var inserter_extension_speed: double?
    var inserter_rotation_speed: double?
    var inserter_pickup_position: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var inserter_drop_position: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var inserter_chases_belt_items: Boolean?
    var count_as_rock_for_filtered_deconstruction: Boolean?
    var filter_count: uint?
    var time_to_live: uint
    var distribution_effectivity: double?
    var explosion_beam: double?
    var explosion_rotate: double?
    var tree_color_count: uint8?
    var alert_when_damaged: Boolean?
    var alert_when_attacking: Boolean?
    var color: ColorTable?
    var collision_mask_collides_with_self: Boolean?
    var collision_mask_collides_with_tiles_only: Boolean?
    var collision_mask_considers_tile_transitions: Boolean?
    var allowed_effects: Record<String, Boolean>?
    var rocket_parts_required: uint?
    var rocket_rising_delay: uint8?
    var launch_wait_time: uint8?
    var light_blinking_speed: double?
    var door_opening_speed: double?
    var rising_speed: double?
    var engine_starting_speed: double?
    var flying_speed: double?
    var flying_acceleration: double?
    var fixed_recipe: String?
    var construction_radius: double?
    var logistic_radius: double?
    var energy_per_hit_point: double?
    var create_ghost_on_death: Boolean?
    var timeout: uint
    var fluidbox_prototypes: Array<LuaFluidBoxPrototype>
    var neighbour_bonus: double
    var neighbour_collision_increase: double
    var container_distance: double
    var belt_distance: double
    var belt_length: double
    var is_building: Boolean?
    var automated_ammo_count: uint?
    var max_speed: double?
    var darkness_for_all_lamps_on: float?
    var darkness_for_all_lamps_off: float?
    var always_on: Boolean?
    var min_darkness_to_spawn: float
    var max_darkness_to_spawn: float
    var call_for_help_radius: double
    var max_count_of_owned_units: double
    var max_friends_around_to_spawn: double
    var spawning_radius: double
    var spawning_spacing: double
    var radius: double
    var cliff_explosive_prototype: String?
    var rocket_entity_prototype: LuaEntityPrototype?
    var has_belt_immunity: Boolean?
    var vision_distance: double?
    var pollution_to_join_attack: float?
    var min_pursue_time: uint?
    var max_pursue_distance: double?
    var radar_range: uint?
    var move_while_shooting: Boolean?
    var can_open_gates: Boolean?
    var affected_by_tiles: Boolean?
    var distraction_cooldown: uint?
    var spawning_time_modifier: double?
    var alert_icon_shift: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var lab_inputs: Array<String>?
    var researching_speed: double?
    var item_slot_count: uint?
    var base_productivity: double?
    var allow_access_to_all_forces: Boolean?
    var supports_direction: Boolean?
    var terrain_friction_modifier: float
    var allow_passengers: Boolean?
    var max_distance_of_sector_revealed: uint
    var max_distance_of_nearby_sector_revealed: uint
    var adjacent_tile_collision_box: BoundingBoxTable
    var adjacent_tile_collision_mask: CollisionMask
    var adjacent_tile_collision_test: CollisionMask
    var center_collision_mask: CollisionMask
    var grid_prototype: LuaEquipmentGridPrototype?
    var remove_decoratives: String
    var related_underground_belt: LuaEntityPrototype
    var running_speed: double
    var maximum_corner_sliding_distance: double
    var build_distance: uint
    var drop_item_distance: uint
    var reach_distance: uint
    var reach_resource_distance: double
    var item_pickup_distance: double
    var loot_pickup_distance: double
    var enter_vehicle_distance: double
    var ticks_to_keep_gun: uint
    var ticks_to_keep_aiming_direction: uint
    var ticks_to_stay_in_combat: uint
    var respawn_time: uint
    var damage_hit_tint: ColorTable
    var character_corpse: LuaEntityPrototype
    var valid: Boolean?
    var object_name: String /* "LuaEntityPrototype" */
    fun help(): String
}

external interface `T$27` {
    var width: uint
    var height: uint
}

external interface LuaEquipment {
    var name: String
    var type: String
    var position: PositionTable
    var shape: `T$27`
    var shield: double
    var max_shield: double
    var max_solar_power: double
    var movement_bonus: double
    var generator_power: double
    var energy: double
    var max_energy: double
    var prototype: LuaEquipmentPrototype
    var burner: LuaBurner?
    var valid: Boolean?
    var object_name: String /* "LuaEquipment" */
    fun help(): String
}

external interface LuaEquipmentCategoryPrototype {
    var name: String
    var order: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var valid: Boolean?
    var object_name: String /* "LuaEquipmentCategoryPrototype" */
    fun help(): String
}

external interface `T$28` {
    var position: Position?
        get() = definedExternally
        set(value) = definedExternally
    var equipment: LuaEquipment?
        get() = definedExternally
        set(value) = definedExternally
    var by_player: dynamic /* uint? | String? | LuaPlayer? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$29` {
    var name: String
    var position: Position?
        get() = definedExternally
        set(value) = definedExternally
    var by_player: dynamic /* uint? | String? | LuaPlayer? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$30` {
    var equipment: LuaEquipment
    var position: Position
}

external interface LuaEquipmentGrid {
    fun take(params: `T$28`): dynamic /* String? | ItemStackDefinition? */
    fun take_all(by_player: uint = definedExternally): Record<String, uint>
    fun take_all(): Record<String, uint>
    fun take_all(by_player: String = definedExternally): Record<String, uint>
    fun take_all(by_player: LuaPlayer = definedExternally): Record<String, uint>
    fun clear(by_player: uint = definedExternally)
    fun clear()
    fun clear(by_player: String = definedExternally)
    fun clear(by_player: LuaPlayer = definedExternally)
    fun put(params: `T$29`): LuaEquipment?
    fun can_move(params: `T$30`): Boolean?
    fun move(params: `T$30`): Boolean?
    fun get(position: Position): LuaEquipment?
    fun get_contents(): Record<String, uint>
    var prototype: LuaEquipmentGridPrototype
    var width: uint
    var height: uint
    var equipment: Array<LuaEquipment>
    var generator_energy: double
    var max_solar_energy: double
    var available_in_batteries: double
    var battery_capacity: double
    var shield: float
    var max_shield: float
    var inhibit_movement_bonus: Boolean?
    var valid: Boolean?
    var object_name: String /* "LuaEquipmentGrid" */
    fun help(): String
}

external interface LuaEquipmentGridPrototype {
    var name: String
    var order: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var equipment_categories: Array<String>
    var width: uint
    var height: uint
    var locked: Boolean?
    var valid: Boolean?
    var object_name: String /* "LuaEquipmentGridPrototype" */
    fun help(): String
}

external interface `T$31` {
    var width: uint
    var height: uint
    var points: Array<EquipmentPoint>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$32` {
    var spawn_and_station_height: float
    var spawn_and_station_shadow_height_offset: float
    var charge_approach_distance: float
    var logistic_radius: float
    var construction_radius: float
    var charging_station_count: uint
    var charging_distance: float
    var charging_station_shift: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var charging_energy: double
    var charging_threshold_distance: float
    var robot_vertical_acceleration: float
    var stationing_offset: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var robot_limit: uint
    var logistics_connection_distance: float
    var robots_shrink_when_entering_and_exiting: Boolean?
}

external interface LuaEquipmentPrototype {
    var name: String
    var type: String
    var order: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var shape: `T$31`
    var take_result: LuaItemPrototype?
    var energy_production: double
    var shield: float
    var energy_per_shield: double
    var logistic_parameters: `T$32`
    var energy_consumption: double
    var movement_bonus: float
    var energy_source: LuaElectricEnergySourcePrototype
    var equipment_categories: Array<String>
    var burner_prototype: LuaBurnerPrototype?
    var electric_energy_source_prototype: LuaElectricEnergySourcePrototype?
    var background_color: ColorTable
    var attack_parameters: dynamic /* ProjectileAttackParameters? | StreamAttackParameters? | OtherAttackParameters? */
        get() = definedExternally
        set(value) = definedExternally
    var automatic: Boolean?
    var valid: Boolean?
    var object_name: String /* "LuaEquipmentPrototype" */
    fun help(): String
}

external interface `T$33` {
    var name: String
    var input: Boolean?
    var precision_index: flow_precision_index
    var count: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface LuaFlowStatistics {
    fun get_input_count(name: String): dynamic /* uint64 | double */
    fun set_input_count(name: String, count: uint64)
    fun get_output_count(name: String): dynamic /* uint64 | double */
    fun set_output_count(name: String, count: uint64)
    fun get_flow_count(params: `T$33`): double
    fun on_flow(name: String, count: float)
    fun clear()
    var input_counts: Record<String, dynamic /* uint64 | double */>
    var output_counts: Record<String, dynamic /* uint64 | double */>
    var force: LuaForce?
    var valid: Boolean?
    var object_name: String /* "LuaFlowStatistics" */
    fun help(): String
}

typealias LuaFluidBox = Array<Fluid?>

external interface LuaFluidBoxPrototype {
    var entity: LuaEntityPrototype
    var index: uint
    var pipe_connections: Array<FluidBoxConnection>
    var production_type: String /* "input" | "output" | "input-output" | "none" */
    var base_area: double
    var base_level: double
    var height: double
    var volume: double
    var filter: LuaFluidPrototype?
    var minimum_temperature: double?
    var maximum_temperature: double?
    var secondary_draw_orders: Array<int>
    var render_layer: String
    var valid: Boolean?
    var object_name: String /* "LuaFluidBoxPrototype" */
    fun help(): String
}

external interface LuaFluidEnergySourcePrototype {
    var emissions: double
    var render_no_network_icon: Boolean?
    var render_no_power_icon: Boolean?
    var effectivity: double
    var burns_fluid: Boolean?
    var scale_fluid_usage: Boolean?
    var fluid_usage_per_tick: double
    var smoke: Array<SmokeSource>
    var maximum_temperature: double
    var fluid_box: LuaFluidBoxPrototype
    var valid: Boolean?
    var object_name: String /* "LuaFluidEnergySourcePrototype" */
    fun help(): String
}

external interface LuaFluidPrototype {
    var name: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var default_temperature: double
    var max_temperature: double
    var heat_capacity: double
    var order: String
    var group: LuaGroup
    var subgroup: LuaGroup
    var base_color: ColorTable
    var flow_color: ColorTable
    var gas_temperature: double
    var emissions_multiplier: double
    var fuel_value: double
    var hidden: Boolean?
    var valid: Boolean?
    var object_name: String /* "LuaFluidPrototype" */
    fun help(): String
}

external interface LuaFontPrototype {
    var name: String
    var from: String
    var size: int
    var spacing: float
    var border: Boolean?
    var filtered: Boolean?
    var border_color: ColorTable?
    var valid: Boolean?
    var object_name: String /* "LuaFontPrototype" */
    fun help(): String
}

external interface `T$34` {
    var path: dynamic /* String |  */
        get() = definedExternally
        set(value) = definedExternally
    var position: Position?
        get() = definedExternally
        set(value) = definedExternally
    var volume_modifier: double?
        get() = definedExternally
        set(value) = definedExternally
    var override_sound_type: String? /* "game-effect" | "gui-effect" | "ambient" | "environment" | "walking" | "alert" | "wind" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$35` {
    var name: dynamic /* String? | Array<String>? */
        get() = definedExternally
        set(value) = definedExternally
    var surface: dynamic /* uint? | String? | LuaSurface? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface LuaForce {
    fun get_entity_count(name: String): uint
    fun disable_research()
    fun enable_research()
    fun disable_all_prototypes()
    fun enable_all_prototypes()
    fun reset_recipes()
    fun enable_all_recipes()
    fun enable_all_technologies()
    fun research_all_technologies(include_disabled_prototypes: Boolean = definedExternally)
    fun reset_technologies()
    fun reset()
    fun reset_technology_effects()
    fun chart(surface: uint, area: BoundingBoxTable)
    fun chart(surface: uint, area: Any /* JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */)
    fun chart(surface: String, area: BoundingBoxTable)
    fun chart(surface: String, area: Any /* JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */)
    fun chart(surface: LuaSurface, area: BoundingBoxTable)
    fun chart(surface: LuaSurface, area: Any /* JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */)
    fun clear_chart(surface: uint = definedExternally)
    fun clear_chart()
    fun clear_chart(surface: String = definedExternally)
    fun clear_chart(surface: LuaSurface = definedExternally)
    fun rechart()
    fun chart_all(surface: uint = definedExternally)
    fun chart_all()
    fun chart_all(surface: String = definedExternally)
    fun chart_all(surface: LuaSurface = definedExternally)
    fun is_chunk_charted(surface: uint, position: ChunkPositionTable): Boolean?
    fun is_chunk_charted(surface: uint, position: Any /* JsTuple<x, int, y, int> */): Boolean?
    fun is_chunk_charted(surface: String, position: ChunkPositionTable): Boolean?
    fun is_chunk_charted(surface: String, position: Any /* JsTuple<x, int, y, int> */): Boolean?
    fun is_chunk_charted(surface: LuaSurface, position: ChunkPositionTable): Boolean?
    fun is_chunk_charted(surface: LuaSurface, position: Any /* JsTuple<x, int, y, int> */): Boolean?
    fun is_chunk_visible(surface: uint, position: ChunkPositionTable): Boolean?
    fun is_chunk_visible(surface: uint, position: Any /* JsTuple<x, int, y, int> */): Boolean?
    fun is_chunk_visible(surface: String, position: ChunkPositionTable): Boolean?
    fun is_chunk_visible(surface: String, position: Any /* JsTuple<x, int, y, int> */): Boolean?
    fun is_chunk_visible(surface: LuaSurface, position: ChunkPositionTable): Boolean?
    fun is_chunk_visible(surface: LuaSurface, position: Any /* JsTuple<x, int, y, int> */): Boolean?
    fun cancel_charting(surface: uint = definedExternally)
    fun cancel_charting()
    fun cancel_charting(surface: String = definedExternally)
    fun cancel_charting(surface: LuaSurface = definedExternally)
    fun get_ammo_damage_modifier(ammo: String): double
    fun set_ammo_damage_modifier(ammo: String, modifier: double)
    fun get_gun_speed_modifier(ammo: String): double
    fun set_gun_speed_modifier(ammo: String, modifier: double)
    fun get_turret_attack_modifier(turret: String): double
    fun set_turret_attack_modifier(turret: String, modifier: double)
    fun set_cease_fire(other: String, cease_fire: Boolean)
    fun set_cease_fire(other: LuaForce, cease_fire: Boolean)
    fun get_cease_fire(other: String): Boolean?
    fun get_cease_fire(other: LuaForce): Boolean?
    fun set_friend(other: String, friend: Boolean)
    fun set_friend(other: LuaForce, friend: Boolean)
    fun get_friend(other: String): Boolean?
    fun get_friend(other: LuaForce): Boolean?
    fun is_pathfinder_busy(): Boolean?
    fun kill_all_units()
    fun find_logistic_network_by_position(position: Position, surface: uint): LuaLogisticNetwork?
    fun find_logistic_network_by_position(position: Position, surface: String): LuaLogisticNetwork?
    fun find_logistic_network_by_position(position: Position, surface: LuaSurface): LuaLogisticNetwork?
    fun set_spawn_position(position: Position, surface: uint)
    fun set_spawn_position(position: Position, surface: String)
    fun set_spawn_position(position: Position, surface: LuaSurface)
    fun get_spawn_position(surface: uint): PositionTable
    fun get_spawn_position(surface: String): PositionTable
    fun get_spawn_position(surface: LuaSurface): PositionTable
    fun unchart_chunk(position: ChunkPositionTable, surface: uint)
    fun unchart_chunk(position: ChunkPositionTable, surface: String)
    fun unchart_chunk(position: ChunkPositionTable, surface: LuaSurface)
    fun unchart_chunk(position: Any /* JsTuple<x, int, y, int> */, surface: uint)
    fun unchart_chunk(position: Any /* JsTuple<x, int, y, int> */, surface: String)
    fun unchart_chunk(position: Any /* JsTuple<x, int, y, int> */, surface: LuaSurface)
    fun get_item_launched(item: String): uint
    fun set_item_launched(item: String, count: uint)
    fun print(message: Any /* JsTuple<String, Any> */, color: ColorTable = definedExternally)
    fun print(message: Any /* JsTuple<String, Any> */)
    fun print(message: Any /* JsTuple<String, Any> */, color: Any /* JsTuple<Any, float, Any, float, Any, float, Any, float> */ = definedExternally)
    fun print(message: String, color: ColorTable = definedExternally)
    fun print(message: String)
    fun print(message: String, color: Any /* JsTuple<Any, float, Any, float, Any, float, Any, float> */ = definedExternally)
    fun print(message: Number, color: ColorTable = definedExternally)
    fun print(message: Number)
    fun print(message: Number, color: Any /* JsTuple<Any, float, Any, float, Any, float, Any, float> */ = definedExternally)
    fun get_trains(surface: uint = definedExternally): Array<LuaTrain>
    fun get_trains(): Array<LuaTrain>
    fun get_trains(surface: String = definedExternally): Array<LuaTrain>
    fun get_trains(surface: LuaSurface = definedExternally): Array<LuaTrain>
    fun add_chart_tag(surface: uint, tag: ChartTagSpec): LuaCustomChartTag?
    fun add_chart_tag(surface: String, tag: ChartTagSpec): LuaCustomChartTag?
    fun add_chart_tag(surface: LuaSurface, tag: ChartTagSpec): LuaCustomChartTag?
    fun find_chart_tags(surface: uint, area: BoundingBoxTable = definedExternally): Array<LuaCustomChartTag>
    fun find_chart_tags(surface: uint): Array<LuaCustomChartTag>
    fun find_chart_tags(surface: uint, area: Any /* JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */ = definedExternally): Array<LuaCustomChartTag>
    fun find_chart_tags(surface: String, area: BoundingBoxTable = definedExternally): Array<LuaCustomChartTag>
    fun find_chart_tags(surface: String): Array<LuaCustomChartTag>
    fun find_chart_tags(surface: String, area: Any /* JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */ = definedExternally): Array<LuaCustomChartTag>
    fun find_chart_tags(surface: LuaSurface, area: BoundingBoxTable = definedExternally): Array<LuaCustomChartTag>
    fun find_chart_tags(surface: LuaSurface): Array<LuaCustomChartTag>
    fun find_chart_tags(surface: LuaSurface, area: Any /* JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */ = definedExternally): Array<LuaCustomChartTag>
    fun get_saved_technology_progress(technology: String): double?
    fun get_saved_technology_progress(technology: LuaTechnology): double?
    fun get_saved_technology_progress(technology: LuaTechnologyPrototype): double?
    fun set_saved_technology_progress(technology: String, progress: double?)
    fun set_saved_technology_progress(technology: LuaTechnology, progress: double?)
    fun set_saved_technology_progress(technology: LuaTechnologyPrototype, progress: double?)
    fun reset_evolution()
    fun play_sound(params: `T$34`)
    fun get_train_stops(params: `T$35` = definedExternally): Array<LuaEntity>
    fun get_hand_crafting_disabled_for_recipe(recipe: String): Boolean?
    fun get_hand_crafting_disabled_for_recipe(recipe: LuaRecipe): Boolean?
    fun set_hand_crafting_disabled_for_recipe(recipe: String, hand_crafting_disabled: Boolean)
    fun set_hand_crafting_disabled_for_recipe(recipe: LuaRecipe, hand_crafting_disabled: Boolean)
    fun add_research(technology: String): Boolean?
    fun add_research(technology: LuaTechnology): Boolean?
    fun add_research(technology: LuaTechnologyPrototype): Boolean?
    fun cancel_current_research()
    fun get_linked_inventory(prototype: LuaEntity, link_id: uint): LuaInventory?
    fun get_linked_inventory(prototype: LuaEntityPrototype, link_id: uint): LuaInventory?
    fun get_linked_inventory(prototype: String, link_id: uint): LuaInventory?
    fun is_friend(other: String): Boolean?
    fun is_friend(other: LuaForce): Boolean?
    fun is_enemy(other: String): Boolean?
    fun is_enemy(other: LuaForce): Boolean?
    var name: String
    var technologies: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaTechnology> */
    var recipes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaRecipe> */
    var manual_mining_speed_modifier: double
    var manual_crafting_speed_modifier: double
    var laboratory_speed_modifier: double
    var laboratory_productivity_bonus: double
    var worker_robots_speed_modifier: double
    var worker_robots_battery_modifier: double
    var worker_robots_storage_bonus: double
    var current_research: LuaTechnology?
    var research_progress: double
    var previous_research: LuaTechnology
    var inserter_stack_size_bonus: double
    var stack_inserter_capacity_bonus: uint
    var character_trash_slot_count: double
    var maximum_following_robot_count: uint
    var following_robots_lifetime_modifier: double
    var ghost_time_to_live: uint
    var players: Array<LuaPlayer>
    var ai_controllable: Boolean?
    var logistic_networks: Record<String, Array<LuaLogisticNetwork>>
    var item_production_statistics: LuaFlowStatistics
    var fluid_production_statistics: LuaFlowStatistics
    var kill_count_statistics: LuaFlowStatistics
    var entity_build_count_statistics: LuaFlowStatistics
    var character_running_speed_modifier: double
    var artillery_range_modifier: double
    var character_build_distance_bonus: uint
    var character_item_drop_distance_bonus: uint
    var character_reach_distance_bonus: uint
    var character_resource_reach_distance_bonus: double
    var character_item_pickup_distance_bonus: double
    var character_loot_pickup_distance_bonus: double
    var character_inventory_slots_bonus: uint
    var deconstruction_time_to_live: uint
    var character_health_bonus: double
    var max_successful_attempts_per_tick_per_construction_queue: uint
    var max_failed_attempts_per_tick_per_construction_queue: uint
    var zoom_to_world_enabled: Boolean?
    var zoom_to_world_ghost_building_enabled: Boolean?
    var zoom_to_world_blueprint_enabled: Boolean?
    var zoom_to_world_deconstruction_planner_enabled: Boolean?
    var zoom_to_world_selection_tool_enabled: Boolean?
    var character_logistic_requests: Boolean?
    var rockets_launched: uint
    var items_launched: Record<String, uint>
    var connected_players: Array<LuaPlayer>
    var mining_drill_productivity_bonus: double
    var train_braking_force_bonus: double
    var evolution_factor: double
    var evolution_factor_by_pollution: double
    var evolution_factor_by_time: double
    var evolution_factor_by_killing_spawners: double
    var friendly_fire: Boolean?
    var share_chart: Boolean?
    var research_queue_enabled: Boolean?
    var index: uint
    var research_queue: Array<dynamic /* String | LuaTechnology | LuaTechnologyPrototype */>?
    var research_enabled: Boolean?
    var valid: Boolean?
    var object_name: String /* "LuaForce" */
    fun help(): String
}

external interface LuaFuelCategoryPrototype {
    var name: String
    var order: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var valid: Boolean?
    var object_name: String /* "LuaFuelCategoryPrototype" */
    fun help(): String
}

external interface `T$36` {
    var game_finished: Boolean?
    var player_won: Boolean?
    var next_level: String
    var can_continue: Boolean?
    var victorious_force: dynamic /* String | LuaForce */
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$37` {
    var text: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var image: String?
        get() = definedExternally
        set(value) = definedExternally
    var point_to: dynamic /* EntityGuiArrowSpecification? | PositionGuiArrowSpecification? | CraftingQueueGuiArrowSpecification? | ItemStackGuiArrowSpecification? | OtherGuiArrowSpecification? */
        get() = definedExternally
        set(value) = definedExternally
    var style: String?
        get() = definedExternally
        set(value) = definedExternally
    var wrapper_frame_style: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$38` {
    var player: dynamic /* uint? | String? | LuaPlayer? */
        get() = definedExternally
        set(value) = definedExternally
    var by_player: dynamic /* uint? | String? | LuaPlayer? */
        get() = definedExternally
        set(value) = definedExternally
    var surface: dynamic /* uint? | String? | LuaSurface? */
        get() = definedExternally
        set(value) = definedExternally
    var position: Position?
        get() = definedExternally
        set(value) = definedExternally
    var resolution: Position?
        get() = definedExternally
        set(value) = definedExternally
    var zoom: double?
        get() = definedExternally
        set(value) = definedExternally
    var path: String?
        get() = definedExternally
        set(value) = definedExternally
    var show_gui: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var show_entity_info: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var show_cursor_building_preview: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var anti_alias: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var quality: int?
        get() = definedExternally
        set(value) = definedExternally
    var allow_in_replay: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var daytime: double?
        get() = definedExternally
        set(value) = definedExternally
    var water_tick: uint?
        get() = definedExternally
        set(value) = definedExternally
    var force_render: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$39` {
    var force: dynamic /* String? | LuaForce? */
        get() = definedExternally
        set(value) = definedExternally
    var path: String?
        get() = definedExternally
        set(value) = definedExternally
    var by_player: dynamic /* uint? | String? | LuaPlayer? */
        get() = definedExternally
        set(value) = definedExternally
    var selected_technology: dynamic /* String? | LuaTechnology? | LuaTechnologyPrototype? */
        get() = definedExternally
        set(value) = definedExternally
    var skip_disabled: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var quality: int?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$40` {
    var name: dynamic /* String? | Array<String>? */
        get() = definedExternally
        set(value) = definedExternally
    var surface: dynamic /* uint? | String? | LuaSurface? */
        get() = definedExternally
        set(value) = definedExternally
    var force: dynamic /* String? | LuaForce? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface LuaGameScript {
    fun set_game_state(params: `T$36`)
    fun get_entity_by_tag(tag: String): LuaEntity
    fun show_message_dialog(params: `T$37`)
    fun is_demo(): Boolean?
    fun reload_script()
    fun reload_mods()
    fun save_atlas()
    fun check_consistency()
    fun regenerate_entity(entities: String)
    fun regenerate_entity(entities: Array<String>)
    fun take_screenshot(params: `T$38`)
    fun set_wait_for_screenshots_to_finish()
    fun take_technology_screenshot(params: `T$39`)
    fun table_to_json(data: table): String
    fun json_to_table(json: String): dynamic /* String? | Number? | Boolean? | table? */
    fun write_file(filename: String, data: Any /* JsTuple<String, Any> */, append: Boolean = definedExternally, for_player: uint = definedExternally)
    fun write_file(filename: String, data: Any /* JsTuple<String, Any> */)
    fun write_file(filename: String, data: Any /* JsTuple<String, Any> */, append: Boolean = definedExternally)
    fun write_file(filename: String, data: String, append: Boolean = definedExternally, for_player: uint = definedExternally)
    fun write_file(filename: String, data: String)
    fun write_file(filename: String, data: String, append: Boolean = definedExternally)
    fun write_file(filename: String, data: Number, append: Boolean = definedExternally, for_player: uint = definedExternally)
    fun write_file(filename: String, data: Number)
    fun write_file(filename: String, data: Number, append: Boolean = definedExternally)
    fun remove_path(path: String)
    fun remove_offline_players(players: Array<Any /* LuaPlayer | String */> = definedExternally)
    fun force_crc()
    fun create_force(force: String): LuaForce
    fun merge_forces(source: String, destination: String)
    fun merge_forces(source: String, destination: LuaForce)
    fun merge_forces(source: LuaForce, destination: String)
    fun merge_forces(source: LuaForce, destination: LuaForce)
    fun create_surface(name: String, settings: MapGenSettings = definedExternally): LuaSurface
    fun server_save(name: String = definedExternally)
    fun auto_save(name: String = definedExternally)
    fun delete_surface(surface: String)
    fun delete_surface(surface: LuaSurface)
    fun disable_replay()
    fun disable_tutorial_triggers()
    fun direction_to_string(direction: direction)
    fun print(message: Any /* JsTuple<String, Any> */, color: ColorTable = definedExternally)
    fun print(message: Any /* JsTuple<String, Any> */)
    fun print(message: Any /* JsTuple<String, Any> */, color: Any /* JsTuple<Any, float, Any, float, Any, float, Any, float> */ = definedExternally)
    fun print(message: String, color: ColorTable = definedExternally)
    fun print(message: String)
    fun print(message: String, color: Any /* JsTuple<Any, float, Any, float, Any, float, Any, float> */ = definedExternally)
    fun print(message: Number, color: ColorTable = definedExternally)
    fun print(message: Number)
    fun print(message: Number, color: Any /* JsTuple<Any, float, Any, float, Any, float, Any, float> */ = definedExternally)
    fun create_random_generator(seed: uint = definedExternally): LuaRandomGenerator
    fun check_prototype_translations()
    fun play_sound(params: `T$34`)
    fun is_valid_sound_path(sound_path: String): Boolean?
//    fun is_valid_sound_path(sound_path: String): Boolean?
    fun is_valid_sprite_path(sprite_path: String): Boolean?
//    fun is_valid_sprite_path(sprite_path: String): Boolean?
    fun kick_player(player: uint, reason: Any /* JsTuple<String, Any> */ = definedExternally)
    fun kick_player(player: uint)
    fun kick_player(player: uint, reason: String = definedExternally)
    fun kick_player(player: uint, reason: Number = definedExternally)
    fun kick_player(player: String, reason: Any /* JsTuple<String, Any> */ = definedExternally)
    fun kick_player(player: String)
    fun kick_player(player: String, reason: String = definedExternally)
    fun kick_player(player: String, reason: Number = definedExternally)
    fun kick_player(player: LuaPlayer, reason: Any /* JsTuple<String, Any> */ = definedExternally)
    fun kick_player(player: LuaPlayer)
    fun kick_player(player: LuaPlayer, reason: String = definedExternally)
    fun kick_player(player: LuaPlayer, reason: Number = definedExternally)
    fun ban_player(player: uint, reason: Any /* JsTuple<String, Any> */ = definedExternally)
    fun ban_player(player: uint)
    fun ban_player(player: uint, reason: String = definedExternally)
    fun ban_player(player: uint, reason: Number = definedExternally)
    fun ban_player(player: String, reason: Any /* JsTuple<String, Any> */ = definedExternally)
    fun ban_player(player: String)
    fun ban_player(player: String, reason: String = definedExternally)
    fun ban_player(player: String, reason: Number = definedExternally)
    fun ban_player(player: LuaPlayer, reason: Any /* JsTuple<String, Any> */ = definedExternally)
    fun ban_player(player: LuaPlayer)
    fun ban_player(player: LuaPlayer, reason: String = definedExternally)
    fun ban_player(player: LuaPlayer, reason: Number = definedExternally)
    fun unban_player(player: uint)
    fun unban_player(player: String)
    fun unban_player(player: LuaPlayer)
    fun purge_player(player: uint)
    fun purge_player(player: String)
    fun purge_player(player: LuaPlayer)
    fun mute_player(player: uint)
    fun mute_player(player: String)
    fun mute_player(player: LuaPlayer)
    fun unmute_player(player: uint)
    fun unmute_player(player: String)
    fun unmute_player(player: LuaPlayer)
    fun count_pipe_groups()
    fun is_multiplayer(): Boolean?
    fun get_active_entities_count(surface: uint = definedExternally): uint
    fun get_active_entities_count(): uint
    fun get_active_entities_count(surface: String = definedExternally): uint
    fun get_active_entities_count(surface: LuaSurface = definedExternally): uint
    fun get_map_exchange_string(): String
    fun parse_map_exchange_string(map_exchange_string: String): MapExchangeStringData
    fun get_train_stops(params: `T$40` = definedExternally): Array<LuaEntity>
    fun get_player(player: uint): LuaPlayer?
    fun get_player(player: String): LuaPlayer?
    fun get_surface(surface: uint): LuaSurface?
    fun get_surface(surface: String): LuaSurface?
    fun create_profiler(stopped: Boolean = definedExternally): LuaProfiler
    fun evaluate_expression(expression: String, variables: Record<String, double> = definedExternally): double
    fun get_filtered_entity_prototypes(filters: Array<Any /* NameEntityPrototypeFilter | TypeEntityPrototypeFilter | CollisionMaskEntityPrototypeFilter | FlagEntityPrototypeFilter | BuildBaseEvolutionRequirementEntityPrototypeFilter | SelectionPriorityEntityPrototypeFilter | EmissionsEntityPrototypeFilter | CraftingCategoryEntityPrototypeFilter | OtherEntityPrototypeFilter */>): LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaEntityPrototype> */
    fun get_filtered_item_prototypes(filters: Array<Any /* PlaceResultItemPrototypeFilter | BurntResultItemPrototypeFilter | PlaceAsTileItemPrototypeFilter | PlacedAsEquipmentResultItemPrototypeFilter | NameItemPrototypeFilter | TypeItemPrototypeFilter | FlagItemPrototypeFilter | SubgroupItemPrototypeFilter | FuelCategoryItemPrototypeFilter | StackSizeItemPrototypeFilter | DefaultRequestAmountItemPrototypeFilter | WireCountItemPrototypeFilter | FuelValueItemPrototypeFilter | FuelAccelerationMultiplierItemPrototypeFilter | FuelTopSpeedMultiplierItemPrototypeFilter | FuelEmissionsMultiplierItemPrototypeFilter | OtherItemPrototypeFilter */>): LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaItemPrototype> */
    fun get_filtered_equipment_prototypes(filters: Array<Any /* TypeEquipmentPrototypeFilter | OtherEquipmentPrototypeFilter */>): LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaEquipmentPrototype> */
    fun get_filtered_mod_setting_prototypes(filters: Array<Any /* TypeModSettingPrototypeFilter | ModModSettingPrototypeFilter | SettingTypeModSettingPrototypeFilter */>): LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaModSettingPrototype> */
    fun get_filtered_achievement_prototypes(filters: Array<Any /* TypeAchievementPrototypeFilter | OtherAchievementPrototypeFilter */>): LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaAchievementPrototype> */
    fun get_filtered_tile_prototypes(filters: Array<Any /* CollisionMaskTilePrototypeFilter | WalkingSpeedModifierTilePrototypeFilter | VehicleFrictionModifierTilePrototypeFilter | DecorativeRemovalProbabilityTilePrototypeFilter | EmissionsTilePrototypeFilter | OtherTilePrototypeFilter */>): LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaTilePrototype> */
    fun get_filtered_decorative_prototypes(filters: Array<Any /* CollisionMaskDecorativePrototypeFilter | OtherDecorativePrototypeFilter */>): LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaDecorativePrototype> */
    fun get_filtered_fluid_prototypes(filters: Array<Any /* NameFluidPrototypeFilter | SubgroupFluidPrototypeFilter | DefaultTemperatureFluidPrototypeFilter | MaxTemperatureFluidPrototypeFilter | HeatCapacityFluidPrototypeFilter | FuelValueFluidPrototypeFilter | EmissionsMultiplierFluidPrototypeFilter | GasTemperatureFluidPrototypeFilter | OtherFluidPrototypeFilter */>): LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaFluidPrototype> */
    fun get_filtered_recipe_prototypes(filters: Array<Any /* HasIngredientItemRecipePrototypeFilter | HasIngredientFluidRecipePrototypeFilter | HasProductItemRecipePrototypeFilter | HasProductFluidRecipePrototypeFilter | SubgroupRecipePrototypeFilter | CategoryRecipePrototypeFilter | EnergyRecipePrototypeFilter | EmissionsMultiplierRecipePrototypeFilter | RequestPasteMultiplierRecipePrototypeFilter | OverloadMultiplierRecipePrototypeFilter | OtherRecipePrototypeFilter */>): LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaRecipePrototype> */
    fun get_filtered_technology_prototypes(filters: Array<Any /* ResearchUnitIngredientTechnologyPrototypeFilter | LevelTechnologyPrototypeFilter | MaxLevelTechnologyPrototypeFilter | TimeTechnologyPrototypeFilter | OtherTechnologyPrototypeFilter */>): LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaTechnologyPrototype> */
    fun create_inventory(size: uint16): LuaInventory
    fun get_script_inventories(mod: String = definedExternally): Record<String, Array<LuaInventory>>
    fun reset_time_played()
    fun encode_string(string: String): String?
    fun decode_string(string: String): String?
    var player: LuaPlayer?
    var players: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<dynamic /* uint | String */, LuaPlayer> */
    var map_settings: MapSettings
    var difficulty_settings: DifficultySettings
    var difficulty: difficulty
    var forces: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<dynamic /* uint | String */, LuaForce> */
    var entity_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaEntityPrototype> */
    var item_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaItemPrototype> */
    var fluid_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaFluidPrototype> */
    var tile_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaTilePrototype> */
    var equipment_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaEquipmentPrototype> */
    var damage_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaDamagePrototype> */
    var virtual_signal_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaVirtualSignalPrototype> */
    var equipment_grid_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaEquipmentGridPrototype> */
    var recipe_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaRecipePrototype> */
    var technology_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaTechnologyPrototype> */
    var decorative_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaDecorativePrototype> */
    var particle_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaParticlePrototype> */
    var autoplace_control_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaAutoplaceControlPrototype> */
    var noise_layer_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaNoiseLayerPrototype> */
    var mod_setting_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaModSettingPrototype> */
    var custom_input_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaCustomInputPrototype> */
    var ammo_category_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaAmmoCategoryPrototype> */
    var named_noise_expressions: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaNamedNoiseExpression> */
    var item_subgroup_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaGroup> */
    var item_group_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaGroup> */
    var fuel_category_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaFuelCategoryPrototype> */
    var resource_category_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaResourceCategoryPrototype> */
    var achievement_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaAchievementPrototype> */
    var module_category_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaModuleCategoryPrototype> */
    var equipment_category_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaEquipmentCategoryPrototype> */
    var trivial_smoke_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaTrivialSmokePrototype> */
    var shortcut_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaShortcutPrototype> */
    var recipe_category_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaRecipeCategoryPrototype> */
    var font_prototypes: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, LuaFontPrototype> */
    var map_gen_presets: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, MapGenPreset> */
    var styles: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, String> */
    var tick: uint
    var ticks_played: uint
    var tick_paused: Boolean?
    var ticks_to_run: uint
    var finished: Boolean?
    var speed: float
    var surfaces: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<dynamic /* uint | String */, LuaSurface> */
    var active_mods: Record<String, String>
    var connected_players: Array<LuaPlayer>
    var permissions: LuaPermissionGroups
    var backer_names: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<uint, String> */
    var default_map_gen_settings: MapGenSettings
    var enemy_has_vision_on_land_mines: Boolean?
    var autosave_enabled: Boolean?
    var draw_resource_selection: Boolean?
    var pollution_statistics: LuaFlowStatistics
    var max_force_distraction_distance: double
    var max_force_distraction_chunk_distance: uint
    var max_electric_pole_supply_area_distance: float
    var max_electric_pole_connection_distance: double
    var max_beacon_supply_area_distance: double
    var max_gate_activation_distance: double
    var max_inserter_reach_distance: double
    var max_pipe_to_ground_distance: uint8
    var max_underground_belt_distance: uint8
    var object_name: String /* "LuaGameScript" */
}

external interface LuaGenericOnOffControlBehavior : LuaControlBehavior {
    var disabled: Boolean?
    var circuit_condition: CircuitConditionDefinition
    var logistic_condition: CircuitConditionDefinition
    var connect_to_logistic_network: Boolean?
    var valid: Boolean?
    var object_name: String
    fun help(): String
}

external interface LuaGroup {
    var name: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var type: String
    var group: LuaGroup?
    var subgroups: Array<LuaGroup>
    var order_in_recipe: String
    var order: String
    var valid: Boolean?
    var object_name: String /* "LuaGroup" */
    fun help(): String
}

external interface LuaGui {
    fun is_valid_sprite_path(sprite_path: String): Boolean?
//    fun is_valid_sprite_path(sprite_path: ): Boolean?
    var player: LuaPlayer
    var children: Record<String, dynamic /* ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers */>
    var top: dynamic /* ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers */
        get() = definedExternally
        set(value) = definedExternally
    var left: dynamic /* ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers */
        get() = definedExternally
        set(value) = definedExternally
    var center: dynamic /* ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers */
        get() = definedExternally
        set(value) = definedExternally
    var goal: dynamic /* ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers */
        get() = definedExternally
        set(value) = definedExternally
    var screen: dynamic /* ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers */
        get() = definedExternally
        set(value) = definedExternally
    var relative: dynamic /* ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers */
        get() = definedExternally
        set(value) = definedExternally
    var valid: Boolean?
    var object_name: String /* "LuaGui" */
    fun help(): String
}

external interface BaseGuiSpec {
    var type: String /* "choose-elem-button" | "drop-down" | "empty-widget" | "entity-preview" | "list-box" | "scroll-pane" | "sprite-button" | "tabbed-pane" | "text-box" | "button" | "camera" | "checkbox" | "flow" | "frame" | "label" | "line" | "minimap" | "progressbar" | "radiobutton" | "slider" | "sprite" | "switch" | "tab" | "table" | "textfield" */
    var name: String?
        get() = definedExternally
        set(value) = definedExternally
    var caption: dynamic /* JsTuple<String, Any> | String? | Number? */
        get() = definedExternally
        set(value) = definedExternally
    var tooltip: dynamic /* JsTuple<String, Any> | String? | Number? */
        get() = definedExternally
        set(value) = definedExternally
    var enabled: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var visible: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var ignored_by_interaction: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var style: String?
        get() = definedExternally
        set(value) = definedExternally
    var tags: Tags?
        get() = definedExternally
        set(value) = definedExternally
    var index: uint?
        get() = definedExternally
        set(value) = definedExternally
    var anchor: GuiAnchor?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ButtonGuiSpec : BaseGuiSpec {
    override var type: String /* "button" */
    var mouse_button_filter: dynamic /* MouseButtonFlagsTable? | MouseButtonFlagsArray? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface FlowGuiSpec : BaseGuiSpec {
    override var type: String /* "flow" */
    var direction: String? /* "horizontal" | "vertical" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface FrameGuiSpec : BaseGuiSpec {
    override var type: String /* "frame" */
    var direction: String? /* "horizontal" | "vertical" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface TableGuiSpec : BaseGuiSpec {
    override var type: String /* "table" */
    var column_count: uint
    var draw_vertical_lines: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var draw_horizontal_lines: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var draw_horizontal_line_after_headers: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var vertical_centering: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TextfieldGuiSpec : BaseGuiSpec {
    override var type: String /* "textfield" */
    var text: String?
        get() = definedExternally
        set(value) = definedExternally
    var numeric: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var allow_decimal: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var allow_negative: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var is_password: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var lose_focus_on_confirm: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var clear_and_focus_on_right_click: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ProgressbarGuiSpec : BaseGuiSpec {
    override var type: String /* "progressbar" */
    var value: double?
        get() = definedExternally
        set(value) = definedExternally
}

external interface CheckboxGuiSpec : BaseGuiSpec {
    override var type: String /* "checkbox" */
    var state: Boolean?
}

external interface RadiobuttonGuiSpec : BaseGuiSpec {
    override var type: String /* "radiobutton" */
    var state: Boolean?
}

external interface SpriteButtonGuiSpec : BaseGuiSpec {
    override var type: String /* "sprite-button" */
    var sprite: dynamic /* String? | ? */
        get() = definedExternally
        set(value) = definedExternally
    var hovered_sprite: dynamic /* String? | ? */
        get() = definedExternally
        set(value) = definedExternally
    var clicked_sprite: dynamic /* String? | ? */
        get() = definedExternally
        set(value) = definedExternally
    var number: double?
        get() = definedExternally
        set(value) = definedExternally
    var show_percent_for_small_numbers: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var mouse_button_filter: dynamic /* MouseButtonFlagsTable? | MouseButtonFlagsArray? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface SpriteGuiSpec : BaseGuiSpec {
    override var type: String /* "sprite" */
    var sprite: dynamic /* String? | ? */
        get() = definedExternally
        set(value) = definedExternally
    var resize_to_sprite: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ScrollPaneGuiSpec : BaseGuiSpec {
    override var type: String /* "scroll-pane" */
    var horizontal_scroll_policy: String? /* "auto" | "never" | "always" | "auto-and-reserve-space" | "dont-show-but-allow-scrolling" */
        get() = definedExternally
        set(value) = definedExternally
    var vertical_scroll_policy: String? /* "auto" | "never" | "always" | "auto-and-reserve-space" | "dont-show-but-allow-scrolling" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface DropDownGuiSpec : BaseGuiSpec {
    override var type: String /* "drop-down" */
    var items: Array<dynamic /* JsTuple<String, Any> | String | Number */>?
        get() = definedExternally
        set(value) = definedExternally
    var selected_index: uint?
        get() = definedExternally
        set(value) = definedExternally
}

external interface LineGuiSpec : BaseGuiSpec {
    override var type: String /* "line" */
    var direction: String? /* "horizontal" | "vertical" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface ListBoxGuiSpec : BaseGuiSpec {
    override var type: String /* "list-box" */
    var items: Array<dynamic /* JsTuple<String, Any> | String | Number */>?
        get() = definedExternally
        set(value) = definedExternally
    var selected_index: uint?
        get() = definedExternally
        set(value) = definedExternally
}

external interface CameraGuiSpec : BaseGuiSpec {
    override var type: String /* "camera" */
    var position: Position
    var surface_index: uint?
        get() = definedExternally
        set(value) = definedExternally
    var zoom: double?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ChooseElemButtonFilters {
    @nativeGetter
    operator fun get(key: String): Any?
    @nativeSetter
    operator fun set(key: String, value: Any)
    var item: Array<dynamic /* PlaceResultItemPrototypeFilter | BurntResultItemPrototypeFilter | PlaceAsTileItemPrototypeFilter | PlacedAsEquipmentResultItemPrototypeFilter | NameItemPrototypeFilter | TypeItemPrototypeFilter | FlagItemPrototypeFilter | SubgroupItemPrototypeFilter | FuelCategoryItemPrototypeFilter | StackSizeItemPrototypeFilter | DefaultRequestAmountItemPrototypeFilter | WireCountItemPrototypeFilter | FuelValueItemPrototypeFilter | FuelAccelerationMultiplierItemPrototypeFilter | FuelTopSpeedMultiplierItemPrototypeFilter | FuelEmissionsMultiplierItemPrototypeFilter | OtherItemPrototypeFilter */>
    var tile: Array<dynamic /* CollisionMaskTilePrototypeFilter | WalkingSpeedModifierTilePrototypeFilter | VehicleFrictionModifierTilePrototypeFilter | DecorativeRemovalProbabilityTilePrototypeFilter | EmissionsTilePrototypeFilter | OtherTilePrototypeFilter */>
    var entity: Array<dynamic /* NameEntityPrototypeFilter | TypeEntityPrototypeFilter | CollisionMaskEntityPrototypeFilter | FlagEntityPrototypeFilter | BuildBaseEvolutionRequirementEntityPrototypeFilter | SelectionPriorityEntityPrototypeFilter | EmissionsEntityPrototypeFilter | CraftingCategoryEntityPrototypeFilter | OtherEntityPrototypeFilter */>
    var signal: Any
    var fluid: Array<dynamic /* NameFluidPrototypeFilter | SubgroupFluidPrototypeFilter | DefaultTemperatureFluidPrototypeFilter | MaxTemperatureFluidPrototypeFilter | HeatCapacityFluidPrototypeFilter | FuelValueFluidPrototypeFilter | EmissionsMultiplierFluidPrototypeFilter | GasTemperatureFluidPrototypeFilter | OtherFluidPrototypeFilter */>
    var recipe: Array<dynamic /* HasIngredientItemRecipePrototypeFilter | HasIngredientFluidRecipePrototypeFilter | HasProductItemRecipePrototypeFilter | HasProductFluidRecipePrototypeFilter | SubgroupRecipePrototypeFilter | CategoryRecipePrototypeFilter | EnergyRecipePrototypeFilter | EmissionsMultiplierRecipePrototypeFilter | RequestPasteMultiplierRecipePrototypeFilter | OverloadMultiplierRecipePrototypeFilter | OtherRecipePrototypeFilter */>
    var decorative: Array<dynamic /* CollisionMaskDecorativePrototypeFilter | OtherDecorativePrototypeFilter */>
    var achievement: Array<dynamic /* TypeAchievementPrototypeFilter | OtherAchievementPrototypeFilter */>
    var equipment: Array<dynamic /* TypeEquipmentPrototypeFilter | OtherEquipmentPrototypeFilter */>
    var technology: Array<dynamic /* ResearchUnitIngredientTechnologyPrototypeFilter | LevelTechnologyPrototypeFilter | MaxLevelTechnologyPrototypeFilter | TimeTechnologyPrototypeFilter | OtherTechnologyPrototypeFilter */>
}

external interface BaseChooseElemButtonSpec : BaseGuiSpec {
    override var type: String /* "choose-elem-button" */
    var elem_type: String /* "item" | "tile" | "entity" | "signal" | "fluid" | "recipe" | "decorative" | "item-group" | "achievement" | "equipment" | "technology" */
    var filters: Any?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ItemChooseElemButtonSpec : BaseChooseElemButtonSpec {
    override var elem_type: String /* "item" */
    var item: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TileChooseElemButtonSpec : BaseChooseElemButtonSpec {
    override var elem_type: String /* "tile" */
    var tile: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface EntityChooseElemButtonSpec : BaseChooseElemButtonSpec {
    override var elem_type: String /* "entity" */
    var entity: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface SignalChooseElemButtonSpec : BaseChooseElemButtonSpec {
    override var elem_type: String /* "signal" */
    var signal: SignalID?
        get() = definedExternally
        set(value) = definedExternally
}

external interface FluidChooseElemButtonSpec : BaseChooseElemButtonSpec {
    override var elem_type: String /* "fluid" */
    var fluid: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface RecipeChooseElemButtonSpec : BaseChooseElemButtonSpec {
    override var elem_type: String /* "recipe" */
    var recipe: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface DecorativeChooseElemButtonSpec : BaseChooseElemButtonSpec {
    override var elem_type: String /* "decorative" */
    var decorative: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ItemGroupChooseElemButtonSpec : BaseChooseElemButtonSpec {
    @nativeGetter
    operator fun get(key: String): String?
    @nativeSetter
    operator fun set(key: String, value: String?)
    override var elem_type: String /* "item-group" */
}

external interface AchievementChooseElemButtonSpec : BaseChooseElemButtonSpec {
    override var elem_type: String /* "achievement" */
    var achievement: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface EquipmentChooseElemButtonSpec : BaseChooseElemButtonSpec {
    override var elem_type: String /* "equipment" */
    var equipment: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TechnologyChooseElemButtonSpec : BaseChooseElemButtonSpec {
    override var elem_type: String /* "technology" */
    var technology: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TextBoxGuiSpec : BaseGuiSpec {
    override var type: String /* "text-box" */
    var text: String?
        get() = definedExternally
        set(value) = definedExternally
    var clear_and_focus_on_right_click: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface SliderGuiSpec : BaseGuiSpec {
    override var type: String /* "slider" */
    var minimum_value: double?
        get() = definedExternally
        set(value) = definedExternally
    var maximum_value: double?
        get() = definedExternally
        set(value) = definedExternally
    var value: double?
        get() = definedExternally
        set(value) = definedExternally
    var value_step: double?
        get() = definedExternally
        set(value) = definedExternally
    var discrete_slider: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var discrete_values: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface MinimapGuiSpec : BaseGuiSpec {
    override var type: String /* "minimap" */
    var position: Position?
        get() = definedExternally
        set(value) = definedExternally
    var surface_index: uint?
        get() = definedExternally
        set(value) = definedExternally
    var chart_player_index: uint?
        get() = definedExternally
        set(value) = definedExternally
    var force: String?
        get() = definedExternally
        set(value) = definedExternally
    var zoom: double?
        get() = definedExternally
        set(value) = definedExternally
}

external interface TabGuiSpec : BaseGuiSpec {
    override var type: String /* "tab" */
    var badge_text: dynamic /* JsTuple<String, Any> | String? | Number? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface SwitchGuiSpec : BaseGuiSpec {
    override var type: String /* "switch" */
    var switch_state: String? /* "left" | "right" | "none" */
        get() = definedExternally
        set(value) = definedExternally
    var allow_none_state: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var left_label_caption: dynamic /* JsTuple<String, Any> | String? | Number? */
        get() = definedExternally
        set(value) = definedExternally
    var left_label_tooltip: dynamic /* JsTuple<String, Any> | String? | Number? */
        get() = definedExternally
        set(value) = definedExternally
    var right_label_caption: dynamic /* JsTuple<String, Any> | String? | Number? */
        get() = definedExternally
        set(value) = definedExternally
    var right_label_tooltip: dynamic /* JsTuple<String, Any> | String? | Number? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface OtherGuiSpec : BaseGuiSpec {
    override var type: String /* "empty-widget" | "entity-preview" | "tabbed-pane" | "label" */
}

external interface GuiElementIndex {
    @nativeGetter
    operator fun get(name: String): dynamic /* ChooseElemButtonGuiElementMembers? | DropDownGuiElementMembers? | EmptyWidgetGuiElementMembers? | EntityPreviewGuiElementMembers? | ListBoxGuiElementMembers? | ScrollPaneGuiElementMembers? | SpriteButtonGuiElementMembers? | TabbedPaneGuiElementMembers? | TextBoxGuiElementMembers? | ButtonGuiElementMembers? | CameraGuiElementMembers? | CheckboxGuiElementMembers? | FlowGuiElementMembers? | FrameGuiElementMembers? | LabelGuiElementMembers? | LineGuiElementMembers? | MinimapGuiElementMembers? | ProgressbarGuiElementMembers? | RadiobuttonGuiElementMembers? | SliderGuiElementMembers? | SpriteGuiElementMembers? | SwitchGuiElementMembers? | TabGuiElementMembers? | TableGuiElementMembers? | TextfieldGuiElementMembers? */
    @nativeSetter
    operator fun set(name: String, value: ChooseElemButtonGuiElementMembers)
    @nativeSetter
    operator fun set(name: String, value: DropDownGuiElementMembers)
    @nativeSetter
    operator fun set(name: String, value: EmptyWidgetGuiElementMembers)
    @nativeSetter
    operator fun set(name: String, value: EntityPreviewGuiElementMembers)
    @nativeSetter
    operator fun set(name: String, value: ListBoxGuiElementMembers)
    @nativeSetter
    operator fun set(name: String, value: ScrollPaneGuiElementMembers)
    @nativeSetter
    operator fun set(name: String, value: SpriteButtonGuiElementMembers)
    @nativeSetter
    operator fun set(name: String, value: TabbedPaneGuiElementMembers)
    @nativeSetter
    operator fun set(name: String, value: TextBoxGuiElementMembers)
    @nativeSetter
    operator fun set(name: String, value: ButtonGuiElementMembers)
    @nativeSetter
    operator fun set(name: String, value: CameraGuiElementMembers)
    @nativeSetter
    operator fun set(name: String, value: CheckboxGuiElementMembers)
    @nativeSetter
    operator fun set(name: String, value: FlowGuiElementMembers)
    @nativeSetter
    operator fun set(name: String, value: FrameGuiElementMembers)
    @nativeSetter
    operator fun set(name: String, value: LabelGuiElementMembers)
    @nativeSetter
    operator fun set(name: String, value: LineGuiElementMembers)
    @nativeSetter
    operator fun set(name: String, value: MinimapGuiElementMembers)
    @nativeSetter
    operator fun set(name: String, value: ProgressbarGuiElementMembers)
    @nativeSetter
    operator fun set(name: String, value: RadiobuttonGuiElementMembers)
    @nativeSetter
    operator fun set(name: String, value: SliderGuiElementMembers)
    @nativeSetter
    operator fun set(name: String, value: SpriteGuiElementMembers)
    @nativeSetter
    operator fun set(name: String, value: SwitchGuiElementMembers)
    @nativeSetter
    operator fun set(name: String, value: TabGuiElementMembers)
    @nativeSetter
    operator fun set(name: String, value: TableGuiElementMembers)
    @nativeSetter
    operator fun set(name: String, value: TextfieldGuiElementMembers)
}

external interface `T$41`<Type> {
    var type: Type
}

external interface BaseGuiElement {
    fun <Type : String> add(element: Any /* ButtonGuiSpec | FlowGuiSpec | FrameGuiSpec | TableGuiSpec | TextfieldGuiSpec | ProgressbarGuiSpec | CheckboxGuiSpec | RadiobuttonGuiSpec | SpriteButtonGuiSpec | SpriteGuiSpec | ScrollPaneGuiSpec | DropDownGuiSpec | LineGuiSpec | ListBoxGuiSpec | CameraGuiSpec | ItemChooseElemButtonSpec | TileChooseElemButtonSpec | EntityChooseElemButtonSpec | SignalChooseElemButtonSpec | FluidChooseElemButtonSpec | RecipeChooseElemButtonSpec | DecorativeChooseElemButtonSpec | ItemGroupChooseElemButtonSpec | AchievementChooseElemButtonSpec | EquipmentChooseElemButtonSpec | TechnologyChooseElemButtonSpec | TextBoxGuiSpec | SliderGuiSpec | MinimapGuiSpec | TabGuiSpec | SwitchGuiSpec | OtherGuiSpec */): Extract<dynamic /* ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers */, `T$41`<Type>>
    fun clear()
    fun destroy()
    fun get_mod(): String?
    fun get_index_in_parent(): uint
    fun clear_items()
    fun get_item(index: uint): dynamic /* JsTuple<String, Any> | String | Number */
    fun set_item(index: uint, string: Any /* JsTuple<String, Any> */)
    fun set_item(index: uint, string: String)
    fun set_item(index: uint, string: Number)
    fun add_item(string: Any /* JsTuple<String, Any> */, index: uint = definedExternally)
    fun add_item(string: Any /* JsTuple<String, Any> */)
    fun add_item(string: String, index: uint = definedExternally)
    fun add_item(string: String)
    fun add_item(string: Number, index: uint = definedExternally)
    fun add_item(string: Number)
    fun remove_item(index: uint)
    fun get_slider_minimum(): double
    fun get_slider_maximum(): double
    fun set_slider_minimum_maximum(minimum: double, maximum: double)
    fun get_slider_value_step(): double
    fun get_slider_discrete_slider(): Boolean?
    fun get_slider_discrete_values(): Boolean?
    fun set_slider_value_step(value: double)
    fun set_slider_discrete_slider(value: Boolean)
    fun set_slider_discrete_values(value: Boolean)
    fun focus()
    fun bring_to_front()
    var index: uint
    var gui: LuaGui
    var parent: dynamic /* ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers */
        get() = definedExternally
        set(value) = definedExternally
    var name: String
    var caption: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var style: dynamic /* LuaStyle | String */
        get() = definedExternally
        set(value) = definedExternally
    var visible: Boolean?
    var children_names: Array<String>
    var player_index: uint
    var sprite: dynamic /* String |  */
        get() = definedExternally
        set(value) = definedExternally
    var resize_to_sprite: Boolean?
    var clicked_sprite: dynamic /* String |  */
        get() = definedExternally
        set(value) = definedExternally
    var tooltip: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var type: String /* "choose-elem-button" | "drop-down" | "empty-widget" | "entity-preview" | "list-box" | "scroll-pane" | "sprite-button" | "tabbed-pane" | "text-box" | "button" | "camera" | "checkbox" | "flow" | "frame" | "label" | "line" | "minimap" | "progressbar" | "radiobutton" | "slider" | "sprite" | "switch" | "tab" | "table" | "textfield" */
    var children: Array<dynamic /* ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers */>
    var items: Array<dynamic /* JsTuple<String, Any> | String | Number */>
    var selected_index: uint
    var number: double?
    var show_percent_for_small_numbers: Boolean?
    var location: dynamic /* GuiLocationTable? | JsTuple<x, int, y, int> */
        get() = definedExternally
        set(value) = definedExternally
    var position: Position
    var surface_index: uint
    var zoom: double
    var force: String?
    var enabled: Boolean?
    var ignored_by_interaction: Boolean?
    var mouse_button_filter: dynamic /* MouseButtonFlagsTable | MouseButtonFlagsArray */
        get() = definedExternally
        set(value) = definedExternally
    var drag_target: dynamic /* ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers */
        get() = definedExternally
        set(value) = definedExternally
    var tabs: Array<TabAndContent>
    var entity: LuaEntity?
    var anchor: GuiAnchor?
    var tags: Tags
    var valid: Boolean?
    var object_name: String /* "LuaGuiElement" */
    fun help(): String
}

external interface ChooseElemButtonGuiElementMembers : BaseGuiElement {
    override var type: String /* "choose-elem-button" */
    var elem_type: String /* "item" | "tile" | "entity" | "signal" | "fluid" | "recipe" | "decorative" | "item-group" | "achievement" | "equipment" | "technology" */
    var elem_value: Any?
    var elem_filters: Any
    var locked: Boolean?
}

external interface DropDownGuiElementMembers : BaseGuiElement {
    override var type: String /* "drop-down" */
}

external interface EmptyWidgetGuiElementMembers : BaseGuiElement {
    override var type: String /* "empty-widget" */
}

external interface EntityPreviewGuiElementMembers : BaseGuiElement {
    override var type: String /* "entity-preview" */
}

external interface ListBoxGuiElementMembers : BaseGuiElement {
    override var type: String /* "list-box" */
    fun scroll_to_item(index: int, scroll_mode: String /* "in-view" | "top-third" */ = definedExternally)
}

external interface ScrollPaneGuiElementMembers : BaseGuiElement {
    override var type: String /* "scroll-pane" */
    fun scroll_to_top()
    fun scroll_to_bottom()
    fun scroll_to_left()
    fun scroll_to_right()
    fun scroll_to_element(element: Any /* ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers */, scroll_mode: String /* "in-view" | "top-third" */ = definedExternally)
    var horizontal_scroll_policy: String /* "auto" | "never" | "always" | "auto-and-reserve-space" | "dont-show-but-allow-scrolling" */
    var vertical_scroll_policy: String /* "auto" | "never" | "always" | "auto-and-reserve-space" | "dont-show-but-allow-scrolling" */
}

external interface SpriteButtonGuiElementMembers : BaseGuiElement {
    override var type: String /* "sprite-button" */
    var hovered_sprite: dynamic /* String |  */
        get() = definedExternally
        set(value) = definedExternally
}

external interface TabbedPaneGuiElementMembers : BaseGuiElement {
    override var type: String /* "tabbed-pane" */
    fun add_tab(tab: Any /* ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers */, content: Any /* ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers */)
    fun remove_tab(tab: Any /* ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers */)
    var selected_tab_index: uint?
}

external interface TextBoxGuiElementMembers : BaseGuiElement {
    override var type: String /* "text-box" */
    fun scroll_to_top()
    fun scroll_to_bottom()
    fun scroll_to_left()
    fun scroll_to_right()
    fun select_all()
    fun select(start: int, end: int)
    var text: String
    var selectable: Boolean?
    var word_wrap: Boolean?
    var read_only: Boolean?
    var clear_and_focus_on_right_click: Boolean?
}

external interface ButtonGuiElementMembers : BaseGuiElement {
    override var type: String /* "button" */
}

external interface CameraGuiElementMembers : BaseGuiElement {
    override var type: String /* "camera" */
}

external interface CheckboxGuiElementMembers : BaseGuiElement {
    override var type: String /* "checkbox" */
    var state: Boolean?
}

external interface FlowGuiElementMembers : BaseGuiElement {
    override var type: String /* "flow" */
    var direction: String /* "horizontal" | "vertical" */
}

external interface FrameGuiElementMembers : BaseGuiElement {
    override var type: String /* "frame" */
    fun force_auto_center()
    var direction: String /* "horizontal" | "vertical" */
    var auto_center: Boolean?
}

external interface LabelGuiElementMembers : BaseGuiElement {
    override var type: String /* "label" */
}

external interface LineGuiElementMembers : BaseGuiElement {
    override var type: String /* "line" */
    var direction: String /* "horizontal" | "vertical" */
}

external interface MinimapGuiElementMembers : BaseGuiElement {
    override var type: String /* "minimap" */
    var minimap_player_index: uint
}

external interface ProgressbarGuiElementMembers : BaseGuiElement {
    override var type: String /* "progressbar" */
    var value: double
}

external interface RadiobuttonGuiElementMembers : BaseGuiElement {
    override var type: String /* "radiobutton" */
    var state: Boolean?
}

external interface SliderGuiElementMembers : BaseGuiElement {
    override var type: String /* "slider" */
    var slider_value: double
}

external interface SpriteGuiElementMembers : BaseGuiElement {
    override var type: String /* "sprite" */
}

external interface SwitchGuiElementMembers : BaseGuiElement {
    override var type: String /* "switch" */
    var switch_state: String
    var allow_none_state: Boolean?
    var left_label_caption: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var left_label_tooltip: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var right_label_caption: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var right_label_tooltip: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
}

external interface TabGuiElementMembers : BaseGuiElement {
    override var type: String /* "tab" */
    var badge_text: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
}

external interface TableGuiElementMembers : BaseGuiElement {
    override var type: String /* "table" */
    var draw_vertical_lines: Boolean?
    var draw_horizontal_lines: Boolean?
    var draw_horizontal_line_after_headers: Boolean?
    var column_count: uint
    var vertical_centering: Boolean?
}

external interface TextfieldGuiElementMembers : BaseGuiElement {
    override var type: String /* "textfield" */
    fun select_all()
    fun select(start: int, end: int)
    var text: String
    var numeric: Boolean?
    var allow_decimal: Boolean?
    var allow_negative: Boolean?
    var is_password: Boolean?
    var lose_focus_on_confirm: Boolean?
    var clear_and_focus_on_right_click: Boolean?
}

external interface LuaHeatEnergySourcePrototype {
    var emissions: double
    var render_no_network_icon: Boolean?
    var render_no_power_icon: Boolean?
    var max_temperature: double
    var default_temperature: double
    var specific_heat: double
    var max_transfer: double
    var min_temperature_gradient: double
    var min_working_temperature: double
    var minimum_glow_temperature: double
    var connections: Array<HeatConnection>
    var valid: Boolean?
    var object_name: String /* "LuaHeatEnergySourcePrototype" */
    fun help(): String
}

external interface LuaInserterControlBehavior : LuaGenericOnOffControlBehavior {
    var circuit_read_hand_contents: Boolean?
    var circuit_mode_of_operation: circuit_mode_of_operation
    var circuit_hand_read_mode: hand_read_mode
    var circuit_set_stack_size: Boolean?
    var circuit_stack_control_signal: SignalID
    override var valid: Boolean?
    override var object_name: String /* "LuaInserterControlBehavior" */
    override fun help(): String
}

typealias LuaInventory = Array<LuaItemStack>

external interface LuaItemPrototype {
    fun has_flag(flag: String): Boolean?
    fun get_ammo_type(ammo_source_type: String /* "default" | "player" | "turret" | "vehicle" */ = definedExternally): AmmoType?
    var type: String /* "gun" | "mining-tool" */
    var name: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var order: String
    var place_result: LuaEntityPrototype?
    var place_as_equipment_result: LuaEquipmentPrototype?
    var place_as_tile_result: PlaceAsTileResult?
    var stackable: Boolean?
    var default_request_amount: uint
    var stack_size: uint
    var wire_count: uint
    var fuel_category: String?
    var burnt_result: LuaItemPrototype?
    var fuel_value: float
    var fuel_acceleration_multiplier: double
    var fuel_top_speed_multiplier: double
    var fuel_emissions_multiplier: double
    var subgroup: LuaGroup
    var group: LuaGroup
    var flags: ItemPrototypeFlags
    var rocket_launch_products: Array<dynamic /* FluidProduct | OtherProduct */>
    var can_be_mod_opened: Boolean?
    var magazine_size: float?
    var reload_time: float?
    var equipment_grid: LuaEquipmentGridPrototype?
    var resistances: Record<String, Resistance>?
    var inventory_size_bonus: uint?
    var capsule_action: CapsuleAction?
    var attack_parameters: dynamic /* ProjectileAttackParameters? | StreamAttackParameters? | OtherAttackParameters? */
        get() = definedExternally
        set(value) = definedExternally
    var inventory_size: uint?
    var item_filters: Record<String, LuaItemPrototype>
    var item_group_filters: Record<String, LuaGroup>
    var item_subgroup_filters: Record<String, LuaGroup>
    var filter_mode: String
    var insertion_priority_mode: String
    var localised_filter_message: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var extend_inventory_by_default: Boolean?
    var default_label_color: ColorTable?
    var draw_label_for_cursor_render: Boolean?
    var speed: float?
    var module_effects: ModuleEffects?
    var category: String
    var tier: uint
    var limitations: Array<String>
    var limitation_message_key: String
    var straight_rail: LuaEntityPrototype
    var curved_rail: LuaEntityPrototype
    var repair_result: Array<TriggerItem>?
    var selection_border_color: ColorTable
    var alt_selection_border_color: ColorTable
    var selection_mode_flags: SelectionModeFlags
    var alt_selection_mode_flags: SelectionModeFlags
    var selection_cursor_box_type: String
    var alt_selection_cursor_box_type: String
    var always_include_tiles: Boolean?
    var entity_filter_mode: String
    var alt_entity_filter_mode: String
    var tile_filter_mode: String
    var alt_tile_filter_mode: String
    var entity_filters: Record<String, LuaEntityPrototype>
    var alt_entity_filters: Record<String, LuaEntityPrototype>
    var entity_type_filters: Record<String, Boolean>
    var alt_entity_type_filters: Record<String, Boolean>
    var tile_filters: Record<String, LuaTilePrototype>
    var alt_tile_filters: Record<String, LuaTilePrototype>
    var entity_filter_slots: uint?
    var tile_filter_slots: uint?
    var durability_description_key: String
    var durability: double?
    var infinite: Boolean?
    var mapper_count: uint?
    var valid: Boolean?
    var object_name: String /* "LuaItemPrototype" */
    fun help(): String
}

external interface `T$42` {
    var surface: dynamic /* uint | String | LuaSurface */
        get() = definedExternally
        set(value) = definedExternally
    var force: dynamic /* String | LuaForce */
        get() = definedExternally
        set(value) = definedExternally
    var position: Position
    var force_build: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var direction: direction?
        get() = definedExternally
        set(value) = definedExternally
    var skip_fog_of_war: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var by_player: dynamic /* uint? | String? | LuaPlayer? */
        get() = definedExternally
        set(value) = definedExternally
    var raise_built: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$43` {
    var surface: dynamic /* uint | String | LuaSurface */
        get() = definedExternally
        set(value) = definedExternally
    var force: dynamic /* String | LuaForce */
        get() = definedExternally
        set(value) = definedExternally
    var area: dynamic /* BoundingBoxTable | JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */
        get() = definedExternally
        set(value) = definedExternally
    var skip_fog_of_war: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var by_player: dynamic /* uint? | String? | LuaPlayer? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$44` {
    var surface: dynamic /* uint | String | LuaSurface */
        get() = definedExternally
        set(value) = definedExternally
    var force: dynamic /* String | LuaForce */
        get() = definedExternally
        set(value) = definedExternally
    var area: dynamic /* BoundingBoxTable | JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */
        get() = definedExternally
        set(value) = definedExternally
    var always_include_tiles: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var include_entities: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var include_modules: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var include_station_names: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var include_trains: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var include_fuel: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface LuaItemStack {
    fun is_blueprint_setup(): Boolean?
    fun get_blueprint_entities(): Array<BlueprintEntity>
    fun set_blueprint_entities(entities: Array<BlueprintEntity>)
    fun add_ammo(amount: float)
    fun drain_ammo(amount: float)
    fun add_durability(amount: double)
    fun drain_durability(amount: double)
    fun can_set_stack(stack: String = definedExternally): Boolean?
    fun can_set_stack(): Boolean?
    fun can_set_stack(stack: ItemStackDefinition = definedExternally): Boolean?
    fun can_set_stack(stack: LuaItemStack = definedExternally): Boolean?
    fun set_stack(stack: String = definedExternally): Boolean?
    fun set_stack(): Boolean?
    fun set_stack(stack: ItemStackDefinition = definedExternally): Boolean?
    fun set_stack(stack: LuaItemStack = definedExternally): Boolean?
    fun transfer_stack(stack: String): Boolean?
    fun transfer_stack(stack: ItemStackDefinition): Boolean?
    fun transfer_stack(stack: LuaItemStack): Boolean?
    fun export_stack(): String
    fun import_stack(data: String): int
    fun swap_stack(stack: LuaItemStack): Boolean?
    fun clear()
    fun get_blueprint_tiles(): Array<Tile>
    fun set_blueprint_tiles(tiles: Array<Tile>)
    fun get_inventory(inventory: inventory): LuaInventory?
    fun build_blueprint(params: `T$42`): Array<LuaEntity>
    fun deconstruct_area(params: `T$43`)
    fun cancel_deconstruct_area(params: `T$43`)
    fun create_blueprint(params: `T$44`): Record<uint, LuaEntity>
    fun get_tag(tag_name: String): dynamic /* String? | Number? | Boolean? | table? */
    fun set_tag(tag_name: String, tag: String): dynamic /* String | Number | Boolean | table */
    fun set_tag(tag_name: String, tag: Number): dynamic /* String | Number | Boolean | table */
    fun set_tag(tag_name: String, tag: Boolean): dynamic /* String | Number | Boolean | table */
    fun set_tag(tag_name: String, tag: table): dynamic /* String | Number | Boolean | table */
    fun remove_tag(tag: String): Boolean?
    fun clear_blueprint()
    fun get_entity_filter(index: uint): String
    fun set_entity_filter(index: uint, filter: String?): Boolean?
    fun set_entity_filter(index: uint, filter: LuaEntityPrototype?): Boolean?
    fun set_entity_filter(index: uint, filter: LuaEntity?): Boolean?
    fun get_tile_filter(index: uint): String
    fun set_tile_filter(index: uint, filter: String?): Boolean?
    fun set_tile_filter(index: uint, filter: LuaTilePrototype?): Boolean?
    fun set_tile_filter(index: uint, filter: LuaTile?): Boolean?
    fun clear_deconstruction_item()
    fun clear_upgrade_item()
    fun get_mapper(index: uint, type: String /* "from" | "to" */): UpgradeFilter
    fun set_mapper(index: uint, type: String, filter: UpgradeFilter?)
    fun get_blueprint_entity_count(): uint
    fun get_blueprint_entity_tags(index: uint): Tags
    fun set_blueprint_entity_tags(index: uint, tags: Tags)
    fun get_blueprint_entity_tag(index: uint, tag: String): dynamic /* String | Number | Boolean | table */
    fun set_blueprint_entity_tag(index: uint, tag: String, value: String?)
    fun set_blueprint_entity_tag(index: uint, tag: String, value: Number?)
    fun set_blueprint_entity_tag(index: uint, tag: String, value: Boolean?)
    fun set_blueprint_entity_tag(index: uint, tag: String, value: table?)
    fun create_grid(): LuaEquipmentGrid
    var valid_for_read: Boolean?
    var prototype: LuaItemPrototype
    var name: String
    var type: String
    var count: uint
    var grid: LuaEquipmentGrid?
    var health: float
    var durability: double?
    var ammo: uint
    var blueprint_icons: Array<BlueprintSignalIcon>?
    var blueprint_snap_to_grid: Position?
    var blueprint_position_relative_to_grid: Position?
    var blueprint_absolute_snapping: Boolean?
    var label: String?
    var label_color: dynamic /* ColorTable? | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var allow_manual_label_change: Boolean?
    var cost_to_build: Record<String, uint>
    var extends_inventory: Boolean?
    var prioritize_insertion_mode: String
    var default_icons: Array<BlueprintItemIcon>
    var tags: Tags
    var custom_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var entity_filters: Array<String>
    var tile_filters: Array<String>
    var entity_filter_mode: entity_filter_mode
    var tile_filter_mode: tile_filter_mode
    var tile_selection_mode: tile_selection_mode
    var trees_and_rocks_only: Boolean?
    var entity_filter_count: uint
    var tile_filter_count: uint
    var active_index: uint?
    var item_number: uint?
    var connected_entity: LuaEntity?
    var is_blueprint: Boolean?
    var is_blueprint_book: Boolean?
    var is_module: Boolean?
    var is_tool: Boolean?
    var is_mining_tool: Boolean?
    var is_armor: Boolean?
    var is_repair_tool: Boolean?
    var is_item_with_label: Boolean?
    var is_item_with_inventory: Boolean?
    var is_item_with_entity_data: Boolean?
    var is_selection_tool: Boolean?
    var is_item_with_tags: Boolean?
    var is_deconstruction_item: Boolean?
    var is_upgrade_item: Boolean?
    var valid: Boolean?
    var object_name: String /* "LuaItemStack" */
    fun help(): String
}

external interface LuaLampControlBehavior : LuaGenericOnOffControlBehavior {
    var use_colors: Boolean?
    var color: ColorTable?
    override var valid: Boolean?
    override var object_name: String /* "LuaLampControlBehavior" */
    override fun help(): String
}

external interface LuaLazyLoadedValue<T> {
    fun get(): T
    var valid: Boolean?
    var object_name: String /* "LuaLazyLoadedValue" */
    fun help(): String
}

external interface LuaLogisticCell {
    fun is_in_logistic_range(position: Position): Boolean?
    fun is_in_construction_range(position: Position): Boolean?
    fun is_neighbour_with(other: LuaLogisticCell): Boolean?
    var logistic_radius: float
    var logistics_connection_distance: float
    var construction_radius: float
    var stationed_logistic_robot_count: uint
    var stationed_construction_robot_count: uint
    var mobile: Boolean?
    var transmitting: Boolean?
    var charge_approach_distance: float
    var charging_robot_count: uint
    var to_charge_robot_count: uint
    var owner: LuaEntity
    var logistic_network: LuaLogisticNetwork?
    var neighbours: Array<LuaLogisticCell>
    var charging_robots: Array<LuaEntity>
    var to_charge_robots: Array<LuaEntity>
    var valid: Boolean?
    var object_name: String /* "LuaLogisticCell" */
    fun help(): String
}

external interface LuaLogisticContainerControlBehavior : LuaControlBehavior {
    var circuit_mode_of_operation: _defines_control_behavior_logistic_container_circuit_mode_of_operation
    var valid: Boolean?
    var object_name: String /* "LuaLogisticContainerControlBehavior" */
    fun help(): String
}

external interface `T$45` {
    var name: String
    var position: Position?
        get() = definedExternally
        set(value) = definedExternally
    var include_buffers: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var members: String? /* "storage" | "passive-provider" | "buffer" | "active-provider" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$46` {
    var stack: dynamic /* String | ItemStackDefinition | LuaItemStack */
        get() = definedExternally
        set(value) = definedExternally
    var members: String? /* "storage" | "storage-empty" | "storage-empty-slot" | "requester" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface LuaLogisticNetwork {
    fun get_item_count(item: String = definedExternally, member: String /* "storage" | "providers" */ = definedExternally): int
    fun get_contents(): Record<String, uint>
    fun remove_item(item: String, members: String /* "storage" | "passive-provider" | "buffer" | "active-provider" */ = definedExternally): uint
    fun remove_item(item: String): uint
    fun remove_item(item: ItemStackDefinition, members: String /* "storage" | "passive-provider" | "buffer" | "active-provider" */ = definedExternally): uint
    fun remove_item(item: ItemStackDefinition): uint
    fun remove_item(item: LuaItemStack, members: String /* "storage" | "passive-provider" | "buffer" | "active-provider" */ = definedExternally): uint
    fun remove_item(item: LuaItemStack): uint
    fun insert(item: String, members: String /* "storage" | "storage-empty" | "storage-empty-slot" | "requester" */ = definedExternally): uint
    fun insert(item: String): uint
    fun insert(item: ItemStackDefinition, members: String /* "storage" | "storage-empty" | "storage-empty-slot" | "requester" */ = definedExternally): uint
    fun insert(item: ItemStackDefinition): uint
    fun insert(item: LuaItemStack, members: String /* "storage" | "storage-empty" | "storage-empty-slot" | "requester" */ = definedExternally): uint
    fun insert(item: LuaItemStack): uint
    fun find_cell_closest_to(position: Position): LuaLogisticCell?
    fun select_pickup_point(params: `T$45`): LuaLogisticPoint?
    fun select_drop_point(params: `T$46`): LuaLogisticPoint?
    var force: LuaForce
    var available_logistic_robots: uint
    var all_logistic_robots: uint
    var available_construction_robots: uint
    var all_construction_robots: uint
    var robot_limit: uint
    var cells: Array<LuaLogisticCell>
    var providers: Array<LuaEntity>
    var empty_providers: Array<LuaEntity>
    var requesters: Array<LuaEntity>
    var storages: Array<LuaEntity>
    var logistic_members: Array<LuaEntity>
    var provider_points: Array<LuaLogisticPoint>
    var passive_provider_points: Array<LuaLogisticPoint>
    var active_provider_points: Array<LuaLogisticPoint>
    var empty_provider_points: Array<LuaLogisticPoint>
    var requester_points: Array<LuaLogisticPoint>
    var storage_points: Array<LuaLogisticPoint>
    var robots: Array<LuaEntity>
    var construction_robots: Array<LuaEntity>
    var logistic_robots: Array<LuaEntity>
    var valid: Boolean?
    var object_name: String /* "LuaLogisticNetwork" */
    fun help(): String
}

external interface LuaLogisticPoint {
    var owner: LuaEntity
    var logistic_network: LuaLogisticNetwork
    var logistic_member_index: uint
    var filters: Array<LogisticFilter>?
    var mode: logistic_mode
    var force: LuaForce
    var targeted_items_pickup: Record<String, uint>
    var targeted_items_deliver: Record<String, uint>
    var exact: Boolean?
    var valid: Boolean?
    var object_name: String /* "LuaLogisticPoint" */
    fun help(): String
}

external interface LuaMiningDrillControlBehavior : LuaGenericOnOffControlBehavior {
    var circuit_enable_disable: Boolean?
    var circuit_read_resources: Boolean?
    var resource_read_mode: resource_read_mode
    var resource_read_targets: Array<LuaEntity>
    override var valid: Boolean?
    override var object_name: String /* "LuaMiningDrillControlBehavior" */
    override fun help(): String
}

external interface LuaModSettingPrototype {
    var name: String
    var order: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var mod: String
    var setting_type: String
    var default_value: dynamic /* Boolean | double | int | String */
        get() = definedExternally
        set(value) = definedExternally
    var minimum_value: dynamic /* double? | int? */
        get() = definedExternally
        set(value) = definedExternally
    var maximum_value: dynamic /* double? | int? */
        get() = definedExternally
        set(value) = definedExternally
    var allowed_values: dynamic /* Array<String>? | Array<int>? | Array<double>? */
        get() = definedExternally
        set(value) = definedExternally
    var allow_blank: Boolean?
    var auto_trim: Boolean?
    var hidden: Boolean?
    var valid: Boolean?
    var object_name: String /* "LuaModSettingPrototype" */
    fun help(): String
}

external interface LuaModuleCategoryPrototype {
    var name: String
    var order: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var valid: Boolean?
    var object_name: String /* "LuaModuleCategoryPrototype" */
    fun help(): String
}

external interface LuaNamedNoiseExpression {
    var name: String
    var order: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var intended_property: String
    var expression: NoiseExpression
    var valid: Boolean?
    var object_name: String /* "LuaNamedNoiseExpression" */
    fun help(): String
}

external interface LuaNoiseLayerPrototype {
    var name: String
    var order: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var valid: Boolean?
    var object_name: String /* "LuaNoiseLayerPrototype" */
    fun help(): String
}

external interface LuaParticlePrototype {
    var name: String
    var order: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var regular_trigger_effect: TriggerEffectItem
    var ended_in_water_trigger_effect: TriggerEffectItem
    var render_layer: dynamic /* Number |  */
        get() = definedExternally
        set(value) = definedExternally
    var render_layer_when_on_ground: dynamic /* Number |  */
        get() = definedExternally
        set(value) = definedExternally
    var life_time: uint
    var regular_trigger_effect_frequency: uint
    var movement_modifier_when_on_ground: float
    var movement_modifier: float
    var mining_particle_frame_speed: float
    var valid: Boolean?
    var object_name: String /* "LuaParticlePrototype" */
    fun help(): String
}

external interface LuaPermissionGroup {
    fun add_player(player: uint): Boolean?
    fun add_player(player: String): Boolean?
    fun add_player(player: LuaPlayer): Boolean?
    fun remove_player(player: uint): Boolean?
    fun remove_player(player: String): Boolean?
    fun remove_player(player: LuaPlayer): Boolean?
    fun allows_action(action: input_action): Boolean?
    fun set_allows_action(action: input_action, allow_action: Boolean): Boolean?
    fun destroy(): Boolean?
    var name: String
    var players: Array<LuaPlayer>
    var group_id: uint
    var valid: Boolean?
    var object_name: String /* "LuaPermissionGroup" */
    fun help(): String
}

external interface LuaPermissionGroups {
    fun create_group(name: String = definedExternally): LuaPermissionGroup?
    fun get_group(group: String): LuaPermissionGroup?
    fun get_group(group: uint): LuaPermissionGroup?
    var groups: Array<LuaPermissionGroup>
    var valid: Boolean?
    var object_name: String /* "LuaPermissionGroups" */
    fun help(): String
}

external interface `T$47` {
    var type: controllers
    var character: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
    var waypoints: CutsceneWaypoint?
        get() = definedExternally
        set(value) = definedExternally
    var start_position: Position?
        get() = definedExternally
        set(value) = definedExternally
    var start_zoom: double?
        get() = definedExternally
        set(value) = definedExternally
    var final_transition_time: uint?
        get() = definedExternally
        set(value) = definedExternally
    var chart_mode_cutoff: double?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$48` {
    var position: Position
}

external interface `T$49` {
    var entity: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
    var prototype: LuaEntityPrototype?
        get() = definedExternally
        set(value) = definedExternally
    var position: Position?
        get() = definedExternally
        set(value) = definedExternally
    var type: alert_type?
        get() = definedExternally
        set(value) = definedExternally
    var surface: dynamic /* uint? | String? | LuaSurface? */
        get() = definedExternally
        set(value) = definedExternally
    var icon: SignalID?
        get() = definedExternally
        set(value) = definedExternally
    var message: dynamic /* JsTuple<String, Any> | String? | Number? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$50` {
    var entity: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
    var prototype: LuaEntityPrototype?
        get() = definedExternally
        set(value) = definedExternally
    var position: Position?
        get() = definedExternally
        set(value) = definedExternally
    var type: alert_type?
        get() = definedExternally
        set(value) = definedExternally
    var surface: dynamic /* uint? | String? | LuaSurface? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$51` {
    var name: String
    var position: Position
    var direction: direction?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$52` {
    var position: Position
    var direction: direction?
        get() = definedExternally
        set(value) = definedExternally
    var alt: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var terrain_building_size: uint?
        get() = definedExternally
        set(value) = definedExternally
    var skip_fog_of_war: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$53` {
    var text: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var position: Position?
        get() = definedExternally
        set(value) = definedExternally
    var create_at_cursor: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var color: dynamic /* ColorTable? | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var time_to_live: uint?
        get() = definedExternally
        set(value) = definedExternally
    var speed: double?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$54` {
    var address: String
    var name: dynamic /* JsTuple<String, Any> | String? | Number? */
        get() = definedExternally
        set(value) = definedExternally
    var description: dynamic /* JsTuple<String, Any> | String? | Number? */
        get() = definedExternally
        set(value) = definedExternally
    var password: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface LuaPlayer : LuaControl {
    fun set_ending_screen_data(message: Any /* JsTuple<String, Any> */, file: String = definedExternally)
    fun set_ending_screen_data(message: Any /* JsTuple<String, Any> */)
    fun set_ending_screen_data(message: String, file: String = definedExternally)
    fun set_ending_screen_data(message: String)
    fun set_ending_screen_data(message: Number, file: String = definedExternally)
    fun set_ending_screen_data(message: Number)
    fun print(message: Any /* JsTuple<String, Any> */, color: ColorTable = definedExternally)
    fun print(message: Any /* JsTuple<String, Any> */)
    fun print(message: Any /* JsTuple<String, Any> */, color: Any /* JsTuple<Any, float, Any, float, Any, float, Any, float> */ = definedExternally)
    fun print(message: String, color: ColorTable = definedExternally)
    fun print(message: String)
    fun print(message: String, color: Any /* JsTuple<Any, float, Any, float, Any, float, Any, float> */ = definedExternally)
    fun print(message: Number, color: ColorTable = definedExternally)
    fun print(message: Number)
    fun print(message: Number, color: Any /* JsTuple<Any, float, Any, float, Any, float, Any, float> */ = definedExternally)
    fun clear_console()
    fun get_goal_description(): dynamic /* JsTuple<String, Any> | String | Number */
    fun set_goal_description(text: Any /* JsTuple<String, Any> */ = definedExternally, only_update: Boolean = definedExternally)
    fun set_goal_description()
    fun set_goal_description(text: Any /* JsTuple<String, Any> */ = definedExternally)
    fun set_goal_description(text: String = definedExternally, only_update: Boolean = definedExternally)
    fun set_goal_description(text: String = definedExternally)
    fun set_goal_description(text: Number = definedExternally, only_update: Boolean = definedExternally)
    fun set_goal_description(text: Number = definedExternally)
    fun set_controller(params: `T$47`)
    fun drag_wire(params: `T$48`): Boolean?
    fun disable_recipe_groups()
    fun enable_recipe_groups()
    fun disable_recipe_subgroups()
    fun enable_recipe_subgroups()
    fun print_entity_statistics(entities: Array<String> = definedExternally)
    fun print_robot_jobs()
    fun print_lua_object_statistics()
    fun log_active_entity_chunk_counts()
    fun log_active_entity_counts()
    fun unlock_achievement(name: String)
    fun clear_cursor(): Boolean?
    fun create_character(character: String = definedExternally): Boolean?
    fun add_alert(entity: LuaEntity, type: alert_type)
    fun add_custom_alert(entity: LuaEntity, icon: SignalID, message: Any /* JsTuple<String, Any> */, show_on_map: Boolean)
    fun add_custom_alert(entity: LuaEntity, icon: SignalID, message: String, show_on_map: Boolean)
    fun add_custom_alert(entity: LuaEntity, icon: SignalID, message: Number, show_on_map: Boolean)
    fun remove_alert(params: `T$49`)
    fun get_alerts(params: `T$50`): Record<uint, Record<alert_type, Array<Alert>>>
    fun mute_alert(alert_type: alert_type): Boolean?
    fun unmute_alert(alert_type: alert_type): Boolean?
    fun is_alert_muted(alert_type: alert_type): Boolean?
    fun enable_alert(alert_type: alert_type): Boolean?
    fun disable_alert(alert_type: alert_type): Boolean?
    fun is_alert_enabled(alert_type: alert_type): Boolean?
    fun pipette_entity(entity: String): Boolean?
    fun pipette_entity(entity: LuaEntity): Boolean?
    fun pipette_entity(entity: LuaEntityPrototype): Boolean?
    fun can_place_entity(params: `T$51`): Boolean?
    fun can_build_from_cursor(params: `T$52`): Boolean?
    fun build_from_cursor(params: `T$52`)
    fun use_from_cursor(position: Position)
    fun play_sound(params: `T$34`)
    fun get_associated_characters(): Array<LuaEntity>
    fun associate_character(character: LuaEntity)
    fun disassociate_character(character: LuaEntity)
    fun create_local_flying_text(params: `T$53`)
    fun get_quick_bar_slot(index: uint): LuaItemPrototype?
    fun set_quick_bar_slot(index: uint, filter: String?)
    fun set_quick_bar_slot(index: uint, filter: LuaItemPrototype?)
    fun set_quick_bar_slot(index: uint, filter: LuaItemStack?)
    fun get_active_quick_bar_page(index: uint): uint8?
    fun set_active_quick_bar_page(screen_index: uint, page_index: uint)
    fun jump_to_cutscene_waypoint(waypoint_index: uint)
    fun exit_cutscene()
    fun open_map(position: Position, scale: double = definedExternally)
    fun zoom_to_world(position: Position, scale: double = definedExternally)
    fun close_map()
    fun is_shortcut_toggled(prototype_name: String): Boolean?
    fun is_shortcut_available(prototype_name: String): Boolean?
    fun set_shortcut_toggled(prototype_name: String, toggled: Boolean)
    fun set_shortcut_available(prototype_name: String, available: Boolean)
    fun connect_to_server(params: `T$54`)
    fun toggle_map_editor()
    fun request_translation(localised_string: Any /* JsTuple<String, Any> */): Boolean?
    fun request_translation(localised_string: String): Boolean?
    fun request_translation(localised_string: Number): Boolean?
    fun get_infinity_inventory_filter(index: uint): InfinityInventoryFilter?
    fun set_infinity_inventory_filter(index: uint, filter: InfinityInventoryFilter?)
    fun clear_recipe_notifications()
    fun add_recipe_notification(recipe: String)
    fun add_to_clipboard(blueprint: LuaItemStack)
    fun activate_paste()
    fun start_selection(position: Position, selection_mode: String)
    fun clear_selection()
    var character: LuaEntity?
    var cutscene_character: LuaEntity?
    var index: uint
    var gui: LuaGui
    var opened_self: Boolean?
    var controller_type: controllers
    var stashed_controller_type: controllers?
    var game_view_settings: GameViewSettings
    var minimap_enabled: Boolean?
    var color: dynamic /* ColorTable | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var chat_color: dynamic /* ColorTable | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var name: String
    var tag: String
    var connected: Boolean?
    var admin: Boolean?
    var entity_copy_source: LuaEntity?
    var afk_time: uint
    var online_time: uint
    var last_online: uint
    var permission_group: LuaPermissionGroup?
    var mod_settings: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, ModSetting> */
    var ticks_to_respawn: uint?
    var display_resolution: DisplayResolution
    var display_scale: double
    var blueprint_to_setup: LuaItemStack
    var render_mode: render_mode
    var spectator: Boolean?
    var remove_unfiltered_items: Boolean?
    var infinity_inventory_filters: Array<InfinityInventoryFilter>
    var auto_sort_main_inventory: Boolean?
    var hand_location: ItemStackLocation
}

external interface LuaProfiler {
    fun reset()
    fun stop()
    fun restart()
    fun add(other: LuaProfiler)
    fun divide(number: double)
    var valid: Boolean?
    var object_name: String /* "LuaProfiler" */
    fun help(): String
}

external interface LuaProgrammableSpeakerControlBehavior : LuaControlBehavior {
    var circuit_parameters: ProgrammableSpeakerCircuitParameters
    var circuit_condition: CircuitConditionDefinition
    var valid: Boolean?
    var object_name: String /* "LuaProgrammableSpeakerControlBehavior" */
    fun help(): String
}

external interface LuaRCON {
    fun print(message: Any /* JsTuple<String, Any> */)
    fun print(message: String)
    fun print(message: Number)
    var object_name: String /* "LuaRCON" */
}

external interface LuaRailChainSignalControlBehavior : LuaControlBehavior {
    var red_signal: SignalID
    var orange_signal: SignalID
    var green_signal: SignalID
    var blue_signal: SignalID
    var valid: Boolean?
    var object_name: String /* "LuaRailChainSignalControlBehavior" */
    fun help(): String
}

external interface LuaRailPath {
    var size: uint
    var current: uint
    var total_distance: double
    var travelled_distance: double
    var rails: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<uint, LuaEntity> */
    var valid: Boolean?
    var object_name: String /* "LuaRailPath" */
    fun help(): String
}

external interface LuaRailSignalControlBehavior : LuaControlBehavior {
    var red_signal: SignalID
    var orange_signal: SignalID
    var green_signal: SignalID
    var close_signal: Boolean?
    var read_signal: Boolean?
    var circuit_condition: CircuitConditionDefinition
    var valid: Boolean?
    var object_name: String /* "LuaRailSignalControlBehavior" */
    fun help(): String
}

external interface LuaRandomGenerator {
    fun re_seed(seed: uint)
    @nativeInvoke
    operator fun invoke(lower: int = definedExternally, upper: int = definedExternally): double
    var valid: Boolean?
    var object_name: String /* "LuaRandomGenerator" */
    fun help(): String
}

external interface LuaRecipe {
    fun reload()
    var name: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var prototype: LuaRecipePrototype
    var enabled: Boolean?
    var category: String
    var ingredients: Array<dynamic /* FluidIngredient | OtherIngredient */>
    var products: Array<dynamic /* FluidProduct | OtherProduct */>
    var hidden: Boolean?
    var hidden_from_flow_stats: Boolean?
    var energy: double
    var order: String
    var group: LuaGroup
    var subgroup: LuaGroup
    var force: LuaForce
    var valid: Boolean?
    var object_name: String /* "LuaRecipe" */
    fun help(): String
}

external interface LuaRecipeCategoryPrototype {
    var name: String
    var order: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var valid: Boolean?
    var object_name: String /* "LuaRecipeCategoryPrototype" */
    fun help(): String
}

external interface LuaRecipePrototype {
    var enabled: Boolean?
    var name: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var category: String
    var ingredients: Array<dynamic /* FluidIngredient | OtherIngredient */>
    var products: Array<dynamic /* FluidProduct | OtherProduct */>
    var main_product: dynamic /* FluidProduct? | OtherProduct? */
        get() = definedExternally
        set(value) = definedExternally
    var hidden: Boolean?
    var hidden_from_flow_stats: Boolean?
    var hidden_from_player_crafting: Boolean?
    var always_show_made_in: Boolean?
    var energy: double
    var order: String
    var group: LuaGroup
    var subgroup: LuaGroup
    var request_paste_multiplier: uint
    var overload_multiplier: uint
    var allow_inserter_overload: Boolean?
    var allow_as_intermediate: Boolean?
    var allow_intermediates: Boolean?
    var show_amount_in_title: Boolean?
    var always_show_products: Boolean?
    var emissions_multiplier: double
    var allow_decomposition: Boolean?
    var unlock_results: Boolean?
    var valid: Boolean?
    var object_name: String /* "LuaRecipePrototype" */
    fun help(): String
}

external interface LuaRemote {
    fun add_interface(name: String, functions: Record<String, (args: Any) -> Unit>)
    fun remove_interface(name: String): Boolean?
    fun call(_interface: String, _function: String, vararg args: Any): Any
    var interfaces: Record<String, Record<String, Boolean>>
    var object_name: String /* "LuaRemote" */
}

external interface `T$55` {
    var color: dynamic /* ColorTable | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var width: float
    var gap_length: double?
        get() = definedExternally
        set(value) = definedExternally
    var dash_length: double?
        get() = definedExternally
        set(value) = definedExternally
    var from: dynamic /* Position | LuaEntity */
        get() = definedExternally
        set(value) = definedExternally
    var from_offset: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var to: dynamic /* Position | LuaEntity */
        get() = definedExternally
        set(value) = definedExternally
    var to_offset: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var surface: dynamic /* uint | String | LuaSurface */
        get() = definedExternally
        set(value) = definedExternally
    var time_to_live: uint?
        get() = definedExternally
        set(value) = definedExternally
    var forces: Array<dynamic /* String | LuaForce */>?
        get() = definedExternally
        set(value) = definedExternally
    var players: Array<dynamic /* uint | String | LuaPlayer */>?
        get() = definedExternally
        set(value) = definedExternally
    var visible: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var draw_on_ground: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var only_in_alt_mode: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$56` {
    var text: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var surface: dynamic /* uint | String | LuaSurface */
        get() = definedExternally
        set(value) = definedExternally
    var target: dynamic /* Position | LuaEntity */
        get() = definedExternally
        set(value) = definedExternally
    var target_offset: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var color: dynamic /* ColorTable | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var scale: double?
        get() = definedExternally
        set(value) = definedExternally
    var font: String?
        get() = definedExternally
        set(value) = definedExternally
    var time_to_live: uint?
        get() = definedExternally
        set(value) = definedExternally
    var forces: Array<dynamic /* String | LuaForce */>?
        get() = definedExternally
        set(value) = definedExternally
    var players: Array<dynamic /* uint | String | LuaPlayer */>?
        get() = definedExternally
        set(value) = definedExternally
    var visible: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var draw_on_ground: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var orientation: RealOrientation?
        get() = definedExternally
        set(value) = definedExternally
    var alignment: String? /* "left" | "right" | "center" */
        get() = definedExternally
        set(value) = definedExternally
    var vertical_alignment: String? /* "top" | "middle" | "baseline" | "bottom" */
        get() = definedExternally
        set(value) = definedExternally
    var scale_with_zoom: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var only_in_alt_mode: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$57` {
    var color: dynamic /* ColorTable | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var radius: double
    var width: float?
        get() = definedExternally
        set(value) = definedExternally
    var filled: Boolean?
    var target: dynamic /* Position | LuaEntity */
        get() = definedExternally
        set(value) = definedExternally
    var target_offset: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var surface: dynamic /* uint | String | LuaSurface */
        get() = definedExternally
        set(value) = definedExternally
    var time_to_live: uint?
        get() = definedExternally
        set(value) = definedExternally
    var forces: Array<dynamic /* String | LuaForce */>?
        get() = definedExternally
        set(value) = definedExternally
    var players: Array<dynamic /* uint | String | LuaPlayer */>?
        get() = definedExternally
        set(value) = definedExternally
    var visible: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var draw_on_ground: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var only_in_alt_mode: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$58` {
    var color: dynamic /* ColorTable | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var width: float?
        get() = definedExternally
        set(value) = definedExternally
    var filled: Boolean?
    var left_top: dynamic /* Position | LuaEntity */
        get() = definedExternally
        set(value) = definedExternally
    var left_top_offset: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var right_bottom: dynamic /* Position | LuaEntity */
        get() = definedExternally
        set(value) = definedExternally
    var right_bottom_offset: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var surface: dynamic /* uint | String | LuaSurface */
        get() = definedExternally
        set(value) = definedExternally
    var time_to_live: uint?
        get() = definedExternally
        set(value) = definedExternally
    var forces: Array<dynamic /* String | LuaForce */>?
        get() = definedExternally
        set(value) = definedExternally
    var players: Array<dynamic /* uint | String | LuaPlayer */>?
        get() = definedExternally
        set(value) = definedExternally
    var visible: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var draw_on_ground: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var only_in_alt_mode: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$59` {
    var color: dynamic /* ColorTable | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var max_radius: double
    var min_radius: double
    var start_angle: float
    var angle: float
    var target: dynamic /* Position | LuaEntity */
        get() = definedExternally
        set(value) = definedExternally
    var target_offset: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var surface: dynamic /* uint | String | LuaSurface */
        get() = definedExternally
        set(value) = definedExternally
    var time_to_live: uint?
        get() = definedExternally
        set(value) = definedExternally
    var forces: Array<dynamic /* String | LuaForce */>?
        get() = definedExternally
        set(value) = definedExternally
    var players: Array<dynamic /* uint | String | LuaPlayer */>?
        get() = definedExternally
        set(value) = definedExternally
    var visible: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var draw_on_ground: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var only_in_alt_mode: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$60` {
    var color: dynamic /* ColorTable | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var vertices: Array<ScriptRenderVertexTarget>
    var target: dynamic /* Position? | LuaEntity? */
        get() = definedExternally
        set(value) = definedExternally
    var target_offset: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var orientation: RealOrientation?
        get() = definedExternally
        set(value) = definedExternally
    var orientation_target: dynamic /* Position? | LuaEntity? */
        get() = definedExternally
        set(value) = definedExternally
    var orientation_target_offset: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var surface: dynamic /* uint | String | LuaSurface */
        get() = definedExternally
        set(value) = definedExternally
    var time_to_live: uint?
        get() = definedExternally
        set(value) = definedExternally
    var forces: Array<dynamic /* String | LuaForce */>?
        get() = definedExternally
        set(value) = definedExternally
    var players: Array<dynamic /* uint | String | LuaPlayer */>?
        get() = definedExternally
        set(value) = definedExternally
    var visible: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var draw_on_ground: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var only_in_alt_mode: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$61` {
    var sprite: dynamic /* String |  */
        get() = definedExternally
        set(value) = definedExternally
    var orientation: RealOrientation?
        get() = definedExternally
        set(value) = definedExternally
    var x_scale: double?
        get() = definedExternally
        set(value) = definedExternally
    var y_scale: double?
        get() = definedExternally
        set(value) = definedExternally
    var tint: dynamic /* ColorTable? | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var render_layer: dynamic /* Number? | ? */
        get() = definedExternally
        set(value) = definedExternally
    var orientation_target: dynamic /* Position? | LuaEntity? */
        get() = definedExternally
        set(value) = definedExternally
    var orientation_target_offset: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var oriented_offset: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var target: dynamic /* Position | LuaEntity */
        get() = definedExternally
        set(value) = definedExternally
    var target_offset: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var surface: dynamic /* uint | String | LuaSurface */
        get() = definedExternally
        set(value) = definedExternally
    var time_to_live: uint?
        get() = definedExternally
        set(value) = definedExternally
    var forces: Array<dynamic /* String | LuaForce */>?
        get() = definedExternally
        set(value) = definedExternally
    var players: Array<dynamic /* uint | String | LuaPlayer */>?
        get() = definedExternally
        set(value) = definedExternally
    var visible: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var only_in_alt_mode: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$62` {
    var sprite: dynamic /* String |  */
        get() = definedExternally
        set(value) = definedExternally
    var orientation: RealOrientation?
        get() = definedExternally
        set(value) = definedExternally
    var scale: float?
        get() = definedExternally
        set(value) = definedExternally
    var intensity: float?
        get() = definedExternally
        set(value) = definedExternally
    var minimum_darkness: float?
        get() = definedExternally
        set(value) = definedExternally
    var oriented: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var color: dynamic /* ColorTable? | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var target: dynamic /* Position | LuaEntity */
        get() = definedExternally
        set(value) = definedExternally
    var target_offset: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var surface: dynamic /* uint | String | LuaSurface */
        get() = definedExternally
        set(value) = definedExternally
    var time_to_live: uint?
        get() = definedExternally
        set(value) = definedExternally
    var forces: Array<dynamic /* String | LuaForce */>?
        get() = definedExternally
        set(value) = definedExternally
    var players: Array<dynamic /* uint | String | LuaPlayer */>?
        get() = definedExternally
        set(value) = definedExternally
    var visible: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var only_in_alt_mode: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$63` {
    var animation: String
    var orientation: RealOrientation?
        get() = definedExternally
        set(value) = definedExternally
    var x_scale: double?
        get() = definedExternally
        set(value) = definedExternally
    var y_scale: double?
        get() = definedExternally
        set(value) = definedExternally
    var tint: dynamic /* ColorTable? | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var render_layer: dynamic /* Number? | ? */
        get() = definedExternally
        set(value) = definedExternally
    var animation_speed: double?
        get() = definedExternally
        set(value) = definedExternally
    var animation_offset: double?
        get() = definedExternally
        set(value) = definedExternally
    var orientation_target: dynamic /* Position? | LuaEntity? */
        get() = definedExternally
        set(value) = definedExternally
    var orientation_target_offset: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var oriented_offset: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var target: dynamic /* Position | LuaEntity */
        get() = definedExternally
        set(value) = definedExternally
    var target_offset: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var surface: dynamic /* uint | String | LuaSurface */
        get() = definedExternally
        set(value) = definedExternally
    var time_to_live: uint?
        get() = definedExternally
        set(value) = definedExternally
    var forces: Array<dynamic /* String | LuaForce */>?
        get() = definedExternally
        set(value) = definedExternally
    var players: Array<dynamic /* uint | String | LuaPlayer */>?
        get() = definedExternally
        set(value) = definedExternally
    var visible: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var only_in_alt_mode: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface LuaRendering {
    fun draw_line(params: `T$55`): uint64
    fun draw_text(params: `T$56`): uint64
    fun draw_circle(params: `T$57`): uint64
    fun draw_rectangle(params: `T$58`): uint64
    fun draw_arc(params: `T$59`): uint64
    fun draw_polygon(params: `T$60`): uint64
    fun draw_sprite(params: `T$61`): uint64
    fun draw_light(params: `T$62`): uint64
    fun draw_animation(params: `T$63`): uint64
    fun destroy(id: uint64)
    fun is_font_valid(font_name: String): Boolean?
    fun is_valid(id: uint64): Boolean?
    fun get_all_ids(mod_name: String = definedExternally): Array<uint64>
    fun clear(mod_name: String = definedExternally)
    fun get_type(id: uint64): String /* "text" | "line" | "circle" | "rectangle" | "arc" | "polygon" | "sprite" | "light" | "animation" */
    fun bring_to_front(id: uint64)
    fun get_surface(id: uint64): LuaSurface
    fun get_time_to_live(id: uint64): uint
    fun set_time_to_live(id: uint64, time_to_live: uint)
    fun get_forces(id: uint64): Array<LuaForce>?
    fun set_forces(id: uint64, forces: Array<Any /* String | LuaForce */>)
    fun get_players(id: uint64): Array<LuaPlayer>?
    fun set_players(id: uint64, players: Array<Any /* uint | String | LuaPlayer */>)
    fun get_visible(id: uint64): Boolean?
    fun set_visible(id: uint64, visible: Boolean)
    fun get_draw_on_ground(id: uint64): Boolean?
    fun set_draw_on_ground(id: uint64, draw_on_ground: Boolean)
    fun get_only_in_alt_mode(id: uint64): Boolean?
    fun set_only_in_alt_mode(id: uint64, only_in_alt_mode: Boolean)
    fun get_color(id: uint64): ColorTable?
    fun set_color(id: uint64, color: ColorTable)
    fun set_color(id: uint64, color: Any /* JsTuple<Any, float, Any, float, Any, float, Any, float> */)
    fun get_width(id: uint64): float?
    fun set_width(id: uint64, width: float)
    fun get_from(id: uint64): ScriptRenderTarget?
    fun set_from(id: uint64, from: Position, from_offset: dynamic /* typealias Vector = dynamic */ = definedExternally)
    fun set_from(id: uint64, from: Position)
    fun set_from(id: uint64, from: LuaEntity, from_offset: dynamic /* typealias Vector = dynamic */ = definedExternally)
    fun set_from(id: uint64, from: LuaEntity)
    fun get_to(id: uint64): ScriptRenderTarget?
    fun set_to(id: uint64, to: Position, to_offset: dynamic /* typealias Vector = dynamic */ = definedExternally)
    fun set_to(id: uint64, to: Position)
    fun set_to(id: uint64, to: LuaEntity, to_offset: dynamic /* typealias Vector = dynamic */ = definedExternally)
    fun set_to(id: uint64, to: LuaEntity)
    fun get_dash_length(id: uint64): double?
    fun set_dash_length(id: uint64, dash_length: double)
    fun get_gap_length(id: uint64): double?
    fun set_gap_length(id: uint64, gap_length: double)
    fun set_dashes(id: uint64, dash_length: double, gap_length: double)
    fun get_target(id: uint64): ScriptRenderTarget?
    fun set_target(id: uint64, target: Position, target_offset: dynamic /* typealias Vector = dynamic */ = definedExternally)
    fun set_target(id: uint64, target: Position)
    fun set_target(id: uint64, target: LuaEntity, target_offset: dynamic /* typealias Vector = dynamic */ = definedExternally)
    fun set_target(id: uint64, target: LuaEntity)
    fun get_orientation(id: uint64): RealOrientation?
    fun set_orientation(id: uint64, orientation: RealOrientation)
    fun get_scale(id: uint64): double?
    fun set_scale(id: uint64, scale: double)
    fun get_text(id: uint64): dynamic /* JsTuple<String, Any> | String? | Number? */
    fun set_text(id: uint64, text: Any /* JsTuple<String, Any> */)
    fun set_text(id: uint64, text: String)
    fun set_text(id: uint64, text: Number)
    fun get_font(id: uint64): String?
    fun set_font(id: uint64, font: String)
    fun get_alignment(id: uint64): String?
    fun set_alignment(id: uint64, alignment: String /* "left" | "right" | "center" */)
    fun get_vertical_alignment(id: uint64): String?
    fun set_vertical_alignment(id: uint64, alignment: String /* "top" | "middle" | "baseline" | "bottom" */)
    fun get_scale_with_zoom(id: uint64): Boolean?
    fun set_scale_with_zoom(id: uint64, scale_with_zoom: Boolean)
    fun get_filled(id: uint64): Boolean?
    fun set_filled(id: uint64, filled: Boolean)
    fun get_radius(id: uint64): double?
    fun set_radius(id: uint64, radius: double)
    fun get_left_top(id: uint64): ScriptRenderTarget?
    fun set_left_top(id: uint64, left_top: Position, left_top_offset: dynamic /* typealias Vector = dynamic */ = definedExternally)
    fun set_left_top(id: uint64, left_top: Position)
    fun set_left_top(id: uint64, left_top: LuaEntity, left_top_offset: dynamic /* typealias Vector = dynamic */ = definedExternally)
    fun set_left_top(id: uint64, left_top: LuaEntity)
    fun get_right_bottom(id: uint64): ScriptRenderTarget?
    fun set_right_bottom(id: uint64, right_bottom: Position, right_bottom_offset: dynamic /* typealias Vector = dynamic */ = definedExternally)
    fun set_right_bottom(id: uint64, right_bottom: Position)
    fun set_right_bottom(id: uint64, right_bottom: LuaEntity, right_bottom_offset: dynamic /* typealias Vector = dynamic */ = definedExternally)
    fun set_right_bottom(id: uint64, right_bottom: LuaEntity)
    fun set_corners(id: uint64, left_top: Position, left_top_offset: dynamic /* typealias Vector = dynamic */, right_bottom: Position, right_bottom_offset: dynamic /* typealias Vector = dynamic */)
    fun set_corners(id: uint64, left_top: Position, left_top_offset: dynamic /* typealias Vector = dynamic */, right_bottom: LuaEntity, right_bottom_offset: dynamic /* typealias Vector = dynamic */)
    fun set_corners(id: uint64, left_top: LuaEntity, left_top_offset: dynamic /* typealias Vector = dynamic */, right_bottom: Position, right_bottom_offset: dynamic /* typealias Vector = dynamic */)
    fun set_corners(id: uint64, left_top: LuaEntity, left_top_offset: dynamic /* typealias Vector = dynamic */, right_bottom: LuaEntity, right_bottom_offset: dynamic /* typealias Vector = dynamic */)
    fun get_max_radius(id: uint64): double?
    fun set_max_radius(id: uint64, max_radius: double)
    fun get_min_radius(id: uint64): double?
    fun set_min_radius(id: uint64, min_radius: double)
    fun get_start_angle(id: uint64): float?
    fun set_start_angle(id: uint64, start_angle: float)
    fun get_angle(id: uint64): float?
    fun set_angle(id: uint64, angle: float)
    fun get_vertices(id: uint64): Array<ScriptRenderTarget>?
    fun set_vertices(id: uint64, vertices: Array<ScriptRenderVertexTarget>)
    fun get_sprite(id: uint64): dynamic /* String? | ? */
    fun set_sprite(id: uint64, sprite: String)
//    fun set_sprite(id: uint64, sprite: )
    fun get_x_scale(id: uint64): double?
    fun set_x_scale(id: uint64, x_scale: double)
    fun get_y_scale(id: uint64): double?
    fun set_y_scale(id: uint64, y_scale: double)
    fun get_render_layer(id: uint64): dynamic /* Number? | ? */
    fun set_render_layer(id: uint64, render_layer: Number)
    fun set_render_layer(id: uint64, render_layer: String)
    fun get_orientation_target(id: uint64): ScriptRenderTarget?
    fun set_orientation_target(id: uint64, orientation_target: Position, orientation_target_offset: dynamic /* typealias Vector = dynamic */ = definedExternally)
    fun set_orientation_target(id: uint64, orientation_target: Position)
    fun set_orientation_target(id: uint64, orientation_target: LuaEntity, orientation_target_offset: dynamic /* typealias Vector = dynamic */ = definedExternally)
    fun set_orientation_target(id: uint64, orientation_target: LuaEntity)
    fun get_oriented_offset(id: uint64): dynamic /* typealias Vector = dynamic */
    fun set_oriented_offset(id: uint64, oriented_offset: dynamic /* typealias Vector = dynamic */)
    fun get_intensity(id: uint64): float?
    fun set_intensity(id: uint64, intensity: float)
    fun get_minimum_darkness(id: uint64): float?
    fun set_minimum_darkness(id: uint64, minimum_darkness: float)
    fun get_oriented(id: uint64): Boolean?
    fun set_oriented(id: uint64, oriented: Boolean)
    fun get_animation(id: uint64): String?
    fun set_animation(id: uint64, animation: String)
    fun get_animation_speed(id: uint64): double?
    fun set_animation_speed(id: uint64, animation_speed: double)
    fun get_animation_offset(id: uint64): double?
    fun set_animation_offset(id: uint64, animation_offset: double)
    var object_name: String /* "LuaRendering" */
}

external interface LuaResourceCategoryPrototype {
    var name: String
    var order: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var valid: Boolean?
    var object_name: String /* "LuaResourceCategoryPrototype" */
    fun help(): String
}

external interface LuaRoboportControlBehavior : LuaControlBehavior {
    var read_logistics: Boolean?
    var read_robot_stats: Boolean?
    var available_logistic_output_signal: SignalID
    var total_logistic_output_signal: SignalID
    var available_construction_output_signal: SignalID
    var total_construction_output_signal: SignalID
    var valid: Boolean?
    var object_name: String /* "LuaRoboportControlBehavior" */
    fun help(): String
}

external interface LuaSettings {
    fun get_player_settings(player: uint): LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, ModSetting> */
    fun get_player_settings(player: String): LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, ModSetting> */
    fun get_player_settings(player: LuaPlayer): LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, ModSetting> */
    var startup: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, ModSetting> */
    var global: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, ModSetting> */
    var player: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<String, ModSetting> */
    var object_name: String /* "LuaSettings" */
}

external interface LuaShortcutPrototype {
    var name: String
    var order: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var action: String
    var item_to_spawn: LuaItemPrototype
    var technology_to_unlock: LuaTechnologyPrototype
    var toggleable: Boolean?
    var associated_control_input: String
    var valid: Boolean?
    var object_name: String /* "LuaShortcutPrototype" */
    fun help(): String
}

external interface LuaStorageTankControlBehavior : LuaControlBehavior {
    var valid: Boolean?
    var object_name: String /* "LuaStorageTankControlBehavior" */
    fun help(): String
}

external interface LuaStyle {
    var gui: LuaGui
    var name: String
    var minimal_width: int
    var maximal_width: int
    var minimal_height: int
    var maximal_height: int
    var natural_width: int
    var natural_height: int
    var top_padding: int
    var right_padding: int
    var bottom_padding: int
    var left_padding: int
    var top_margin: int
    var right_margin: int
    var bottom_margin: int
    var left_margin: int
    var horizontal_align: String /* "left" | "center" | "right" */
    var vertical_align: String /* "top" | "center" | "bottom" */
    var font_color: dynamic /* ColorTable | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var font: String
    var top_cell_padding: int
    var right_cell_padding: int
    var bottom_cell_padding: int
    var left_cell_padding: int
    var horizontally_stretchable: Boolean?
    var vertically_stretchable: Boolean?
    var horizontally_squashable: Boolean?
    var vertically_squashable: Boolean?
    var rich_text_setting: rich_text_setting
    var hovered_font_color: dynamic /* ColorTable | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var clicked_font_color: dynamic /* ColorTable | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var disabled_font_color: dynamic /* ColorTable | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var pie_progress_color: dynamic /* ColorTable | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var clicked_vertical_offset: int
    var selected_font_color: dynamic /* ColorTable | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var selected_hovered_font_color: dynamic /* ColorTable | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var selected_clicked_font_color: dynamic /* ColorTable | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var strikethrough_color: dynamic /* ColorTable | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var horizontal_spacing: int
    var vertical_spacing: int
    var use_header_filler: Boolean?
    var color: dynamic /* ColorTable | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var column_alignments: LuaCustomTableMembers /* LuaCustomTableMembers & CustomTableIndex<uint, String /* "top-left" | "middle-left" | "left" | "bottom-left" | "top-center" | "middle-center" | "center" | "bottom-center" | "top-right" | "right" | "bottom-right" */> */
    var single_line: Boolean?
    var extra_top_padding_when_activated: int
    var extra_bottom_padding_when_activated: int
    var extra_left_padding_when_activated: int
    var extra_right_padding_when_activated: int
    var extra_top_margin_when_activated: int
    var extra_bottom_margin_when_activated: int
    var extra_left_margin_when_activated: int
    var extra_right_margin_when_activated: int
    var stretch_image_to_widget_size: Boolean?
    var badge_font: String
    var badge_horizontal_spacing: int
    var default_badge_font_color: dynamic /* ColorTable | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var selected_badge_font_color: dynamic /* ColorTable | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var disabled_badge_font_color: dynamic /* ColorTable | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
}

external interface BaseSurfaceCreateEntity {
    var name: String
    var position: Position
    var direction: direction?
        get() = definedExternally
        set(value) = definedExternally
    var force: dynamic /* String? | LuaForce? */
        get() = definedExternally
        set(value) = definedExternally
    var target: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
    var source: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
    var fast_replace: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var player: dynamic /* uint? | String? | LuaPlayer? */
        get() = definedExternally
        set(value) = definedExternally
    var spill: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var raise_built: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var create_build_effect_smoke: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var spawn_decorations: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var move_stuck_players: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var item: LuaItemStack?
        get() = definedExternally
        set(value) = definedExternally
}

external interface AssemblingMachineSurfaceCreateEntity : BaseSurfaceCreateEntity {
    var recipe: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface BeamSurfaceCreateEntity : BaseSurfaceCreateEntity {
    var target_position: Position?
        get() = definedExternally
        set(value) = definedExternally
    var source_position: Position?
        get() = definedExternally
        set(value) = definedExternally
    var max_length: uint?
        get() = definedExternally
        set(value) = definedExternally
    var duration: uint?
        get() = definedExternally
        set(value) = definedExternally
    var source_offset: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
}

external interface ContainerSurfaceCreateEntity : BaseSurfaceCreateEntity {
    var bar: uint?
        get() = definedExternally
        set(value) = definedExternally
}

external interface CliffSurfaceCreateEntity : BaseSurfaceCreateEntity {
    var cliff_orientation: String? /* "west-to-east" | "north-to-south" | "east-to-west" | "south-to-north" | "west-to-north" | "north-to-east" | "east-to-south" | "south-to-west" | "west-to-south" | "north-to-west" | "east-to-north" | "south-to-east" | "west-to-none" | "none-to-east" | "east-to-none" | "none-to-west" | "north-to-none" | "none-to-south" | "south-to-none" | "none-to-north" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface FlyingTextSurfaceCreateEntity : BaseSurfaceCreateEntity {
    var text: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var color: dynamic /* ColorTable? | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var render_player_index: uint?
        get() = definedExternally
        set(value) = definedExternally
}

external interface EntityGhostSurfaceCreateEntity : BaseSurfaceCreateEntity {
    var inner_name: String
    var expires: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface FireSurfaceCreateEntity : BaseSurfaceCreateEntity {
    var initial_ground_flame_count: uint8?
        get() = definedExternally
        set(value) = definedExternally
}

external interface InserterSurfaceCreateEntity : BaseSurfaceCreateEntity {
    var conditions: InserterCircuitConditions
    var filters: Array<InventoryFilter>
}

external interface ItemEntitySurfaceCreateEntity : BaseSurfaceCreateEntity {
    var stack: dynamic /* String | ItemStackDefinition */
        get() = definedExternally
        set(value) = definedExternally
}

external interface ItemRequestProxySurfaceCreateEntity : BaseSurfaceCreateEntity {
    var modules: Record<String, uint>
}

external interface RollingStockSurfaceCreateEntity : BaseSurfaceCreateEntity {
    var orientation: RealOrientation?
        get() = definedExternally
        set(value) = definedExternally
    var color: dynamic /* ColorTable? | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
}

external interface LocomotiveSurfaceCreateEntity : BaseSurfaceCreateEntity {
    var snap_to_train_stop: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface LogisticContainerSurfaceCreateEntity : BaseSurfaceCreateEntity {
    var request_filters: Array<InventoryFilter>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ParticleSurfaceCreateEntity : BaseSurfaceCreateEntity {
    var movement: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var height: float
    var vertical_speed: float
    var frame_speed: float
}

external interface ArtilleryFlareSurfaceCreateEntity : BaseSurfaceCreateEntity {
    var movement: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var height: float
    var vertical_speed: float
    var frame_speed: float
}

external interface ProjectileSurfaceCreateEntity : BaseSurfaceCreateEntity {
    var speed: double
    var max_range: double
}

external interface ResourceSurfaceCreateEntity : BaseSurfaceCreateEntity {
    var amount: uint
    var enable_tree_removal: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var enable_cliff_removal: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var snap_to_tile_center: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface UndergroundBeltSurfaceCreateEntity : BaseSurfaceCreateEntity {
    var type: String? /* "output" | "input" */
        get() = definedExternally
        set(value) = definedExternally
}

external interface ProgrammableSpeakerSurfaceCreateEntity : BaseSurfaceCreateEntity {
    var parameters: ProgrammableSpeakerParameters?
        get() = definedExternally
        set(value) = definedExternally
    var alert_parameters: ProgrammableSpeakerAlertParameters?
        get() = definedExternally
        set(value) = definedExternally
}

external interface CharacterCorpseSurfaceCreateEntity : BaseSurfaceCreateEntity {
    var inventory_size: uint?
        get() = definedExternally
        set(value) = definedExternally
    var player_index: uint?
        get() = definedExternally
        set(value) = definedExternally
}

external interface HighlightBoxSurfaceCreateEntity : BaseSurfaceCreateEntity {
    var bounding_box: dynamic /* BoundingBoxTable? | JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */
        get() = definedExternally
        set(value) = definedExternally
    var box_type: String? /* "entity" | "not-allowed" | "electricity" | "pair" | "copy" | "train-visualization" | "logistics" | "blueprint-snap-rectangle" */
        get() = definedExternally
        set(value) = definedExternally
    var render_player_index: uint?
        get() = definedExternally
        set(value) = definedExternally
    var blink_interval: uint?
        get() = definedExternally
        set(value) = definedExternally
    var time_to_live: uint?
        get() = definedExternally
        set(value) = definedExternally
}

external interface SpeechBubbleSurfaceCreateEntity : BaseSurfaceCreateEntity {
    var text: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var lifetime: uint?
        get() = definedExternally
        set(value) = definedExternally
}

external interface SimpleEntityWithOwnerSurfaceCreateEntity : BaseSurfaceCreateEntity {
    var render_player_index: uint?
        get() = definedExternally
        set(value) = definedExternally
}

external interface SimpleEntityWithForceSurfaceCreateEntity : BaseSurfaceCreateEntity {
    var render_player_index: uint?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$64` {
    var name: String
    var position: Position
    var direction: direction?
        get() = definedExternally
        set(value) = definedExternally
    var force: dynamic /* String? | LuaForce? */
        get() = definedExternally
        set(value) = definedExternally
    var build_check_type: build_check_type?
        get() = definedExternally
        set(value) = definedExternally
    var forced: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var inner_name: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$65` {
    var name: String
    var position: Position
    var direction: direction?
        get() = definedExternally
        set(value) = definedExternally
    var force: dynamic /* String? | LuaForce? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$66` {
    var area: dynamic /* BoundingBoxTable? | JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */
        get() = definedExternally
        set(value) = definedExternally
    var position: Position?
        get() = definedExternally
        set(value) = definedExternally
    var radius: double?
        get() = definedExternally
        set(value) = definedExternally
    var name: dynamic /* String? | Array<String>? */
        get() = definedExternally
        set(value) = definedExternally
    var type: dynamic /* String? | Array<String>? */
        get() = definedExternally
        set(value) = definedExternally
    var ghost_name: dynamic /* String? | Array<String>? */
        get() = definedExternally
        set(value) = definedExternally
    var ghost_type: dynamic /* String? | Array<String>? */
        get() = definedExternally
        set(value) = definedExternally
    var direction: dynamic /* defines.direction? | Array<defines.direction>? */
        get() = definedExternally
        set(value) = definedExternally
    var collision_mask: dynamic /* "ground-tile" | "water-tile" | "resource-layer" | "doodad-layer" | "floor-layer" | "item-layer" | "ghost-layer" | "object-layer" | "player-layer" | "train-layer" | "rail-layer" | "transport-belt-layer" | "not-setup" | ? | Array<dynamic /* "ground-tile" | "water-tile" | "resource-layer" | "doodad-layer" | "floor-layer" | "item-layer" | "ghost-layer" | "object-layer" | "player-layer" | "train-layer" | "rail-layer" | "transport-belt-layer" | "not-setup" |  */>? */
        get() = definedExternally
        set(value) = definedExternally
    var force: dynamic /* String? | LuaForce? | Array<dynamic /* String | LuaForce */>? */
        get() = definedExternally
        set(value) = definedExternally
    var to_be_deconstructed: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var to_be_upgraded: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var limit: uint?
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$67` {
    var area: dynamic /* BoundingBoxTable? | JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */
        get() = definedExternally
        set(value) = definedExternally
    var position: Position?
        get() = definedExternally
        set(value) = definedExternally
    var radius: double?
        get() = definedExternally
        set(value) = definedExternally
    var name: dynamic /* String? | Array<String>? */
        get() = definedExternally
        set(value) = definedExternally
    var limit: uint?
        get() = definedExternally
        set(value) = definedExternally
    var has_hidden_tile: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var collision_mask: dynamic /* "ground-tile" | "water-tile" | "resource-layer" | "doodad-layer" | "floor-layer" | "item-layer" | "ghost-layer" | "object-layer" | "player-layer" | "train-layer" | "rail-layer" | "transport-belt-layer" | "not-setup" | ? | Array<dynamic /* "ground-tile" | "water-tile" | "resource-layer" | "doodad-layer" | "floor-layer" | "item-layer" | "ghost-layer" | "object-layer" | "player-layer" | "train-layer" | "rail-layer" | "transport-belt-layer" | "not-setup" |  */>? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$68` {
    var area: dynamic /* BoundingBoxTable? | JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */
        get() = definedExternally
        set(value) = definedExternally
    var position: Position?
        get() = definedExternally
        set(value) = definedExternally
    var radius: double?
        get() = definedExternally
        set(value) = definedExternally
    var name: dynamic /* String? | Array<String>? */
        get() = definedExternally
        set(value) = definedExternally
    var type: dynamic /* String? | Array<String>? */
        get() = definedExternally
        set(value) = definedExternally
    var ghost_name: dynamic /* String? | Array<String>? */
        get() = definedExternally
        set(value) = definedExternally
    var ghost_type: dynamic /* String? | Array<String>? */
        get() = definedExternally
        set(value) = definedExternally
    var direction: dynamic /* defines.direction? | Array<defines.direction>? */
        get() = definedExternally
        set(value) = definedExternally
    var collision_mask: dynamic /* "ground-tile" | "water-tile" | "resource-layer" | "doodad-layer" | "floor-layer" | "item-layer" | "ghost-layer" | "object-layer" | "player-layer" | "train-layer" | "rail-layer" | "transport-belt-layer" | "not-setup" | ? | Array<dynamic /* "ground-tile" | "water-tile" | "resource-layer" | "doodad-layer" | "floor-layer" | "item-layer" | "ghost-layer" | "object-layer" | "player-layer" | "train-layer" | "rail-layer" | "transport-belt-layer" | "not-setup" |  */>? */
        get() = definedExternally
        set(value) = definedExternally
    var force: dynamic /* String? | LuaForce? | Array<dynamic /* String | LuaForce */>? */
        get() = definedExternally
        set(value) = definedExternally
    var to_be_deconstructed: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var to_be_upgraded: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var limit: uint?
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$69` {
    var area: dynamic /* BoundingBoxTable? | JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */
        get() = definedExternally
        set(value) = definedExternally
    var position: Position?
        get() = definedExternally
        set(value) = definedExternally
    var radius: double?
        get() = definedExternally
        set(value) = definedExternally
    var name: dynamic /* String? | Array<String>? */
        get() = definedExternally
        set(value) = definedExternally
    var limit: uint?
        get() = definedExternally
        set(value) = definedExternally
    var has_hidden_tile: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var collision_mask: dynamic /* "ground-tile" | "water-tile" | "resource-layer" | "doodad-layer" | "floor-layer" | "item-layer" | "ghost-layer" | "object-layer" | "player-layer" | "train-layer" | "rail-layer" | "transport-belt-layer" | "not-setup" | ? | Array<dynamic /* "ground-tile" | "water-tile" | "resource-layer" | "doodad-layer" | "floor-layer" | "item-layer" | "ghost-layer" | "object-layer" | "player-layer" | "train-layer" | "rail-layer" | "transport-belt-layer" | "not-setup" |  */>? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$70` {
    var area: dynamic /* BoundingBoxTable | JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */
        get() = definedExternally
        set(value) = definedExternally
    var force: dynamic /* LuaForce | String */
        get() = definedExternally
        set(value) = definedExternally
    var condition: String /* "all" | "enemy" | "ally" | "friend" | "not-friend" | "same" | "not-same" */
}

external interface `T$71` {
    var position: Position
    var max_distance: double
    var force: dynamic /* String? | LuaForce? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$72` {
    var command: dynamic /* AttackCommand | GoToLocationCommand | CompoundCommand | GroupCommand | AttackAreaCommand | WanderCommand | StopCommand | FleeCommand | BuildBaseCommand */
        get() = definedExternally
        set(value) = definedExternally
    var unit_count: uint
    var force: dynamic /* String? | LuaForce? */
        get() = definedExternally
        set(value) = definedExternally
    var unit_search_distance: uint?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$73` {
    var name: String
    var position: Position
}

external interface `T$74` {
    var name: String
    var position: Position
    var movement: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var height: float
    var vertical_speed: float
    var frame_speed: float
}

external interface `T$75` {
    var position: Position
    var force: dynamic /* String? | LuaForce? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$76` {
    var area: dynamic /* BoundingBoxTable | JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */
        get() = definedExternally
        set(value) = definedExternally
    var force: dynamic /* String | LuaForce */
        get() = definedExternally
        set(value) = definedExternally
    var player: dynamic /* uint? | String? | LuaPlayer? */
        get() = definedExternally
        set(value) = definedExternally
    var skip_fog_of_war: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var item: LuaItemStack?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$77` {
    var area: dynamic /* BoundingBoxTable | JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */
        get() = definedExternally
        set(value) = definedExternally
    var force: dynamic /* String | LuaForce */
        get() = definedExternally
        set(value) = definedExternally
    var player: dynamic /* uint? | String? | LuaPlayer? */
        get() = definedExternally
        set(value) = definedExternally
    var skip_fog_of_war: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var item: LuaItemStack
}

external interface `T$78` {
    var area: dynamic /* BoundingBoxTable? | JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */
        get() = definedExternally
        set(value) = definedExternally
    var position: dynamic /* TilePositionTable? | JsTuple<x, int, y, int> */
        get() = definedExternally
        set(value) = definedExternally
    var name: dynamic /* String? | Array<String>? | LuaDecorativePrototype? | Array<LuaDecorativePrototype>? */
        get() = definedExternally
        set(value) = definedExternally
    var limit: uint?
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$79` {
    var check_collision: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var decoratives: Array<Decorative>
}

external interface `T$80` {
    var area: dynamic /* BoundingBoxTable? | JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */
        get() = definedExternally
        set(value) = definedExternally
    var position: dynamic /* TilePositionTable? | JsTuple<x, int, y, int> */
        get() = definedExternally
        set(value) = definedExternally
    var name: dynamic /* String? | Array<String>? | LuaDecorativePrototype? | Array<LuaDecorativePrototype>? */
        get() = definedExternally
        set(value) = definedExternally
    var limit: uint?
        get() = definedExternally
        set(value) = definedExternally
    var invert: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$81` {
    var source_area: dynamic /* BoundingBoxTable | JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */
        get() = definedExternally
        set(value) = definedExternally
    var destination_area: dynamic /* BoundingBoxTable | JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */
        get() = definedExternally
        set(value) = definedExternally
    var destination_surface: dynamic /* uint? | String? | LuaSurface? */
        get() = definedExternally
        set(value) = definedExternally
    var destination_force: dynamic /* LuaForce? | String? */
        get() = definedExternally
        set(value) = definedExternally
    var clone_tiles: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var clone_entities: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var clone_decoratives: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var clear_destination_entities: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var clear_destination_decoratives: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var expand_map: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var create_build_effect_smoke: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$82` {
    var source_offset: dynamic /* TilePositionTable | JsTuple<x, int, y, int> */
        get() = definedExternally
        set(value) = definedExternally
    var destination_offset: dynamic /* TilePositionTable | JsTuple<x, int, y, int> */
        get() = definedExternally
        set(value) = definedExternally
    var source_positions: Array<dynamic /* TilePositionTable | JsTuple<x, int, y, int> */>
    var destination_surface: dynamic /* uint? | String? | LuaSurface? */
        get() = definedExternally
        set(value) = definedExternally
    var destination_force: dynamic /* LuaForce? | String? */
        get() = definedExternally
        set(value) = definedExternally
    var clone_tiles: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var clone_entities: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var clone_decoratives: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var clear_destination_entities: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var clear_destination_decoratives: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var expand_map: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var manual_collision_mode: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var create_build_effect_smoke: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$83` {
    var entities: Array<LuaEntity>
    var destination_offset: dynamic /* typealias Vector = dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var destination_surface: dynamic /* uint? | String? | LuaSurface? */
        get() = definedExternally
        set(value) = definedExternally
    var destination_force: dynamic /* String? | LuaForce? */
        get() = definedExternally
        set(value) = definedExternally
    var snap_to_grid: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var create_build_effect_smoke: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$84` {
    var bounding_box: dynamic /* BoundingBoxTable | JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */
        get() = definedExternally
        set(value) = definedExternally
    var collision_mask: dynamic /* CollisionMask | Array<String> */
        get() = definedExternally
        set(value) = definedExternally
    var start: Position
    var goal: Position
    var force: dynamic /* String | LuaForce */
        get() = definedExternally
        set(value) = definedExternally
    var radius: double?
        get() = definedExternally
        set(value) = definedExternally
    var pathfind_flags: PathfinderFlags?
        get() = definedExternally
        set(value) = definedExternally
    var can_open_gates: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var path_resolution_modifier: int?
        get() = definedExternally
        set(value) = definedExternally
    var entity_to_ignore: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$85` {
    var name: dynamic /* String? | Array<String>? */
        get() = definedExternally
        set(value) = definedExternally
    var force: dynamic /* String? | LuaForce? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface LuaSurface {
    fun get_pollution(position: Position): double
    fun can_place_entity(params: `T$64`): Boolean?
    fun can_fast_replace(params: `T$65`): Boolean?
    fun find_entity(entity: String, position: Position): LuaEntity?
    fun find_entities(area: BoundingBoxTable = definedExternally): Array<LuaEntity>
    fun find_entities(): Array<LuaEntity>
    fun find_entities(area: Any /* JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */ = definedExternally): Array<LuaEntity>
    fun find_entities_filtered(params: `T$66`): Array<LuaEntity>
    fun find_tiles_filtered(params: `T$67`): Array<LuaTile>
    fun count_entities_filtered(params: `T$68`): uint
    fun count_tiles_filtered(params: `T$69`): uint
    fun find_non_colliding_position(name: String, center: Position, radius: double, precision: double, force_to_tile_center: Boolean = definedExternally): PositionTable?
    fun find_non_colliding_position_in_box(name: String, search_space: BoundingBoxTable, precision: double, force_to_tile_center: Boolean = definedExternally): PositionTable?
    fun find_non_colliding_position_in_box(name: String, search_space: BoundingBoxTable, precision: double): PositionTable?
    fun find_non_colliding_position_in_box(name: String, search_space: Any /* JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */, precision: double, force_to_tile_center: Boolean = definedExternally): PositionTable?
    fun find_non_colliding_position_in_box(name: String, search_space: Any /* JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */, precision: double): PositionTable?
    fun spill_item_stack(position: Position, items: String, enable_looted: Boolean = definedExternally, force: LuaForce = definedExternally, allow_belts: Boolean = definedExternally): Array<LuaEntity>
    fun spill_item_stack(position: Position, items: String): Array<LuaEntity>
    fun spill_item_stack(position: Position, items: String, enable_looted: Boolean = definedExternally): Array<LuaEntity>
    fun spill_item_stack(position: Position, items: String, enable_looted: Boolean = definedExternally, force: LuaForce = definedExternally): Array<LuaEntity>
    fun spill_item_stack(position: Position, items: String, enable_looted: Boolean = definedExternally, force: String = definedExternally, allow_belts: Boolean = definedExternally): Array<LuaEntity>
    fun spill_item_stack(position: Position, items: String, enable_looted: Boolean = definedExternally, force: String = definedExternally): Array<LuaEntity>
    fun spill_item_stack(position: Position, items: ItemStackDefinition, enable_looted: Boolean = definedExternally, force: LuaForce = definedExternally, allow_belts: Boolean = definedExternally): Array<LuaEntity>
    fun spill_item_stack(position: Position, items: ItemStackDefinition): Array<LuaEntity>
    fun spill_item_stack(position: Position, items: ItemStackDefinition, enable_looted: Boolean = definedExternally): Array<LuaEntity>
    fun spill_item_stack(position: Position, items: ItemStackDefinition, enable_looted: Boolean = definedExternally, force: LuaForce = definedExternally): Array<LuaEntity>
    fun spill_item_stack(position: Position, items: ItemStackDefinition, enable_looted: Boolean = definedExternally, force: String = definedExternally, allow_belts: Boolean = definedExternally): Array<LuaEntity>
    fun spill_item_stack(position: Position, items: ItemStackDefinition, enable_looted: Boolean = definedExternally, force: String = definedExternally): Array<LuaEntity>
    fun spill_item_stack(position: Position, items: LuaItemStack, enable_looted: Boolean = definedExternally, force: LuaForce = definedExternally, allow_belts: Boolean = definedExternally): Array<LuaEntity>
    fun spill_item_stack(position: Position, items: LuaItemStack): Array<LuaEntity>
    fun spill_item_stack(position: Position, items: LuaItemStack, enable_looted: Boolean = definedExternally): Array<LuaEntity>
    fun spill_item_stack(position: Position, items: LuaItemStack, enable_looted: Boolean = definedExternally, force: LuaForce = definedExternally): Array<LuaEntity>
    fun spill_item_stack(position: Position, items: LuaItemStack, enable_looted: Boolean = definedExternally, force: String = definedExternally, allow_belts: Boolean = definedExternally): Array<LuaEntity>
    fun spill_item_stack(position: Position, items: LuaItemStack, enable_looted: Boolean = definedExternally, force: String = definedExternally): Array<LuaEntity>
    fun find_enemy_units(center: Position, radius: double, force: LuaForce = definedExternally): Array<LuaEntity>
    fun find_enemy_units(center: Position, radius: double): Array<LuaEntity>
    fun find_enemy_units(center: Position, radius: double, force: String = definedExternally): Array<LuaEntity>
    fun find_units(params: `T$70`): Array<LuaEntity>
    fun find_nearest_enemy(params: `T$71`): LuaEntity?
    fun find_nearest_enemy_entity_with_owner(params: `T$71`): LuaEntity?
    fun set_multi_command(params: `T$72`): uint
    fun create_entity(params: Any /* AssemblingMachineSurfaceCreateEntity | BeamSurfaceCreateEntity | ContainerSurfaceCreateEntity | CliffSurfaceCreateEntity | FlyingTextSurfaceCreateEntity | EntityGhostSurfaceCreateEntity | FireSurfaceCreateEntity | InserterSurfaceCreateEntity | ItemEntitySurfaceCreateEntity | ItemRequestProxySurfaceCreateEntity | RollingStockSurfaceCreateEntity | LocomotiveSurfaceCreateEntity | LogisticContainerSurfaceCreateEntity | ParticleSurfaceCreateEntity | ArtilleryFlareSurfaceCreateEntity | ProjectileSurfaceCreateEntity | ResourceSurfaceCreateEntity | UndergroundBeltSurfaceCreateEntity | ProgrammableSpeakerSurfaceCreateEntity | CharacterCorpseSurfaceCreateEntity | HighlightBoxSurfaceCreateEntity | SpeechBubbleSurfaceCreateEntity | SimpleEntityWithOwnerSurfaceCreateEntity | SimpleEntityWithForceSurfaceCreateEntity */): LuaEntity?
    fun create_trivial_smoke(params: `T$73`)
    fun create_particle(params: `T$74`)
    fun create_unit_group(params: `T$75`): LuaUnitGroup
    fun build_enemy_base(position: Position, unit_count: uint, force: String = definedExternally)
    fun build_enemy_base(position: Position, unit_count: uint)
    fun build_enemy_base(position: Position, unit_count: uint, force: LuaForce = definedExternally)
    fun get_tile(x: int, y: int): LuaTile
    fun set_tiles(tiles: Array<Tile>, correct_tiles: Boolean = definedExternally, remove_colliding_entities: Boolean = definedExternally, remove_colliding_decoratives: Boolean = definedExternally, raise_event: Boolean = definedExternally)
    fun set_tiles(tiles: Array<Tile>)
    fun set_tiles(tiles: Array<Tile>, correct_tiles: Boolean = definedExternally)
    fun set_tiles(tiles: Array<Tile>, correct_tiles: Boolean = definedExternally, remove_colliding_entities: Boolean = definedExternally)
    fun set_tiles(tiles: Array<Tile>, correct_tiles: Boolean = definedExternally, remove_colliding_entities: Boolean = definedExternally, remove_colliding_decoratives: Boolean = definedExternally)
    fun set_tiles(tiles: Array<Tile>, correct_tiles: Boolean = definedExternally, remove_colliding_entities: String = definedExternally, remove_colliding_decoratives: Boolean = definedExternally, raise_event: Boolean = definedExternally)
    fun set_tiles(tiles: Array<Tile>, correct_tiles: Boolean = definedExternally, remove_colliding_entities: String = definedExternally)
    fun set_tiles(tiles: Array<Tile>, correct_tiles: Boolean = definedExternally, remove_colliding_entities: String = definedExternally, remove_colliding_decoratives: Boolean = definedExternally)
    fun pollute(source: Position, amount: double)
    fun get_chunks(): LuaChunkIterator
    fun is_chunk_generated(position: ChunkPositionTable): Boolean?
    fun is_chunk_generated(position: Any /* JsTuple<x, int, y, int> */): Boolean?
    fun request_to_generate_chunks(position: Position, radius: uint)
    fun force_generate_chunk_requests()
    fun set_chunk_generated_status(position: ChunkPositionTable, status: chunk_generated_status)
    fun set_chunk_generated_status(position: Any /* JsTuple<x, int, y, int> */, status: chunk_generated_status)
    fun find_logistic_network_by_position(position: Position, force: String): LuaLogisticNetwork?
    fun find_logistic_network_by_position(position: Position, force: LuaForce): LuaLogisticNetwork?
    fun find_logistic_networks_by_construction_area(position: Position, force: String): Array<LuaLogisticNetwork>
    fun find_logistic_networks_by_construction_area(position: Position, force: LuaForce): Array<LuaLogisticNetwork>
    fun deconstruct_area(params: `T$76`)
    fun cancel_deconstruct_area(params: `T$76`)
    fun upgrade_area(params: `T$77`)
    fun cancel_upgrade_area(params: `T$76`)
    fun get_hidden_tile(position: TilePositionTable): String?
    fun get_hidden_tile(position: Any /* JsTuple<x, int, y, int> */): String?
    fun set_hidden_tile(position: TilePositionTable, tile: String?)
    fun set_hidden_tile(position: TilePositionTable, tile: LuaTilePrototype?)
    fun set_hidden_tile(position: Any /* JsTuple<x, int, y, int> */, tile: String?)
    fun set_hidden_tile(position: Any /* JsTuple<x, int, y, int> */, tile: LuaTilePrototype?)
    fun get_connected_tiles(position: Position, tiles: Array<String>): Array<PositionTable>
    fun delete_chunk(position: ChunkPositionTable)
    fun delete_chunk(position: Any /* JsTuple<x, int, y, int> */)
    fun regenerate_entity(entities: String = definedExternally, chunks: Array<Any /* ChunkPositionTable | JsTuple<x, int, y, int> */> = definedExternally)
    fun regenerate_entity()
    fun regenerate_entity(entities: String = definedExternally)
    fun regenerate_entity(entities: Array<String> = definedExternally, chunks: Array<Any /* ChunkPositionTable | JsTuple<x, int, y, int> */> = definedExternally)
    fun regenerate_entity(entities: Array<String> = definedExternally)
    fun regenerate_decorative(decoratives: String = definedExternally, chunks: Array<Any /* ChunkPositionTable | JsTuple<x, int, y, int> */> = definedExternally)
    fun regenerate_decorative()
    fun regenerate_decorative(decoratives: String = definedExternally)
    fun regenerate_decorative(decoratives: Array<String> = definedExternally, chunks: Array<Any /* ChunkPositionTable | JsTuple<x, int, y, int> */> = definedExternally)
    fun regenerate_decorative(decoratives: Array<String> = definedExternally)
    fun print(message: Any /* JsTuple<String, Any> */, color: ColorTable = definedExternally)
    fun print(message: Any /* JsTuple<String, Any> */)
    fun print(message: Any /* JsTuple<String, Any> */, color: Any /* JsTuple<Any, float, Any, float, Any, float, Any, float> */ = definedExternally)
    fun print(message: String, color: ColorTable = definedExternally)
    fun print(message: String)
    fun print(message: String, color: Any /* JsTuple<Any, float, Any, float, Any, float, Any, float> */ = definedExternally)
    fun print(message: Number, color: ColorTable = definedExternally)
    fun print(message: Number)
    fun print(message: Number, color: Any /* JsTuple<Any, float, Any, float, Any, float, Any, float> */ = definedExternally)
    fun destroy_decoratives(params: `T$78`)
    fun create_decoratives(params: `T$79`)
    fun find_decoratives_filtered(params: `T$80`): Array<DecorativeResult>
    fun get_trains(force: String = definedExternally): Array<LuaTrain>
    fun get_trains(): Array<LuaTrain>
    fun get_trains(force: LuaForce = definedExternally): Array<LuaTrain>
    fun clear_pollution()
    fun play_sound(params: `T$34`)
    fun get_resource_counts(): Record<String, uint>
    fun get_random_chunk(): ChunkPositionTable
    fun clone_area(params: `T$81`)
    fun clone_brush(params: `T$82`)
    fun clone_entities(params: `T$83`)
    fun clear(ignore_characters: Boolean = definedExternally)
    fun request_path(params: `T$84`): uint
    fun get_script_areas(name: String = definedExternally): Array<ScriptArea>
    fun get_script_area(key: String = definedExternally): ScriptArea
    fun get_script_area(): ScriptArea
    fun get_script_area(key: uint = definedExternally): ScriptArea
    fun edit_script_area(id: uint, area: ScriptArea)
    fun add_script_area(area: ScriptArea): uint
    fun remove_script_area(id: uint): Boolean?
    fun get_script_positions(name: String = definedExternally): Array<ScriptPosition>
    fun get_script_position(key: String = definedExternally): ScriptPosition
    fun get_script_position(): ScriptPosition
    fun get_script_position(key: uint = definedExternally): ScriptPosition
    fun edit_script_position(id: uint, area: ScriptPosition)
    fun add_script_position(area: ScriptPosition): uint
    fun remove_script_position(id: uint): Boolean?
    fun get_map_exchange_string(): String
    fun get_starting_area_radius(): double
    fun get_closest(position: Position, entities: Array<LuaEntity>): LuaEntity
    fun get_train_stops(params: `T$85` = definedExternally): Array<LuaEntity>
    fun get_total_pollution(): double
    fun entity_prototype_collides(prototype: LuaEntity, position: Position, use_map_generation_bounding_box: Boolean, direction: direction = definedExternally)
    fun entity_prototype_collides(prototype: LuaEntity, position: Position, use_map_generation_bounding_box: Boolean)
    fun entity_prototype_collides(prototype: LuaEntityPrototype, position: Position, use_map_generation_bounding_box: Boolean, direction: direction = definedExternally)
    fun entity_prototype_collides(prototype: LuaEntityPrototype, position: Position, use_map_generation_bounding_box: Boolean)
    fun entity_prototype_collides(prototype: String, position: Position, use_map_generation_bounding_box: Boolean, direction: direction = definedExternally)
    fun entity_prototype_collides(prototype: String, position: Position, use_map_generation_bounding_box: Boolean)
    fun decorative_prototype_collides(prototype: String, position: Position)
    fun calculate_tile_properties(property_names: Array<String>, positions: Array<Position>): Record<String, Array<double>>
    fun get_entities_with_force(position: ChunkPositionTable, force: LuaForce): Array<LuaEntity>
    fun get_entities_with_force(position: ChunkPositionTable, force: String): Array<LuaEntity>
    fun get_entities_with_force(position: Any /* JsTuple<x, int, y, int> */, force: LuaForce): Array<LuaEntity>
    fun get_entities_with_force(position: Any /* JsTuple<x, int, y, int> */, force: String): Array<LuaEntity>
    fun build_checkerboard(area: BoundingBoxTable)
    fun build_checkerboard(area: Any /* JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */)
    var name: String
    var index: uint
    var map_gen_settings: MapGenSettings
    var generate_with_lab_tiles: Boolean?
    var always_day: Boolean?
    var daytime: float
    var darkness: float
    var wind_speed: double
    var wind_orientation: RealOrientation
    var wind_orientation_change: double
    var peaceful_mode: Boolean?
    var freeze_daytime: Boolean?
    var ticks_per_day: uint
    var dusk: double
    var dawn: double
    var evening: double
    var morning: double
    var solar_power_multiplier: double
    var min_brightness: double
    var brightness_visual_weights: dynamic /* ColorModifierTable | JsTuple<Any, float, Any, float, Any, float, Any, float> */
        get() = definedExternally
        set(value) = definedExternally
    var show_clouds: Boolean?
    var valid: Boolean?
    var object_name: String /* "LuaSurface" */
    fun help(): String
}

external interface LuaTechnology {
    fun reload()
    var force: LuaForce
    var name: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var prototype: LuaTechnologyPrototype
    var enabled: Boolean?
    var visible_when_disabled: Boolean?
    var upgrade: Boolean?
    var researched: Boolean?
    var prerequisites: Record<String, LuaTechnology>
    var research_unit_ingredients: Array<dynamic /* FluidIngredient | OtherIngredient */>
    var effects: Array<dynamic /* GunSpeedTechnologyModifier | AmmoDamageTechnologyModifier | GiveItemTechnologyModifier | TurretAttackTechnologyModifier | UnlockRecipeTechnologyModifier | NothingTechnologyModifier | OtherTechnologyModifier */>
    var research_unit_count: uint
    var research_unit_energy: double
    var order: String
    var level: uint
    var research_unit_count_formula: String?
    var valid: Boolean?
    var object_name: String /* "LuaTechnology" */
    fun help(): String
}

external interface LuaTechnologyPrototype {
    var name: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var enabled: Boolean?
    var hidden: Boolean?
    var visible_when_disabled: Boolean?
    var ignore_tech_cost_multiplier: Boolean?
    var upgrade: Boolean?
    var prerequisites: Record<String, LuaTechnologyPrototype>
    var research_unit_ingredients: Array<dynamic /* FluidIngredient | OtherIngredient */>
    var effects: Array<dynamic /* GunSpeedTechnologyModifier | AmmoDamageTechnologyModifier | GiveItemTechnologyModifier | TurretAttackTechnologyModifier | UnlockRecipeTechnologyModifier | NothingTechnologyModifier | OtherTechnologyModifier */>
    var research_unit_count: uint
    var research_unit_energy: double
    var order: String
    var level: uint
    var max_level: uint
    var research_unit_count_formula: String?
    var valid: Boolean?
    var object_name: String /* "LuaTechnologyPrototype" */
    fun help(): String
}

external interface LuaTile {
    fun collides_with(layer: String /* "ground-tile" | "water-tile" | "resource-layer" | "doodad-layer" | "floor-layer" | "item-layer" | "ghost-layer" | "object-layer" | "player-layer" | "train-layer" | "rail-layer" | "transport-belt-layer" | "not-setup" */): Boolean?
//    fun collides_with(layer: ): Boolean?
    fun to_be_deconstructed(): Boolean?
    fun order_deconstruction(force: String, player: uint = definedExternally): LuaEntity?
    fun order_deconstruction(force: String): LuaEntity?
    fun order_deconstruction(force: String, player: String = definedExternally): LuaEntity?
    fun order_deconstruction(force: String, player: LuaPlayer = definedExternally): LuaEntity?
    fun order_deconstruction(force: LuaForce, player: uint = definedExternally): LuaEntity?
    fun order_deconstruction(force: LuaForce): LuaEntity?
    fun order_deconstruction(force: LuaForce, player: String = definedExternally): LuaEntity?
    fun order_deconstruction(force: LuaForce, player: LuaPlayer = definedExternally): LuaEntity?
    fun cancel_deconstruction(force: String, player: uint = definedExternally)
    fun cancel_deconstruction(force: String)
    fun cancel_deconstruction(force: String, player: String = definedExternally)
    fun cancel_deconstruction(force: String, player: LuaPlayer = definedExternally)
    fun cancel_deconstruction(force: LuaForce, player: uint = definedExternally)
    fun cancel_deconstruction(force: LuaForce)
    fun cancel_deconstruction(force: LuaForce, player: String = definedExternally)
    fun cancel_deconstruction(force: LuaForce, player: LuaPlayer = definedExternally)
    var name: String
    var prototype: LuaTilePrototype
    var position: PositionTable
    var hidden_tile: String?
    var surface: LuaSurface
    var valid: Boolean?
    var object_name: String /* "LuaTile" */
    fun help(): String
}

external interface `T$86` {
    var minable: Boolean?
    var miningtime: double
    var miningparticle: String?
        get() = definedExternally
        set(value) = definedExternally
    var products: Array<dynamic /* FluidProduct | OtherProduct */>
}

external interface LuaTilePrototype {
    var name: String
    var order: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var collision_mask: CollisionMask
    var collision_mask_with_flags: CollisionMaskWithFlags
    var layer: uint
    var autoplace_specification: AutoplaceSpecification?
    var walking_speed_modifier: float
    var vehicle_friction_modifier: float
    var map_color: ColorTable
    var decorative_removal_probability: float
    var automatic_neighbors: Boolean?
    var allowed_neighbors: Record<String, LuaTilePrototype>
    var needs_correction: Boolean?
    var mineable_properties: `T$86`
    var next_direction: LuaTilePrototype?
    var items_to_place_this: Array<dynamic /* String | ItemStackDefinition */>
    var can_be_part_of_blueprint: Boolean?
    var emissions_per_second: double
    var valid: Boolean?
    var object_name: String /* "LuaTilePrototype" */
    fun help(): String
}

external interface LuaTrain {
    fun get_item_count(item: String = definedExternally): uint
    fun get_contents(): Record<String, uint>
    fun remove_item(stack: String): uint
    fun remove_item(stack: ItemStackDefinition): uint
    fun remove_item(stack: LuaItemStack): uint
    fun insert(stack: String)
    fun insert(stack: ItemStackDefinition)
    fun insert(stack: LuaItemStack)
    fun clear_items_inside()
    fun recalculate_path(force: Boolean = definedExternally): Boolean?
    fun get_fluid_count(fluid: String = definedExternally): double
    fun get_fluid_contents(): Record<String, double>
    fun remove_fluid(fluid: Fluid): double
    fun insert_fluid(fluid: Fluid): double
    fun clear_fluids_inside()
    fun go_to_station(index: uint)
    fun get_rails(): Array<LuaEntity>
    var manual_mode: Boolean?
    var speed: double
    var max_forward_speed: double
    var max_backward_speed: double
    var weight: double
    var carriages: Array<LuaEntity>
    var locomotives: Record<String, Array<LuaEntity>>
    var cargo_wagons: Array<LuaEntity>
    var fluid_wagons: Array<LuaEntity>
    var schedule: TrainSchedule?
    var state: train_state
    var front_rail: LuaEntity?
    var back_rail: LuaEntity?
    var rail_direction_from_front_rail: rail_direction
    var rail_direction_from_back_rail: rail_direction
    var front_stock: LuaEntity?
    var back_stock: LuaEntity?
    var station: LuaEntity?
    var has_path: Boolean?
    var path_end_rail: LuaEntity?
    var path_end_stop: LuaEntity?
    var id: uint
    var passengers: Array<LuaPlayer>
    var riding_state: RidingState
    var killed_players: Record<uint, uint>
    var kill_count: uint
    var path: LuaRailPath?
    var signal: LuaEntity?
    var valid: Boolean?
    var object_name: String /* "LuaTrain" */
    fun help(): String
}

external interface LuaTrainStopControlBehavior : LuaGenericOnOffControlBehavior {
    var send_to_train: Boolean?
    var read_from_train: Boolean?
    var read_stopped_train: Boolean?
    var set_trains_limit: Boolean?
    var read_trains_count: Boolean?
    var enable_disable: Boolean?
    var stopped_train_signal: SignalID
    var trains_count_signal: SignalID
    var trains_limit_signal: SignalID
    override var valid: Boolean?
    override var object_name: String /* "LuaTrainStopControlBehavior" */
    override fun help(): String
}

external interface LuaTransportBeltControlBehavior : LuaGenericOnOffControlBehavior {
    var enable_disable: Boolean?
    var read_contents: Boolean?
    var read_contents_mode: content_read_mode
    override var valid: Boolean?
    override var object_name: String /* "LuaTransportBeltControlBehavior" */
    override fun help(): String
}

typealias LuaTransportLine = Array<LuaItemStack>

external interface LuaTrivialSmokePrototype {
    var name: String
    var order: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var color: ColorTable
    var start_scale: double
    var end_scale: double
    var movement_slow_down_factor: double
    var duration: uint
    var spread_duration: uint
    var fade_away_duration: uint
    var fade_in_duration: uint
    var glow_fade_away_duration: uint
    var cyclic: Boolean?
    var affected_by_wind: Boolean?
    var show_when_smoke_off: Boolean?
    var glow_animation: Boolean?
    var render_layer: dynamic /* Number |  */
        get() = definedExternally
        set(value) = definedExternally
    var valid: Boolean?
    var object_name: String /* "LuaTrivialSmokePrototype" */
    fun help(): String
}

external interface LuaUnitGroup {
    fun add_member(unit: LuaEntity)
    fun set_command(command: AttackCommand)
    fun set_command(command: GoToLocationCommand)
    fun set_command(command: CompoundCommand)
    fun set_command(command: GroupCommand)
    fun set_command(command: AttackAreaCommand)
    fun set_command(command: WanderCommand)
    fun set_command(command: StopCommand)
    fun set_command(command: FleeCommand)
    fun set_command(command: BuildBaseCommand)
    fun set_distraction_command(command: AttackCommand)
    fun set_distraction_command(command: GoToLocationCommand)
    fun set_distraction_command(command: CompoundCommand)
    fun set_distraction_command(command: GroupCommand)
    fun set_distraction_command(command: AttackAreaCommand)
    fun set_distraction_command(command: WanderCommand)
    fun set_distraction_command(command: StopCommand)
    fun set_distraction_command(command: FleeCommand)
    fun set_distraction_command(command: BuildBaseCommand)
    fun set_autonomous()
    fun start_moving()
    fun destroy()
    var members: Array<LuaEntity>
    var position: PositionTable
    var state: group_state
    var force: LuaForce
    var surface: LuaSurface
    var group_number: uint
    var is_script_driven: Boolean?
    var command: dynamic /* AttackCommand? | GoToLocationCommand? | CompoundCommand? | GroupCommand? | AttackAreaCommand? | WanderCommand? | StopCommand? | FleeCommand? | BuildBaseCommand? */
        get() = definedExternally
        set(value) = definedExternally
    var distraction_command: dynamic /* AttackCommand? | GoToLocationCommand? | CompoundCommand? | GroupCommand? | AttackAreaCommand? | WanderCommand? | StopCommand? | FleeCommand? | BuildBaseCommand? */
        get() = definedExternally
        set(value) = definedExternally
    var valid: Boolean?
    var object_name: String /* "LuaUnitGroup" */
    fun help(): String
}

external interface LuaVirtualSignalPrototype {
    var name: String
    var order: String
    var localised_name: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var localised_description: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var special: Boolean?
    var subgroup: LuaGroup
    var valid: Boolean?
    var object_name: String /* "LuaVirtualSignalPrototype" */
    fun help(): String
}

external interface LuaVoidEnergySourcePrototype {
    var emissions: double
    var render_no_network_icon: Boolean?
    var render_no_power_icon: Boolean?
    var valid: Boolean?
    var object_name: String /* "LuaVoidEnergySourcePrototype" */
    fun help(): String
}

external interface LuaWallControlBehavior : LuaControlBehavior {
    var circuit_condition: CircuitConditionDefinition
    var open_gate: Boolean?
    var read_sensor: Boolean?
    var output_signal: SignalID
    var valid: Boolean?
    var object_name: String /* "LuaWallControlBehavior" */
    fun help(): String
}