import com.pswidersk.gradle.python.VenvTask

plugins {
    id("com.pswidersk.python-plugin")
}

pythonPlugin {
    useHomeDir = true
}

tasks {
    register<VenvTask>("runPython") {
        args = listOf("script.py")
    }
}
