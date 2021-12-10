import dev.adamko.kafkatorio.gradle.asConsumer
import dev.adamko.kafkatorio.jsonMapper
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

plugins {
  id("dev.adamko.kafkatorio.infra.docker-compose")
}

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
}


val connectorJarDir: Directory = layout.projectDirectory.dir("src/camel-kafka-connectors")

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


val availableCamelVersions by tasks.registering {
  // dicking around with some sort of 'all camel connectors' docker image.
  // it's unfinished.

  val camelGroup = "org.apache.camel.kafkaconnector"
  val camelParent = "connectors"

  val requestUrl = """
    https://search.maven.org/solrsearch/select?q=g:"$camelGroup"+AND+a:"$camelParent"&core=gav&rows=20&wt=json
  """.trimIndent()
    .replace("\"", "%22")

  doLast {

    val connectorVersions = java.net.URL(requestUrl).readText()
    logger.lifecycle("conn ==== $connectorVersions")
    val versionsJson = jsonMapper.parseToJsonElement(connectorVersions).jsonObject

    logger.lifecycle("json ==== \n ${jsonMapper.encodeToString(versionsJson)}")

    val deps = versionsJson["response"]
      ?.jsonObject
      ?.get("docs")
      ?.jsonArray
      ?.map { it.jsonObject }
      ?.mapNotNull { j ->
        val group = j["g"]?.jsonPrimitive?.contentOrNull
        val artifact = j["a"]?.jsonPrimitive?.contentOrNull
        val version = j["v"]?.jsonPrimitive?.contentOrNull
        val packaging = j["p"]?.jsonPrimitive?.contentOrNull?.toLowerCase()

        if (listOf(group, artifact, version, packaging).all { it != null }) {
          val d = project.dependencies.create(
            group = group!!,
            name = artifact!!,
            version = version!!,
            ext = packaging,
          )
          logger.lifecycle("dep = $d / $packaging")
          d
        } else {
          null
        }
      } ?: emptyList()

    val c = configurations.detachedConfiguration()
    c.dependencies.addAll(deps)
    c.resolve()
      .joinToString("\n\n---\n\n") { it.readLines().take(5).joinToString("\n") }

//    logger.lifecycle("ids: ${versions?.joinToString("\n", "\n\n", "\n\n")}")
//
//    versions?.filterNotNull()?.forEach { id ->
//
//      configurations.detachedConfiguration(
//        project.dependencies.create(id)
//      )
//        .resolve()
//        .joinToString("\n\n---\n\n") { it.readLines().take(5).joinToString("\n") }
//
//    }


//  jsonMapper.parseToJsonElement()

  }
}
