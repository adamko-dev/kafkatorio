@file:JsQualifier("defines.events")
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package defines.events

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
import uint

external var on_tick: uint /* uint & `T$93`<OnTickEvent, Unit> */

external var on_gui_click: uint /* uint & `T$93`<OnGuiClickEvent, Unit> */

external var on_gui_confirmed: uint /* uint & `T$93`<OnGuiConfirmedEvent, Unit> */

external var on_gui_text_changed: uint /* uint & `T$93`<OnGuiTextChangedEvent, Unit> */

external var on_gui_checked_state_changed: uint /* uint & `T$93`<OnGuiCheckedStateChangedEvent, Unit> */

external var on_entity_died: uint /* uint & `T$93`<OnEntityDiedEvent, LuaEntityDiedEventFilter> */

external var on_post_entity_died: uint /* uint & `T$93`<OnPostEntityDiedEvent, LuaPostEntityDiedEventFilter> */

external var on_entity_damaged: uint /* uint & `T$93`<OnEntityDamagedEvent, LuaEntityDamagedEventFilter> */

external var on_picked_up_item: uint /* uint & `T$93`<OnPickedUpItemEvent, Unit> */

external var on_built_entity: uint /* uint & `T$93`<OnBuiltEntityEvent, LuaPlayerBuiltEntityEventFilter> */

external var on_sector_scanned: uint /* uint & `T$93`<OnSectorScannedEvent, LuaSectorScannedEventFilter> */

external var on_player_mined_item: uint /* uint & `T$93`<OnPlayerMinedItemEvent, Unit> */

external var on_pre_build: uint /* uint & `T$93`<OnPreBuildEvent, Unit> */

external var on_rocket_launched: uint /* uint & `T$93`<OnRocketLaunchedEvent, Unit> */

external var on_pre_player_mined_item: uint /* uint & `T$93`<OnPrePlayerMinedItemEvent, LuaPrePlayerMinedEntityEventFilter> */

external var on_chunk_generated: uint /* uint & `T$93`<OnChunkGeneratedEvent, Unit> */

external var on_player_crafted_item: uint /* uint & `T$93`<OnPlayerCraftedItemEvent, Unit> */

external var on_robot_built_entity: uint /* uint & `T$93`<OnRobotBuiltEntityEvent, LuaRobotBuiltEntityEventFilter> */

external var on_robot_pre_mined: uint /* uint & `T$93`<OnRobotPreMinedEvent, LuaPreRobotMinedEntityEventFilter> */

external var on_robot_mined: uint /* uint & `T$93`<OnRobotMinedEvent, Unit> */

external var on_research_started: uint /* uint & `T$93`<OnResearchStartedEvent, Unit> */

external var on_research_finished: uint /* uint & `T$93`<OnResearchFinishedEvent, Unit> */

external var on_research_reversed: uint /* uint & `T$93`<OnResearchReversedEvent, Unit> */

external var on_player_rotated_entity: uint /* uint & `T$93`<OnPlayerRotatedEntityEvent, Unit> */

external var on_marked_for_deconstruction: uint /* uint & `T$93`<OnMarkedForDeconstructionEvent, LuaEntityMarkedForDeconstructionEventFilter> */

external var on_cancelled_deconstruction: uint /* uint & `T$93`<OnCancelledDeconstructionEvent, LuaEntityDeconstructionCancelledEventFilter> */

external var on_trigger_created_entity: uint /* uint & `T$93`<OnTriggerCreatedEntityEvent, Unit> */

external var on_trigger_fired_artillery: uint /* uint & `T$93`<OnTriggerFiredArtilleryEvent, Unit> */

external var on_train_changed_state: uint /* uint & `T$93`<OnTrainChangedStateEvent, Unit> */

external var on_player_created: uint /* uint & `T$93`<OnPlayerCreatedEvent, Unit> */

external var on_resource_depleted: uint /* uint & `T$93`<OnResourceDepletedEvent, Unit> */

external var on_player_driving_changed_state: uint /* uint & `T$93`<OnPlayerDrivingChangedStateEvent, Unit> */

external var on_force_created: uint /* uint & `T$93`<OnForceCreatedEvent, Unit> */

external var on_forces_merging: uint /* uint & `T$93`<OnForcesMergingEvent, Unit> */

