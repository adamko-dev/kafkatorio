package dev.adamko.kafkatorio.processor.topology

import kotlinx.serialization.Serializable

@Serializable
data class SurfaceIndex(
  val surfaceIndex: Int
) {
  override fun toString() = "$surfaceIndex"
}
