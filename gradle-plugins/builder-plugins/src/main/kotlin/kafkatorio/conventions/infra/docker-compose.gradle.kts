package kafkatorio.conventions.infra

import kafkatorio.tasks.DockerEnvUpdateTask
import kafkatorio.extensions.DOCKER_COMPOSE_TASK_GROUP
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

plugins {
  base
  id("kafkatorio.conventions.infra.process-runner")
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


tasks.matching { it.name == "processRun" }.configureEach { dependsOn(dockerUp, dockerEnvUpdate) }
tasks.matching { it.name == "processKill" }.configureEach { dependsOn(dockerDown) }


val dockerEnvUpdate by tasks.registering(DockerEnvUpdateTask::class) {
  dotEnvFile.set(dockerSrcDir.file(".env"))

  properties(
    "COMPOSE_PROJECT_NAME" to dockerComposeProjectName,
    "KAFKATORIO_VERSION" to project.version,
    "REGISTRY_HOST" to providers.gradleProperty("dockerContainerRegistryHost"),
  )
}


tasks.assemble { dependsOn(dockerEnvUpdate) }
