package dev.adamko.kafkatorio.processor.syslog

import com.github.palindromicity.syslog.dsl.SyslogFieldKeys
import dev.adamko.kafkatorio.processor.config.ApplicationProperties
import dev.adamko.kafkatorio.processor.config.TOPIC_SRC_SERVER_LOG
import dev.adamko.kafkatorio.processor.config.TOPIC_SRC_SERVER_LOG_DLQ
import dev.adamko.kafkatorio.processor.core.Authenticator
import dev.adamko.kafkatorio.processor.syslog.config.jsonMapper
import dev.adamko.kafkatorio.schema.common.FactorioServerId
import dev.adamko.kotka.kxs.kafkaSerializer
import java.util.Properties
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.job
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.internals.RecordHeader
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsConfig


/**
 * Consume Syslog messages, verifies each, and forwards them to a Kafka topic.
 */
class KafkatorioPacketKafkaProducer(
  private val appProps: ApplicationProperties,
  private val syslogServer: SyslogSocketServer,
  private val authenticator: Authenticator,
) : AutoCloseable {

  private val props: Properties = appProps.kafkaStreamsConfig.toProperties()

  init {
    props.updateValue(StreamsConfig.APPLICATION_ID_CONFIG) { "$it.KafkatorioPacketKafkaProducer" }
  }

  private val producer: KafkaProducer<FactorioServerId, String> = KafkaProducer(
    props.apply {
      updateValue(StreamsConfig.APPLICATION_ID_CONFIG) { "$it.KafkatorioPacketKafkaProducer" }
    },
    jsonMapper.kafkaSerializer(FactorioServerId.serializer()),
    Serdes.String().serializer(),
  )

  private val dlqProducer: KafkaProducer<String, String> = KafkaProducer(
    props.apply {
      updateValue(StreamsConfig.APPLICATION_ID_CONFIG) { "$it.KafkatorioPacketKafkaProducer.dlq" }
    },
    Serdes.String().serializer(),
    Serdes.String().serializer(),
  )


  suspend fun launch() {
    coroutineContext.job.invokeOnCompletion {
      val cause = if (it != null) "(cause: ${it::class.java.name} ${it.message})" else ""
      log("Closing KafkatorioPacketKafkaProducer $cause")
      this@KafkatorioPacketKafkaProducer.close()
    }

    syslogServer.messages
      .filter {
        (it.message ?: "").run {
          startsWith("KafkatorioPacket:::")
        }
      }.onEach { syslog ->
        val serverId = getFactorioServerId(syslog)
        if (serverId != null) {
          produceMessage(serverId, syslog)
        }
      }.collect()
  }


  private fun getFactorioServerId(syslog: SyslogMsg): FactorioServerId? {
    val token = syslog.headerAppName
    if (token.isNullOrBlank()) {
      produceDlqMessage(syslog, "no token in syslog message. msg: ${syslog.src}")
      return null
    }

    val serverToken = authenticator.verifyJwt(token)
    if (serverToken.isNullOrBlank()) {
      produceDlqMessage(syslog, "could not decode token $token. msg: ${syslog.src}")
      return null
    }

    val serverId = appProps.kafkatorioServers[serverToken]
    if (serverId.isNullOrBlank()) {
      produceDlqMessage(syslog, "no server ID for token $serverToken. msg: ${syslog.src}")
      return null
    }

    return serverId
  }


  private fun produceMessage(
    serverId: FactorioServerId,
    syslog: SyslogMsg,
  ) {
    val syslogMessage = syslog.message?.trim()

    if (syslogMessage.isNullOrBlank()) {
      // ignore blank messages
      return
    }

    if (!syslogMessage.startsWith(KAFKATORIO_MESSAGE_TAG)) {
      // ignore non-Kafkatorio message
      return
    }

    val kafkatorioMessage = syslogMessage.substringAfter(KAFKATORIO_MESSAGE_TAG)

    if (kafkatorioMessage.isBlank()) {
      produceDlqMessage(syslog, "error - Kafkatorio message was blank $syslogMessage")
      return
    }

    val record = ProducerRecord(
      TOPIC_SRC_SERVER_LOG,
      serverId,
      kafkatorioMessage,
    )

    syslog.copyHeadersTo(record)

    producer.send(record)
  }


  private fun produceDlqMessage(syslog: SyslogMsg, errorMessage: String) {
    log("error: $errorMessage, $syslog")

    val record = ProducerRecord(
      TOPIC_SRC_SERVER_LOG_DLQ,
      "unknown",
      errorMessage,
    )

    syslog.copyHeadersTo(record)

    dlqProducer.send(record)
  }


  override fun close() {
    producer.close()
  }


  companion object {
    private const val KAFKATORIO_MESSAGE_TAG = "KafkatorioPacket:::"


    private fun log(msg: String) = println("[KafkatorioPacketKafkaProducer] $msg")


    private fun Properties.updateValue(
      key: String,
      update: (oldValue: String) -> String
    ) {
      val newValue = update(getValue(key) as String)
      setProperty(key, newValue)
    }


    private fun SyslogMsg.copyHeadersTo(record: ProducerRecord<*, *>) {
      val headers = SyslogFieldKeys.values()
        .filter { "HEADER_" in it.name }
        .mapNotNull { headerKey ->
          val headerValue = src[headerKey.field]
          when {
            headerValue.isNullOrBlank() -> null
            else                        -> RecordHeader(
              headerKey.field,
              headerValue.toByteArray()
            )
          }
        }

      headers.forEach { header ->
        record.headers().add(header)
      }
    }
  }
}
