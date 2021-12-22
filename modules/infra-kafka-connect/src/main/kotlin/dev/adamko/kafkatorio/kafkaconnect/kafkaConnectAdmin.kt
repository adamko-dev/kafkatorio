package dev.adamko.kafkatorio.kafkaconnect

import at.syntaxerror.json5.Json5Module
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

import kotlinx.serialization.json.Json

val jsonMapper = Json {
  prettyPrint = true
  prettyPrintIndent = "  "
}

val j5 = Json5Module()

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
