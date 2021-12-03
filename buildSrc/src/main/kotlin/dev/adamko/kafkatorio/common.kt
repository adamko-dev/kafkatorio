package dev.adamko.kafkatorio

import kotlinx.serialization.json.Json


val jsonMapper = Json {
  prettyPrint = true
  prettyPrintIndent = "  "
}
