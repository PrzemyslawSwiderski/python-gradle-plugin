# Python Gradle Plugin
This Gradle Plugin wraps JetBrains [gradle-python-envs-plugin](https://github.com/JetBrains/gradle-python-env)
with tasks to run `python` or `pip` executables in prepared virtual envs.

## Usage
### Steps to run python script from Gradle
1. Apply a plugin to a project as described on [gradle portal](https://plugins.gradle.org/plugin/com.pswidersk.python-plugin).
2. Configure a plugin by specifying desired python version in build script:
    ```kotlin
    pythonPlugin {
        pythonVersion.set("3.8.2")
    }
    ```
3. Run `build_envs` task to download and install a python virtual environment:
    ```shell script
    # Linux
    ./gradlew build_envs
    # Windows
    gradlew.bat build_envs
    ```
4. Define a task to run desired python script, for example to run `quicksort.py` script add the following task configuration in build script:
    ```kotlin
    tasks {
        register<com.pswidersk.gradle.python.PythonTask>("runQuickSort") {
            execArgs.set("quicksort.py")
        }
    }
    ```
5. Run python script from gradle:
```shell script
# Linux
./gradlew runQuickSort
# Windows
gradlew.bat runQuickSort
```

### Additional examples alongside with sample PipTasks configurations can be found in `examples` module in this project. 

## Common issues
* in case of uninstalling venv from Windows, it can be necessary to run uninstall exec (downloaded in `build` directory) to fully uninstall python, 
deleting python venv dir could not be sufficient
* installing python on Linux can require installation of additional packages, 
for example openssl, so before virtual envs installation run: `sudo apt-get install openssl` 