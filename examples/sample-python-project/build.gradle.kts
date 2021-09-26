import com.pswidersk.gradle.python.VenvTask

plugins {
    id("com.pswidersk.python-plugin")
}

pythonPlugin {
    pythonVersion.set("3.9.2")
    minicondaVersion.set("py38_4.8.3")
}

tasks {

    register<VenvTask>("pip") {
        venvExec = "pip"
        // exec args can be passed by commandline, for example
        // ./gradlew pip --args="install -r requirements.txt"
    }

    register<VenvTask>("condaInfo") {
        venvExec = "conda"
        args = listOf("info")
    }

    register<VenvTask>("runQuickSort") {
        workingDir.set(projectDir.resolve("main"))
        args = listOf("quicksort.py")
    }

    val pipInstall by registering(VenvTask::class) {
        venvExec = "pip"
        args = listOf("install", "--isolated", "-r", "requirements.txt")
    }

    register<VenvTask>("runNumpy") {
        workingDir.set(projectDir.resolve("main"))
        args = listOf("numpy_test.py")
        environment = mapOf("ENV_VAR_TO_PRINT" to "sampleEnvVar")
        dependsOn(pipInstall)
    }
    register<VenvTask>("runPyTests") {
        venvExec = "pytest"
        workingDir.set(projectDir.resolve("test"))
        environment = mapOf("PYTHONPATH" to projectDir.resolve("main").absolutePath)
        args = listOf("test_quicksort.py")
        dependsOn(pipInstall)
    }

}