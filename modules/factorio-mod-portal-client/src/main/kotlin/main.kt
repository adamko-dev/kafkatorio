import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLBuilder
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import io.ktor.resources.Resource
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject


private val client = HttpClient(CIO) {
  install(Resources)
  install(Logging) {
    logger = Logger.DEFAULT
    level = LogLevel.ALL
  }
  install(ContentNegotiation) {
    json(Json {
      prettyPrint = true
      isLenient = true
    })
  }
  defaultRequest {
    header(HttpHeaders.Authorization, "Bearer 123")
  }
  followRedirects = false
  expectSuccess = true
}

fun main() = runBlocking {

  val apiBase = "https://mods.factorio.com/api/v2"
  val uploadEndpoint = "mods/releases/"

  val portalUploadEndpoint = URLBuilder(apiBase).apply {
    takeFrom(uploadEndpoint)
  }.buildString()

  println(portalUploadEndpoint)

  val response = client.post(InitUploadRequest("123")) {
    contentType(ContentType.Application.Json)
  }

  println(response)
}

@Resource("/init_upload")
@Serializable
data class InitUploadRequest(
  @SerialName("mod") val modName: String,
)


@Serializable(with = InitUploadResponse.Serializer::class)
sealed interface InitUploadResponse {

  /**
   * @param[uploadUrl] URL the mod zip file should be uploaded to
   */
  @Serializable
  data class Success(
    @SerialName("upload_url") val uploadUrl: String,
  ) : InitUploadResponse

  object Serializer :
    JsonContentPolymorphicSerializer<InitUploadResponse>(InitUploadResponse::class) {
    override fun selectDeserializer(element: JsonElement) = when {
      "upload_url" in element.jsonObject -> Success.serializer()
      else                               -> Failure.serializer()
    }
  }
}

@Serializable
data class Failure(
  val error: String? = null,
  val message: String? = null,
) : InitUploadResponse
