import com.pswidersk.gradle.python.VenvTask

plugins {
    id("com.pswidersk.python-plugin")
}

pythonPlugin {
    pythonVersion = "3.9.2"
    condaVersion = "25.3.0-3"
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

    register<VenvTask>("runInlineScript") {
        doFirst {
            val scriptFile = temporaryDir.resolve("inlineScript.py")
            scriptFile.writeText(
                """
import random

lucky_numbers = []
print('Welcome To Lucky Lottery Numbers')
for num in range(0,5):
    random_num = random.randint(1, 100)
    lucky_numbers.append(random_num)
print(f'Lucky numbers are: {lucky_numbers}')
            """.trimIndent()
            )
            inputFile = scriptFile
        }
    }

    register<VenvTask>("runQuickSort") {
        workingDir = projectDir.resolve("main")
        args = listOf("quicksort.py")
    }

    val pipInstall by registering(VenvTask::class) {
        venvExec = "pip"
        args = listOf("install", "--isolated", "-r", "requirements.txt")
    }

    register<VenvTask>("runNumpy") {
        workingDir = projectDir.resolve("main")
        args = listOf("numpy_test.py")
        environment = mapOf("ENV_VAR_TO_PRINT" to "sampleEnvVar")
        dependsOn(pipInstall)
    }
    register<VenvTask>("runPyTests") {
        venvExec = "pytest"
        workingDir = projectDir.resolve("test")
        environment = mapOf("PYTHONPATH" to projectDir.resolve("main").canonicalPath)
        args = listOf("test_quicksort.py")
        dependsOn(pipInstall)
    }

}
