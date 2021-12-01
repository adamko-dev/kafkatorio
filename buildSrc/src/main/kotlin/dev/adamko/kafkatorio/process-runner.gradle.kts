package dev.adamko.kafkatorio

import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.registering

plugins {
  id("dev.adamko.kafkatorio.base")
}

val processRunnerTaskGroup: String by extra("processes")

val processRun by tasks.registering {
  group = processRunnerTaskGroup
}

val processKill by tasks.registering {
  group = processRunnerTaskGroup
}

val processRestart by tasks.registering {
  group = processRunnerTaskGroup

  processKill.get().state
  dependsOn(processKill)
  finalizedBy(processRun)
}
