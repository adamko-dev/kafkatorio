import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

plugins {
  id("dev.adamko.kafkatorio.infra.docker-compose")
}

description = "Send events from a Factorio server to a Kafka topic "


val dockerSrcDir: Directory by extra

tasks.dockerUp {
  dependsOn(":modules:infra-kafka-cluster:dockerUp")
}

val dockerBuildKafkaPipe by tasks.registering(Exec::class) {
  group = "docker-compose"

  logging.captureStandardOutput(LogLevel.LIFECYCLE)

  dependsOn(tasks.dockerEnv)

  inputs.dir(dockerSrcDir)

  workingDir = dockerSrcDir.asFile
  commandLine = parseSpaceSeparatedArgs(""" docker-compose build kafka-pipe """)
}
tasks.assemble { dependsOn(dockerBuildKafkaPipe) }
