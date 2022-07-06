package dev.adamko.kafkatorio.infra

import dev.adamko.kafkatorio.gradle.DOCKER_COMPOSE_TASK_GROUP
import dev.adamko.kafkatorio.task.DockerEnvUpdateTask
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

plugins {
  base
  id("dev.adamko.kafkatorio.infra.process-runner")
}


val dockerComposeProjectName: String by extra(rootProject.name)
val dockerSrcDir: Directory by extra(layout.projectDirectory.dir("src"))


val dockerUp by tasks.registering(Exec::class) {
  group = DOCKER_COMPOSE_TASK_GROUP

  dependsOn(tasks.assemble)
  logging.captureStandardOutput(LogLevel.LIFECYCLE)

  workingDir = dockerSrcDir.asFile
  commandLine = parseSpaceSeparatedArgs(""" docker-compose up -d """)
}


val dockerDown by tasks.registering(Exec::class) {
  group = DOCKER_COMPOSE_TASK_GROUP

  logging.captureStandardOutput(LogLevel.LIFECYCLE)

  workingDir = dockerSrcDir.asFile
  commandLine = parseSpaceSeparatedArgs(""" docker-compose down """)
}


val dockerRemove by tasks.registering(Exec::class) {
  group = DOCKER_COMPOSE_TASK_GROUP

  logging.captureStandardOutput(LogLevel.LIFECYCLE)

  workingDir = dockerSrcDir.asFile
  commandLine = parseSpaceSeparatedArgs(""" docker-compose rm --stop -v -f """)
}


afterEvaluate {
  tasks.named("processRun") { dependsOn(dockerUp, dockerEnvUpdate) }
  tasks.named("processKill") { dependsOn(dockerDown) }
}


val dockerEnvUpdate by tasks.registering(DockerEnvUpdateTask::class) {
  dotEnvFile.set(dockerSrcDir.file(".env"))

  properties(
    "COMPOSE_PROJECT_NAME" to dockerComposeProjectName,
    "KAFKATORIO_VERSION" to project.version,
  )
}


tasks.assemble { dependsOn(dockerEnvUpdate) }
