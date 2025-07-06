package dev.adamko.geedeecee.internal

import java.io.File
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.invariantSeparatorsPathString
import kotlin.io.path.relativeTo
import okio.FileSystem
import okio.HashingSink.Companion.sha512
import okio.Path.Companion.toOkioPath
import okio.blackholeSink
import okio.buffer

// should be moved to common plugins lib


@JvmName("checksumFiles")
internal fun Collection<File>.checksum(): String =
  map(File::toPath).checksum()

@JvmName("checksumPaths")
internal fun Collection<Path>.checksum(): String =
  checksumImpl(paths = toTypedArray())

private fun checksumImpl(
  basePath: Path? = null,
  vararg paths: Path,
): String {
  return sha512(blackholeSink()).use { hashingSink ->
    hashingSink.buffer().use { sink ->

      paths
        .sorted()
        .forEachIndexed { i, path ->
          if (path.exists()) {
            // hash relative path
            if (basePath != null) {
              val relativePath = path.relativeTo(basePath).invariantSeparatorsPathString
              sink.writeUtf8(relativePath)
            }

            // hash file content
            FileSystem.SYSTEM.source(path.toOkioPath()).use { source ->
              sink.writeAll(source)
            }
          } else {
            // file doesn't exist, so we can't hash any details,
            // but add something so the hash takes the file into account.
            sink.writeInt(i)
          }
        }
    }

    hashingSink.hash.hex()
  }
}
