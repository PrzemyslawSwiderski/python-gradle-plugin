plugins {
    id("com.pswidersk.python-plugin") version "1.1.0"
}

pythonPlugin {
    pythonVersion.set("3.8.2")
}

tasks {

    register<com.pswidersk.gradle.python.PythonTask>("runQuickSort") {
        execArgs.set("quicksort.py")
    }

    val pipInstall by registering(com.pswidersk.gradle.python.PipTask::class) {
        execArgs.set(listOf("install", "-r", "requirements.txt"))
    }

    register<com.pswidersk.gradle.python.PythonTask>("runNumpy") {
        execArgs.set("numpy_test.py")
        dependsOn(pipInstall)
    }
}