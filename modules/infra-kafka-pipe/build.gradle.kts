import dev.adamko.geedeecee.DockerComposeExec

plugins {
  id("kafkatorio.conventions.base")
  id("dev.adamko.geedeecee")
}

description = "Send events from a Factorio server to a Kafka topic "


geedeecee {
  srcDir.set(layout.projectDirectory.dir("src"))
}

//val dockerSrcDir: Directory by extra

tasks.dockerComposeUp {
  dependsOn(":modules:infra-kafka-cluster:dockerUp")
}


val dockerBuildKafkaPipe by tasks.registering(DockerComposeExec::class) {

  dockerComposeDir.set(geedeecee.srcDir)
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
