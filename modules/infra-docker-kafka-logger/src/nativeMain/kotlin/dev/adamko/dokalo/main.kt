package dev.adamko.dokalo

import dev.adamko.dokalo.clib.errStr
import dev.adamko.dokalo.kafkalib.DeliveryReportCallback
import dev.adamko.dokalo.kafkalib.RdKafkaConfig
import dev.adamko.dokalo.kafkalib.RdKafkaMessage
import dev.adamko.dokalo.kafkalib.RdKafkaProducer
import dev.adamko.dokalo.kafkalib.description
import dev.adamko.dokalo.kafkalib.set
import dev.adamko.dokalo.kafkalib.setDeliveryReportCallback
import external.libkafka.rd_kafka_conf_new
import external.libkafka.rd_kafka_get_err_descs
import external.libkafka.rd_kafka_message_t
import external.libkafka.rd_kafka_new
import external.libkafka.rd_kafka_t
import external.libkafka.rd_kafka_type_t
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.pointed
import kotlinx.cinterop.staticCFunction


fun main(args: Array<String>) {

  val conf: RdKafkaConfig = rd_kafka_conf_new() ?: error("error creating rd config")


  conf["compression.code"] = "snappy"
  conf["transactional.id"] = "dokalo"

  conf.setDeliveryReportCallback(deliveryReportCallback)


  val producer = errStr {
    rd_kafka_new(rd_kafka_type_t.RD_KAFKA_PRODUCER, conf, stringRef, stringLen)
  }.getOrThrow { "failed to create producer: $it" }

  rd_kafka_get_err_descs()

}


val deliveryReportCallback: CPointer<DeliveryReportCallback> =
  staticCFunction { producerPtr: RdKafkaProducer?, msgPtr: RdKafkaMessage?, _: COpaquePointer? ->

    val msg: rd_kafka_message_t = msgPtr?.pointed ?: return@staticCFunction
    val producer: rd_kafka_t = producerPtr?.pointed ?: return@staticCFunction

    if (msg.err != 0) {
      // failure
      println(msg.err.description)

    } else {
      // success

    }


  }
