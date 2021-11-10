package dev.adamko.factorioevents.model

import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import me.ntrrgc.tsGenerator.ClassTransformer
import me.ntrrgc.tsGenerator.TypeScriptGenerator
import me.ntrrgc.tsGenerator.camelCaseToSnakeCase


fun main() {

  val gen =
    TypeScriptGenerator(
      rootClasses =
      buildSet {
        add(FactorioEvent::class)
        addAll(FactorioObjectData::class.sealedSubclasses)
      },
      mappings = mapOf(
        // builtin Factorio numeric types > `typed-factorio/generated/builtin-types.d.ts`
        Float::class to "float",
        Double::class to "double",
        Int::class to "int",
        Byte::class to "int8",
        UInt::class to "uint",
        UByte::class to "uint8",
        UShort::class to "uint16",
        ULong::class to "uint64",
      ),
      classTransformers = listOf(
        PropertyNameTransformer,
      )
    )

  println(gen.definitionsText)
}

object PropertyNameTransformer : ClassTransformer {
  override fun transformPropertyName(
    propertyName: String,
    property: KProperty<*>,
    klass: KClass<*>
  ): String {
    return camelCaseToSnakeCase(propertyName)
  }
}
