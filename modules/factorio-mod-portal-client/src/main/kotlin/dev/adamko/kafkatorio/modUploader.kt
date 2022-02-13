package dev.adamko.kafkatorio

import dev.adamko.kafkatorio.processor.serdes.KXS.asPrettyJsonString


private const val modName = "kafkatorio-events"


fun main() {
  val client = FactorioModPortalClient()
//  val modInfo = client.getModInfo(modName)
//  println(modInfo.asPrettyJsonString())
  println(client.fetchModUploadToken(modName))
}
