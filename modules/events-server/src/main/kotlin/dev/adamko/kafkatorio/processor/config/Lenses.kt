package dev.adamko.kafkatorio.processor.config

//import java.io.File
//import org.http4k.cloudnative.env.Environment
//import org.http4k.cloudnative.env.EnvironmentKey
//import org.http4k.core.Credentials
//import org.http4k.lens.BiDiLens
//import org.http4k.lens.BiDiMapping
//
//
//internal object Lenses {
//
//  /** The location of this app's env file. */
//  val APPLICATION_PROPERTIES_FILE_PATH: BiDiLens<Environment, File?> =
//    EnvironmentKey
//      .map(
//        { path: String -> File(path) },
//        { file: File -> file.canonicalPath },
//      )
//      .optional("APPLICATION_PROPERTIES_FILE_PATH")
//
//
//  /** Bidirectional [String] and [Credentials] mapper. */
//  val credentialsLens: BiDiMapping<String, Credentials> = run {
//    val separator = ":"
//    BiDiMapping(
//      // from String to Credentials
//      { property ->
//        property.split(separator, limit = 2)
//          .takeIf { it.size == 2 }
//          ?.let { (user, pass) -> Credentials(user, pass) }
//          ?: throw IllegalArgumentException("Credentials are invalid")
//      },
//      // from Credentials to String
//      { it.user + separator + it.password }
//    )
//  }
//
//}
