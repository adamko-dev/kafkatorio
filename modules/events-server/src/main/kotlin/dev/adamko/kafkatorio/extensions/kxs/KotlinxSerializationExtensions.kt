package dev.adamko.kafkatorio.extensions.kxs

//import kotlinx.serialization.decodeFromString
//import kotlinx.serialization.encodeToString
//import kotlinx.serialization.json.Json
//import org.apache.kafka.common.serialization.Deserializer
//import org.apache.kafka.common.serialization.Serde
//import org.apache.kafka.common.serialization.Serializer
//
//object KotlinxSerializationExtensions {
//
//  inline fun <reified T> Json.kafkaSerializer() = Serializer { topic: String, data: T ->
//    runCatching {
//      encodeToString(data).encodeToByteArray()
////      jsonMapper.encodeToString(data as T).encodeToByteArray()
//    }.getOrElse { e ->
//      println(
//        """
//            Exception on encodeToString
//            Topic: $topic
//            topicData: $data
//            topicData as T: ${data as T}
//          """.trimIndent()
//      )
//      e.printStackTrace()
//      throw e
//    }
//  }
//
//  inline fun <reified T> Json.kafkaDeserializer() = Deserializer { topic: String, data: ByteArray ->
//    runCatching {
//      decodeFromString<T>(data.decodeToString())
//    }.getOrElse { e ->
//      println(
//        """
//            Exception on decodeFromString,
//            Topic: $topic
//            topicData: $data
//            topicData as T: ${data as T}
//          """.trimIndent()
//      )
//      e.printStackTrace()
//      throw e
//    }
//  }
//
//
//  inline fun <reified T> Json.serde() = object : Serde<T> {
//
//    override fun serializer(): Serializer<T> = kafkaSerializer()
//
//    override fun deserializer(): Deserializer<T> = kafkaDeserializer()
//  }
//
//}
