package dev.adamko.kafkatorio.kafkaconnect

import java.io.File

object Resources {
  fun loadResource(name: String): File =
    File(requireNotNull(Resources::class.java.getResource(name)?.file) {
      "could not load resource '$name'"
    })
}
