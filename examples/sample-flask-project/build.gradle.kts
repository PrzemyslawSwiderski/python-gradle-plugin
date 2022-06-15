import com.pswidersk.gradle.python.VenvTask

plugins {
    id("com.pswidersk.python-plugin")
}

tasks {

    val pipInstall by registering(VenvTask::class) {
        venvExec = "pip"
        args = listOf("install", "--isolated", "-r", "requirements.txt")
    }

    register<VenvTask>("condaListEnv") {
        venvExec = "conda"
        args = listOf("env", "list")
    }

    register<VenvTask>("runFlaskApp") {
        venvExec = "flask"
        args = listOf("run")
        environment = mapOf("FLASK_APP" to "flask_app")
        dependsOn(pipInstall)
    }

    register<VenvTask>("runFlaskHelp") {
        venvExec = "flask"
        args = listOf("--help")
        environment = mapOf("FLASK_APP" to "flask_app")
        dependsOn(pipInstall)
    }

}