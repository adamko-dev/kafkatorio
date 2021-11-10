How I can run a npx Gradle task with Kotlin/JS?

I have a project that includes some dependencies downloaded from NPM and source code in Typescript.

I want to use [TypeScriptToLua](https://typescripttolua.github.io/) to convert my Typescript files
into Lua.

I see that Kotlin/JS has two

https://github.com/JetBrains/kotlin/blob/b76cbdaa41c8cae04d8bd1bd857186d7835045b5/libraries/tools/kotlin-gradle-plugin/src/main/kotlin/org/jetbrains/kotlin/gradle/targets/js/npm/NpmProject.kt#L86

```kotlin
fun useTool(
  exec: ExecSpec,
  tool: String,
  nodeArgs: List<String> = listOf(),
  args: List<String>
) {
  exec.workingDir = dir
  exec.executable = nodeExecutable
  exec.args = nodeArgs + require(tool) + args
}
```

https://github.com/JetBrains/kotlin/blob/92d200e093c693b3c06e53a39e0b0973b84c7ec5/libraries/tools/kotlin-gradle-plugin/src/main/kotlin/org/jetbrains/kotlin/gradle/targets/js/dukat/DukatRunner.kt#L47

```kotlin
npmProject.useTool(
  exec,
  "dukat/bin/dukat-cli.js",
  listOf(),
  args
)
```

https://github.com/JetBrains/kotlin/blob/711b882e7176060fc7fe2968773a393c7be73cb8/libraries/tools/kotlin-gradle-plugin/src/main/kotlin/org/jetbrains/kotlin/gradle/targets/js/nodejs/NodeJsExec.kt

In `build.gradle.kts` I've configured the Kotlin Multiplatform plugin to download
the `typescript-to-lua` dependency

```kotlin
plugins {
  kotlin("multiplatform")
}

kotlin {
  js(IR) {
    browser()
  }

  sourceSets {
    val jsMain: KotlinSourceSet by getting {
      dependencies {
        implementation(npm("typescript-to-lua", "1.1.1"))
      }
    }
  }
}
```

and this correctly downloads the NPM module binaries:
`build/js/packages/data-model/node_modules/.bin/tstl`

