package dev.adamko.kafkatorio.task

import dev.adamko.kafkatorio.gradle.execCapture
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.Destroys
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.intellij.lang.annotations.Language
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

abstract class KafkaConsumersResetTask : DefaultTask() {

  @get:Input
  val bootstrapServer: String = "localhost:9092"
  @get:Input
  val kafkaContainerName: String = "kafka"
  @get:Destroys
  abstract val kafkaStateDir: DirectoryProperty


  @TaskAction
  fun resetKafkaConsumers() {
    val bootstrapArg = "--bootstrap-server $bootstrapServer"

    val allGroups = dockerExec(
      """
        /kafka/bin/kafka-consumer-groups.sh --list $bootstrapArg
      """.trimIndent()
    ).lines()
      .filter { it.trim().isNotBlank() }

    allGroups
      .forEach { group ->
        logger.lifecycle("Resetting group $group\n---")
        dockerExec(
          """
            /kafka/bin/kafka-consumer-groups.sh --group $group --execute --reset-offsets --all-topics --to-earliest $bootstrapArg
          """.trimIndent()
        )
        logger.lifecycle("---")
      }

    project.delete {
      delete(kafkaStateDir)
    }
  }

//  fun reset(applicationId: String) = """
//      /kafka/bin/kafka-streams-application-reset.sh --application-id $applicationId --force --to-earliest
//    """.trimIndent()

  private fun dockerExec(
    @Language("Shell Script")
    cmd: String
  ): String = project.execCapture {
    logging.captureStandardOutput(LogLevel.LIFECYCLE)
    commandLine = parseSpaceSeparatedArgs(""" docker exec $kafkaContainerName $cmd """)
  }
}
