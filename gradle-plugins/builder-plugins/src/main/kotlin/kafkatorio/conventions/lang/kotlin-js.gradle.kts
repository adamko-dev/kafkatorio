package kafkatorio.conventions.lang

import kafkatorio.conventions.relocateKotlinJsStore


plugins {
  kotlin("multiplatform")
  id("kafkatorio.conventions.base")
}

kotlin {
  js()
}

relocateKotlinJsStore()
