package dev.adamko.kafkatorio.factoriomod

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.append
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import java.io.File
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.gradle.api.internal.tasks.userinput.UserInputHandler
import org.gradle.kotlin.dsl.support.useToRun
import org.jetbrains.kotlin.cli.common.toBooleanLenient


class FactorioModPortalPublishClient(
  private val userInputHandler: UserInputHandler,
  private val distributionZip: File,
  private val modName: String,
  private val modVersion: String,
  private val portalApiKey: String,
  private val portalUploadEndpoint: String,
) {

  private fun client() = HttpClient(CIO) {
    install(Logging) {
      logger = Logger.SIMPLE
      level = LogLevel.ALL
    }
    install(ContentNegotiation) {
      json(Json {
        prettyPrint = false
        isLenient = true
      })
    }
    defaultRequest {
      header(HttpHeaders.Authorization, "Bearer $portalApiKey")
      port = 443
    }
    followRedirects = false
    expectSuccess = false
  }

  fun uploadMod() = runBlocking {
    client().useToRun {
      val initUploadResponse = initUpload()

      val confirmation = userInputHandler
        .askQuestion("Are you sure you want to publish $modName:$modVersion?", "no")
        .toBooleanLenient() ?: false

      if (confirmation) {
        upload(initUploadResponse)
      } else {
        println("aborting upload")
      }
    }
  }

  private suspend fun HttpClient.initUpload(): InitUploadResponse.Success {

    val response = submitForm(
      url = portalUploadEndpoint,
      formParameters = Parameters.build {
        append("mod", modName)
      }
    )

//    val response = post(portalUploadEndpoint) {
//      contentType(ContentType.MultiPart.FormData)
//      setBody(Json.encodeToString(InitUploadRequest.serializer(), InitUploadRequest(modName)))
////      setBody(InitUploadRequest(modName))
//    }
    println(response)
    val initUploadResponse: InitUploadResponse =
      Json.decodeFromString(InitUploadResponse.serializer(), response.bodyAsText())
    println(initUploadResponse)

    require(response.status.isSuccess()) { "init upload request failed" }

    return when (initUploadResponse) {
      is Failure -> error(initUploadResponse)
      is InitUploadResponse.Success -> initUploadResponse
    }
  }

  private suspend fun HttpClient.upload(initUploadResponse: InitUploadResponse.Success) {

    val requestForm = formData {
      append(
        "file",
        distributionZip.readBytes(),
        Headers.build {
          append(HttpHeaders.ContentType, ContentType.Application.Zip)
          append(HttpHeaders.ContentDisposition, "filename=${distributionZip.name}")
        }
      )
    }


    val response = post(initUploadResponse.uploadUrl) {
      setBody(MultiPartFormDataContent(requestForm))
    }


//    val response = submitFormWithBinaryData(initUploadResponse.uploadUrl, requestForm) {
//      header(HttpHeaders.Authorization, "Bearer $portalApiKey")
//    }

    val submitUploadResponse =
      Json.decodeFromString(SubmitUploadResponse.serializer(), response.bodyAsText())

    println(response)
    println(submitUploadResponse)
    require(response.status.isSuccess() && submitUploadResponse is SubmitUploadResponse.Success) { "upload request failed" }
  }
}
