package kafkatorio.extensions

import java.io.ByteArrayOutputStream
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.Directory
import org.gradle.api.file.FileCopyDetails
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RelativePath
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.provider.Provider
import org.gradle.api.specs.NotSpec
import org.gradle.api.specs.Spec
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.application.CreateStartScripts
import org.gradle.kotlin.dsl.register
import org.gradle.plugins.ide.idea.model.IdeaModule
import org.gradle.process.ExecSpec
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension
import org.jetbrains.kotlin.gradle.targets.js.yarn.yarn
import org.jetbrains.kotlin.incremental.md5


operator fun <T> Spec<T>.not(): NotSpec<T> = NotSpec(this)


//fun isProcessRunning(process: String, ignoreCase: Boolean = true): Spec<Task> =
//  Spec<Task> {
//    ByteArrayOutputStream().use { outputStream ->
//      project.exec {
//      }
//      outputStream.toString().contains(process, ignoreCase)
//    }
//  }
//


/** exclude generated Gradle code, so it doesn't clog up search results */
fun IdeaModule.excludeGeneratedGradleDsl(layout: ProjectLayout) {
  excludeDirs.addAll(
    layout.files(
      "buildSrc/build/generated-sources/kotlin-dsl-accessors",
      "buildSrc/build/generated-sources/kotlin-dsl-accessors/kotlin",
      "buildSrc/build/generated-sources/kotlin-dsl-accessors/kotlin/gradle",
      "buildSrc/build/generated-sources/kotlin-dsl-external-plugin-spec-builders",
      "buildSrc/build/generated-sources/kotlin-dsl-external-plugin-spec-builders/kotlin",
      "buildSrc/build/generated-sources/kotlin-dsl-external-plugin-spec-builders/kotlin/gradle",
      "buildSrc/build/generated-sources/kotlin-dsl-plugins",
      "buildSrc/build/generated-sources/kotlin-dsl-plugins/kotlin",
      "buildSrc/build/generated-sources/kotlin-dsl-plugins/kotlin/dev",
      "buildSrc/build/pluginUnderTestMetadata",
    )
  )
}


// https://stackoverflow.com/a/70317110/4161471
fun Project.execCapture(spec: ExecSpec.() -> Unit): String {
  return ByteArrayOutputStream().use { outputStream ->
    exec {
      this.spec()
      this.standardOutput = outputStream
    }
    val output = outputStream.toString().trim()
    logger.lifecycle(output)
    output
  }
}


/** https://youtrack.jetbrains.com/issue/KT-50848 */
fun Project.yarn(configure: YarnRootExtension.() -> Unit) = with(yarn, configure)


fun Directory.filesChecksum(): Long = asFileTree
  .files
  .map { it.readBytes() + it.absolutePath.toByteArray() }
  .fold(byteArrayOf()) { acc, bytes -> acc + bytes }
  .md5()


/** Drop the first [count] directories from the path */
fun FileCopyDetails.dropDirectories(count: Int): RelativePath =
  RelativePath(true, *relativePath.segments.drop(count).toTypedArray())


/** Drop the first directory from the path */
fun FileCopyDetails.dropDirectory(): RelativePath =
  dropDirectories(1)


/** https://github.com/gradle/gradle/issues/16543 */
fun Project.taskProvider(taskName: String): Provider<Task> = providers.provider {
  taskName
}.flatMap {
  tasks.named(it)
}


const val DOCKER_COMPOSE_TASK_GROUP = "docker-compose"


fun Project.registerStartScriptTask(
  appName: String,
  configureStartScripts: CreateStartScripts.() -> Unit = {},
): TaskProvider<CreateStartScripts> =
  tasks.register<CreateStartScripts>("startScript$appName") {

    val jarTask = tasks.getByName(JavaPlugin.JAR_TASK_NAME)
    val runtimeClasspath = configurations.getByName(JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME)
    dependsOn(jarTask)
    dependsOn(runtimeClasspath)

    applicationName = appName
    outputDir = layout.buildDirectory.dir("scripts").get().asFile
    classpath = jarTask.outputs.files + runtimeClasspath
    configureStartScripts()
  }
