@file:JsQualifier("defines")
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package defines

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

external enum class alert_type {
    entity_destroyed,
    entity_under_attack,
    not_enough_construction_robots,
    no_material_for_construction,
    not_enough_repair_packs,
    turret_fire,
    custom,
    no_storage,
    train_out_of_fuel
}

external enum class behavior_result {
    in_progress,
    fail,
    success,
    deleted
}

external enum class build_check_type {
    script,
    manual,
    manual_ghost,
    script_ghost,
    blueprint_ghost,
    ghost_revive
}

external enum class chain_signal_state {
    none,
    all_open,
    partially_open,
    none_open
}

external enum class chunk_generated_status {
    nothing,
    custom_tiles,
    basic_tiles,
    corrected_tiles,
    tiles,
    entities
}

external enum class circuit_condition_index {
    inserter_circuit,
    inserter_logistic,
    lamp,
    arithmetic_combinator,
    decider_combinator,
    constant_combinator,
    offshore_pump,
    pump
}

external enum class circuit_connector_id {
    accumulator,
    constant_combinator,
    container,
    programmable_speaker,
    rail_signal,
    rail_chain_signal,
    roboport,
    storage_tank,
    wall,
    electric_pole,
    inserter,
    lamp,
    combinator_input,
    combinator_output,
    offshore_pump,
    pump
}

external enum class command {
    attack /* = 0 */,
    go_to_location /* = 1 */,
    compound /* = 2 */,
    group /* = 3 */,
    attack_area /* = 4 */,
    wander /* = 5 */,
    flee /* = 6 */,
    stop /* = 7 */,
    build_base /* = 8 */
}

external enum class compound_command {
    logical_and,
    logical_or,
    return_last
}

external enum class controllers {
    ghost,
    character,
    god,
    editor,
    cutscene,
    spectator
}

external enum class difficulty {
    easy,
    normal,
    hard
}

external enum class direction {
    north /* = 0 */,
    northeast /* = 1 */,
    east /* = 2 */,
    southeast /* = 3 */,
    south /* = 4 */,
    southwest /* = 5 */,
    west /* = 6 */,
    northwest /* = 7 */
}

external enum class disconnect_reason {
    quit,
    dropped,
    reconnect,
    wrong_input,
    desync_limit_reached,
    cannot_keep_up,
    afk,
    kicked,
    kicked_and_deleted,
    banned,
    switching_servers
}

external enum class distraction {
    none,
    by_enemy,
    by_anything,
    by_damage
}

external enum class entity_status {
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
    cant_divide_segments
}

external enum class flow_precision_index {
    five_seconds,
    one_minute,
    ten_minutes,
    one_hour,
    ten_hours,
    fifty_hours,
    two_hundred_fifty_hours,
    one_thousand_hours
}

external enum class group_state {
    gathering,
    moving,
    attacking_distraction,
    attacking_target,
    finished,
    pathfinding,
    wander_in_group
}

external enum class gui_type {
    none,
    entity,
    research,
    controller,
    production,
    item,
    bonus,
    trains,
    achievement,
    blueprint_library,
    equipment,
    logistic,
    other_player,
    permissions,
    tutorials,
    custom,
    server_management,
    player_management,
    tile
}

