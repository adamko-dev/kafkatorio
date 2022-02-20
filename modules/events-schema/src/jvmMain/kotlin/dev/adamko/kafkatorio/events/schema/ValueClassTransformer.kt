package dev.adamko.kafkatorio.events.schema

import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.withNullability
import me.ntrrgc.tsGenerator.ClassTransformer

/**
 * 'Unwrap' value classes
 */
object ValueClassTransformer : ClassTransformer {

  override fun transformPropertyType(type: KType, property: KProperty<*>, klass: KClass<*>): KType {

    val classifier = type.classifier ?: return super.transformPropertyType(type, property, klass)

    val valueClassType = unwrapValueClassType(type)

    if (valueClassType != null) {
      println("$klass has a value class $type -> $valueClassType")
      return valueClassType
    }

    val hasValueClassTypeParameters = type.arguments
      .mapNotNull { it.type?.classifier }
      .filterIsInstance<KClass<*>>()
      .any { it.isValue }

    if (hasValueClassTypeParameters) {

      val args = type.arguments.map { arg ->
        when (val vct = unwrapValueClassType(arg.type)) {
          null -> arg
          else -> KTypeProjection(arg.variance, vct.withNullability(arg.type!!.isMarkedNullable))
        }
      }

      println("$klass has a prop with value class type parameters ${type.arguments} -> $args")

      return classifier.createType(
        arguments = args,
        nullable = type.isMarkedNullable,
        annotations = type.annotations,
      )
    }

    return super.transformPropertyType(type, property, klass)
  }

  private fun unwrapValueClassType(type: KType?): KType? {
    val klass = type?.classifier as? KClass<*> ?: return null

    return when {
      klass.isValue -> klass
        .declaredMemberProperties
        .first()
        .returnType
        .withNullability(type.isMarkedNullable)

      else          -> null
    }
  }
}
