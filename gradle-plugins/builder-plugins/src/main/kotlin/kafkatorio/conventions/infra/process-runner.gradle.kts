package kafkatorio.conventions.infra

import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.registering

plugins {
  id("kafkatorio.conventions.base")
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
