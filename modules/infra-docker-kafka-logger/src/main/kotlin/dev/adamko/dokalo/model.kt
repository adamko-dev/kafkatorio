@file:OptIn(ExperimentalSerializationApi::class)

package dev.adamko.dokalo

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber


@Serializable
@JvmInline
value class ContainerId(private val id: String)


@Serializable
@JvmInline
value class LogFile(val file: String)


/** Info provides enough information for a logging driver to do its function. */
@Serializable
data class ContainerInfo(
  @SerialName("ContainerID")
  val containerId: ContainerId,

  @SerialName("Config")
  val config: Map<String, String>? = null,

  @SerialName("ContainerName")
  val containerName: String? = null,

  @SerialName("ContainerEntrypoint")
  val containerEntrypoint: String? = null,

  @SerialName("ContainerArgs")
  val containerArgs: List<String>? = null,

  @SerialName("ContainerImageID")
  val containerImageId: String? = null,

  @SerialName("ContainerImageName")
  val containerImageName: String? = null,

  @SerialName("ContainerCreated")
  val containerCreated: LocalDateTime? = null,

  @SerialName("ContainerEnv")
  val containerEnv: List<String>? = null,

  @SerialName("ContainerLabels")
  val containerLabels: Map<String, String>? = null,

  @SerialName("LogPath")
  val logPath: String? = null,

  @SerialName("DaemonName")
  val daemonName: String? = null,
)


@Serializable
data class LogEntry(
  @ProtoNumber(1)
  val source: String,
  @ProtoNumber(2)
  val time_nano: Long,
  @ProtoNumber(3)
  val line: ByteArray,
  @ProtoNumber(4)
  val partial: Boolean,
  @ProtoNumber(5)
  val partial_log_metadata: PartialLogEntryMetadata,
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as LogEntry

    if (source != other.source) return false
    if (time_nano != other.time_nano) return false
    if (!line.contentEquals(other.line)) return false
    if (partial != other.partial) return false
    if (partial_log_metadata != other.partial_log_metadata) return false

    return true
  }

  override fun hashCode(): Int {
    var result = source.hashCode()
    result = 31 * result + time_nano.hashCode()
    result = 31 * result + line.contentHashCode()
    result = 31 * result + partial.hashCode()
    result = 31 * result + partial_log_metadata.hashCode()
    return result
  }
}


@Serializable
data class PartialLogEntryMetadata(
  @ProtoNumber(1)
  val last: Boolean,
  @ProtoNumber(2)
  val id: String,
  @ProtoNumber(3)
  val ordinal: Int,
)
