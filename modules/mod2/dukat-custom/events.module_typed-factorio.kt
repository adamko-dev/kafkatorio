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
import tsstdlib.Position
import defines.behavior_result
import defines.mouse_button_type
import defines.gui_type
import defines.direction
import defines.input_action
import defines.disconnect_reason
import tsstdlib.Record
import defines.train_state

external interface CustomInputEvent {
    var player_index: uint
    var input_name: String
    var cursor_position: Position
    var selected_prototype: SelectedPrototypeData?
        get() = definedExternally
        set(value) = definedExternally
    var name: String
    var tick: uint
}

external interface OnAiCommandCompletedEvent {
    var unit_number: uint
    var result: behavior_result
    var was_distracted: Boolean
    var name: Any
    var tick: uint
}

external interface OnAreaClonedEvent {
    var source_surface: LuaSurface
    var source_area: dynamic /* BoundingBoxTable | JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */
        get() = definedExternally
        set(value) = definedExternally
    var destination_surface: LuaSurface
    var destination_area: dynamic /* BoundingBoxTable | JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */
        get() = definedExternally
        set(value) = definedExternally
    var destination_force: LuaForce?
        get() = definedExternally
        set(value) = definedExternally
    var clone_tiles: Boolean
    var clone_entities: Boolean
    var clone_decoratives: Boolean
    var clear_destination_entities: Boolean
    var clear_destination_decoratives: Boolean
    var name: Any
    var tick: uint
}

external interface OnBiterBaseBuiltEvent {
    var entity: LuaEntity
    var name: Any
    var tick: uint
}

external interface OnBrushClonedEvent {
    var source_offset: dynamic /* TilePositionTable | JsTuple<x, int, y, int> */
        get() = definedExternally
        set(value) = definedExternally
    var destination_offset: dynamic /* TilePositionTable | JsTuple<x, int, y, int> */
        get() = definedExternally
        set(value) = definedExternally
    var source_surface: LuaSurface
    var source_positions: Array<dynamic /* TilePositionTable | JsTuple<x, int, y, int> */>
    var destination_surface: LuaSurface
    var destination_force: LuaForce?
        get() = definedExternally
        set(value) = definedExternally
    var clone_tiles: Boolean
    var clone_entities: Boolean
    var clone_decoratives: Boolean
    var clear_destination_entities: Boolean
    var clear_destination_decoratives: Boolean
    var name: Any
    var tick: uint
}

