# Python Gradle Plugin
**Now, all what is required to run python scripts is Java.** 

This Gradle Plugin uses [Miniconda](https://docs.conda.io/en/latest/miniconda.html)
to run executables (`python`, `pip`, `conda` etc.) from virtual env. 

Python project configuration can be fully automated by Gradle tasks.   

## Requirements
* Java JDK or JRE version 8 or higher

## Purpose

Running python scripts or projects by executing single tasks which will download and install Python virtual environment.

Additional Python configuration (pip/conda packages installation etc.) can be done by defining Gradle tasks in `build.gradle.kts` file. 

## Usage

### Steps to run python script from Gradle
1. Apply a plugin to a project as described on [gradle portal](https://plugins.gradle.org/plugin/com.pswidersk.python-plugin).
2. Configure a plugin by specifying desired python version in build script:
    ```kotlin
    pythonPlugin {
        pythonVersion.set("3.8.2")
    }
    ```
    Possible properties in plugin extension are:
   - `pythonVersion` -> Python environment version, default `3.10.4`
   - `minicondaVersion` -> Miniconda3 version, default `latest`
3. Define a task to run desired python script, for example to run `quicksort.py` script in `main` dir add the following task configuration to build script:
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

### Additional examples alongside with sample PipTasks configurations can be found in `examples` module in this project. 

## Intellij setup
* To have autocomplete and modules properly recognized in Intellij Idea simply point python executable as described in: 
https://www.jetbrains.com/help/idea/configuring-python-sdk.html
* To have properly recognized imported source modules in tests, right click on sources directory (for example `main`) -> `Mark Direcotry as` -> `as Sources root`.

### Python exec locations (`*` is a configured python version)

#### Linux - `.gradle/python/pythonVenvs/virtualenv-*/bin/python`

#### Windows - `.gradle/python/pythonVenvs/virtualenv-*/Scripts/python.exe`

## Notes
* It may be required to unset `PYTHONPATH` in system before running any tasks (https://stackoverflow.com/a/31841132)  