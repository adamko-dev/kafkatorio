package dev.adamko.factorioevents.model

import kotlin.js.JsExport
import kotlin.jvm.JvmInline

//expect sealed interface JsonPrimitive {
//  value class JsonString(val string: String) : JsonPrimitive
//  value class JsonNumber(val number: Number) : JsonPrimitive
//  value class JsonBoolean(val boolean: Boolean) : JsonPrimitive
//}

sealed interface JsonPrimitive {
  @JvmInline
  value class JsonString(val string: String) : JsonPrimitive
  @JvmInline
  value class JsonNumber(val number: Number) : JsonPrimitive
  @JvmInline
  value class JsonBoolean(val boolean: Boolean) : JsonPrimitive
}

class JsonArray<T : JsonPrimitive?>(
  private val list: MutableList<T> = mutableListOf()
) : MutableList<T> by list


sealed interface JsonTableValue {
  @JvmInline
  value class JsonPrimitiveTV(val primitive: JsonPrimitive) : JsonTableValue
  @JvmInline
  value class JsonArrayTV<T : JsonPrimitive?>(val array: JsonArray<T>) : JsonTableValue
  @JvmInline
  value class JsonTableTV<T : JsonTable?>(val array: JsonTable) : JsonTableValue
}

//expect class JsonNumber() : JsonPrimitiveType

//expect class JsonArray<T : JsonPrimitive?>

//expect open class JsonTable<K : JsonPrimitive.JsonString, V : JsonTableValue?>

open class JsonTable(
  protected val map: MutableMap<JsonPrimitive.JsonString, JsonTableValue?> = mutableMapOf()
) : MutableMap<JsonPrimitive.JsonString, JsonTableValue?> by map {

  inline fun <reified T> get2(key: String): T? {

    val jKey = JsonPrimitive.JsonString(key)

    return when {
      T::class is Number -> ((get(jKey) as? JsonTableValue.JsonPrimitiveTV)?.primitive as? JsonPrimitive.JsonNumber)?.number as? T
      else               -> return get(jKey) as? T
    }

  }

}

//@JsExport
class FactorioEvent : JsonTable() {
  val tick: UInt?
    get() = get2("tick")
//    set(value) = map.put("tick", value)
}