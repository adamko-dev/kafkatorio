package dev.adamko.kafkatorio.processor.serdes

import dev.adamko.kafkatorio.events.schema.jsonMapperKafkatorio
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serializer

val protoMapper: ProtoBuf = ProtoBuf {
  serializersModule = jsonMapperKafkatorio.serializersModule
}


inline fun <reified T> BinaryFormat.kafkaSerializer() =
  Serializer { topic: String, data: T ->
    runCatching {
      encodeToByteArray(data)
    }.getOrElse { e ->
      println(
        """
            Exception on encodeToString 
            Topic: $topic
            topicData: $data
            topicData as T: $data
          """.trimIndent()
      )
      e.printStackTrace()
      throw e
    }
  }

inline fun <reified T> BinaryFormat.kafkaDeserializer() =
  Deserializer { topic: String, data: ByteArray ->
    runCatching {
      decodeFromByteArray<T>(data)
    }.getOrElse { e ->
      println(
        """
            Exception on decodeFromString,    
            Topic: $topic
            topicData: $data
            topicData as T: ${data as? T}
          """.trimIndent()
      )
      e.printStackTrace()
      throw e
    }
  }

inline fun <reified T> ProtoBuf.serde() = object : Serde<T> {

  override fun serializer(): Serializer<T> = kafkaSerializer()

  override fun deserializer(): Deserializer<T> = kafkaDeserializer()

}
