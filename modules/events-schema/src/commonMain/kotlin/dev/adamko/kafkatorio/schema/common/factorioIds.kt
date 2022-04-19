package dev.adamko.kafkatorio.schema.common

import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable


@Serializable
@JvmInline
value class Tick(val value: UInt)


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


interface EntityIdentifiers {
  val unitNumber: UnitNumber?
  val name: String
  /** The prototype-type of the entity. */
  val protoType: String
}


// instant of EntityIdentifiers, because kxs needs a concrete instance
@Serializable
data class EntityIdentifiersData(
  override val unitNumber: UnitNumber? = null,
  override val name: String,
  override val protoType: String,
) : EntityIdentifiers
