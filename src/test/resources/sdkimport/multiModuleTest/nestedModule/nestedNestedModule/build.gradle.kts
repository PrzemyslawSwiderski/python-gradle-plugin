import com.pswidersk.gradle.python.sdkimport.SaveSdkImportConfigTask

plugins {
    id("com.pswidersk.python-plugin")
}

tasks.register<SaveSdkImportConfigTask>("saveSdkImportConfigTest") {
    sdkConfigFile = file("../../sdk-import.yml")
    inputFile = file("locatePython.txt")
}
