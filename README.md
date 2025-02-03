[![Build Status](https://img.shields.io/endpoint.svg?url=https%3A%2F%2Factions-badge.atrox.dev%2FPrzemyslawSwiderski%2Fpython-gradle-plugin%2Fbadge&style=plastic)](https://actions-badge.atrox.dev/PrzemyslawSwiderski/python-gradle-plugin/goto)
[![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/PrzemyslawSwiderski/python-gradle-plugin?label=Plugin%20Version&sort=semver&style=plastic)](https://plugins.gradle.org/plugin/com.pswidersk.python-plugin)
[![Gradle Version](https://img.shields.io/badge/Gradle%20Version-8.12-yellowgreen?style=plastic)](https://gradle.org/releases/)
[![Kotlin Version](https://img.shields.io/badge/Kotlin%20Version-2.0.21-darkviolet?style=plastic)](https://kotlinlang.org/docs/releases.html)

# Python Gradle Plugin

**Now, you can run Python scripts with Java and Gradle builds only.**

This Gradle Plugin **automatically downloads and installs** specific [Conda](https://repo.anaconda.com/) tool
to run python scripts or other executables (`pip`, `conda` etc.) from virtual env.

Python project configuration can be fully automated by Gradle tasks now.

## Requirements

* Java `11` or higher
* Gradle `6.2` or higher project

## Purpose

Running python scripts or projects by executing single task which will download and install Python virtual environment.

Additional Python configuration (pip/conda packages installation etc.) can be done by defining Gradle tasks
in `build.gradle.kts` file.

## Usage

### Steps to run python script from Gradle

1. Apply a plugin to a project as described
   on [gradle portal](https://plugins.gradle.org/plugin/com.pswidersk.python-plugin).
2. Configure a plugin by specifying desired python version in build script:
    ```kotlin
    pythonPlugin {
        pythonVersion = "3.8.2"
    }
    ```
3. Define a task to run desired python script, for example to run `quicksort.py` script in `main` dir add the following
   task configuration to build script:
    ```kotlin
    tasks {
        register<VenvTask>("runQuickSort") {
                workingDir = projectDir.resolve("main")
                args = listOf("quicksort.py")
        }
    }
    ```
4. Run python script from gradle:
    ```shell script
    # Linux
    ./gradlew runQuickSort
    # Windows
    gradlew.bat runQuickSort
    ```
5. Wait until Conda is installed and environment set up.

6. Enjoy :)
   ![Quick Sort Python Script run](./images/quickSortPy.gif)

### Python Plugin properties

Plugin default behavior can be adjusted by specifying the following properties:

- `pythonVersion` -> Python environment version, default `3.13.0`, the available ones can be checked
  at https://anaconda.org/conda-forge/python/
- `condaVersion` -> Miniconda or Anaconda version, default `py312_24.9.2-0`, the available ones can be checked
  at https://repo.anaconda.com/miniconda/ or https://repo.anaconda.com/archive/ (Anaconda)
- `condaInstaller` -> Conda environment installer name, default is `Miniconda3`, for Anaconda change to `Anaconda3`
- `condaRepoUrl` -> repository URL which should be used to download binaries,
  default `https://repo.anaconda.com/miniconda`, for Anaconda `https://repo.anaconda.com/archive/`
- `condaRepoUsername` -> username for the basic auth if needed, absent by default
- `condaRepoPassword` -> password for the basic auth, used if `condaRepoUsername` is specified, should not be
  passed directly in script file, can be supplied
  by [gradle properties](https://docs.gradle.org/current/userguide/build_environment.html#sec:gradle_configuration_properties)
  , absent by default
- `condaRepoHeaders` -> additional optional headers used for connection, empty map by default
- `useHomeDir` -> when `true` the default install directory will be the one from `user.home` system property,
  `false` by default
- `installDir` -> property to customize conda installation directory, equals to `<rootProjectDir>/.gradle/python` by
  default or user home if `useHomeDir` = `true`
- `systemArch` -> operating system architecture, default is got from `os.arch` system property
- `ideaDir` -> target `.idea` directory to detect Intellij project, equals to `<rootProjectDir>/.idea` by
  default

Sample extension configuration inside of `build.gradle.kts` file:

```kotlin
pythonPlugin {
    pythonVersion = "3.7.0"
    condaVersion = "2022.05"
    condaInstaller = "Anaconda3"
    condaRepoUrl = "https://nexus.com/repositories/conda"
    condaRepoUsername = "user"
    condaRepoPassword = extra["conda.repo.pass"].toString()
    condaRepoHeaders = mapOf(
        "CUSTOM_HEADER_1" to "headerValue1",
        "CUSTOM_HEADER_2" to "headerValue2"
    )
    installDir = file(layout.buildDirectory.dir("python"))
    systemArch = "arm64"
}
```

### `VenvTask` task properties

All tasks which should be executed in virtual env can be customized as well by the following fields:

- `venvExec` -> name of executable from virtual env which will be executed, `python` by default
- `inputFile` -> optional input file, none by default
- `outputFile` -> optional output file, none by default
- `args` -> list of arguments for a `venvExec` executable, empty by default
- `workingDir` -> working directory, project directory by default
- `environment` -> map with environment variables to apply during the execution, empty by default

Sample `VenvTask` configuration inside of `build.gradle.kts` file:

```kotlin
register<VenvTask>("runPythonScript") {
    venvExec = "python"
    inputFile = file("inputFile.txt")
    outputFile = file("outputFile.txt")
    args = listOf("--some-flag", "arg1")
    workingDir = projectDir.resolve("main")
    environment = mapOf("ENV_VAR_TO_PRINT" to "sampleEnvVar")
}
```

### Additional examples alongside with sample PipTasks configurations can be found in `examples` module in this project.

## Existing project integration

Integration with the existing Python projects can also be done within a separate Gradle module.

An example can be found [here](https://github.com/PrzemyslawSwiderski/Janus/tree/main/gradle-setup).

By simply running the `runDemoScript` Gradle task user can bootstrap the whole project locally or in Docker container
via `runDemoContainer` task.

## Intellij setup

Auto import installed Python SDK:

* Install [SDK-Import Intellij Plugin](https://github.com/PrzemyslawSwiderski/sdk-import-plugin).
* Execute gradle `envSetup` task.
* Choose from "Tools" -> "Reimport SDK" to import installed Python SDK with plugin.

Manual way:

* To have autocomplete and modules properly recognized in Intellij Idea point to Conda environment as described in:
  https://www.jetbrains.com/help/idea/configuring-python-sdk.html
* To have properly recognized imported source modules in tests, right click on sources directory (for example `main`)
  -> `Mark Directory as` -> `as Sources root`.

### Conda install directories

* Linux - `<installDir>/.gradle/python/Linux/<condaInstaller>-<condaVersion>`

* Windows - `<installDir>/.gradle/python/Windows/<condaInstaller>-<condaVersion>`

* MacOSX - `<installDir>/.gradle/python/MacOSX/<condaInstaller>-<condaVersion>`

Where `<installDir>` is the root catalog where the Conda will be installed specified by `installDir` property,
`<condaInstaller>` is Conda installer e.g. `Miniconda3` and `<condaVersion>` is Conda installer version e.g.
`py38_4.8.3`

If you are familiar with [conda](https://conda.io/projects/conda/en/latest/user-guide/index.html) you can also execute
conda commands like `conda deactivate` or `conda install` directly with the binaries from the catalogs above.

## Notes

* It may be required to unset `PYTHONPATH` in system before running any tasks (https://stackoverflow.com/a/31841132)
* You can also run some simple inline Python scripts inside build files like this:

  ![Quick Sort Python Script run](./images/inlineScriptTask.JPG)

  Intellij 'inject language' feature can be useful in such scenario :)

## Known Issues

* `/usr/bin/env: ‘python’: No such file or directory` when executing `envSetup` task -> It is related to the shebang
  char limit which is **128**.

  When installing the conda if the prefix path is longer than the limit the default shebang (`#!/usr/bin/env
  python`) is being used in the installed conda script file (`condabin/conda`).
  Since no python binary is accessible by this path the exception is being thrown.

  The easiest solution is to store the root project at the shortest possible path or use the `installDir`
  to specify shorter path per particular troublesome subproject.
