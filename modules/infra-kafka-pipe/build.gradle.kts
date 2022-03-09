import org.jetbrains.kotlin.incremental.md5
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

plugins {
  dev.adamko.kafkatorio.infra.`docker-compose`
}

description = "Send events from a Factorio server to a Kafka topic "


val dockerSrcDir: Directory by extra
fun Directory.filesChecksum() = asFileTree
  .files
  .map { it.readBytes() + it.absolutePath.toByteArray() }
  .fold(byteArrayOf()) { acc, bytes -> acc + bytes }
  .md5()

tasks.dockerUp {
  dependsOn(":modules:infra-kafka-cluster:dockerUp")
}

val dockerBuildKafkaPipe by tasks.registering(Exec::class) {
  group = "docker-compose"

  logging.captureStandardOutput(LogLevel.LIFECYCLE)

  dependsOn(tasks.dockerEnv)

  inputs.dir(dockerSrcDir)
  inputs.property("dockerSrcDirChecksum") { dockerSrcDir.filesChecksum() }

  outputs.upToDateWhen {
    it.inputs.properties["dockerSrcDirChecksum"] as? Long? == dockerSrcDir.filesChecksum()
  }

//  val imageIdFile = file("$temporaryDir/docker-image-id.txt")
//  outputs.file(imageIdFile)

  workingDir = dockerSrcDir.asFile
  commandLine = parseSpaceSeparatedArgs(""" docker-compose build kafka-pipe """)

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
