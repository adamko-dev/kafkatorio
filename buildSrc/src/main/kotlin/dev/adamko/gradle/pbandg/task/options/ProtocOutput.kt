package dev.adamko.gradle.pbandg.task.options

import dev.adamko.gradle.pbandg.Constants.pbAndGBuildDir
import java.io.Serializable
import org.gradle.api.file.Directory
import org.gradle.api.file.ProjectLayout
import org.gradle.api.provider.Provider


interface ProtocOutput : Serializable {
  val cliParam: String
  val protocOptions: String
  val outputDirectoryName: String

  fun outputDirectory(layout: ProjectLayout): Provider<Directory> {
    return layout.pbAndGBuildDir.map { it.dir(outputDirectoryName) }
  }

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