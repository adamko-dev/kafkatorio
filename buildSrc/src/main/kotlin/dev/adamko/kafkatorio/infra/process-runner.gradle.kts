package dev.adamko.kafkatorio.infra

plugins {
  id("dev.adamko.kafkatorio.base")
}

val processRunnerTaskGroup: String by extra("processes")

val processRun by tasks.registering(Task::class) {
  group = processRunnerTaskGroup
}

val processKill by tasks.registering(Task::class) {
  group = processRunnerTaskGroup
}

val processRestart by tasks.registering {
  group = processRunnerTaskGroup

  dependsOn(processKill)
  finalizedBy(processRun)
}
