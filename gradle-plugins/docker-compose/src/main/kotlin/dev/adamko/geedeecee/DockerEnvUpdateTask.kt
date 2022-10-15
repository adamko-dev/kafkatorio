package dev.adamko.geedeecee

import java.io.File
import java.nio.charset.Charset
import java.util.Properties
import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction


/**
 * (Based on [org.gradle.api.tasks.WriteProperties], but updated to use [MapProperty])
 */
@CacheableTask
abstract class DockerEnvUpdateTask @Inject constructor(
) : DefaultTask() {

  @get:Input
  abstract val envProperties: MapProperty<String, String>

  @get:Input
  @get:Optional
  abstract val comment: Property<String>

  @get:Input
  @get:Optional
  abstract val charset: Property<Charset>

  @get:OutputFile
  abstract val dotEnvFile: RegularFileProperty

  init {
    group = GDCPlugin.GCD_TASK_GROUP
  }

  @TaskAction
  fun writeProperties() {
    val dotEnvFile: File = dotEnvFile.get().asFile
    val charset: Charset = charset.orNull ?: Charsets.UTF_8

    val comment: String = comment.orNull
      ?: " Do not edit manually. This file was created with task '$name'"


    dotEnvFile.writer(charset).use { writer ->
      val properties = Properties()
      properties.putAll(envProperties.get())
      properties.store(writer, comment)
    }

    // make the file reproducible by sorting and filtering lines in the file
    val lines = dotEnvFile.useLines(charset) { lines ->
      lines
        .filterNot { it.isBlank() }
        .sorted()
        .toList()
    }

    // first comment is our comment
    // second comment is the auto generated timestamp - which we will filter out, so the file is reproducible
    val parsedComment = lines.firstOrNull { it.isComment() } ?: ""
    val parsedLines = lines.filterNot { it.isComment() }.joinToString(separator = "\n")

    dotEnvFile.writeText(
      charset = charset,
      text = """
          |$parsedComment
          |
          |$parsedLines
          |
        """.trimMargin()
    )
  }

  fun envProperties(configure: MapProperty<String, String>.() -> Unit) {
    envProperties.configure()
  }

  companion object {
    @JvmName("putProvider")
    fun <K : Any, V : Any> MapProperty<K, V>.put(entry: Pair<K, Provider<V>>) =
      put(entry.first, entry.second)

    @JvmName("putValue")
    fun <K : Any, V : Any> MapProperty<K, V>.put(entry: Pair<K, V>) =
      put(entry.first, entry.second)

    private fun String.isComment(): Boolean = trim().startsWith('#')
  }
}
