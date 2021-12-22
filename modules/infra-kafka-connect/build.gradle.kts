@file:Suppress("UnstableApiUsage") // VERSION_PLATFORM + platform(...) is unstable

import dev.adamko.kafkatorio.gradle.asConsumer

plugins {
  id("dev.adamko.kafkatorio.infra.docker-compose")
  id("dev.adamko.kafkatorio.lang.kotlin-jvm")
}

val dockerSrcDir: Directory by extra(layout.projectDirectory.dir("src/main/docker"))
val connectorJarDir: Directory = dockerSrcDir.dir("camel-kafka-connectors")

val camelConnectorDependencies: Configuration by configurations.creating { asConsumer() }

fun DependencyHandlerScope.kafkaConnector(
  name: String,
  group: String = "org.apache.camel.kafkaconnector",
  version: String = libs.versions.camel.kafkaConnectors.get(),
) {
  camelConnectorDependencies(group, name, version)
}

dependencies {
  kafkaConnector("camel-websocket-kafka-connector")
  kafkaConnector("camel-ahc-ws-kafka-connector")

  implementation("com.github.adamko-dev:json5-kotlin:2.0.3")

  implementation(platform(libs.http4k.bom))
  implementation("org.http4k:http4k-core")
  implementation("org.http4k:http4k-client-okhttp")

  implementation(platform(libs.kotlinx.serialization.bom))
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-core")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")
}


val downloadCamelConnectors by tasks.registering(Sync::class) {
  group = project.name
  description = "Retrieve Camel Kafka Connectors from Maven, in preparation for the Docker build"

  from(
    provider { camelConnectorDependencies }
      .map { eventsSchema ->
        eventsSchema.incoming
          .artifactView { lenient(true) }
          .artifacts
          .artifactFiles
      }
  )
  into(connectorJarDir)
}

tasks.dockerUp {
  dependsOn(":modules:infra-kafka-cluster:dockerUp")
  dependsOn(downloadCamelConnectors)
}

tasks.dockerEnv {
  properties(
    "CONNECTOR_ID" to "kafkatorio-connect",
    "KAFKA_CONNECT_VERSION" to libs.versions.confluent.kafkaConnect.get(),
  )
}
