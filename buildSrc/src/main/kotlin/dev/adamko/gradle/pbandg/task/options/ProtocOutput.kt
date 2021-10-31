package dev.adamko.gradle.pbandg.task.options

import java.io.Serializable


interface ProtocOutput : Serializable {
  val cliParam: String
  val protocOptions: String
  val outputDirectoryName: String

  data class KotlinOutput(
    override val protocOptions: String = "lite",
    override val outputDirectoryName: String = "kotlin",
  ) : ProtocOutput {
    override val cliParam: String = "--kotlin_out"
  }

  data class JavaOutput(
    override val protocOptions: String = "lite",
    override val outputDirectoryName: String = "java",
  ) : ProtocOutput {
    override val cliParam: String = "--java_out"
  }

}