external enum class input_action {
    activate_copy,
    activate_cut,
    activate_paste,
    add_permission_group,
    add_train_station,
    admin_action,
    alt_select_area,
    alt_select_blueprint_entities,
    alternative_copy,
    begin_mining,
    begin_mining_terrain,
    build,
    build_rail,
    build_terrain,
    cancel_craft,
    cancel_deconstruct,
    cancel_new_blueprint,
    cancel_research,
    cancel_upgrade,
    change_active_character_tab,
    change_active_item_group_for_crafting,
    change_active_item_group_for_filters,
    change_active_quick_bar,
    change_arithmetic_combinator_parameters,
    change_decider_combinator_parameters,
    change_entity_label,
    change_item_description,
    change_item_label,
    change_multiplayer_config,
    change_picking_state,
    change_programmable_speaker_alert_parameters,
    change_programmable_speaker_circuit_parameters,
    change_programmable_speaker_parameters,
    change_riding_state,
    change_shooting_state,
    change_train_stop_station,
    change_train_wait_condition,
    change_train_wait_condition_data,
    clear_cursor,
    connect_rolling_stock,
    copy,
    copy_entity_settings,
    copy_opened_blueprint,
    copy_opened_item,
    craft,
    cursor_split,
    cursor_transfer,
    custom_input,
    cycle_blueprint_book_backwards,
    cycle_blueprint_book_forwards,
    deconstruct,
    delete_blueprint_library,
    delete_blueprint_record,
    delete_custom_tag,
    delete_permission_group,
    destroy_item,
    destroy_opened_item,
    disconnect_rolling_stock,
    drag_train_schedule,
    drag_train_wait_condition,
    drop_blueprint_record,
    drop_item,
    edit_blueprint_tool_preview,
    edit_custom_tag,
    edit_permission_group,
    export_blueprint,
    fast_entity_split,
    fast_entity_transfer,
    flush_opened_entity_fluid,
    flush_opened_entity_specific_fluid,
    go_to_train_station,
    grab_blueprint_record,
    gui_checked_state_changed,
    gui_click,
    gui_confirmed,
    gui_elem_changed,
    gui_location_changed,
    gui_selected_tab_changed,
    gui_selection_state_changed,
    gui_switch_state_changed,
    gui_text_changed,
    gui_value_changed,
    import_blueprint,
    import_blueprint_string,
    import_blueprints_filtered,
    import_permissions_string,
    inventory_split,
    inventory_transfer,
    launch_rocket,
    lua_shortcut,
    map_editor_action,
    market_offer,
    mod_settings_changed,
    open_achievements_gui,
    open_blueprint_library_gui,
    open_blueprint_record,
    open_bonus_gui,
    open_character_gui,
    open_current_vehicle_gui,
    open_equipment,
    open_gui,
    open_item,
    open_logistic_gui,
    open_mod_item,
    open_parent_of_opened_item,
    open_production_gui,
    open_technology_gui,
    open_tips_and_tricks_gui,
    open_train_gui,
    open_train_station_gui,
    open_trains_gui,
    paste_entity_settings,
    place_equipment,
    quick_bar_pick_slot,
    quick_bar_set_selected_page,
    quick_bar_set_slot,
    reassign_blueprint,
    remove_cables,
    remove_train_station,
    reset_assembling_machine,
    reset_item,
    rotate_entity,
    select_area,
    select_blueprint_entities,
    select_entity_slot,
    select_item,
    select_mapper_slot,
    select_next_valid_gun,
    select_tile_slot,
    send_spidertron,
    set_auto_launch_rocket,
    set_autosort_inventory,
    set_behavior_mode,
    set_car_weapons_control,
    set_circuit_condition,
    set_circuit_mode_of_operation,
    set_controller_logistic_trash_filter_item,
    set_deconstruction_item_tile_selection_mode,
    set_deconstruction_item_trees_and_rocks_only,
    set_entity_color,
    set_entity_energy_property,
    set_entity_logistic_trash_filter_item,
    set_filter,
    set_flat_controller_gui,
    set_heat_interface_mode,
    set_heat_interface_temperature,
    set_infinity_container_filter_item,
    set_infinity_container_remove_unfiltered_items,
    set_infinity_pipe_filter,
    set_inserter_max_stack_size,
    set_inventory_bar,
    set_linked_container_link_i_d,
    set_logistic_filter_item,
    set_logistic_filter_signal,
    set_player_color,
    set_recipe_notifications,
    set_request_from_buffers,
    set_research_finished_stops_game,
    set_signal,
    set_splitter_priority,
    set_train_stopped,
    set_trains_limit,
    set_vehicle_automatic_targeting_parameters,
    setup_assembling_machine,
    setup_blueprint,
    setup_single_blueprint_record,
    smart_pipette,
    spawn_item,
    stack_split,
    stack_transfer,
    start_repair,
    start_research,
    start_walking,
    stop_building_by_moving,
    switch_connect_to_logistic_network,
    switch_constant_combinator_state,
    switch_inserter_filter_mode_state,
    switch_power_switch_state,
    switch_to_rename_stop_gui,
    take_equipment,
    toggle_deconstruction_item_entity_filter_mode,
    toggle_deconstruction_item_tile_filter_mode,
    toggle_driving,
    toggle_enable_vehicle_logistics_while_moving,
    toggle_entity_logistic_requests,
    toggle_equipment_movement_bonus,
    toggle_map_editor,
    toggle_personal_logistic_requests,
    toggle_personal_roboport,
    toggle_show_entity_info,
    translate_string,
    undo,
    upgrade,
    upgrade_opened_blueprint_by_item,
    upgrade_opened_blueprint_by_record,
    use_artillery_remote,
    use_item,
    wire_dragging,
    write_to_console
}

