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
value class SurfaceIndex(val value: UInt)


@Serializable
@JvmInline
value class ForceIndex(val value: UInt)


/** The player's index in `LuaGameScript::players` */
@Serializable
@JvmInline
value class PlayerIndex(val value: UInt)


/** This is universally unique for every entity that has one, for the lifetime of a whole game. */
@Serializable
@JvmInline
value class UnitNumber(val value: UInt)


@Serializable
@JvmInline
value class PrototypeName(val value: String)
