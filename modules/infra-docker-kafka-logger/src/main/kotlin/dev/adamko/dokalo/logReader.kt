package dev.adamko.dokalo

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.yield
import okio.BufferedSource
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.buffer


class LogReaderManager : CoroutineScope {

  override val coroutineContext: CoroutineContext = CoroutineName("LogReaderManager")

  private val commands: MutableSharedFlow<LogReaderCmd> =
    MutableSharedFlow(0, Int.MAX_VALUE, BufferOverflow.SUSPEND)

  private val readers: MutableStateFlow<Map<LogFile, LogReader>> = MutableStateFlow(emptyMap())

  init {
    launch {
      supervisorScope {
        commands.runningFold(mapOf<LogFile, LogReader>()) { readers, cmd ->
          when (cmd) {
            is LogReaderCmd.Start -> {
              if (cmd.file in readers) {
                error("can't process 'start' for reader ${cmd.file} it's already registered")
              } else {
                readers + (cmd.file to LogReader(cmd.file))

              }
            }

            is LogReaderCmd.Stop  -> {
              if (cmd.file !in readers) {
                error("can't process 'stop' for reader ${cmd.file} - it's not registered")
              } else {
                readers - cmd.file
              }
            }
          }
        }.collect(readers)
      }
    }
  }

  suspend fun start(data: StartLogging.Request) {
    commands.emit(LogReaderCmd.Start(data.file))
  }

  suspend fun stop(data: StopLogging.Request) {
    commands.emit(LogReaderCmd.Stop(data.file))
  }


}

private data class LogReader(
  val file: LogFile,
) : CoroutineScope {
  private val path: Path = file.file.toPath()
  private val source: BufferedSource = FileSystem.SYSTEM.source(path).buffer()

  override val coroutineContext: CoroutineContext =
    CoroutineName("LogReader-${file.file.filter { it.isLetterOrDigit() }}") +
        CoroutineExceptionHandler { coroutineContext, throwable ->
          source.close()
        }

  private val _entries: MutableSharedFlow<String> =
    MutableSharedFlow(0, Int.MAX_VALUE, BufferOverflow.SUSPEND)


  init {
    launch {
      val f = flow {
        while (currentCoroutineContext().isActive && source.isOpen) {
          val entrySize = source.readInt().toLong()
          val entry = source.readString(entrySize, Charsets.UTF_8)
          emit(entry)
          yield()
        }
      }

      f.shareIn(this, SharingStarted.Lazily)
      f.launchIn(this)


    }
  }
}

fun logReader(file: LogFile) {
  val reader = LogReader(file)


}


private sealed interface LogReaderCmd {
  val file: LogFile

  data class Start(override val file: LogFile) : LogReaderCmd

  data class Stop(override val file: LogFile) : LogReaderCmd
}


private sealed interface LogReaderState {
  object Active : LogReaderState
  object Stopped : LogReaderState
  data class Error(val msg: String, val cause: Exception?) : LogReaderState
}
