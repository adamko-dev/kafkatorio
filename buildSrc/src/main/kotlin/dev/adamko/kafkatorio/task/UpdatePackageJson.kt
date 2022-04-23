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
abstract class UpdatePackageJson : DefaultTask() {

  @get:Input
  abstract val propertiesToCheck: MapProperty<String, String>

  @get:OutputFile
  abstract val packageJsonFile: RegularFileProperty

  @get:Inject
  abstract val providers: ProviderFactory
//
//  @get:Inject
//  abstract val fs: FileSystemOperations

  init {
    group = NodePlugin.NODE_GROUP
    description = "Read the package.json file and update the version and name, based on the project."

    outputs.upToDateWhen(JsonPropertiesUpToDateSpec)

    super.doFirst {
      propertiesToCheck.disallowChanges()
      packageJsonFile.disallowChanges()
    }
  }

  @TaskAction
  fun exec() {
    val pj = packageJsonFile.asFile.get()

    logger.info("updating package.json ${pj.canonicalPath}")
    val packageJson = jsonMapper.parseToJsonElement(pj.readText()).jsonObject

    val newJsonProps = propertiesToCheck.get().mapValues { (_, newVal) -> JsonPrimitive(newVal) }

    val packageJsonUpdate = JsonObject(packageJson + newJsonProps)
    val packageJsonContentUpdated =
      jsonMapper
        .encodeToString(packageJsonUpdate)
        .suffixIfNot("\n")

    logger.debug(packageJsonContentUpdated)

    packageJsonFile.get().asFile.writer().useToRun {
      write(packageJsonContentUpdated)
    }
  }
}
