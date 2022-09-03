package dev.adamko.kafkatorio.webmap.config

import kotlinx.browser.window


object ApplicationProperties {

  /** Determine the WebSocket URL based on the window location URL. */
  @Suppress("HttpUrlsUsage")
  val websocketServerUrl: String
    get() = window.location.origin
      .replace("http://", "ws://")
      .replace("https://", "wss://") +
        "/kafkatorio/ws"
}
