import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

plugins {
  dev.adamko.kafkatorio.infra.`docker-compose`
}

description = "Send events from a Factorio server to a Kafka topic "


val dockerSrcDir: Directory by extra

tasks.dockerUp {
  dependsOn(":modules:infra-kafka-cluster:dockerUp")
}



@Suppress("UnstableApiUsage") // normalizeLineEndings is incubating
val dockerBuildKafkaPipe by tasks.registering(dev.adamko.kafkatorio.task.DockerComposeExec::class) {
  dependsOn(tasks.dockerEnv)

  dockerComposeDir.set(dockerSrcDir)
  command.set("docker-compose build kafka-pipe")


//  inputs.property("dockerSrcDirChecksum") { dockerSrcDir.filesChecksum() }
//
//  outputs.upToDateWhen {
//    it.inputs.properties["dockerSrcDirChecksum"] as? Long? == dockerSrcDir.filesChecksum()
//  }

//  val imageIdFile = file("$temporaryDir/docker-image-id.txt")
//  outputs.file(imageIdFile)

//  workingDir = dockerSrcDir.asFile
//  commandLine = parseSpaceSeparatedArgs(""" docker-compose build kafka-pipe """)

//  doLast {
//    sync {
//      from(dockerSrcDir)
//      into(temporaryDir)
//    }
//    imageIdFile.outputStream().use { os ->
//      exec {
//        workingDir = dockerSrcDir.asFile
//        commandLine = parseSpaceSeparatedArgs(""" docker-compose images -q kafka-pipe """)
//        standardOutput = os
//      }
//    }
//}
}

tasks.assemble { dependsOn(dockerBuildKafkaPipe) }
