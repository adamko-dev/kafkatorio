package dev.adamko.kafkatorio.processor.core

import dev.adamko.kafkatorio.schema.common.MaskedValue
import dev.adamko.kafkatorio.server.config.ApplicationProperties
import dev.adamko.kafkatorio.server.config.PortVal
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import kotlin.io.path.Path


class AuthenticatorTest : FunSpec({

  test("verify correct JWT is verified") {

    val authenticator = Authenticator(
      testApplicationProperties(
        jwtSecret = "123 456 789"
      )
    )

    val result = authenticator.verifyJwt(jwtMessage)

    result.shouldNotBeNull()
  }

  test("verify invalid JWT returns null") {

    val authenticator = Authenticator(
      testApplicationProperties(
        jwtSecret = "wrong"
      )
    )

    val result = authenticator.verifyJwt(jwtMessage)

    result.shouldBeNull()
  }

}) {

  companion object {
    /** Key: `123 465 789` */
    private val jwtMessage = """
        eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.ToFLkPBs_EKF4B2bRQyVPfkKrB-x6WgyAq6KsR9oHwg
    """.trimIndent()

    private fun testApplicationProperties(
      jwtSecret: String,
    ) = ApplicationProperties(
      webPort = PortVal.DEFAULT,
      serverDataDir = Path(""),
      kafkaAdminConfig = emptyMap(),
      kafkaStreamsConfig = emptyMap(),
      socketServerHost = "",
      socketServerPort = 9999,
      jwtSecret = MaskedValue(jwtSecret),
      kafkatorioServers = emptyMap(),
    )
  }
}
