import com.pswidersk.gradle.python.VenvTask

plugins {
    id("com.pswidersk.python-plugin")
}

pythonPlugin {
    pythonVersion = "3.8.3"
    condaVersion = "py38_23.11.0-2"
    installDir = file(rootProject.file(".gradle/python/${project.name}"))
}

tasks {

    val pipInstall by registering(VenvTask::class) {
        venvExec = "pip"
        args = listOf("install", "geobuf==1.1.1", "protobuf==3.20.1")
    }

    register<VenvTask>("runGeobufEncode") {
        venvExec = "geobuf"
        inputFile = file("sample.geojson")
        outputFile = file("sample.pbf")
        args = listOf("encode")
        dependsOn(pipInstall)
    }

    register<VenvTask>("runGeobufDecode") {
        venvExec = "geobuf"
        inputFile = file("sample.pbf")
        outputFile = file("sample.geojson")
        args = listOf("decode")
        dependsOn(pipInstall)
    }

}