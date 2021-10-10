@file:JsModule("mod-gui")
@file:JsNonModule
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package mod_gui

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
import LuaPlayer
import FrameGuiElementMembers
import FlowGuiElementMembers

external var button_style: String

external var frame_style: String

external fun get_button_flow(player: LuaPlayer): FrameGuiElementMembers /* FrameGuiElementMembers & GuiElementIndex */

external fun get_frame_flow(player: LuaPlayer): FlowGuiElementMembers /* FlowGuiElementMembers & GuiElementIndex */