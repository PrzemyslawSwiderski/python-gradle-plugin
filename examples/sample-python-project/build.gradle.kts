plugins {
    id("com.pswidersk.python-plugin") version "1.1.1"
}

pythonPlugin {
    pythonVersion.set("3.8.2")
}

tasks {

    register<com.pswidersk.gradle.python.PipTask>("pip") {
        // execArgs can be passed by commandline, for example
        // ./gradlew pip --args="install -r requirements.txt"
    }

    register<com.pswidersk.gradle.python.PythonTask>("runQuickSort") {
        args.set(listOf("quicksort.py"))
    }

    val pipInstall by registering(com.pswidersk.gradle.python.PipTask::class) {
        args.set(listOf("install", "-r", "requirements.txt"))
    }

    register<com.pswidersk.gradle.python.PythonTask>("runNumpy") {
        args.set(listOf("numpy_test.py"))
        environment.set(mapOf("ENV_VAR_TO_PRINT" to "sampleEnvVar"))
        dependsOn(pipInstall)
    }

}