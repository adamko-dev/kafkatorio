package dev.adamko.kafkatorio.task

import dev.adamko.kafkatorio.gradle.execCapture
import org.gradle.api.DefaultTask
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.intellij.lang.annotations.Language
import org.jetbrains.kotlin.util.parseSpaceSeparatedArgs

abstract class KafkaConsumerGroupsTask : DefaultTask() {

  @get:Input
  val bootstrapServer: String = "localhost:9092"
  @get:Input
  val kafkaDockerContainerName: String = "kafka"

  @get:Internal
  protected val bootstrapArg = "--bootstrap-server $bootstrapServer"

  abstract class ResetAll : KafkaConsumerGroupsTask() {

    @get:Input
    @Language("Shell Script")
    val resetCommand: String =
      """
        /kafka/bin/kafka-consumer-groups.sh --execute --reset-offsets --all-topics --to-earliest $bootstrapArg
      """.trimIndent()

    @TaskAction
    fun reset() {
      getAllGroups().forEach { group ->
        logger.lifecycle("Resetting group $group\n---")
        dockerExec("$resetCommand --group $group")
        logger.lifecycle("---")
      }
    }
  }

  abstract class DeleteAll : KafkaConsumerGroupsTask() {

    @get:Input
    @Language("Shell Script")
    val deleteCommand: String =
      """
        /kafka/bin/kafka-consumer-groups.sh --delete $bootstrapArg
      """.trimIndent()

    @TaskAction
    fun reset() {
      val groups = getAllGroups().joinToString(" ") { group -> "--group $group" }
      dockerExec("$deleteCommand $groups")
    }
  }

  @Internal
  protected fun getAllGroups(): List<String> = dockerExec(
    """
      /kafka/bin/kafka-consumer-groups.sh --list $bootstrapArg
    """.trimIndent()
  ).lines()
    .filter { it.trim().isNotBlank() }


//  fun reset(applicationId: String) = """
//      /kafka/bin/kafka-streams-application-reset.sh --application-id $applicationId --force --to-earliest
//    """.trimIndent()

  protected fun dockerExec(
    @Language("Shell Script")
    cmd: String
  ): String = project.execCapture {
    logging.captureStandardOutput(LogLevel.LIFECYCLE)
    commandLine = parseSpaceSeparatedArgs(""" docker exec $kafkaDockerContainerName $cmd """)
  }
}
