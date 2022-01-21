package dev.adamko.kafkatorio.processor.topology

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class SurfaceIndex(
  val index: Int
) {
  override fun toString() = "$index"
}
