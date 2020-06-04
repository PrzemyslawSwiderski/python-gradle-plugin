import com.pswidersk.gradle.python.VenvTask

plugins {
    id("com.pswidersk.python-plugin") version "1.1.2"
}

pythonPlugin {
    pythonVersion.set("3.8.2")
}

tasks {

    register<VenvTask>("pip") {
        venvExec = "pip"
        // exec args can be passed by commandline, for example
        // ./gradlew pip --args="install -r requirements.txt"
    }

    register<VenvTask>("runQuickSort") {
        workingDir(projectDir.resolve("main"))
        args(listOf("quicksort.py"))
    }

    val pipInstall by registering(VenvTask::class) {
        venvExec = "pip"
        args(listOf("install", "--isolated", "-r", "requirements.txt"))
    }

    register<VenvTask>("runNumpy") {
        workingDir(projectDir.resolve("main"))
        args(listOf("numpy_test.py"))
        environment["ENV_VAR_TO_PRINT"] = "sampleEnvVar"
        dependsOn(pipInstall)
    }
    register<VenvTask>("runPyTests") {
        venvExec = "pytest"
        workingDir(projectDir.resolve("test"))
        environment["PYTHONPATH"] = projectDir.resolve("main").absolutePath
        args(listOf("test_quicksort.py"))
        dependsOn(pipInstall)
    }

}