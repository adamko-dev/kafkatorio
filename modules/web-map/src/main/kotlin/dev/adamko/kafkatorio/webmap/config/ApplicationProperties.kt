package dev.adamko.kafkatorio.webmap.config

import kotlinx.browser.window


object ApplicationProperties {
  @Suppress("HttpUrlsUsage")
  val websocketServerUrl: String
    get() = window.location.origin
      .replace("http://", "ws://")
      .replace("https://", "wss://") +
        "/kafkatorio/ws"
}
