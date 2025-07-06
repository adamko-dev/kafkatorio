package dev.adamko.gradle.factorio

import dev.adamko.gradle.factorio.internal.FactorioModConfigurations
import dev.adamko.gradle.factorio.internal.configureDistribution
import dev.adamko.gradle.factorio.internal.configureIdea
import dev.adamko.gradle.factorio.tasks.*
import javax.inject.Inject
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.file.ProjectLayout
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.slf4j.LoggerFactory


abstract class FactorioModPlugin @Inject constructor(
  private val objects: ObjectFactory,
  private val providers: ProviderFactory,
  private val layout: ProjectLayout,
  private val files: FileSystemOperations,
) : Plugin<Project> {

  override fun apply(project: Project) {
    project.plugins.apply(FactorioModLibraryPlugin::class)

    val settings = project.extensions.create<FactorioModSettings>(FACTORIO_GRADLE_EXTENSION_NAME)

    val factorioModConfigurations = project.extensions.getByType(FactorioModConfigurations::class)

    val context = PluginContext(
      project = project,

      settings = settings,
      tasks = createTasks(project),
      configurations = factorioModConfigurations,

      objects = objects,
      providers = providers,
      layout = layout,
      files = files,
    )

    context.configureModSettings()

    context.configureTasks()

    context.configureIdea()

    context.configureDistribution()
  }

  private fun createTasks(project: Project): PluginContext.Tasks {
    return PluginContext.Tasks(
      launchFactorioClient = project.tasks.register<LaunchFactorioClientTask>("launchFactorioClient"),
      assembleModContents = project.tasks.register<AssembleFactorioModContents>("assembleFactorioModContents"),
      packageMod = project.tasks.register<PackageFactorioModTask>("packageFactorioMod"),
      publishModToLocalClient = project.tasks.register<LocalPublishFactorioModTask>("publishFactorioModToLocalClient"),
      generateModInfoJson = project.tasks.register<GenerateFactorioModInfoTask>("generateFactorioModInfoJson"),
    )
  }

  internal class PluginContext(
    val project: Project,
    val settings: FactorioModSettings,
    val tasks: Tasks,
    val configurations: FactorioModConfigurations,

    val objects: ObjectFactory,
    val providers: ProviderFactory,
    val layout: ProjectLayout,
    val files: FileSystemOperations,
  ) {
    internal val logger = LoggerFactory.getLogger(this::class.java)

    data class Tasks(
      val launchFactorioClient: TaskProvider<LaunchFactorioClientTask>,
      val assembleModContents: TaskProvider<AssembleFactorioModContents>,
      val packageMod: TaskProvider<PackageFactorioModTask>,
      val publishModToLocalClient: TaskProvider<LocalPublishFactorioModTask>,
      val generateModInfoJson: TaskProvider<GenerateFactorioModInfoTask>,
    )
  }


  companion object {
    const val FACTORIO_GRADLE_EXTENSION_NAME = "factorioMod"

    const val TASK_GROUP = "factorio mod"

//    /** Lifecycle task for publishing the mod locally */
//    const val PUBLISH_MOD_LOCAL_TASK_NAME = "publishModLocal"


    //private val logger = LoggerFactory.getLogger(FactorioModPlugin::class.java)
  }
}