external var on_player_cursor_stack_changed: uint /* uint & `T$93`<OnPlayerCursorStackChangedEvent, Unit> */

external var on_pre_entity_settings_pasted: uint /* uint & `T$93`<OnPreEntitySettingsPastedEvent, Unit> */

external var on_entity_settings_pasted: uint /* uint & `T$93`<OnEntitySettingsPastedEvent, Unit> */

external var on_player_main_inventory_changed: uint /* uint & `T$93`<OnPlayerMainInventoryChangedEvent, Unit> */

external var on_player_armor_inventory_changed: uint /* uint & `T$93`<OnPlayerArmorInventoryChangedEvent, Unit> */

external var on_player_ammo_inventory_changed: uint /* uint & `T$93`<OnPlayerAmmoInventoryChangedEvent, Unit> */

external var on_player_gun_inventory_changed: uint /* uint & `T$93`<OnPlayerGunInventoryChangedEvent, Unit> */

external var on_player_placed_equipment: uint /* uint & `T$93`<OnPlayerPlacedEquipmentEvent, Unit> */

external var on_player_removed_equipment: uint /* uint & `T$93`<OnPlayerRemovedEquipmentEvent, Unit> */

external var on_pre_player_died: uint /* uint & `T$93`<OnPrePlayerDiedEvent, Unit> */

external var on_player_died: uint /* uint & `T$93`<OnPlayerDiedEvent, Unit> */

external var on_player_respawned: uint /* uint & `T$93`<OnPlayerRespawnedEvent, Unit> */

external var on_player_joined_game: uint /* uint & `T$93`<OnPlayerJoinedGameEvent, Unit> */

external var on_player_left_game: uint /* uint & `T$93`<OnPlayerLeftGameEvent, Unit> */

external var on_player_built_tile: uint /* uint & `T$93`<OnPlayerBuiltTileEvent, Unit> */

external var on_player_mined_tile: uint /* uint & `T$93`<OnPlayerMinedTileEvent, Unit> */

external var on_robot_built_tile: uint /* uint & `T$93`<OnRobotBuiltTileEvent, Unit> */

external var on_robot_mined_tile: uint /* uint & `T$93`<OnRobotMinedTileEvent, Unit> */

external var on_player_selected_area: uint /* uint & `T$93`<OnPlayerSelectedAreaEvent, Unit> */

external var on_player_alt_selected_area: uint /* uint & `T$93`<OnPlayerAltSelectedAreaEvent, Unit> */

external var on_player_changed_surface: uint /* uint & `T$93`<OnPlayerChangedSurfaceEvent, Unit> */

external var on_selected_entity_changed: uint /* uint & `T$93`<OnSelectedEntityChangedEvent, Unit> */

external var on_market_item_purchased: uint /* uint & `T$93`<OnMarketItemPurchasedEvent, Unit> */

external var on_player_dropped_item: uint /* uint & `T$93`<OnPlayerDroppedItemEvent, Unit> */

external var on_biter_base_built: uint /* uint & `T$93`<OnBiterBaseBuiltEvent, Unit> */

external var on_player_changed_force: uint /* uint & `T$93`<OnPlayerChangedForceEvent, Unit> */

external var on_entity_renamed: uint /* uint & `T$93`<OnEntityRenamedEvent, Unit> */

external var on_gui_selection_state_changed: uint /* uint & `T$93`<OnGuiSelectionStateChangedEvent, Unit> */

external var on_runtime_mod_setting_changed: uint /* uint & `T$93`<OnRuntimeModSettingChangedEvent, Unit> */

external var on_difficulty_settings_changed: uint /* uint & `T$93`<OnDifficultySettingsChangedEvent, Unit> */

external var on_surface_created: uint /* uint & `T$93`<OnSurfaceCreatedEvent, Unit> */

external var on_surface_deleted: uint /* uint & `T$93`<OnSurfaceDeletedEvent, Unit> */

external var on_pre_surface_deleted: uint /* uint & `T$93`<OnPreSurfaceDeletedEvent, Unit> */

external var on_player_mined_entity: uint /* uint & `T$93`<OnPlayerMinedEntityEvent, LuaPlayerMinedEntityEventFilter> */

external var on_robot_mined_entity: uint /* uint & `T$93`<OnRobotMinedEntityEvent, LuaRobotMinedEntityEventFilter> */

