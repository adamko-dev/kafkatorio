package dev.adamko.kafkatorio.factoriomod

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject


//@Serializable
//data class InitUploadRequest(
//  @SerialName("mod") val modName: String,
//)


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


@Serializable(with = SubmitUploadResponse.Serializer::class)
sealed interface SubmitUploadResponse {

  @Serializable
  data class Success(
    val success: Boolean
  ) : SubmitUploadResponse

  object Serializer : JsonContentPolymorphicSerializer<SubmitUploadResponse>(
    SubmitUploadResponse::class
  ) {
    override fun selectDeserializer(element: JsonElement) = when {
      "success" in element.jsonObject -> Success.serializer()
      else                            -> Failure.serializer()
    }
  }
}


@Serializable
data class Failure(
  val error: String? = null,
  val message: String? = null,
) : InitUploadResponse, SubmitUploadResponse {

  private val reason: String?
    get() = reasons[error]

  override fun toString() = "Failure(error=$error, message=$message, reason=$reason)"

  companion object {
    private val reasons: Map<String?, String> = mapOf(
      //@formatter:off
      "InvalidApiKey"     to "Missing or invalid API key for the current endpoint",
      "InvalidRequest"    to "Invalid request.",
      "InternalError"     to "Internal error, please try again later.",
      "Forbidden"         to "Insufficient permission for current endpoint",
      "Unknown"           to "Unknown error, please try again later.",
      "InvalidModRelease" to "Invalid release data in info.json",
      "InvalidModUpload"  to "Invalid mod data in zipfile",
      "UnknownMod"        to "Mod does not exist in mod portal",
      //@formatter:on
    )
  }
}


@Serializable
data class FactorioModInfo(
  val name: String,
  val version: String,
  val title: String,
  val description: String,
  val author: String,
  @SerialName("factorio_version") val factorioVersion: String,
  val dependencies: List<String>,
)
