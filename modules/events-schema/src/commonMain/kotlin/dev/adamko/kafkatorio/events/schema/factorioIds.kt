package dev.adamko.kafkatorio.events.schema

import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable


/**
 * Unique ID associated with this surface
 *
 * See [`LuaSurface.index`](https://lua-api.factorio.com/latest/LuaSurface.html#LuaSurface.index)
 */
@Serializable
@JvmInline
value class SurfaceIndex(private val id: UInt) {
  override fun toString(): String = "$id"
}


@Serializable
@JvmInline
value class ForceIndex(private val id: UInt) {
  override fun toString(): String = "$id"
}


/** The player's index in `LuaGameScript::players` */
@Serializable
@JvmInline
value class PlayerIndex(private val id: UInt) {
  override fun toString(): String = "$id"
}


/** > This is universally unique for every entity that has one, for the lifetime of a whole game. */
@Serializable
@JvmInline
value class UnitNumber(private val id: UInt) {
  override fun toString(): String = "$id"
}


@Serializable
@JvmInline
value class PrototypeName(private val id: String) {
  override fun toString(): String = id
}
