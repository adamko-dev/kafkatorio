package dev.adamko.kafkatorio.webmap.layout

import dev.adamko.kafkatorio.webmap.routing.SiteView
import dev.adamko.kafkatorio.webmap.state.SiteState
import io.kvision.core.Container
import io.kvision.navbar.nav
import io.kvision.navbar.navbar


fun Container.headerNav(state: SiteState) {
  navbar(label = "Kafkatorio", link = SiteView.HOME.path) {
    nav {
//      navLink("Home", SiteView.HOME.path)
    }
  }
}
