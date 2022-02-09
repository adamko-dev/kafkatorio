package dev.adamko.kafkatorio.lang

import dev.adamko.kafkatorio.relocateKotlinJsStore


plugins {
  id("dev.adamko.kafkatorio.base")
  id("dev.adamko.kafkatorio.lang.node")
  kotlin("multiplatform")
}


relocateKotlinJsStore()
