package dev.adamko.dokalo.kafkalib

import kotlinx.cinterop.CFunction
import kotlinx.cinterop.CPointer

import external.libkafka.rd_kafka_conf_t
import external.libkafka.rd_kafka_message_t
import external.libkafka.rd_kafka_t
import kotlinx.cinterop.COpaquePointer


typealias RdKafkaConfig = CPointer<rd_kafka_conf_t>
typealias RdKafkaProducer = CPointer<rd_kafka_t>
typealias RdKafkaMessage = CPointer<rd_kafka_message_t>


typealias DeliveryReportCallback = CFunction<(CPointer<rd_kafka_t>?, CPointer<rd_kafka_message_t>?, COpaquePointer?) -> Unit>
