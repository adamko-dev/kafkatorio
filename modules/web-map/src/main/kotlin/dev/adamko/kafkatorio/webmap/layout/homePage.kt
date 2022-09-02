package dev.adamko.kafkatorio.webmap.layout

import dev.adamko.kafkatorio.webmap.SiteRouting
import dev.adamko.kafkatorio.webmap.SiteState
import io.kvision.core.Container
import io.kvision.html.ButtonSize
import io.kvision.html.ButtonStyle
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.html.h5
import io.kvision.html.span


fun Container.homePage(state: SiteState) {
  div(className = "home-page") {
    servers(state)
  }
}


private fun Container.servers(state: SiteState) {
  div(className = "servers") {
    h5("Servers")
    if (state.serverIds != null) {
      div {
        span("Available servers: ${state.serverIds.size}")
      }

      div {
        state.serverIds.forEach { serverId ->
//        div {
//          link(
//            label = "$serverId",
//            dataNavigo = true,
//            url = "/servers/$serverId",
//          ) {
////            onClick {
////              SiteRouting.serverView(serverId)
////            }
//          }
//        }

          button(text = "$serverId") {
            style = ButtonStyle.PRIMARY
            size = ButtonSize.SMALL

            onClick {
              SiteRouting.serverView(serverId)
            }
          }
        }
      }
    } else {
      div("no servers available")
    }
  }
}