external interface OnBuildBaseArrivedEvent {
    var unit: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
    var group: LuaUnitGroup?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnBuiltEntityEvent {
    var created_entity: LuaEntity
    var player_index: uint
    var stack: LuaItemStack
    var item: LuaItemPrototype?
        get() = definedExternally
        set(value) = definedExternally
    var tags: Tags?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnCancelledDeconstructionEvent {
    var entity: LuaEntity
    var player_index: uint?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnCancelledUpgradeEvent {
    var entity: LuaEntity
    var player_index: uint?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnCharacterCorpseExpiredEvent {
    var corpse: LuaEntity
    var name: Any
    var tick: uint
}

external interface OnChartTagAddedEvent {
    var tag: LuaCustomChartTag
    var force: LuaForce
    var player_index: uint?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnChartTagModifiedEvent {
    var tag: LuaCustomChartTag
    var player_index: uint?
        get() = definedExternally
        set(value) = definedExternally
    var force: LuaForce
    var old_text: String
    var old_icon: SignalID?
        get() = definedExternally
        set(value) = definedExternally
    var old_player: uint?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnChartTagRemovedEvent {
    var tag: LuaCustomChartTag
    var force: LuaForce
    var player_index: uint?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnChunkChartedEvent {
    var surface_index: uint
    var position: dynamic /* ChunkPositionTable | JsTuple<x, int, y, int> */
        get() = definedExternally
        set(value) = definedExternally
    var area: dynamic /* BoundingBoxTable | JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */
        get() = definedExternally
        set(value) = definedExternally
    var force: LuaForce
    var name: Any
    var tick: uint
}

external interface OnChunkDeletedEvent {
    var surface_index: uint
    var positions: Array<dynamic /* ChunkPositionTable | JsTuple<x, int, y, int> */>
    var name: Any
    var tick: uint
}

external interface OnChunkGeneratedEvent {
    var area: dynamic /* BoundingBoxTable | JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */
        get() = definedExternally
        set(value) = definedExternally
    var position: dynamic /* ChunkPositionTable | JsTuple<x, int, y, int> */
        get() = definedExternally
        set(value) = definedExternally
    var surface: LuaSurface
    var name: Any
    var tick: uint
}

external interface OnCombatRobotExpiredEvent {
    var robot: LuaEntity
    var owner: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnConsoleChatEvent {
    var player_index: uint?
        get() = definedExternally
        set(value) = definedExternally
    var message: String
    var name: Any
    var tick: uint
}

external interface OnConsoleCommandEvent {
    var player_index: uint?
        get() = definedExternally
        set(value) = definedExternally
    var command: String
    var parameters: String
    var name: Any
    var tick: uint
}

external interface OnCutsceneCancelledEvent {
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnCutsceneWaypointReachedEvent {
    var player_index: uint
    var waypoint_index: uint
    var name: Any
    var tick: uint
}

external interface OnDifficultySettingsChangedEvent {
    var old_recipe_difficulty: uint
    var old_technology_difficulty: uint
    var name: Any
    var tick: uint
}

external interface OnEntityClonedEvent {
    var source: LuaEntity
    var destination: LuaEntity
    var name: Any
    var tick: uint
}

external interface OnEntityDamagedEvent {
    var entity: LuaEntity
    var damage_type: LuaDamagePrototype
    var original_damage_amount: float
    var final_damage_amount: float
    var final_health: float
    var cause: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
    var force: LuaForce?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnEntityDestroyedEvent {
    var registration_number: uint64
    var unit_number: uint?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnEntityDiedEvent {
    var entity: LuaEntity
    var cause: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
    var loot: LuaInventory
    var force: LuaForce?
        get() = definedExternally
        set(value) = definedExternally
    var damage_type: LuaDamagePrototype?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnEntityLogisticSlotChangedEvent {
    var player_index: uint?
        get() = definedExternally
        set(value) = definedExternally
    var entity: LuaEntity
    var slot_index: uint
    var name: Any
    var tick: uint
}

external interface OnEntityRenamedEvent {
    var player_index: uint?
        get() = definedExternally
        set(value) = definedExternally
    var by_script: Boolean
    var entity: LuaEntity
    var old_name: String
    var name: Any
    var tick: uint
}

external interface OnEntitySettingsPastedEvent {
    var player_index: uint
    var source: LuaEntity
    var destination: LuaEntity
    var name: Any
    var tick: uint
}

external interface OnEntitySpawnedEvent {
    var spawner: LuaEntity
    var entity: LuaEntity
    var name: Any
    var tick: uint
}

external interface OnEquipmentInsertedEvent {
    var grid: LuaEquipmentGrid
    var equipment: LuaEquipment
    var name: Any
    var tick: uint
}

external interface OnEquipmentRemovedEvent {
    var grid: LuaEquipmentGrid
    var equipment: String
    var count: uint
    var name: Any
    var tick: uint
}

external interface OnForceCeaseFireChangedEvent {
    var force: LuaForce
    var other_force: LuaForce
    var added: Boolean
    var name: Any
    var tick: uint
}

external interface OnForceCreatedEvent {
    var force: LuaForce
    var name: Any
    var tick: uint
}

external interface OnForceFriendsChangedEvent {
    var force: LuaForce
    var other_force: LuaForce
    var added: Boolean
    var name: Any
    var tick: uint
}

external interface OnForceResetEvent {
    var force: LuaForce
    var name: Any
    var tick: uint
}

external interface OnForcesMergedEvent {
    var source_name: String
    var source_index: uint
    var destination: LuaForce
    var name: Any
    var tick: uint
}

external interface OnForcesMergingEvent {
    var source: LuaForce
    var destination: LuaForce
    var name: Any
    var tick: uint
}

external interface OnGameCreatedFromScenarioEvent {
    var name: Any
    var tick: uint
}

external interface OnGuiCheckedStateChangedEvent {
    var element: dynamic /* ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers */
        get() = definedExternally
        set(value) = definedExternally
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnGuiClickEvent {
    var element: dynamic /* ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers */
        get() = definedExternally
        set(value) = definedExternally
    var player_index: uint
    var button: mouse_button_type
    var alt: Boolean
    var control: Boolean
    var shift: Boolean
    var name: Any
    var tick: uint
}

external interface OnGuiClosedEvent {
    var player_index: uint
    var gui_type: gui_type
    var entity: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
    var item: LuaItemStack?
        get() = definedExternally
        set(value) = definedExternally
    var equipment: LuaEquipment?
        get() = definedExternally
        set(value) = definedExternally
    var other_player: LuaPlayer?
        get() = definedExternally
        set(value) = definedExternally
    var element: dynamic /* ChooseElemButtonGuiElementMembers? | DropDownGuiElementMembers? | EmptyWidgetGuiElementMembers? | EntityPreviewGuiElementMembers? | ListBoxGuiElementMembers? | ScrollPaneGuiElementMembers? | SpriteButtonGuiElementMembers? | TabbedPaneGuiElementMembers? | TextBoxGuiElementMembers? | ButtonGuiElementMembers? | CameraGuiElementMembers? | CheckboxGuiElementMembers? | FlowGuiElementMembers? | FrameGuiElementMembers? | LabelGuiElementMembers? | LineGuiElementMembers? | MinimapGuiElementMembers? | ProgressbarGuiElementMembers? | RadiobuttonGuiElementMembers? | SliderGuiElementMembers? | SpriteGuiElementMembers? | SwitchGuiElementMembers? | TabGuiElementMembers? | TableGuiElementMembers? | TextfieldGuiElementMembers? */
        get() = definedExternally
        set(value) = definedExternally
    var technology: LuaTechnology?
        get() = definedExternally
        set(value) = definedExternally
    var tile_position: dynamic /* TilePositionTable? | JsTuple<x, int, y, int> */
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnGuiConfirmedEvent {
    var element: dynamic /* ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers */
        get() = definedExternally
        set(value) = definedExternally
    var player_index: uint
    var alt: Boolean
    var control: Boolean
    var shift: Boolean
    var name: Any
    var tick: uint
}

external interface OnGuiElemChangedEvent {
    var element: dynamic /* ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers */
        get() = definedExternally
        set(value) = definedExternally
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnGuiLocationChangedEvent {
    var element: dynamic /* ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers */
        get() = definedExternally
        set(value) = definedExternally
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnGuiOpenedEvent {
    var player_index: uint
    var gui_type: gui_type
    var entity: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
    var item: LuaItemStack?
        get() = definedExternally
        set(value) = definedExternally
    var equipment: LuaEquipment?
        get() = definedExternally
        set(value) = definedExternally
    var other_player: LuaPlayer?
        get() = definedExternally
        set(value) = definedExternally
    var element: dynamic /* ChooseElemButtonGuiElementMembers? | DropDownGuiElementMembers? | EmptyWidgetGuiElementMembers? | EntityPreviewGuiElementMembers? | ListBoxGuiElementMembers? | ScrollPaneGuiElementMembers? | SpriteButtonGuiElementMembers? | TabbedPaneGuiElementMembers? | TextBoxGuiElementMembers? | ButtonGuiElementMembers? | CameraGuiElementMembers? | CheckboxGuiElementMembers? | FlowGuiElementMembers? | FrameGuiElementMembers? | LabelGuiElementMembers? | LineGuiElementMembers? | MinimapGuiElementMembers? | ProgressbarGuiElementMembers? | RadiobuttonGuiElementMembers? | SliderGuiElementMembers? | SpriteGuiElementMembers? | SwitchGuiElementMembers? | TabGuiElementMembers? | TableGuiElementMembers? | TextfieldGuiElementMembers? */
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnGuiSelectedTabChangedEvent {
    var element: dynamic /* ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers */
        get() = definedExternally
        set(value) = definedExternally
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnGuiSelectionStateChangedEvent {
    var element: dynamic /* ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers */
        get() = definedExternally
        set(value) = definedExternally
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnGuiSwitchStateChangedEvent {
    var element: dynamic /* ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers */
        get() = definedExternally
        set(value) = definedExternally
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnGuiTextChangedEvent {
    var element: dynamic /* ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers */
        get() = definedExternally
        set(value) = definedExternally
    var player_index: uint
    var text: String
    var name: Any
    var tick: uint
}

external interface OnGuiValueChangedEvent {
    var element: dynamic /* ChooseElemButtonGuiElementMembers | DropDownGuiElementMembers | EmptyWidgetGuiElementMembers | EntityPreviewGuiElementMembers | ListBoxGuiElementMembers | ScrollPaneGuiElementMembers | SpriteButtonGuiElementMembers | TabbedPaneGuiElementMembers | TextBoxGuiElementMembers | ButtonGuiElementMembers | CameraGuiElementMembers | CheckboxGuiElementMembers | FlowGuiElementMembers | FrameGuiElementMembers | LabelGuiElementMembers | LineGuiElementMembers | MinimapGuiElementMembers | ProgressbarGuiElementMembers | RadiobuttonGuiElementMembers | SliderGuiElementMembers | SpriteGuiElementMembers | SwitchGuiElementMembers | TabGuiElementMembers | TableGuiElementMembers | TextfieldGuiElementMembers */
        get() = definedExternally
        set(value) = definedExternally
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnLandMineArmedEvent {
    var mine: LuaEntity
    var name: Any
    var tick: uint
}

external interface OnLuaShortcutEvent {
    var player_index: uint
    var prototype_name: String
    var name: Any
    var tick: uint
}

external interface OnMarkedForDeconstructionEvent {
    var entity: LuaEntity
    var player_index: uint?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnMarkedForUpgradeEvent {
    var entity: LuaEntity
    var target: LuaEntityPrototype
    var player_index: uint?
        get() = definedExternally
        set(value) = definedExternally
    var direction: direction?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnMarketItemPurchasedEvent {
    var player_index: uint
    var market: LuaEntity
    var offer_index: uint
    var count: uint
    var name: Any
    var tick: uint
}

external interface OnModItemOpenedEvent {
    var player_index: uint
    var item: LuaItemPrototype
    var name: Any
    var tick: uint
}

external interface OnPermissionGroupAddedEvent {
    var player_index: uint
    var group: LuaPermissionGroup
    var name: Any
    var tick: uint
}

external interface OnPermissionGroupDeletedEvent {
    var player_index: uint
    var group_name: String
    var id: uint
    var name: Any
    var tick: uint
}

external interface OnPermissionGroupEditedEvent {
    var player_index: uint
    var group: LuaPermissionGroup
    var type: String /* "add-permission" | "remove-permission" | "enable-all" | "disable-all" | "add-player" | "remove-player" | "rename" */
    var action: input_action
    var other_player_index: uint
    var old_name: String
    var new_name: String
    var name: Any
    var tick: uint
}

external interface OnPermissionStringImportedEvent {
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnPickedUpItemEvent {
    var item_stack: dynamic /* String | ItemStackDefinition */
        get() = definedExternally
        set(value) = definedExternally
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnPlayerAltSelectedAreaEvent {
    var player_index: uint
    var surface: LuaSurface
    var area: dynamic /* BoundingBoxTable | JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */
        get() = definedExternally
        set(value) = definedExternally
    var item: String
    var entities: Array<LuaEntity>
    var tiles: Array<LuaTile>
    var name: Any
    var tick: uint
}

external interface OnPlayerAmmoInventoryChangedEvent {
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnPlayerArmorInventoryChangedEvent {
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnPlayerBannedEvent {
    var player_index: uint?
        get() = definedExternally
        set(value) = definedExternally
    var player_name: String
    var by_player: uint?
        get() = definedExternally
        set(value) = definedExternally
    var reason: String?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnPlayerBuiltTileEvent {
    var player_index: uint
    var surface_index: uint
    var tiles: Array<OldTileAndPosition>
    var tile: LuaTilePrototype
    var item: LuaItemPrototype?
        get() = definedExternally
        set(value) = definedExternally
    var stack: LuaItemStack?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnPlayerCancelledCraftingEvent {
    var player_index: uint
    var items: LuaInventory
    var recipe: LuaRecipe
    var cancel_count: uint
    var name: Any
    var tick: uint
}

external interface OnPlayerChangedForceEvent {
    var player_index: uint
    var force: LuaForce
    var name: Any
    var tick: uint
}

external interface OnPlayerChangedPositionEvent {
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnPlayerChangedSurfaceEvent {
    var player_index: uint
    var surface_index: uint
    var name: Any
    var tick: uint
}

external interface OnPlayerCheatModeDisabledEvent {
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnPlayerCheatModeEnabledEvent {
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnPlayerClickedGpsTagEvent {
    var player_index: uint
    var position: Position
    var surface: String
    var name: Any
    var tick: uint
}

external interface OnPlayerConfiguredBlueprintEvent {
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnPlayerConfiguredSpiderRemoteEvent {
    var player_index: uint
    var vehicle: LuaEntity
    var name: Any
    var tick: uint
}

external interface OnPlayerCraftedItemEvent {
    var item_stack: LuaItemStack
    var player_index: uint
    var recipe: LuaRecipe
    var name: Any
    var tick: uint
}

external interface OnPlayerCreatedEvent {
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnPlayerCursorStackChangedEvent {
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnPlayerDeconstructedAreaEvent {
    var player_index: uint
    var surface: LuaSurface
    var area: dynamic /* BoundingBoxTable | JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */
        get() = definedExternally
        set(value) = definedExternally
    var item: String
    var alt: Boolean
    var name: Any
    var tick: uint
}

external interface OnPlayerDemotedEvent {
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnPlayerDiedEvent {
    var player_index: uint
    var cause: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnPlayerDisplayResolutionChangedEvent {
    var player_index: uint
    var old_resolution: DisplayResolution
    var name: Any
    var tick: uint
}

external interface OnPlayerDisplayScaleChangedEvent {
    var player_index: uint
    var old_scale: double
    var name: Any
    var tick: uint
}

external interface OnPlayerDrivingChangedStateEvent {
    var player_index: uint
    var entity: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnPlayerDroppedItemEvent {
    var player_index: uint
    var entity: LuaEntity
    var name: Any
    var tick: uint
}

external interface OnPlayerFastTransferredEvent {
    var player_index: uint
    var entity: LuaEntity
    var from_player: Boolean
    var name: Any
    var tick: uint
}

external interface OnPlayerFlushedFluidEvent {
    var player_index: uint
    var fluid: String
    var amount: double
    var entity: LuaEntity
    var only_this_entity: Boolean
    var name: Any
    var tick: uint
}

external interface OnPlayerGunInventoryChangedEvent {
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnPlayerJoinedGameEvent {
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnPlayerKickedEvent {
    var player_index: uint
    var by_player: uint?
        get() = definedExternally
        set(value) = definedExternally
    var reason: String?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnPlayerLeftGameEvent {
    var player_index: uint
    var reason: disconnect_reason
    var name: Any
    var tick: uint
}

external interface OnPlayerMainInventoryChangedEvent {
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnPlayerMinedEntityEvent {
    var player_index: uint
    var entity: LuaEntity
    var buffer: LuaInventory
    var name: Any
    var tick: uint
}

external interface OnPlayerMinedItemEvent {
    var item_stack: dynamic /* String | ItemStackDefinition */
        get() = definedExternally
        set(value) = definedExternally
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnPlayerMinedTileEvent {
    var player_index: uint
    var surface_index: uint
    var tiles: Array<OldTileAndPosition>
    var name: Any
    var tick: uint
}

external interface OnPlayerMutedEvent {
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnPlayerPipetteEvent {
    var player_index: uint
    var item: LuaItemPrototype
    var used_cheat_mode: Boolean
    var name: Any
    var tick: uint
}

external interface OnPlayerPlacedEquipmentEvent {
    var player_index: uint
    var equipment: LuaEquipment
    var grid: LuaEquipmentGrid
    var name: Any
    var tick: uint
}

external interface OnPlayerPromotedEvent {
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnPlayerRemovedEvent {
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnPlayerRemovedEquipmentEvent {
    var player_index: uint
    var grid: LuaEquipmentGrid
    var equipment: String
    var count: uint
    var name: Any
    var tick: uint
}

external interface OnPlayerRepairedEntityEvent {
    var player_index: uint
    var entity: LuaEntity
    var name: Any
    var tick: uint
}

external interface OnPlayerRespawnedEvent {
    var player_index: uint
    var player_port: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnPlayerRotatedEntityEvent {
    var entity: LuaEntity
    var previous_direction: direction
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnPlayerSelectedAreaEvent {
    var player_index: uint
    var surface: LuaSurface
    var area: dynamic /* BoundingBoxTable | JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */
        get() = definedExternally
        set(value) = definedExternally
    var item: String
    var entities: Array<LuaEntity>
    var tiles: Array<LuaTile>
    var name: Any
    var tick: uint
}

external interface OnPlayerSetQuickBarSlotEvent {
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnPlayerSetupBlueprintEvent {
    var player_index: uint
    var surface: LuaSurface
    var area: dynamic /* BoundingBoxTable | JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */
        get() = definedExternally
        set(value) = definedExternally
    var item: String
    var alt: Boolean
    var mapping: LuaLazyLoadedValue<Record<uint, LuaEntity>>
    var name: Any
    var tick: uint
}

external interface OnPlayerToggledAltModeEvent {
    var player_index: uint
    var alt_mode: Boolean
    var name: Any
    var tick: uint
}

external interface OnPlayerToggledMapEditorEvent {
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnPlayerTrashInventoryChangedEvent {
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnPlayerUnbannedEvent {
    var player_index: uint?
        get() = definedExternally
        set(value) = definedExternally
    var player_name: String
    var by_player: uint?
        get() = definedExternally
        set(value) = definedExternally
    var reason: String?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnPlayerUnmutedEvent {
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnPlayerUsedCapsuleEvent {
    var player_index: uint
    var item: LuaItemPrototype
    var position: Position
    var name: Any
    var tick: uint
}

external interface OnPlayerUsedSpiderRemoteEvent {
    var player_index: uint
    var vehicle: LuaEntity
    var position: Position
    var success: Boolean
    var name: Any
    var tick: uint
}

external interface OnPostEntityDiedEvent {
    var ghost: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
    var force: LuaForce?
        get() = definedExternally
        set(value) = definedExternally
    var position: Position
    var prototype: LuaEntityPrototype
    var damage_type: LuaDamagePrototype?
        get() = definedExternally
        set(value) = definedExternally
    var corpses: Array<LuaEntity>
    var surface_index: uint
    var unit_number: uint?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnPreBuildEvent {
    var player_index: uint
    var position: Position
    var shift_build: Boolean
    var direction: direction
    var flip_horizontal: Boolean
    var flip_vertical: Boolean
    var created_by_moving: Boolean
    var name: Any
    var tick: uint
}

external interface OnPreChunkDeletedEvent {
    var surface_index: uint
    var positions: Array<dynamic /* ChunkPositionTable | JsTuple<x, int, y, int> */>
    var name: Any
    var tick: uint
}

external interface OnPreEntitySettingsPastedEvent {
    var player_index: uint
    var source: LuaEntity
    var destination: LuaEntity
    var name: Any
    var tick: uint
}

external interface OnPreGhostDeconstructedEvent {
    var player_index: uint?
        get() = definedExternally
        set(value) = definedExternally
    var ghost: LuaEntity
    var name: Any
    var tick: uint
}

external interface OnPrePermissionGroupDeletedEvent {
    var player_index: uint
    var group: LuaPermissionGroup
    var name: Any
    var tick: uint
}

external interface OnPrePermissionStringImportedEvent {
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnPrePlayerCraftedItemEvent {
    var player_index: uint
    var recipe: LuaRecipe
    var items: LuaInventory
    var queued_count: uint
    var name: Any
    var tick: uint
}

external interface OnPrePlayerDiedEvent {
    var player_index: uint
    var cause: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnPrePlayerLeftGameEvent {
    var player_index: uint
    var reason: disconnect_reason
    var name: Any
    var tick: uint
}

external interface OnPrePlayerMinedItemEvent {
    var entity: LuaEntity
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnPrePlayerRemovedEvent {
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnPrePlayerToggledMapEditorEvent {
    var player_index: uint
    var name: Any
    var tick: uint
}

external interface OnPreRobotExplodedCliffEvent {
    var robot: LuaEntity
    var cliff: LuaEntity
    var item: LuaItemPrototype
    var name: Any
    var tick: uint
}

external interface OnPreScriptInventoryResizedEvent {
    var player_index: uint?
        get() = definedExternally
        set(value) = definedExternally
    var mod: String
    var inventory: LuaInventory
    var old_size: uint
    var new_size: uint
    var name: Any
    var tick: uint
}

external interface OnPreSurfaceClearedEvent {
    var surface_index: uint
    var name: Any
    var tick: uint
}

external interface OnPreSurfaceDeletedEvent {
    var surface_index: uint
    var name: Any
    var tick: uint
}

external interface OnResearchFinishedEvent {
    var research: LuaTechnology
    var by_script: Boolean
    var name: Any
    var tick: uint
}

external interface OnResearchReversedEvent {
    var research: LuaTechnology
    var by_script: Boolean
    var name: Any
    var tick: uint
}

external interface OnResearchStartedEvent {
    var research: LuaTechnology
    var last_research: LuaTechnology?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnResourceDepletedEvent {
    var entity: LuaEntity
    var name: Any
    var tick: uint
}

external interface OnRobotBuiltEntityEvent {
    var robot: LuaEntity
    var created_entity: LuaEntity
    var stack: LuaItemStack
    var tags: Tags?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnRobotBuiltTileEvent {
    var robot: LuaEntity
    var tiles: Array<OldTileAndPosition>
    var tile: LuaTilePrototype
    var item: LuaItemPrototype
    var stack: LuaItemStack
    var surface_index: uint
    var name: Any
    var tick: uint
}

external interface OnRobotExplodedCliffEvent {
    var robot: LuaEntity
    var item: LuaItemPrototype
    var name: Any
    var tick: uint
}

external interface OnRobotMinedEvent {
    var robot: LuaEntity
    var item_stack: dynamic /* String | ItemStackDefinition */
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnRobotMinedEntityEvent {
    var robot: LuaEntity
    var entity: LuaEntity
    var buffer: LuaInventory
    var name: Any
    var tick: uint
}

external interface OnRobotMinedTileEvent {
    var robot: LuaEntity
    var tiles: Array<OldTileAndPosition>
    var surface_index: uint
    var name: Any
    var tick: uint
}

external interface OnRobotPreMinedEvent {
    var robot: LuaEntity
    var entity: LuaEntity
    var name: Any
    var tick: uint
}

external interface OnRocketLaunchOrderedEvent {
    var rocket: LuaEntity
    var rocket_silo: LuaEntity
    var player_index: uint?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnRocketLaunchedEvent {
    var rocket: LuaEntity
    var rocket_silo: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
    var player_index: uint?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnRuntimeModSettingChangedEvent {
    var player_index: uint?
        get() = definedExternally
        set(value) = definedExternally
    var setting: String
    var setting_type: String /* "runtime-per-user" | "runtime-global" */
    var name: Any
    var tick: uint
}

external interface OnScriptInventoryResizedEvent {
    var player_index: uint?
        get() = definedExternally
        set(value) = definedExternally
    var mod: String
    var inventory: LuaInventory
    var old_size: uint
    var new_size: uint
    var overflow_inventory: LuaInventory
    var name: Any
    var tick: uint
}

external interface OnScriptPathRequestFinishedEvent {
    var path: Array<PathfinderWaypoint>?
        get() = definedExternally
        set(value) = definedExternally
    var id: uint
    var try_again_later: Boolean
    var name: Any
    var tick: uint
}

external interface OnScriptTriggerEffectEvent {
    var effect_id: String
    var surface_index: uint
    var source_position: Position?
        get() = definedExternally
        set(value) = definedExternally
    var source_entity: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
    var target_position: Position?
        get() = definedExternally
        set(value) = definedExternally
    var target_entity: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnSectorScannedEvent {
    var radar: LuaEntity
    var chunk_position: dynamic /* ChunkPositionTable | JsTuple<x, int, y, int> */
        get() = definedExternally
        set(value) = definedExternally
    var area: dynamic /* BoundingBoxTable | JsTuple<left_top, Position, right_bottom, Position, Any, RealOrientation> */
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnSelectedEntityChangedEvent {
    var player_index: uint
    var last_entity: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnSpiderCommandCompletedEvent {
    var vehicle: LuaEntity
    var name: Any
    var tick: uint
}

external interface OnStringTranslatedEvent {
    var player_index: uint
    var localised_string: dynamic /* JsTuple<String, Any> | String | Number */
        get() = definedExternally
        set(value) = definedExternally
    var result: String
    var translated: Boolean
    var name: Any
    var tick: uint
}

external interface OnSurfaceClearedEvent {
    var surface_index: uint
    var name: Any
    var tick: uint
}

external interface OnSurfaceCreatedEvent {
    var surface_index: uint
    var name: Any
    var tick: uint
}

external interface OnSurfaceDeletedEvent {
    var surface_index: uint
    var name: Any
    var tick: uint
}

external interface OnSurfaceImportedEvent {
    var surface_index: uint
    var original_name: String
    var name: Any
    var tick: uint
}

external interface OnSurfaceRenamedEvent {
    var surface_index: uint
    var old_name: String
    var new_name: String
    var name: Any
    var tick: uint
}

external interface OnTechnologyEffectsResetEvent {
    var force: LuaForce
    var name: Any
    var tick: uint
}

external interface OnTickEvent {
    var name: Any
    var tick: uint
}

external interface OnTrainChangedStateEvent {
    var train: LuaTrain
    var old_state: train_state
    var name: Any
    var tick: uint
}

external interface OnTrainCreatedEvent {
    var train: LuaTrain
    var old_train_id_1: uint?
        get() = definedExternally
        set(value) = definedExternally
    var old_train_id_2: uint?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnTrainScheduleChangedEvent {
    var train: LuaTrain
    var player_index: uint?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnTriggerCreatedEntityEvent {
    var entity: LuaEntity
    var source: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnTriggerFiredArtilleryEvent {
    var entity: LuaEntity
    var source: LuaEntity?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface OnUnitAddedToGroupEvent {
    var unit: LuaEntity
    var group: LuaUnitGroup
    var name: Any
    var tick: uint
}

external interface OnUnitGroupCreatedEvent {
    var group: LuaUnitGroup
    var name: Any
    var tick: uint
}

external interface OnUnitGroupFinishedGatheringEvent {
    var group: LuaUnitGroup
    var name: Any
    var tick: uint
}

external interface OnUnitRemovedFromGroupEvent {
    var unit: LuaEntity
    var group: LuaUnitGroup
    var name: Any
    var tick: uint
}

external interface OnWorkerRobotExpiredEvent {
    var robot: LuaEntity
    var name: Any
    var tick: uint
}

external interface ScriptRaisedBuiltEvent {
    var entity: LuaEntity
    var name: Any
    var tick: uint
}

external interface ScriptRaisedDestroyEvent {
    var entity: LuaEntity
    var name: Any
    var tick: uint
}

external interface ScriptRaisedReviveEvent {
    var entity: LuaEntity
    var tags: Tags?
        get() = definedExternally
        set(value) = definedExternally
    var name: Any
    var tick: uint
}

external interface ScriptRaisedSetTilesEvent {
    var surface_index: uint
    var tiles: Array<Tile>
    var name: Any
    var tick: uint
}