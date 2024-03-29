package dev.adamko.kafkatorio.schema.common

import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@SerialName("kafkatorio.id.Tick")
@JvmInline
value class Tick(val value: UInt)

val UInt.tick
  get() = Tick(this)

/**
 * Unique ID associated with this surface
 *
 * See [`LuaSurface.index`](https://lua-api.factorio.com/latest/LuaSurface.html#LuaSurface.index)
 */
@Serializable
@SerialName("kafkatorio.id.SurfaceIndex")
@JvmInline
value class SurfaceIndex(private val id: UInt) {
  override fun toString(): String = "$id"
}


@Serializable
@SerialName("kafkatorio.id.ForceIndex")
@JvmInline
value class ForceIndex(private val id: UInt) {
  override fun toString(): String = "$id"
}


/** The player's index in `LuaGameScript::players` */
@Serializable
@SerialName("kafkatorio.id.PlayerIndex")
@JvmInline
value class PlayerIndex(private val id: UInt) {
  override fun toString(): String = "$id"
}


/** > This is universally unique for every entity that has one, for the lifetime of a whole game. */
@Serializable
@SerialName("kafkatorio.id.UnitNumber")
@JvmInline
value class UnitNumber(private val id: UInt) {
  override fun toString(): String = "$id"
}


@Serializable
@SerialName("kafkatorio.id.EventName")
@JvmInline
value class EventName(private val name: String) {
  override fun toString(): String = name
}


interface EntityIdentifiers {
  val unitNumber: UnitNumber?
  /** The 'prototype type' and 'prototype name' of the entity. */
  val protoId: PrototypeId
}


// instant of EntityIdentifiers, because kxs needs a concrete instance
@Serializable
@SerialName("kafkatorio.id.EntityIdentifiersData")
data class EntityIdentifiersData(
  override val unitNumber: UnitNumber? = null,
  override val protoId: PrototypeId,
) : EntityIdentifiers


/** Friendly identifier, used in URLs. Should be human-readable. */
@Serializable
@SerialName("kafkatorio.id.FactorioServerId")
@JvmInline
value class FactorioServerId(val id: String) : CharSequence by id {

  init {
    require(id.isNotBlank()) { "require FactorioServerId is not blank: '$id'" }
    require(id.matches(validIdRegex)) { "require FactorioServerId matches $validIdRegex: '$id'" }
  }

  override fun toString(): String = id

  companion object {
    val validIdRegex = Regex("""^[a-zA-Z0-9\-_]+""" + "\$")
  }
}


/** Short identifier, used in tokens. Might not be human-readable. */
@Serializable
@SerialName("kafkatorio.id.FactorioServerToken")
@JvmInline
value class FactorioServerToken(private val id: String) : CharSequence by id {

  init {
    require(id.isNotBlank()) { "require FactorioServerToken is not blank: '$id'" }
    require(id.matches(FactorioServerId.validIdRegex)) { "require FactorioServerToken matches ${validIdRegex}: '$id'" }
  }

  override fun toString(): String = id

  companion object {
    val validIdRegex = Regex("^[a-zA-Z0-9]+\$")
  }
}
