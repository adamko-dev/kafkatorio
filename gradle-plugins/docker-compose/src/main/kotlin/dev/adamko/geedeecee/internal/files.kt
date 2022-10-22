package dev.adamko.geedeecee.internal

import org.gradle.api.file.Directory
import org.jetbrains.kotlin.incremental.md5

// should be moved to common plugins lib

internal fun Directory.filesChecksum(): Long = asFileTree
  .files
  .map { it.readBytes() + it.absolutePath.toByteArray() }
  .fold(byteArrayOf()) { acc, bytes -> acc + bytes }
  .md5()
