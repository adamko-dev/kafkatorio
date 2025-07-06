package dev.adamko.geedeecee.tasks

import dev.adamko.geedeecee.GDCPlugin
import dev.adamko.geedeecee.internal.*
import java.io.OutputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream
import javax.inject.Inject
import kotlin.concurrent.thread
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.api.tasks.PathSensitivity.RELATIVE
import org.gradle.internal.logging.progress.ProgressLoggerFactory
import org.gradle.process.ExecOperations
import org.gradle.process.ExecResult
import org.gradle.process.ExecSpec
import org.gradle.work.NormalizeLineEndings


// look into using 'build context' for up-to-date check
// https://stackoverflow.com/questions/38946683/how-to-test-dockerignore-file
// https://github.com/pwaller/docker-show-context
// https://snippets.khromov.se/see-which-files-are-included-in-your-docker-build-context/
// or parsing Dockerfile
// https://github.com/keilerkonzept/dockerfile-json


abstract class GDCCommandTask @Inject constructor(
  private val executor: ExecOperations,
  private val progressLoggerFactory: ProgressLoggerFactory
) : DefaultTask() {

  @get:Internal
  abstract val workingDir: DirectoryProperty

  /**
   * Files used for up-to-date checks.
   */
  @get:InputFiles
  @get:SkipWhenEmpty
  @get:PathSensitive(RELATIVE)
  @get:NormalizeLineEndings
  abstract val workingDirFiles: ConfigurableFileCollection

  /**
   * The `PATH` of the current system.
   */
  @get:Internal
  abstract val systemPath: Property<String>

  @get:Input
  abstract val dockerComposeExecutable: Property<String>

  @get:Input
  abstract val separatedArgs: ListProperty<String>

  @get:Input
  abstract val dockerActive: Property<Boolean>

  @get:Input
  @get:Optional
  abstract val cacheable: Property<Boolean>

  @get:OutputFile
  abstract val stateFile: RegularFileProperty

  init {
    group = GDCPlugin.GCD_TASK_GROUP
    description = "Run a docker-compose command."

    enabledIf { it.dockerActive.getOrElse(false) }

    //logging.captureStandardOutput(LogLevel.LIFECYCLE)
  }

  @TaskAction
  protected fun action() {
    val dockerComposeExecutable = dockerComposeExecutable.get()
    val separatedArgs = separatedArgs.get()

    val progressLogger = progressLoggerFactory.newOperation(javaClass)
    progressLogger.start("Running docker-compose $separatedArgs", "Starting...")

    val result = executor.execListen(
      outputListener = progressLogger::progress
    ) {
      executable(dockerComposeExecutable)
      args(separatedArgs)
      systemPath.orNull?.let { environment("PATH", it) }
      workingDir(this@GDCCommandTask.workingDir)
      environment("BUILDKIT_PROGRESS", "plain")
    }

    progressLogger.completed("Finished", result.isFailure)

    updateStateFile(success = result.isSuccess)
  }

  private fun updateStateFile(success: Boolean) {
    val stateFile = stateFile.get().asFile

    if (cacheable.getOrElse(false)) {
      if (success) {
        val inputChecksum = workingDirFiles.asFileTree.files.checksum()
        stateFile.writeText(inputChecksum)
      } else {
        stateFile.delete()
      }
    }
  }

  /** Set the `docker-compose` argument */
  @Suppress("FunctionName")
  fun `docker-compose`(args: String) {
    separatedArgs.set(
      parseSpaceSeparatedArgs(args)
    )
  }

  companion object
}


/**
 * @param[outputListener] Get each line in real-time, as it's emitted
 */
private fun ExecOperations.execListen(
  outputListener: (String) -> Unit,
  spec: ExecSpec.() -> Unit,
): ExecResult {

//  val byteStream = ByteArrayOutputStream()
//  val fileStream  = FileOutputStream(File("/tmp/somefile"))
//  val outStream: OutputStream = object : OutputStream() {
//    @Throws(IOException::class)
//    override fun write(b: Int) {
//      byteStream.write(b)
//      fileStream.write(b)
//    }
//  }
//  outStream.write("Hello world".toByteArray())

  PipedOutputStream().use { outputStream ->

    // Start a thread to read from the inputStream and pass each line to outputListener
    thread(isDaemon = true) {
      PipedInputStream(outputStream).use { inputStream ->
        inputStream.bufferedReader().useLines { lines ->
          lines.forEach { line ->
            outputListener(line)
          }
        }
      }
    }

    return exec {
      spec()
      standardOutput = outputStream
//        errorOutput = outputStream
    }
  }
}


private data class ExecListenResult(
  val exitCode: Int,
  val standardOutput: String,
)


private class TeeOutputStream(
  private vararg val branches: OutputStream
) : OutputStream() {

  override fun write(b: Int) {
    for (branch in branches) {
      branch.write(b)
    }
  }

  override fun write(b: ByteArray) {
    for (branch in branches) {
      branch.write(b)
    }
  }

  override fun write(b: ByteArray, off: Int, len: Int) {
    for (branch in branches) {
      branch.write(b, off, len)
    }
  }

  override fun flush() {
    for (branch in branches) {
      branch.flush()
    }
  }

  override fun close() {
    for (branch in branches) {
      branch.close()
    }
  }
}
