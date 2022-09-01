package dev.adamko.kafkatorio.webmap

import kotlinx.browser.window

object Props {
  @Suppress("HttpUrlsUsage")
  val websocketServerUrl: String
    get() = window.location.href
      .replace("http://", "ws://")
      .replace("https://", "wss://") +
        "kafkatorio/ws"
}
