package dev.adamko.kafkatorio.processor

import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse


class StreamsExceptionHandler(
  val shutdown: (e: Throwable) -> Unit,
) : StreamsUncaughtExceptionHandler {
  override fun handle(exception: Throwable): StreamThreadExceptionResponse {
    exception.printStackTrace()
    shutdown(exception)
    return StreamThreadExceptionResponse.SHUTDOWN_APPLICATION
  }
}
