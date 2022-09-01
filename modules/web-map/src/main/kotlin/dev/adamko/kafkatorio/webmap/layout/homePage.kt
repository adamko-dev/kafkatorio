package dev.adamko.kafkatorio.webmap.layout

import dev.adamko.kafkatorio.webmap.SiteState
import io.kvision.core.Container
import io.kvision.html.div
import io.kvision.html.h5
import io.kvision.html.link


fun Container.homePage(state: SiteState) {
  div(className = "home-page") {
    servers(state)
  }
}


private fun Container.servers(state: SiteState) {
  div(className = "servers") {
    h5("Servers")
    if (state.serverIds != null) {
      state.serverIds.forEach { serverId ->
        div {
          link("server", "/servers/$serverId")
        }
      }
    } else {
      div("no servers available")
    }
  }
}
