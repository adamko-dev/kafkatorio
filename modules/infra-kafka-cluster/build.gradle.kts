plugins {
  id("dev.adamko.geedeecee")
}

description = "Kafka cluster"

geedeecee {
  srcDir.set(layout.projectDirectory.dir("src"))
}

tasks.dockerComposeEnvUpdate {
  properties(
    "GRAFANA_VERSION" to libs.versions.grafana,
    "PROMETHEUS_VERSION" to libs.versions.prometheus,
    "KAFKA_VERSION" to libs.versions.kafka,
    "KAFKA_DL_URL" to libs.versions.kafka.map { ver ->
      "https://dlcdn.apache.org/kafka/$ver/kafka_2.13-$ver.tgz"
    }

    // Limit JVM Heap Size
//    "KAFKA_BROKER_HEAP_OPTS" to "-XX:MaxRAMPercentage=70.0",
//    "KAFKA_CONNECT_HEAP_OPTS" to "-XX:MaxRAMPercentage=70.0",
//    "SCHEMA_REGISTRY_HEAP_OPTS" to "-XX:MaxRAMPercentage=70.0",
//    "KAFKA_KSQL_HEAP_OPTS" to "-XX:MaxRAMPercentage=70.0",
//    "PROMETHEUS_JMX_AGENT_JVM_OPTS" to "-Xmx128M",

//    // Limit container resources
//    "KAFKA_BROKER_MEM_LIMIT" to "512m",
//    "KAFKA_CONNECT_MEM_LIMIT" to "512m",
//    "KAFKA_KSQL_MEM_LIMIT" to "256m",
//    "SCHEMA_REGISTRY_MEM_LIMIT" to "256m",
  )
}

val runKafkaCluster by tasks.registering {
  group = rootProject.name

  dependsOn(tasks.dockerComposeUp)
}
