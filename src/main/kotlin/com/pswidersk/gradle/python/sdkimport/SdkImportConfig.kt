package com.pswidersk.gradle.python.sdkimport

class SdkImportConfig {
    var import: MutableList<SdkImportConfigEntry> = mutableListOf()
}

class SdkImportConfigEntry {
    var type: String = PYTHON_SDK_TYPE
    var path: String = ""
    var module: String = ""
}

const val PYTHON_SDK_TYPE = "PYTHON"
