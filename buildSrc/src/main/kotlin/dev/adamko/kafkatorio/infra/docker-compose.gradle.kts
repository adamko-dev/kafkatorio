package dev.adamko.kafkatorio.infra

import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

plugins {
  id("dev.adamko.kafkatorio.infra.process-runner")
}

val dockerComposeTaskGroup: String by extra("docker-compose")

val dockerComposeProjectName: String by extra(rootProject.name)
val dockerSrcDir: Directory by extra(layout.projectDirectory.dir("src"))


val dockerUp by tasks.registering(Exec::class) {
  group = dockerComposeTaskGroup

  dependsOn(tasks.assemble)
  logging.captureStandardOutput(LogLevel.LIFECYCLE)

  workingDir = dockerSrcDir.asFile
  commandLine = parseSpaceSeparatedArgs(""" docker-compose up --build -d """)
}

val dockerDown by tasks.registering(Exec::class) {
  group = dockerComposeTaskGroup

  logging.captureStandardOutput(LogLevel.LIFECYCLE)

  workingDir = dockerSrcDir.asFile
  commandLine = parseSpaceSeparatedArgs(""" docker-compose down """)
}

afterEvaluate {
  tasks.named("processRun") { dependsOn(dockerUp, dockerEnv) }
  tasks.named("processKill") { dependsOn(dockerDown) }
}

val dockerEnv by tasks.registering(WriteProperties::class) {
  group = dockerComposeTaskGroup

  logging.captureStandardOutput(LogLevel.LIFECYCLE)

  setOutputFile(dockerSrcDir.file(".env"))
  properties(
    "COMPOSE_PROJECT_NAME" to dockerComposeProjectName,
    "KAFKATORIO_VERSION" to project.version,
  )
}

tasks.assemble { dependsOn(dockerEnv) }
