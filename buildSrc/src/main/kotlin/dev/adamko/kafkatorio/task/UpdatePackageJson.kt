package dev.adamko.kafkatorio.task

import com.github.gradle.node.NodePlugin
import dev.adamko.kafkatorio.jsonMapper
import javax.inject.Inject
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
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
) : DefaultTask() {

  @get:Input
  abstract val propertiesToCheck: MapProperty<String, String>

  @get:OutputFile
  abstract val packageJsonFile: RegularFileProperty


  init {
    group = NodePlugin.NODE_GROUP
    description =
      "Read the package.json file and update the version and name, based on the project."

    outputs.upToDateWhen(JsonPropertiesUpToDateSpec)
  }


  @TaskAction
  fun exec() {
    val packageJsonFile = packageJsonFile.asFile.get()

    logger.lifecycle("updating package.json ${packageJsonFile.canonicalPath}")

    val updatedPackageJson = updatedPackageJson()

    val packageJsonContentUpdated =
      jsonMapper
        .encodeToString(updatedPackageJson)
        .suffixIfNot("\n")

    logger.debug(packageJsonContentUpdated)

    packageJsonFile.writer().useToRun {
      write(packageJsonContentUpdated)
    }
  }

  internal fun currentPackageJson(): JsonObject {
    val packageJsonFile = packageJsonFile.get().asFile
    return jsonMapper.parseToJsonElement(packageJsonFile.readText()).jsonObject
  }

  internal fun updatedPackageJson(
    currentPackageJson: JsonObject = currentPackageJson(),
  ): JsonObject {
    val propertiesToCheck: Map<String, String> = propertiesToCheck.get()
    val newJsonProps = propertiesToCheck.mapValues { (_, newVal) ->
      JsonPrimitive(newVal)
    }
    return JsonObject(currentPackageJson + newJsonProps)
  }
}
