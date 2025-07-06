package dev.adamko.geedeecee.tasks

import dev.adamko.geedeecee.GDCPlugin
import dev.adamko.geedeecee.config.DotEnvContent
import java.io.File
import java.util.*
import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.api.tasks.Optional
import org.gradle.kotlin.dsl.newInstance


/**
 * (Based on [org.gradle.api.tasks.WriteProperties], but updated to use [MapProperty])
 */
@CacheableTask
abstract class DockerEnvUpdateTask @Inject constructor(
  objects: ObjectFactory,
) : DefaultTask() {

  @get:Nested
  @get:Optional
  val envProperties: DotEnvContent = objects.newInstance()

  @get:Input
  @get:Optional
  abstract val comment: Property<String>

  @get:OutputFile
  abstract val dotEnvFile: RegularFileProperty

  init {
    group = GDCPlugin.GCD_TASK_GROUP
  }

  @TaskAction
  fun writeProperties() {
    val dotEnvFile: File = dotEnvFile.get().asFile

    val comment: String = comment.orNull
      ?: " Do not edit manually. This file is managed by task '$name'"

    val envProperties: Map<String, String> = envProperties.compute()
      .mapValues { it.value.toString() }


    // Write the values into dotEnvFile.
    // (The main reason to use Properties here is to make sure the values are correctly escaped.)
    dotEnvFile.bufferedWriter().use { writer ->
      Properties().apply {
        putAll(envProperties)
        store(writer, comment)
      }
    }

    // make the file reproducible by sorting and filtering lines in the file
    val lines = dotEnvFile.useLines { lines ->
      lines
        .filterNot { it.isBlank() }
        .sorted()
        .toList()
    }

    // first comment is our comment
    // second comment is the auto generated timestamp - which we will filter out, so the file is reproducible
    fun String.isComment(): Boolean = trim().startsWith('#')
    val parsedComment = lines.firstOrNull { it.isComment() } ?: ""
    val parsedLines = lines.filterNot { it.isComment() }.joinToString(separator = "\n")

    dotEnvFile.writeText(
      text = """
          |$parsedComment
          |
          |$parsedLines
          |
          """.trimMargin()
    )
  }

  fun envProperties(configure: DotEnvContent.() -> Unit) {
    envProperties.configure()
  }

  companion object {
//    @JvmName("putProvider")
//    fun <K : Any, V : Any> MapProperty<K, V>.put(entry: Pair<K, Provider<V>>) =
//      put(entry.first, entry.second)
//
//    @JvmName("putValue")
//    fun <K : Any, V : Any> MapProperty<K, V>.put(entry: Pair<K, V>) =
//      put(entry.first, entry.second)
  }
}