external enum class inventory {
    fuel,
    burnt_result,
    chest,
    furnace_source,
    furnace_result,
    furnace_modules,
    character_main,
    character_guns,
    character_ammo,
    character_armor,
    character_vehicle,
    character_trash,
    god_main,
    editor_main,
    editor_guns,
    editor_ammo,
    editor_armor,
    roboport_robot,
    roboport_material,
    robot_cargo,
    robot_repair,
    assembling_machine_input,
    assembling_machine_output,
    assembling_machine_modules,
    lab_input,
    lab_modules,
    mining_drill_modules,
    item_main,
    rocket_silo_rocket,
    rocket_silo_result,
    rocket,
    car_trunk,
    car_ammo,
    cargo_wagon,
    turret_ammo,
    beacon_modules,
    character_corpse,
    artillery_turret_ammo,
    artillery_wagon_ammo,
    spider_trunk,
    spider_ammo,
    spider_trash
}

external enum class logistic_member_index {
    logistic_container,
    vehicle_storage,
    character_requester,
    character_storage,
    character_provider,
    generic_on_off_behavior
}

external enum class logistic_mode {
    none,
    active_provider,
    storage,
    requester,
    passive_provider,
    buffer
}

external enum class mouse_button_type {
    none,
    left,
    right,
    middle
}

external var prototypes: Any

external enum class rail_connection_direction {
    left,
    straight,
    right,
    none
}

external enum class rail_direction {
    front,
    back
}

external enum class relative_gui_position {
    top,
    bottom,
    left,
    right
}

external enum class relative_gui_type {
    accumulator_gui,
    achievement_gui,
    additional_entity_info_gui,
    admin_gui,
    arithmetic_combinator_gui,
    armor_gui,
    assembling_machine_gui,
    assembling_machine_select_recipe_gui,
    beacon_gui,
    blueprint_book_gui,
    blueprint_library_gui,
    blueprint_setup_gui,
    bonus_gui,
    burner_equipment_gui,
    car_gui,
    constant_combinator_gui,
    container_gui,
    controller_gui,
    decider_combinator_gui,
    deconstruction_item_gui,
    electric_energy_interface_gui,
    electric_network_gui,
    entity_variations_gui,
    entity_with_energy_source_gui,
    equipment_grid_gui,
    furnace_gui,
    generic_on_off_entity_gui,
    heat_interface_gui,
    infinity_pipe_gui,
    inserter_gui,
    item_with_inventory_gui,
    lab_gui,
    lamp_gui,
    linked_container_gui,
    loader_gui,
    logistic_gui,
    market_gui,
    mining_drill_gui,
    other_player_gui,
    permissions_gui,
    pipe_gui,
    power_switch_gui,
    production_gui,
    programmable_speaker_gui,
    rail_chain_signal_gui,
    rail_signal_gui,
    reactor_gui,
    rename_stop_gui,
    resource_entity_gui,
    roboport_gui,
    rocket_silo_gui,
    server_config_gui,
    spider_vehicle_gui,
    splitter_gui,
    standalone_character_gui,
    storage_tank_gui,
    tile_variations_gui,
    train_gui,
    train_stop_gui,
    trains_gui,
    transport_belt_gui,
    upgrade_item_gui,
    wall_gui
}

external enum class render_mode {
    game,
    chart,
    chart_zoomed_in
}

external enum class rich_text_setting {
    enabled,
    disabled,
    highlight
}

external enum class shooting {
    not_shooting,
    shooting_enemies,
    shooting_selected
}

external enum class signal_state {
    open,
    closed,
    reserved,
    reserved_by_circuit_network
}

external enum class train_state {
    on_the_path,
    path_lost,
    no_schedule,
    no_path,
    arrive_signal,
    wait_signal,
    arrive_station,
    wait_station,
    manual_control_stop,
    manual_control,
    destination_full
}

external enum class transport_line {
    left_line,
    right_line,
    left_underground_line,
    right_underground_line,
    secondary_left_line,
    secondary_right_line,
    left_split_line,
    right_split_line,
    secondary_left_split_line,
    secondary_right_split_line
}

external enum class wire_connection_id {
    electric_pole,
    power_switch_left,
    power_switch_right
}

external enum class wire_type {
    red,
    green,
    copper
}