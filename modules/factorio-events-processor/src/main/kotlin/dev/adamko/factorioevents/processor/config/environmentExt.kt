package dev.adamko.factorioevents.processor.config

import java.io.File
import java.io.FileNotFoundException
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.MapEnvironment
import org.http4k.cloudnative.env.fromYaml
import org.http4k.lens.Lens


/**
 * Load properties from either a `.yaml` or `.env` file.
 *
 * (An improved version of [Environment.fromResource])
 */
internal fun Environment.Companion.fromResourcesFile(
  resource: String,
  default: Environment = EMPTY
) =
  try {
    val file =
      this::class.java
        .getResource("/${resource.removePrefix("/")}")
        ?.file
        ?.let { File(it) }
    fromFile(file, default)
  } catch (e: FileNotFoundException) {
    default
  }

/**
 * Try fetching a properties file the location provided by the
 * [`JVM`][jvmProperties] or [`System`][env] environment variable
 * [Lenses.APPLICATION_PROPERTIES_FILE_PATH], if it exists.
 *
 * If it does not exist, return [default].
 */
internal fun Environment.Companion.fromEnvVarPath(
  location: Lens<Environment, File?> = Lenses.APPLICATION_PROPERTIES_FILE_PATH,
  default: Environment = EMPTY,
): Environment {
  val file = (jvmProperties() overrides env())[location]

  return fromFile(file, default)
}

/**
 * Load properties from either a `.yaml` or `.env` file.
 *
 * (An improved version of [Environment.from])
 */
internal fun Environment.Companion.fromFile(
  file: File?,
  default: Environment = EMPTY,
): Environment {
  return file
    ?.takeIf { it.isFile }
    .let {
      when (it?.extension) {
        "yml", "yaml" -> fromYaml(it)
        "env"         -> from(it)
        else          -> default
      }
    }
}

/** The same as [Environment.ENV], but refreshes on each invocation (which is useful for testing). */
internal fun Environment.Companion.env(): Environment =
  MapEnvironment.from(System.getenv().toProperties())

/** The same as [Environment.JVM_PROPERTIES], but refreshes on each invocation (which is useful for testing). */
internal fun Environment.Companion.jvmProperties(): Environment =
  MapEnvironment.from(System.getProperties())
