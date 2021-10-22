package dev.adamko.factorioevents.model


//actual sealed interface JsonPrimitive {
//  @JvmInline
//  actual value class JsonString(val string: String) : JsonPrimitive
//  @JvmInline
//  actual value class JsonNumber(val number: Number) : JsonPrimitive
//  @JvmInline
//  actual value class JsonBoolean(val boolean: Boolean) : JsonPrimitive
//}

//actual class JsonArray<T : JsonPrimitive?>(
//  private val list: MutableList<T> = mutableListOf()
//) : MutableList<T> by list

//actual sealed interface JsonTableValue {
//  @JvmInline
//  actual value class JsonPrimitiveTV(val primitive: JsonPrimitive) : JsonTableValue
//  @JvmInline
//  actual value class JsonArrayTV<T : JsonPrimitive?>(val array: JsonArray<T>) : JsonTableValue
//}

//actual open class JsonTable<K : JsonPrimitive.JsonString, V: JsonTableValue?>(
//  private val map: MutableMap<K, V> = mutableMapOf()
//) : MutableMap<K, V> by map

