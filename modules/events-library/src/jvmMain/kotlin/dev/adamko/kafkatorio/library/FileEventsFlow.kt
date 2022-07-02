package dev.adamko.kafkatorio.library

import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds.ENTRY_CREATE
import java.nio.file.StandardWatchEventKinds.ENTRY_DELETE
import java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY
import java.nio.file.WatchEvent
import java.nio.file.WatchKey
import java.nio.file.WatchService
import kotlin.coroutines.CoroutineContext
import kotlin.io.path.isReadable
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield


fun File.fileEventsFlow() = FileEventsFlow(
  root = this.toPath(),
)

fun Path.fileEventsFlow() = FileEventsFlow(
  root = this,
)


/** [SharedFlow] wrapper for Java's [WatchService] */
class FileEventsFlow private constructor(
  private val root: Path,
  private val fileEvents: MutableSharedFlow<KWatchEvent>,
) : SharedFlow<KWatchEvent> by fileEvents, CoroutineScope {

  constructor(root: Path) : this(root, MutableSharedFlow(0, Int.MAX_VALUE, BufferOverflow.SUSPEND))

  override val coroutineContext: CoroutineContext =
    CoroutineName("FileEventsFlow") +
        Dispatchers.Unconfined +
        CoroutineExceptionHandler { _, e ->
          println("handling exception in FileEventsFlow")
          e.printStackTrace()
          watchService.close()
          watchKeys.update {
            it.cancelAll()
            emptyList()
          }
        }


  private val watchService: WatchService = FileSystems.getDefault().newWatchService()

  private val watchKeys: MutableStateFlow<List<WatchKey>> = MutableStateFlow(emptyList())


  init {
    println("launching FileEventsFlow... $root")
    registerPaths()
    pollFileEvents()

    launch {
      fileEvents.emit(KWatchEvent.Initialized)
    }

    println("launched FileEventsFlow $root")

    root.toFile().walk()
//      .filter { it.isFile }
      .onEach {
        launch {
          fileEvents.emit(KWatchEvent.Created(it.toPath()))
        }
      }
      .count()
      .let {
        println("initializing KWatchEvent.Created $it")
      }
  }


  /** Watch for any file tree changes and re-register the [watchKeys] */
  private fun registerPaths() = launch {
    println("launching registerPaths")
    fileEvents
//      .onEach { println("file event! $it") }
      .filter { event ->
        when (event) {
          is KWatchEvent.Initialized -> true

          is KWatchEvent.Created     -> event.path.isDirectory
          is KWatchEvent.Deleted     -> event.path.isDirectory

          is KWatchEvent.Modified    -> false
        }
      }.debounce(1.seconds)
      .runningFold(emptyList<WatchKey>()) { previousKeys, directoryTreeAlteredEvent ->
        println("re-registering paths $directoryTreeAlteredEvent")

        previousKeys.cancelAll()

        root.toFile().walk()
          .filter { it.isDirectory }
          .map {
//            println("re-registering watch on $it")
            it.toPath().register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE)
          }
          .toList()
          .also {
            println("${it.count()} registered watches")
          }
      }
      .cancellable()
      .collect(watchKeys)
  }


  /** Poll [watchService] for updates while [coroutineContext] is active. */
  private fun pollFileEvents() = launch {
    flow<WatchKey> {
      while (coroutineContext.isActive) {
        emit(
          withContext(Dispatchers.IO) { watchService.take() }
        )
        println("emitted watchService.take()")
        yield()
      }
    }.mapNotNull { monitorKey ->
      when (val keyPath = monitorKey.watchable()) {
        is Path -> monitorKey to keyPath
        else    -> null
      }
    }.filter { it.second.isReadable() }
      .flatMapConcat { (monitorKey: WatchKey, keyPath: Path) ->
      val events = monitorKey.pollEvents()
      monitorKey.reset()
      events
        .onEach { println("monitorKey event $it") }
        .filter { it.context() as? Path != null }
        .mapNotNull { event ->
          val eventPath = keyPath.resolve(event.context() as Path)

          when (event.kind()) {
            ENTRY_CREATE -> KWatchEvent.Created(eventPath)
            ENTRY_DELETE -> KWatchEvent.Deleted(eventPath)
            ENTRY_MODIFY -> KWatchEvent.Modified(eventPath)
            else         -> null
          }
        }.asFlow()
    }.cancellable()
      .collect(fileEvents)
  }

  companion object {
    private val Path.isDirectory: Boolean get() = Files.isDirectory(this)

    private fun List<WatchKey>.cancelAll() = forEach { it.cancel(); it.pollEvents() }
  }
}

/** Wrapper around [WatchEvent] that comes with properly resolved absolute path */
sealed interface KWatchEvent {

  object Initialized : KWatchEvent

  sealed interface PathEvent : KWatchEvent {
    /** path of modified file or directory */
    val path: Path
  }

  data class Created(override val path: Path) : PathEvent
  data class Modified(override val path: Path) : PathEvent
  data class Deleted(override val path: Path) : PathEvent

}
