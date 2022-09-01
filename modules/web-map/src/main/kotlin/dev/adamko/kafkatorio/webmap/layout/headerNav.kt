package dev.adamko.kafkatorio.webmap.layout

import dev.adamko.kafkatorio.webmap.SiteState
import dev.adamko.kafkatorio.webmap.SiteView
import io.kvision.core.Container
import io.kvision.html.div
import io.kvision.html.link
import io.kvision.html.nav

fun Container.headerNav(state: SiteState) {
  nav(className = "navbar") {
    div(className = "container") {
      link("home", "#${SiteView.HOME.pathMatcher}")
    }
  }
}
