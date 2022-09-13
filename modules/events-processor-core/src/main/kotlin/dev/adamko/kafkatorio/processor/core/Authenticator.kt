package dev.adamko.kafkatorio.processor.core

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import dev.adamko.kafkatorio.processor.config.ApplicationProperties
import dev.adamko.kafkatorio.schema.common.FactorioServerToken
import kotlin.reflect.jvm.jvmName
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okio.ByteString.Companion.decodeBase64


class Authenticator(
  appProps: ApplicationProperties,
) {
  private val algorithm: Algorithm = Algorithm.HMAC256(appProps.jwtSecret.value)

  private val verifier = JWT.require(algorithm).build()

  fun verifyJwt(token: String): FactorioServerToken? {
    return runCatching {
      val decodedJwt: DecodedJWT? = verifier.verify(token)

      val payloadJson = decodedJwt?.payload?.decodeBase64()?.string(Charsets.UTF_8) ?: "{}"

      jsonMapper.decodeFromString(
        KafkatorioJwtPayload.serializer(),
        payloadJson,
      ).serverToken

    }.onFailure { e ->
      if (e is CancellationException) throw e

      println("[Authenticator] Could not decode JWT ${e::class.jvmName}: ${e.message}")
    }.getOrNull()
  }

  companion object {
    private val jsonMapper = Json {
      ignoreUnknownKeys = true
    }
  }
}


@Serializable
@SerialName("kafkatorio.auth.KafkatorioJwtPayload")
private data class KafkatorioJwtPayload(
  @SerialName("sub")
  val serverToken: FactorioServerToken,
  /** unix epoch seconds*/
  @SerialName("iat")
  val issuedAtTime: Long,
)


//// https://github.com/auth0/java-jwt/blob/f799e589c3d43de318c7ca0f0c36e462297db973/lib/src/test/java/com/auth0/jwt/PemUtils.java
//private object PemUtils {
//
//  fun readPublicKey(filepath: String, algorithm: String): PublicKey {
//    val bytes = parsePemFile(File(filepath))
//    return getPublicKey(bytes, algorithm)
//  }
//
////  fun readPublicKey(reader: Reader, algorithm: String): PublicKey {
////     return getPublicKey(bytes, algorithm)
////  }
//
//  fun readPrivateKey(filepath: String, algorithm: String): PrivateKey {
//    val bytes = parsePemFile(File(filepath))
//    return getPrivateKey(bytes, algorithm)
//  }
//
//  private fun parsePemFile(pemFile: File): ByteArray {
//    return when {
//      !pemFile.isFile   ->
//        throw FileNotFoundException("'${pemFile.absolutePath}' is not a file")
//
//      !pemFile.exists() ->
//        throw FileNotFoundException("The file '${pemFile.absolutePath}' doesn't exist.")
//
//      else              ->
//        PemReader(FileReader(pemFile)).use { reader ->
//          reader.readPemObject().content
//        }
//    }
//  }
//
//  private fun getPublicKey(keyBytes: ByteArray, algorithm: String): PublicKey =
//    getKey(algorithm) {
//      val keySpec: EncodedKeySpec = X509EncodedKeySpec(keyBytes)
//      generatePublic(keySpec)
//    }
//
//  private fun getPrivateKey(keyBytes: ByteArray, algorithm: String): PrivateKey =
//    getKey(algorithm) {
//      val keySpec: EncodedKeySpec = PKCS8EncodedKeySpec(keyBytes)
//      generatePrivate(keySpec)
//    }
//
//
//  private fun <K : Key> getKey(
//    algorithm: String,
//    generateKey: KeyFactory.() -> K
//  ): K = runCatching {
//    val kf = KeyFactory.getInstance(algorithm)
//    kf.generateKey()
//  }.getOrElse { e ->
//    when (e) {
//      is NoSuchAlgorithmException ->
//        throw IllegalStateException(
//          "Could not reconstruct key, algorithm '$algorithm' could not be found.",
//          e
//        )
//
//      is InvalidKeySpecException  ->
//        throw IllegalStateException("Could not reconstruct key", e)
//
//      else                        -> throw e
//    }
//  }
//}
