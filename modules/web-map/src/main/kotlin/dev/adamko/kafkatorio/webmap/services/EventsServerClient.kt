package dev.adamko.kafkatorio.webmap.services

import dev.adamko.kafkatorio.schema.common.FactorioServerId
import io.kvision.rest.RestClient
import io.kvision.rest.call
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.await

object EventsServerClient :
  CoroutineScope by CoroutineScope(Dispatchers.Default + SupervisorJob()) {

  private val restClient = RestClient()


  /** Fetch all possible server IDs. */
  suspend fun serverIds(): List<FactorioServerId> =
    restClient
      .call<List<FactorioServerId>>(window.location.origin + "/kafkatorio/data/servers")
      .await()


}
