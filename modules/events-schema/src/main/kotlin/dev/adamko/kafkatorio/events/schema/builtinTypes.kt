@file:Suppress("ClassName")

package dev.adamko.kafkatorio.events.schema


import kotlinx.serialization.Serializable


/*
 * The basic types used in the Factorio API. While Lua only supports the `boolean`, `number`,
 * `string`, and `table` types (as they relate to the types listed below), the API docs will
 * specify what C++ types these are converted to or from internally.
 */


/**
 * Tables are enclosed in curly brackets, like this `{}`
 *
 * [View documentation](https://lua-api.factorio.com/next/Builtin-Types.html#table)
 */
typealias F_table = MutableMap<Any, Any>

sealed interface FBuiltInType {

  sealed interface FNumber {
    /**
     * A floating-point number. This is a single-precision floating point number. Whilst Lua only uses double-precision
     * numbers, when a function takes a float, the game engine will immediately convert the double-precision number to
     * single-precision.
     *
     * [View documentation](https://lua-api.factorio.com/next/Builtin-Types.html#float)
     */
    @Serializable
    @JvmInline
    value class F_float(val float: Float) : FNumber
    /**
     * A double-precision floating-point number. This is the same data type as all Lua numbers use.
     *
     * [View documentation](https://lua-api.factorio.com/next/Builtin-Types.html#double)
     */
    @Serializable
    @JvmInline
    value class F_double(val double: Double) : FNumber

    /**
     * 32-bit signed integer. Possible values are -2,147,483,648 to 2,147,483,647.
     *
     * [View documentation](https://lua-api.factorio.com/next/Builtin-Types.html#int)
     */
    @Serializable
    @JvmInline
    value class F_int(val int: Int) : FNumber
    /**
     * 8-bit signed integer. Possible values are -128 to 127.
     *
     * [View documentation](https://lua-api.factorio.com/next/Builtin-Types.html#int8)
     */
    @Serializable
    @JvmInline
    value class F_int8(val int8: Byte) : FNumber

    /**
     * 32-bit unsigned integer. Possible values are 0 to 4,294,967,295.
     *
     * [View documentation](https://lua-api.factorio.com/next/Builtin-Types.html#uint)
     */
    @Serializable
    @JvmInline
    value class F_uint(val uint: UInt) : FNumber
    /**
     * 8-bit unsigned integer. Possible values are 0 to 255.
     *
     * [View documentation](https://lua-api.factorio.com/next/Builtin-Types.html#uint8)
     */
    @Serializable
    @JvmInline
    value class F_uint8(val uint8: UByte) : FNumber
    /**
     * 16-bit unsigned integer. Possible values are 0 to 65535.
     *
     * [View documentation](https://lua-api.factorio.com/next/Builtin-Types.html#uint16)
     */
    @Serializable
    @JvmInline
    value class F_uint16(val uint16: UShort) : FNumber
    /**
     * 64-bit unsigned integer. Possible values are 0 to 18,446,744,073,709,551,615.
     *
     * [View documentation](https://lua-api.factorio.com/next/Builtin-Types.html#uint64)
     */
    @Serializable
    @JvmInline
    value class F_uint64(val uint64: ULong) : FNumber
  }


}
