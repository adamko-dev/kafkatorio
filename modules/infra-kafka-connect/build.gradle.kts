import dev.adamko.kafkatorio.gradle.asConsumer
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

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
  classifier: String = "package",
  extension: String = "tar.gz",
) {
  camelConnectorDependencies(group, name, version, ext = extension, classifier = classifier)
}

dependencies {
  kotlin("reflect")

  kafkaConnector("camel-ahc-ws-kafka-connector")
  kafkaConnector("camel-websocket-kafka-connector")

  implementation("com.github.adamko-dev:json5-kotlin:2.0.3")

  implementation(platform(libs.http4k.bom))
  implementation(libs.bundles.http4k)

  implementation(platform(libs.kotlinx.serialization.bom))
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-core")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")
}

// disabled for now...
val projectEnabled = false

val dockerBuildKafkaConnect by tasks.registering(Exec::class) {
  enabled = projectEnabled

  group = "docker-compose"
  dependsOn(downloadCamelConnectors, tasks.dockerEnv)

  logging.captureStandardOutput(LogLevel.LIFECYCLE)

  inputs.dir(dockerSrcDir)

  workingDir = dockerSrcDir.asFile
  commandLine = parseSpaceSeparatedArgs(""" docker-compose build connect """)
}
//tasks.assemble { dependsOn(dockerBuildKafkaConnect) }


val downloadCamelConnectors by tasks.registering(Sync::class) {
  enabled = projectEnabled

  group = project.name
  description = "Retrieve Camel Kafka Connectors from Maven, in preparation for the Docker build"

  dependsOn(camelConnectorDependencies)

  from(
    provider { camelConnectorDependencies }
      .map { eventsSchema ->
        eventsSchema.incoming
          .artifacts
          .artifactFiles
      }
  )
  into(temporaryDir)

  doLast {
    sync {
      fileTree(temporaryDir)
        .asFileTree
        .forEach { zip ->
          from(tarTree(zip))
        }
      into(connectorJarDir)
    }
  }
}

tasks.dockerUp {
  enabled = projectEnabled

  dependsOn(":modules:infra-kafka-cluster:dockerUp")
  dependsOn(downloadCamelConnectors)
}

tasks.dockerEnv {
  enabled = projectEnabled

  properties(
    "CONNECTOR_ID" to "kafkatorio-connect",
    "KAFKA_CONNECT_VERSION" to libs.versions.confluent.kafkaConnect.get(),
  )
}

idea {
  module {
    excludeDirs.add(file("src/main/docker/camel-kafka-connectors"))
  }
}

tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions.freeCompilerArgs += listOf(
    "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
  )
}