external var on_train_created: uint /* uint & `T$93`<OnTrainCreatedEvent, Unit> */

external var on_gui_elem_changed: uint /* uint & `T$93`<OnGuiElemChangedEvent, Unit> */

external var on_player_setup_blueprint: uint /* uint & `T$93`<OnPlayerSetupBlueprintEvent, Unit> */

external var on_player_deconstructed_area: uint /* uint & `T$93`<OnPlayerDeconstructedAreaEvent, Unit> */

external var on_player_configured_blueprint: uint /* uint & `T$93`<OnPlayerConfiguredBlueprintEvent, Unit> */

external var on_console_chat: uint /* uint & `T$93`<OnConsoleChatEvent, Unit> */

external var on_console_command: uint /* uint & `T$93`<OnConsoleCommandEvent, Unit> */

external var on_player_removed: uint /* uint & `T$93`<OnPlayerRemovedEvent, Unit> */

external var on_pre_player_removed: uint /* uint & `T$93`<OnPrePlayerRemovedEvent, Unit> */

external var on_player_used_capsule: uint /* uint & `T$93`<OnPlayerUsedCapsuleEvent, Unit> */

external var script_raised_built: uint /* uint & `T$93`<ScriptRaisedBuiltEvent, LuaScriptRaisedBuiltEventFilter> */

external var script_raised_destroy: uint /* uint & `T$93`<ScriptRaisedDestroyEvent, LuaScriptRaisedDestroyEventFilter> */

external var script_raised_revive: uint /* uint & `T$93`<ScriptRaisedReviveEvent, LuaScriptRaisedReviveEventFilter> */

external var script_raised_set_tiles: uint /* uint & `T$93`<ScriptRaisedSetTilesEvent, Unit> */

external var on_player_promoted: uint /* uint & `T$93`<OnPlayerPromotedEvent, Unit> */

external var on_player_demoted: uint /* uint & `T$93`<OnPlayerDemotedEvent, Unit> */

external var on_combat_robot_expired: uint /* uint & `T$93`<OnCombatRobotExpiredEvent, Unit> */

external var on_worker_robot_expired: uint /* uint & `T$93`<OnWorkerRobotExpiredEvent, Unit> */

external var on_player_changed_position: uint /* uint & `T$93`<OnPlayerChangedPositionEvent, Unit> */

external var on_mod_item_opened: uint /* uint & `T$93`<OnModItemOpenedEvent, Unit> */

external var on_gui_opened: uint /* uint & `T$93`<OnGuiOpenedEvent, Unit> */

external var on_gui_closed: uint /* uint & `T$93`<OnGuiClosedEvent, Unit> */

external var on_gui_value_changed: uint /* uint & `T$93`<OnGuiValueChangedEvent, Unit> */

external var on_player_muted: uint /* uint & `T$93`<OnPlayerMutedEvent, Unit> */

external var on_player_unmuted: uint /* uint & `T$93`<OnPlayerUnmutedEvent, Unit> */

external var on_player_cheat_mode_enabled: uint /* uint & `T$93`<OnPlayerCheatModeEnabledEvent, Unit> */

external var on_player_cheat_mode_disabled: uint /* uint & `T$93`<OnPlayerCheatModeDisabledEvent, Unit> */

external var on_character_corpse_expired: uint /* uint & `T$93`<OnCharacterCorpseExpiredEvent, Unit> */

external var on_pre_ghost_deconstructed: uint /* uint & `T$93`<OnPreGhostDeconstructedEvent, LuaPreGhostDeconstructedEventFilter> */

external var on_player_pipette: uint /* uint & `T$93`<OnPlayerPipetteEvent, Unit> */

external var on_player_display_resolution_changed: uint /* uint & `T$93`<OnPlayerDisplayResolutionChangedEvent, Unit> */

external var on_player_display_scale_changed: uint /* uint & `T$93`<OnPlayerDisplayScaleChangedEvent, Unit> */

external var on_pre_player_crafted_item: uint /* uint & `T$93`<OnPrePlayerCraftedItemEvent, Unit> */

external var on_player_cancelled_crafting: uint /* uint & `T$93`<OnPlayerCancelledCraftingEvent, Unit> */

external var on_chunk_charted: uint /* uint & `T$93`<OnChunkChartedEvent, Unit> */

