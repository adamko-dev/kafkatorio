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

  val imageIdFile = file("$temporaryDir/docker-image-id.txt")
  outputs.file(imageIdFile)

  workingDir = dockerSrcDir.asFile
  commandLine = parseSpaceSeparatedArgs(""" docker-compose build kafka-pipe """)

  doLast {
    imageIdFile.outputStream().use { os ->
      exec {
        workingDir = dockerSrcDir.asFile
        commandLine = parseSpaceSeparatedArgs(""" docker-compose images -q kafka-pipe """)
        standardOutput = os
      }
    }
  }
}
tasks.assemble { dependsOn(dockerBuildKafkaPipe) }
