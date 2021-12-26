package dev.adamko.kafkatorio.kafkaconnect

import at.syntaxerror.json5.Json5Module
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.http4k.client.OkHttp
import org.http4k.core.Method.GET
import org.http4k.core.Method.PUT
import org.http4k.core.Request
import org.http4k.core.Uri

val jsonMapper = Json {
  prettyPrint = true
  prettyPrintIndent = "  "
}

val j5 = Json5Module()

fun main() {

  val connectorHostUrl = Uri.of("http://localhost:8083")

  val configFile = Resources.loadResource("/configs/websocket-sink-lua-topics.json5")

  val jsonObject: JsonObject = j5.decodeObject(configFile.readText())
  println("json5 connector config: $jsonObject")
  val configJson = jsonMapper.encodeToString(jsonObject)
  println("json connector config: $configJson")

  val connectorName = jsonObject["name"]!!.jsonPrimitive.content

  val request: Request =
    Request(PUT, connectorHostUrl.path("/connectors/$connectorName/config"))
      .header("Content-Type", "application/json")
      .header("Accept", "application/json")
//      .header("Accept-Encoding", "identity")
      .body(configJson)

  val client = OkHttp()

  // check the status of KC
  val statusResponse = client(Request(GET, connectorHostUrl))
  println(statusResponse.toMessage())

  val response = client(request)

  println(response.toMessage())
  println(
    jsonMapper.encodeToString(
      jsonMapper.decodeFromString<JsonElement>(response.body.toString())
    )
  )
}

//val updateConnectors by tasks.registering {
//  group = project.name
//
//  val configFile = layout.projectDirectory.file("configs/websocket-sink-lua-topics.json5")
//  inputs.file(configFile)
//
//  doLast {
//    val fileText = resources.text.fromFile(configFile)
//
//    val jsonObject: JsonObject = j5.decodeObject(fileText.asString())
//
//    logger.lifecycle("out: $jsonObject")
//    val configJson = jsonMapper.encodeToString(jsonObject)
//    logger.lifecycle("configJson: $configJson")
//
//    val request = Request(
//      PUT,
//      Uri.of("http://localhost:8083").path("/connectors/kafkatorio-ws-connector/config")
//    ).header("Content-Type", "application/json")
//      .body(configJson)
//
//
//    val client = OkHttp()
//    val response = client(request)
//
//    println(response.toMessage())
//    println(
//      jsonMapper.encodeToString(
//        jsonMapper.decodeFromString<JsonElement>(response.body.toString())
//      )
//    )
//  }
//
//}
