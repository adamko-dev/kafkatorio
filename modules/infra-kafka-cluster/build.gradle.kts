plugins {
  id("kafkatorio.conventions.base")
  id("dev.adamko.geedeecee")
}

description = "Kafka cluster"

geedeecee {
  srcDir.set(layout.projectDirectory.dir("src"))
}

tasks.dockerComposeEnvUpdate {
  envProperties {
    put("GRAFANA_VERSION", libs.versions.grafana)
    put("PROMETHEUS_VERSION", libs.versions.prometheus)
    put("KAFKA_VERSION", libs.versions.kafka)
    put("KAFKA_DL_URL", libs.versions.kafka.map { ver ->
      "https://dlcdn.apache.org/kafka/$ver/kafka_2.13-$ver.tgz"
    })
    put("APP_NAME", "kafka-kraft")
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
//  )
}

val runKafkaCluster by tasks.registering {
  group = rootProject.name

  dependsOn(tasks.dockerComposeUp)
}
