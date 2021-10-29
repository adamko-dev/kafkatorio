package dev.adamko.factorioevents.processor

import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse

class StreamsExceptionHandler : StreamsUncaughtExceptionHandler {
  override fun handle(exception: Throwable): StreamThreadExceptionResponse {
    println("${exception.message} - ${exception.stackTrace}")
    return StreamThreadExceptionResponse.SHUTDOWN_APPLICATION
  }
}
