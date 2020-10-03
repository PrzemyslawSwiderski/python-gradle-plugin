import com.pswidersk.gradle.python.VenvTask

plugins {
    id("com.pswidersk.python-plugin") version "1.2.2"
}

pythonPlugin {
    pythonVersion.set("3.8.3")
}

tasks {

    val pipInstall by registering(VenvTask::class) {
        venvExec = "pip"
        args = listOf("install", "geobuf==1.1.1")
    }

    register<VenvTask>("runGeobufEncode") {
        venvExec = "geobuf"
        doFirst {
            standardInput = file("sample.geojson").inputStream()
            standardOutput = file("sample.pbf").outputStream()
        }
        args = listOf("encode")
        dependsOn(pipInstall)
    }

    register<VenvTask>("runGeobufDecode") {
        venvExec = "geobuf"
        doFirst {
            standardInput = file("sample.pbf").inputStream()
            standardOutput = file("sample.geojson").outputStream()
        }
        args = listOf("decode")
        dependsOn(pipInstall)
    }

}