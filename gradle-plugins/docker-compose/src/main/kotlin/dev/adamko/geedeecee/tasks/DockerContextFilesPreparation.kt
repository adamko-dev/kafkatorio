package dev.adamko.geedeecee.tasks

import dev.adamko.geedeecee.GDCPlugin
import javax.inject.Inject
import org.gradle.api.tasks.Sync


// look into using 'build context' for up-to-date check
// https://stackoverflow.com/questions/38946683/how-to-test-dockerignore-file
// https://github.com/pwaller/docker-show-context
// https://snippets.khromov.se/see-which-files-are-included-in-your-docker-build-context/


//@CacheableTask
//@Suppress("UnstableApiUsage")
abstract class DockerContextFilesPreparation @Inject constructor(
//  private val files: FileSystemOperations,
) : Sync() {

//  @get:InputFiles
////  @get:SkipWhenEmpty
//  @get:PathSensitive(PathSensitivity.RELATIVE)
//  @get:NormalizeLineEndings
//  abstract val dockerContextFiles: ConfigurableFileCollection

//  @get:Input
//  abstract val copySpec: Property<CopySpec>
//
//  @get:OutputDirectory
//  abstract val dockerContextDir: DirectoryProperty

  init {
    group = GDCPlugin.GCD_TASK_GROUP
  }

//  @TaskAction
//  fun exec() {
//    files.sync {
//      from(dockerContextFiles, copySpec)
//      into(dockerContextDir)
////      with(copySpec)
//    }
//  }

//  /** @see [CopySpec.from] */
//  override fun from(vararg sourcePaths: Any?): CopySpec =
//    copySpec.from(sourcePaths)
//
//  /** @see [CopySpec.from] */
//  override fun from(sourcePath: Any, configureClosure: Closure<*>): CopySpec =
//    copySpec.from(sourcePath, configureClosure)
//
//  /** @see [CopySpec.from] */
//  override fun from(sourcePath: Any, configureAction: Action<in CopySpec>): CopySpec =
//    copySpec.from(sourcePath, configureAction)
}
