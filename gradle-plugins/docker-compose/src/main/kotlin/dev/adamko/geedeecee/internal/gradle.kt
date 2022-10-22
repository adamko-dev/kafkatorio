package dev.adamko.geedeecee.internal

import org.gradle.api.Task
import org.gradle.api.file.FileCopyDetails
import org.gradle.api.file.RelativePath

// move to common-lib

internal inline fun <reified T : Task> T.enabledIf(crossinline spec: (T) -> Boolean) {
  onlyIf {
    require(it is T)
    spec(it)
  }
}
