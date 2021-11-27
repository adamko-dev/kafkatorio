package dev.adamko.factoriowebmap.configurations

import org.gradle.api.artifacts.Configuration

fun Configuration.asProvider() {
  isVisible = false
  isCanBeResolved = false
  isCanBeConsumed = true
}

fun Configuration.asConsumer() {
  isVisible = false
  isCanBeResolved = true
  isCanBeConsumed = false
}
