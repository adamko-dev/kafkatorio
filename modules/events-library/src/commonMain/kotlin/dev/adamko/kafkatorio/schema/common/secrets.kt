package dev.adamko.kafkatorio.schema.common

import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@SerialName("kafkatorio.common.MaskedValue")
@JvmInline
value class MaskedValue(val value: String) {
  override fun toString(): String = "Secret(***)"
}
