plugins {
  id("dev.adamko.kafkatorio.infra.docker-compose")
}

description = "Send events from a Factorio server to a Kafka topic "

tasks.dockerUp {
  dependsOn(":modules:infra-kafka-cluster:dockerUp")
}
