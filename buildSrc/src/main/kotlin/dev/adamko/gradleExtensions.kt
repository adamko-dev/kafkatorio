package dev.adamko

import java.io.ByteArrayOutputStream
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.specs.Spec
import org.gradle.kotlin.dsl.invoke

operator fun <T> Spec<T>.not(): Spec<T> = Spec<T> { !this(it) }

fun Project.isProcessRunning(process: String, ignoreCase: Boolean = true): Spec<Task> =
  Spec<Task> {
    ByteArrayOutputStream().use { outputStream ->
      project.exec {
        commandLine("tasklist")
        standardOutput = outputStream
      }
      outputStream.toString()
        .contains(process, ignoreCase)
    }
  }