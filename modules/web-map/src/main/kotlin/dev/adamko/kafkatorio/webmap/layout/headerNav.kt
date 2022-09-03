package dev.adamko.kafkatorio.webmap.layout

import dev.adamko.kafkatorio.webmap.SiteState
import dev.adamko.kafkatorio.webmap.SiteView
import io.kvision.core.Container
import io.kvision.navbar.nav
import io.kvision.navbar.navLink
import io.kvision.navbar.navbar


fun Container.headerNav(state: SiteState) {
  navbar {
    nav {
      navLink("Home", SiteView.HOME.path)
    }
  }
}
