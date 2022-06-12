import com.pswidersk.gradle.python.VenvTask

plugins {
    id("com.pswidersk.python-plugin")
}

pythonPlugin {
    pythonVersion.set("3.8.3")
// TODO: Uncomment below after upgrading plugin in samples
//    installDir.set(file(buildDir.resolve("python"))) // setting different install directory this time
}

tasks {

    val pipInstall by registering(VenvTask::class) {
        venvExec = "pip"
        args = listOf("install", "geobuf==1.1.1", "protobuf==3.20.1")
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