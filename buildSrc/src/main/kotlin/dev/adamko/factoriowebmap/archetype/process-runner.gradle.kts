package dev.adamko.factoriowebmap.archetype

plugins {
  id("dev.adamko.factoriowebmap.archetype.base")
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
