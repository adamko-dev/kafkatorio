package dev.adamko.kafkatorio.webmap.layout

import dev.adamko.kafkatorio.webmap.state.SiteState
import io.kvision.core.Container
import io.kvision.html.ListType
import io.kvision.html.div
import io.kvision.html.h5
import io.kvision.html.li
import io.kvision.html.link
import io.kvision.html.listTag
import io.kvision.html.p


fun Container.homePage(state: SiteState) {
  div(className = "home-page") {
    introduction()
    servers(state)
  }
}


private fun Container.introduction() {
  div {
    h5("Introduction")
    // <a target="_blank" href="https://github.com/adamko-dev/kafkatorio">Kafkatorio</a>
    p(
      """
        Kafkatorio is a platform used for creating
        <a target="_blank" href="https://www.factorio.com/">Factorio</a> mods that require communication with an external server.
      """.trimIndent(),
      rich = true,
    )
    p(
      """
        It was created to explore the possibilities of using 
        <a target="_blank" href="https://kafka.apache.org/">Apache Kafka</a>
        to process updates from a Factorio server.
      """.trimIndent(),
      rich = true,
    )
    p("To keep up to date on progress, check out:")
    listTag(ListType.UL) {
      link(
        label = "Factorio Mod Portal",
        url = "https://mods.factorio.com/mod/kafkatorio-events",
        target = "_blank"
      )
      link(
        label = "Factorio forum discussion",
        url = "https://forums.factorio.com/viewtopic.php?f=190&t=102841&p=570598#p570598",
        target = "_blank"
      )
    }
    p(
      """
        Development is ongoing. Presently it used to create a live-updating web-map of a Factorio
        server. 
      """.trimIndent(),
      rich = true,
    )
  }
}


private fun Container.servers(state: SiteState) {
  div(className = "servers") {
    h5("Factorio Servers Live maps")
    if (state.serverIds != null) {
      p("View live-updating maps of the following Factorio servers")
      p("Available servers: ${state.serverIds.size}")
      listTag(ListType.UL) {
        state.serverIds.forEach { serverId ->
          li {
            div {
              link(
                label = "$serverId",
                url = "/servers/$serverId",
//                url = routing.link("servers/$serverId"),
              )
            }
          }
        }
      }

//      div {
//        state.serverIds.forEach { serverId ->
//          div {
//            link(
//              label = "$serverId",
//              dataNavigo = true,
//              url = "/servers/$serverId",
//            ) {
////            onClick {
////              SiteRouting.serverView(serverId)
////            }
//            }
//          }
//
//          button(text = "$serverId", className = "btn btn-link") {
//            style = ButtonStyle.PRIMARY
//            size = ButtonSize.SMALL
//
//            onClick {
//              SiteRouting.serverView(serverId)
//            }
//          }
//        }
//      }
    } else {
      div("no servers available")
    }
  }
}
