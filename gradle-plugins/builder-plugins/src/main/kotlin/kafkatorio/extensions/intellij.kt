package kafkatorio.extensions

import java.io.File
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.provider.SetProperty
import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import org.gradle.kotlin.dsl.of
import org.gradle.kotlin.dsl.support.serviceOf
import org.gradle.plugins.ide.idea.model.IdeaModel


/**
 * Exclude directories containing
 *
 * - generated Gradle code,
 * - IDE files,
 * - Gradle config,
 *
 * so they don't clog up search results.
 *
 * @param[doNotWalkDirs] Do not enter directories with these names (speeds up search results).
 */
fun Project.excludeProjectConfigurationDirs(
  idea: IdeaModel,
  dirsToExclude: Set<String>,
  generatedSrcDirs: Set<String> = setOf(
    "kotlin-dsl-accessors",
    "kotlin-dsl-external-plugin-spec-builders",
    "kotlin-dsl-plugins",
  ),
  doNotWalkDirs: Set<String> = setOf(
    ".git",
    ".kotlin",
  )
) {
  val excludedDirs = providers.of(IdeaExcludedDirectoriesSource::class) {
    parameters.projectDir.set(layout.projectDirectory)
    parameters.generatedSrcDirs.set(generatedSrcDirs)
    parameters.dirsToExclude.set(dirsToExclude)
    parameters.doNotWalkDirs.set(doNotWalkDirs)
  }.get()

  idea.module.excludeDirs.addAll(excludedDirs)
}

// Have to use a ValueSource to find the files, otherwise Gradle
// considers _all files_ an input for configuration cache, which is VERY SLOW.
internal abstract class IdeaExcludedDirectoriesSource :
  ValueSource<Set<File>, IdeaExcludedDirectoriesSource.Parameters> {

  interface Parameters : ValueSourceParameters {
    val projectDir: DirectoryProperty
    val generatedSrcDirs: SetProperty<String>
    val dirsToExclude: SetProperty<String>
    val doNotWalkDirs: SetProperty<String>
  }

  override fun obtain(): Set<File> {
    val projectDir = parameters.projectDir.get().asFile
    val dirsToExclude = parameters.dirsToExclude.orNull.orEmpty()
    val doNotWalkDirs = parameters.doNotWalkDirs.orNull.orEmpty()
    val generatedSrcDirs = parameters.generatedSrcDirs.orNull.orEmpty()

    val generatedDirs = projectDir
      .walk()
      .onEnter { it.name !in doNotWalkDirs && it.parentFile.name !in generatedSrcDirs }
      .filter { it.isDirectory }
      .filter { it.parentFile.name in generatedSrcDirs }
      .flatMap { file ->
        file.walk().maxDepth(1).filter { it.isDirectory }.toList()
      }
      .toSet()

    val excludedProjectDirs = projectDir
      .walk()
      .onEnter { it.name !in doNotWalkDirs }
//      .filter { it.isDirectory }
      .filter { dir ->
        dirsToExclude.any {
          dir.invariantSeparatorsPath.endsWith("/$it")
        }
      }
      .toSet()

    // can't use buildSet {} https://github.com/gradle/gradle/issues/28325
    return mutableSetOf<File>().apply {
      addAll(generatedDirs)
      addAll(excludedProjectDirs)
    }
  }
}


/**
 * Sets a logo for project IDEs.
 *
 * (Avoid updating the logo during project configuration,
 * instead piggyback off a random task that runs on IJ import.)
 */
fun Task.initIdeProjectLogo(
  svgLogoPath: String
) {
  val fs = project.serviceOf<FileSystemOperations>()

  val logoSvg = project.layout.projectDirectory.file(svgLogoPath)
  val ideaDir = project.layout.projectDirectory.dir(".idea")
  // don't register task inputs, we don't really care about up-to-date checks

  doLast("initIdeProjectLogo") {
    if (
      logoSvg.asFile.exists()
      && ideaDir.asFile.exists()
      && !ideaDir.file("icon.png").asFile.exists()
      && !ideaDir.file("icon.svg").asFile.exists()
    ) {
      fs.copy {
        from(logoSvg) { rename { "icon.svg" } }
        into(ideaDir)
      }
    }
  }
}
