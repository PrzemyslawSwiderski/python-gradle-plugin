import com.pswidersk.gradle.python.VenvTask

plugins {
    id("com.pswidersk.python-plugin")
}

pythonPlugin {
    pythonVersion = "3.10.4"
}

tasks {

    val pipInstall by registering(VenvTask::class) {
        venvExec = "pip"
        args = listOf("install", "--isolated", "-r", "requirements.txt")
    }

    register<VenvTask>("runTTSScript") {
        args = listOf("tts_script.py")
        dependsOn(pipInstall)
    }

}