package dev.adamko.kafkatorio

import dev.adamko.kafkatorio.processor.serdes.KXS.asPrettyJsonString
import dev.adamko.kafkatorio.processor.serdes.jsonMapper
import it.skrape.core.htmlDocument
import it.skrape.selects.html5.input
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import org.http4k.client.OkHttp
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.body.form
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.cookie
import org.http4k.core.cookie.cookies
import org.http4k.core.then
import org.http4k.filter.ClientFilters


/**
 * * https://github.com/shanemadden/factorio-mod-portal-publish/blob/master/entrypoint.sh
 * * https://github.com/nicolas-lang/factorio-create-release-action/blob/master/entrypoint.py
 * * https://github.com/nicolas-lang/factorio-create-release-action/blob/master/factorioModPortal.py
 * * https://github.com/justarandomgeek/vscode-factoriomod-debug/blob/master/src/ModPackageProvider.ts#L530
 *
 *
 */
internal class FactorioModPortalClient(
  private val appProps: ApplicationProperties = ApplicationProperties(),
  private val loginFormUrl: String = "https://factorio.com/login?mods=1",
  private val loginUrl: String = "https://factorio.com/login",
) {

  private val client: HttpHandler =
    ClientFilters.FollowRedirects()
      .then(OkHttp())

  private val csrfCookie: Cookie by lazy { fetchCsrfToken() }
  private val sessionCookie: Cookie by lazy { login() }


  private fun fetchCsrfToken(): Cookie {

    val request = Request(Method.GET, loginFormUrl)
    val response: Response = client(request)

    val csrfToken = htmlDocument(response.bodyString()) {
      input {
        withId = csrfTokenName
        withAttribute = "name" to csrfTokenName
        findFirst { attribute("value") }
      }
    }

    println("csrfToken: $csrfToken")
    return Cookie(csrfTokenName, csrfToken)
  }


  private fun login(): Cookie {

    val loginRequest = Request(Method.POST, loginUrl)
      .header("Referer", loginFormUrl)
//      .cookie(csrfCookie)
      .form("csrf_token", csrfCookie.value)
      .form("username_or_email", appProps.factorioModPortalUsername)
      .form("password", appProps.factorioModPortalPassword.use { it })
      .form("next_url", "/profile")

    val loginResponse = client(loginRequest)

    println(loginResponse.bodyString())

    val sessionCookie = loginResponse.cookies()
      .first { it.name == "session" && it.domain == ".factorio.com" }

    println("sessionCookie: $sessionCookie")

    return sessionCookie
  }

  fun getModInfo(modName: String): JsonObject {
    val response = client(
      Request(Method.GET, "https://mods.factorio.com/api/mods/${modName}/full")
        .cookie(csrfCookie)
        .cookie(sessionCookie)
    )
    return jsonMapper.parseToJsonElement(response.bodyString()).jsonObject
  }

    fun fetchModUploadToken(modName: String): String {
    val response = client(
      Request(Method.GET, "https://mods.factorio.com/mod/${modName}/edit")
        .cookie(csrfCookie)
        .cookie(sessionCookie)
    )

    return response.bodyString()
  }

  companion object {
    private const val csrfTokenName = "csrf_token"
  }

}
