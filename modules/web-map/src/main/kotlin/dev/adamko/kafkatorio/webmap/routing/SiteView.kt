package dev.adamko.kafkatorio.webmap.routing


sealed interface SiteView {

  sealed interface StaticPath : SiteView {
    val path: String
  }

  sealed interface DynamicPath : SiteView {
    val path: String
  }


  object HOME : StaticPath {
    override val path: String = "/"
  }

  object SERVER : DynamicPath {
    override val path: String = "/servers/:serverId/"
  }
}
