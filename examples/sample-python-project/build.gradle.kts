plugins {
    id("com.pswidersk.python-plugin") version "1.0.0"
}

pythonPlugin {
    pythonVersion.set("3.8.2")
}

tasks {
    register<com.pswidersk.gradle.python.PythonTask>("runQuickSort") {
        script.set("quicksort.py")
    }

    register<com.pswidersk.gradle.python.PythonTask>("runNumpy") {
        script.set("numpy_test.py")
    }
}