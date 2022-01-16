package dev.adamko.kafkatorio.processor

import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse

class StreamsExceptionHandler : StreamsUncaughtExceptionHandler {
  override fun handle(exception: Throwable): StreamThreadExceptionResponse {
    exception.printStackTrace()
    return StreamThreadExceptionResponse.SHUTDOWN_APPLICATION
  }
}
