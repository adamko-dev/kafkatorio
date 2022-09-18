package kafkatorio.tasks

import com.github.gradle.node.NodePlugin
import kafkatorio.*
import java.io.File
import javax.inject.Inject
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.support.useToRun
import org.jetbrains.kotlin.util.suffixIfNot


@CacheableTask
abstract class UpdatePackageJson @Inject constructor(
  private val providers: ProviderFactory,
) : DefaultTask(),
  ProviderFactory by providers {

  /** Cumulative updates - will be combined one by one, then applied to [packageJsonFile] */
  @get:Input
  internal abstract val expectedJsonUpdates: ListProperty<String>

  @get:OutputFile
  abstract val packageJsonFile: RegularFileProperty


  init {
    group = NodePlugin.NPM_GROUP
    description = "Update package.json properties"

    outputs.upToDateWhen(JsonPropertiesUpToDateSpec)
  }


  @TaskAction
  fun exec() {
    val packageJsonFile = packageJsonFile.asFile.get()
    val expectedJsonUpdates: List<String> = expectedJsonUpdates.get().toList()

    logger.lifecycle("updating package.json ${packageJsonFile.canonicalFile}")

    val expectedJson = expectedJsonUpdates.fold(JsonObject(emptyMap())) { acc, update ->
      val updateJson = jsonMapper.parseToJsonElement(update).jsonObject
      JsonObject(acc + updateJson)
    }

    val updatedPackageJson = JsonObject(
      packageJsonFile.parseToJsonObject() + expectedJson
    )

    val packageJsonContentFormatted =
      jsonMapper
        .encodeToString(updatedPackageJson)

        // change empty arrays/objects, because `npm install` also formats package.json,
        // but slightly differently to kxs.
        .replace(": {\n  }", ": {}")
        .replace(": [\n  ]", ": []")

        .suffixIfNot("\n")

    logger.info(packageJsonContentFormatted)

    packageJsonFile.writer().useToRun {
      write(packageJsonContentFormatted)
    }
  }


  /** Update [expectedJson] by merging [updateJsonProvider] with the existing value. */
  fun updateExpectedJson(
    expectedJsonUpdate: JsonObjectBuilder.() -> Unit
  ) {
    val expectedJsonUpdateProvider = provider {
      buildJsonObject(expectedJsonUpdate).toString()
    }

    expectedJsonUpdates.add(expectedJsonUpdateProvider)
  }


  companion object {
    private val jsonMapper = Json {
      prettyPrint = true
      prettyPrintIndent = "  "
    }


    internal fun File.parseToJsonObject(): JsonObject =
      jsonMapper.parseToJsonElement(readText()).jsonObject


    internal fun Property<String>.parseToJsonObject(): JsonObject =
      jsonMapper.parseToJsonElement(get()).jsonObject


    internal fun List<String>.foldParseToJsonObject(): JsonObject =
      fold(JsonObject(emptyMap())) { acc, update ->
        val updateJson = jsonMapper.parseToJsonElement(update).jsonObject
        JsonObject(acc + updateJson)
      }
  }
}
