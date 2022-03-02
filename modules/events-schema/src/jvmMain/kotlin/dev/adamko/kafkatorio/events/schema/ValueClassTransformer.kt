package dev.adamko.kafkatorio.events.schema

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.full.withNullability
import me.ntrrgc.tsGenerator.ClassTransformer

/**
 * 'Unwrap' value classes
 */
object ValueClassTransformer : ClassTransformer {

  private val mappedValueClasses: MutableMap<KType, KType> = mutableMapOf(
    // don't unwrap unsigned numbers - they're mapped to Factorio types
    UInt::class.starProjectedType to UInt::class.starProjectedType,
    UByte::class.starProjectedType to UByte::class.starProjectedType,
    UShort::class.starProjectedType to UShort::class.starProjectedType,
    ULong::class.starProjectedType to ULong::class.starProjectedType,
  )

  override fun transformPropertyType(type: KType, property: KProperty<*>, klass: KClass<*>): KType {
    val unwrappedType =
      unwrapValueClassType(type, property, klass)
        ?: unwrapArgs(type, property, klass)
    return when (unwrappedType) {
      null -> super.transformPropertyType(type, property, klass)
      else -> super.transformPropertyType(unwrappedType, property, klass)
    }
  }

  private fun unwrapArgs(type: KType, property: KProperty<*>, klass: KClass<*>): KType? {

    val classifier: KClassifier = type.classifier ?: return type

    return if (type.arguments.isNotEmpty()) {
      println("unwrapping args for $type - ${type.arguments}")

      val args = type.arguments.map { arg: KTypeProjection ->
        // recursively map each argument
        val argType = arg.type!!
        val converted = transformPropertyType(argType, property, klass)
        KTypeProjection(arg.variance, converted.withNullability(argType.isMarkedNullable))
      }

      classifier.createType(
        arguments = args,
        nullable = type.isMarkedNullable,
        annotations = type.annotations,
      )
    } else {
      null
    }
  }

  private fun unwrapValueClassType(type: KType, property: KProperty<*>, klass: KClass<*>): KType? {

    val valueKlass: KClass<*> = type.classifier as? KClass<*> ?: return null

    return if (isValueClass(type)) {
      val mappedType = mappedValueClasses.getOrPut(type.withNullability(false)) {

        val result = valueKlass
          .declaredMemberProperties
          .first()
          .returnType
          .withNullability(type.isMarkedNullable)

        println("unwrapping value class $type -> $result")

        // recursively unwrap...
        val transformed = transformPropertyType(result, property, klass)
        transformed.withNullability(false)
      }
      mappedType.withNullability(type.isMarkedNullable)
    } else {
      null
    }
  }

  @OptIn(ExperimentalContracts::class)
  private fun isValueClass(type: KType?): Boolean {
    contract { returnsNotNull() implies (type != null) }
    return (type?.classifier as? KClass<*>)?.isValue == true
  }

}