external var on_technology_effects_reset: uint /* uint & `T$93`<OnTechnologyEffectsResetEvent, Unit> */

external var on_force_reset: uint /* uint & `T$93`<OnForceResetEvent, Unit> */

external var on_land_mine_armed: uint /* uint & `T$93`<OnLandMineArmedEvent, Unit> */

external var on_forces_merged: uint /* uint & `T$93`<OnForcesMergedEvent, Unit> */

external var on_player_trash_inventory_changed: uint /* uint & `T$93`<OnPlayerTrashInventoryChangedEvent, Unit> */

external var on_pre_player_left_game: uint /* uint & `T$93`<OnPrePlayerLeftGameEvent, Unit> */

external var on_pre_surface_cleared: uint /* uint & `T$93`<OnPreSurfaceClearedEvent, Unit> */

external var on_surface_cleared: uint /* uint & `T$93`<OnSurfaceClearedEvent, Unit> */

external var on_chunk_deleted: uint /* uint & `T$93`<OnChunkDeletedEvent, Unit> */

external var on_pre_chunk_deleted: uint /* uint & `T$93`<OnPreChunkDeletedEvent, Unit> */

external var on_train_schedule_changed: uint /* uint & `T$93`<OnTrainScheduleChangedEvent, Unit> */

external var on_player_banned: uint /* uint & `T$93`<OnPlayerBannedEvent, Unit> */

external var on_player_kicked: uint /* uint & `T$93`<OnPlayerKickedEvent, Unit> */

external var on_player_unbanned: uint /* uint & `T$93`<OnPlayerUnbannedEvent, Unit> */

external var on_rocket_launch_ordered: uint /* uint & `T$93`<OnRocketLaunchOrderedEvent, Unit> */

external var on_script_path_request_finished: uint /* uint & `T$93`<OnScriptPathRequestFinishedEvent, Unit> */

external var on_ai_command_completed: uint /* uint & `T$93`<OnAiCommandCompletedEvent, Unit> */

external var on_marked_for_upgrade: uint /* uint & `T$93`<OnMarkedForUpgradeEvent, LuaEntityMarkedForUpgradeEventFilter> */

external var on_cancelled_upgrade: uint /* uint & `T$93`<OnCancelledUpgradeEvent, LuaUpgradeCancelledEventFilter> */

external var on_player_toggled_map_editor: uint /* uint & `T$93`<OnPlayerToggledMapEditorEvent, Unit> */

external var on_entity_cloned: uint /* uint & `T$93`<OnEntityClonedEvent, LuaEntityClonedEventFilter> */

external var on_area_cloned: uint /* uint & `T$93`<OnAreaClonedEvent, Unit> */

external var on_brush_cloned: uint /* uint & `T$93`<OnBrushClonedEvent, Unit> */

external var on_game_created_from_scenario: uint /* uint & `T$93`<OnGameCreatedFromScenarioEvent, Unit> */

external var on_surface_imported: uint /* uint & `T$93`<OnSurfaceImportedEvent, Unit> */

external var on_surface_renamed: uint /* uint & `T$93`<OnSurfaceRenamedEvent, Unit> */

external var on_player_toggled_alt_mode: uint /* uint & `T$93`<OnPlayerToggledAltModeEvent, Unit> */

external var on_player_repaired_entity: uint /* uint & `T$93`<OnPlayerRepairedEntityEvent, LuaPlayerRepairedEntityEventFilter> */

external var on_player_fast_transferred: uint /* uint & `T$93`<OnPlayerFastTransferredEvent, Unit> */

external var on_pre_robot_exploded_cliff: uint /* uint & `T$93`<OnPreRobotExplodedCliffEvent, Unit> */

external var on_robot_exploded_cliff: uint /* uint & `T$93`<OnRobotExplodedCliffEvent, Unit> */

external var on_entity_spawned: uint /* uint & `T$93`<OnEntitySpawnedEvent, Unit> */

external var on_cutscene_waypoint_reached: uint /* uint & `T$93`<OnCutsceneWaypointReachedEvent, Unit> */

external var on_unit_group_created: uint /* uint & `T$93`<OnUnitGroupCreatedEvent, Unit> */

external var on_unit_added_to_group: uint /* uint & `T$93`<OnUnitAddedToGroupEvent, Unit> */

