package dev.adamko.dokalo

import dev.adamko.dokalo.Capabilities.Response.readLogs
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resource
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.response.respondOutputStream
import io.ktor.server.routing.routing
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


fun Application.configureRouting() {

  routing {

    post<StartLogging.Request> { request ->
      // Signals to the plugin that a container is starting that the plugin should start receiving logs for.
      call.respond(StartLogging.Response())
    }

    post<StopLogging.Request> { request ->
      // Signals to the plugin to stop collecting logs from the defined file.
      call.respond(StopLogging.Response())
    }

    post<Capabilities.Request> {
      // Defines the capabilities of the log driver.
      call.respond(Capabilities.Response)
    }

    post<ReadLogsRequest> { request ->
      // Reads back logs to the client. This is used when docker logs <container> is called.

      call.respondOutputStream(
        ContentType.parse("application/x-json-stream"),
        HttpStatusCode.OK,
      ) {

      }
    }
  }
}


sealed interface StartLogging {

  @Resource("/LogDriver.StartLogging")
  @Serializable
  data class Request(

    @SerialName("File")
    val file: LogFile,

    @SerialName("Info")
    val info: ContainerInfo,
  ) : StartLogging


  @Serializable
  data class Response(
    @SerialName("Err")
    val error: String? = null,
  ) : StartLogging
}


sealed interface StopLogging {

  @Resource("/LogDriver.StopLogging")
  @Serializable
  data class Request(
    @SerialName("File")
    val file: LogFile,
  ) : StopLogging


  @Serializable
  data class Response(
    @SerialName("Err")
    val error: String? = null,
  ) : StopLogging
}


sealed interface Capabilities {

  @Resource("/LogDriver.Capabilities")
  @Serializable
  object Request : Capabilities

  /**
   * Defines the list of capabilities that a driver can implement. These capabilities are not
   * required to be a logging driver, however do determine how a logging driver can be used.
   *
   * @property[readLogs] Determines if a log driver can read back logs
   */
  @Serializable
  object Response : Capabilities {
    @SerialName("ReadLogs")
    val readLogs: Boolean = true
  }
}


@Resource("/LogDriver.ReadLogs")
@Serializable
data class ReadLogsRequest(

  @SerialName("Info")
  val info: ContainerInfo,

  @SerialName("Config")
  val config: ReadConfig,
) {

  /** ReadConfig is the configuration passed into ReadLogs. */
  @Serializable
  data class ReadConfig(

    @SerialName("Since")
    val since: LocalDateTime,

    @SerialName("Until")
    val until: LocalDateTime,

    @SerialName("Tail")
    val tail: Int,

    @SerialName("Follow")
    val follow: Boolean,
  )
}
