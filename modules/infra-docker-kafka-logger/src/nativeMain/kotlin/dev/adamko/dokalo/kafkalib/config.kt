package dev.adamko.dokalo.kafkalib

import dev.adamko.dokalo.clib.errStr
import dev.adamko.dokalo.clib.throwIfNotSuccess
import external.libkafka.rd_kafka_conf_set
import external.libkafka.rd_kafka_conf_set_dr_msg_cb
import external.libkafka.rd_kafka_err2str
import external.libkafka.rd_kafka_resp_err_t
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.toKString

operator fun RdKafkaConfig.set(name: String, value: String) {
  errStr {
    rd_kafka_conf_set(
      this@set,
      name,
      value,
      stringRef,
      stringLen,
    )
  }.throwIfNotSuccess {
    "Error while setting RdKafkaConf[$name] = $value. $errString"
  }
}

fun RdKafkaConfig.setDeliveryReportCallback(callback: CPointer<DeliveryReportCallback>) {
  rd_kafka_conf_set_dr_msg_cb(this, callback)
}


val rd_kafka_resp_err_t.description: String?
  get() = rd_kafka_err2str(this)?.toKString()
