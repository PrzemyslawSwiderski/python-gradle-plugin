import com.pswidersk.gradle.python.VenvTask

plugins {
    id("com.pswidersk.python-plugin")
}

pythonPlugin {
    pythonVersion.set("3.8.3")
    installDir.set(file(buildDir.resolve("python"))) // setting different install directory this time
}

tasks {

    val pipInstall by registering(VenvTask::class) {
        venvExec = "pip"
        args = listOf("install", "geobuf==1.1.1", "protobuf==3.20.1")
    }

    register<VenvTask>("runGeobufEncode") {
        venvExec = "geobuf"
        inputFile.set(file("sample.geojson"))
        outputFile.set(file("sample.pbf"))
        args = listOf("encode")
        dependsOn(pipInstall)
    }

    register<VenvTask>("runGeobufDecode") {
        venvExec = "geobuf"
        inputFile.set(file("sample.pbf"))
        outputFile.set(file("sample.geojson"))
        args = listOf("decode")
        dependsOn(pipInstall)
    }

}