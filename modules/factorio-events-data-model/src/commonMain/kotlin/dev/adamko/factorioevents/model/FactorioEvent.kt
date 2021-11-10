package dev.adamko.factorioevents.model

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

//
//@JsExport
//@ExperimentalJsExport
//data class FactorioEvent(
//  val modVersion: String,
//  val tick: F_uint,
//  val eventType: String,
//  val data: FactorioObjectData,
//)
//
//@JsExport
//abstract class FactorioObjectData {
//  abstract val objectName: String
//}
//
//@JsExport
//data class PlayerData(
//  override val objectName: String,
//  val name: String,
//  val characterUnitNumber: F_uint,
//  val associatedCharactersUnitNumbers: List<F_uint>,
//  val position: PositionData,
//) : FactorioObjectData()
//
//
//@JsExport
//data class PositionData(
//  val x: F_int,
//  val y: F_int,
//)
//
//
//
//typealias F_float = Float
//typealias F_double = Double
//typealias F_int = Int
//typealias F_int8 = Byte
//
//
////@JsExport
////@ExperimentalJsExport
//typealias F_uint = UInt
////expect class F_uint
//
////expect value class F_uint internal constructor(internal val data: Int)
//expect value class F_uint8 internal constructor(internal val data: Int)
//expect value class F_uint16 internal constructor(internal val data: Int)
//expect value class F_uint64 internal constructor(internal val data: Int)
//
//typealias F_table = Any?
//