external var on_unit_removed_from_group: uint /* uint & `T$93`<OnUnitRemovedFromGroupEvent, Unit> */

external var on_unit_group_finished_gathering: uint /* uint & `T$93`<OnUnitGroupFinishedGatheringEvent, Unit> */

external var on_build_base_arrived: uint /* uint & `T$93`<OnBuildBaseArrivedEvent, Unit> */

external var on_chart_tag_added: uint /* uint & `T$93`<OnChartTagAddedEvent, Unit> */

external var on_chart_tag_modified: uint /* uint & `T$93`<OnChartTagModifiedEvent, Unit> */

external var on_chart_tag_removed: uint /* uint & `T$93`<OnChartTagRemovedEvent, Unit> */

external var on_lua_shortcut: uint /* uint & `T$93`<OnLuaShortcutEvent, Unit> */

external var on_gui_location_changed: uint /* uint & `T$93`<OnGuiLocationChangedEvent, Unit> */

external var on_gui_selected_tab_changed: uint /* uint & `T$93`<OnGuiSelectedTabChangedEvent, Unit> */

external var on_gui_switch_state_changed: uint /* uint & `T$93`<OnGuiSwitchStateChangedEvent, Unit> */

external var on_force_cease_fire_changed: uint /* uint & `T$93`<OnForceCeaseFireChangedEvent, Unit> */

external var on_force_friends_changed: uint /* uint & `T$93`<OnForceFriendsChangedEvent, Unit> */

external var on_string_translated: uint /* uint & `T$93`<OnStringTranslatedEvent, Unit> */

external var on_script_trigger_effect: uint /* uint & `T$93`<OnScriptTriggerEffectEvent, Unit> */

external var on_player_set_quick_bar_slot: uint /* uint & `T$93`<OnPlayerSetQuickBarSlotEvent, Unit> */

external var on_pre_player_toggled_map_editor: uint /* uint & `T$93`<OnPrePlayerToggledMapEditorEvent, Unit> */

external var on_pre_script_inventory_resized: uint /* uint & `T$93`<OnPreScriptInventoryResizedEvent, Unit> */

external var on_script_inventory_resized: uint /* uint & `T$93`<OnScriptInventoryResizedEvent, Unit> */

external var on_entity_destroyed: uint /* uint & `T$93`<OnEntityDestroyedEvent, Unit> */

external var on_player_clicked_gps_tag: uint /* uint & `T$93`<OnPlayerClickedGpsTagEvent, Unit> */

external var on_player_flushed_fluid: uint /* uint & `T$93`<OnPlayerFlushedFluidEvent, Unit> */

external var on_permission_group_edited: uint /* uint & `T$93`<OnPermissionGroupEditedEvent, Unit> */

external var on_pre_permission_string_imported: uint /* uint & `T$93`<OnPrePermissionStringImportedEvent, Unit> */

external var on_permission_string_imported: uint /* uint & `T$93`<OnPermissionStringImportedEvent, Unit> */

external var on_pre_permission_group_deleted: uint /* uint & `T$93`<OnPrePermissionGroupDeletedEvent, Unit> */

external var on_permission_group_deleted: uint /* uint & `T$93`<OnPermissionGroupDeletedEvent, Unit> */

external var on_permission_group_added: uint /* uint & `T$93`<OnPermissionGroupAddedEvent, Unit> */

external var on_cutscene_cancelled: uint /* uint & `T$93`<OnCutsceneCancelledEvent, Unit> */

external var on_player_configured_spider_remote: uint /* uint & `T$93`<OnPlayerConfiguredSpiderRemoteEvent, Unit> */

external var on_player_used_spider_remote: uint /* uint & `T$93`<OnPlayerUsedSpiderRemoteEvent, Unit> */

external var on_spider_command_completed: uint /* uint & `T$93`<OnSpiderCommandCompletedEvent, Unit> */

external var on_entity_logistic_slot_changed: uint /* uint & `T$93`<OnEntityLogisticSlotChangedEvent, Unit> */

external var on_equipment_inserted: uint /* uint & `T$93`<OnEquipmentInsertedEvent, Unit> */

external var on_equipment_removed: uint /* uint & `T$93`<OnEquipmentRemovedEvent, Unit> */