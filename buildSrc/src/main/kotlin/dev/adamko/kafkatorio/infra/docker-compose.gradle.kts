package dev.adamko.kafkatorio.infra

import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

plugins {
  id("dev.adamko.kafkatorio.infra.process-runner")
}

val dockerComposeTaskGroup : String by extra("docker-compose")

val composeProjectName: String by extra(rootProject.name)
val srcDir: Directory by extra(layout.projectDirectory.dir("src"))


val dockerUp by tasks.creating(Exec::class) {
  group = dockerComposeTaskGroup
  workingDir = srcDir.asFile
  commandLine = parseSpaceSeparatedArgs(""" docker-compose up -d """)
}

val dockerDown by tasks.creating(Exec::class) {
  group = dockerComposeTaskGroup
  workingDir = srcDir.asFile
  commandLine = parseSpaceSeparatedArgs(""" docker-compose down """)
}

afterEvaluate {
  tasks.named("processRun") { dependsOn(dockerUp, dockerEnv) }
  tasks.named("processKill") { dependsOn(dockerDown) }
}

val dockerEnv by tasks.registering(WriteProperties::class) {
  group = dockerComposeTaskGroup

  setOutputFile(srcDir.file(".env"))
  properties(
    "COMPOSE_PROJECT_NAME" to composeProjectName,
  )
}

tasks.assemble { dependsOn(dockerEnv) }
