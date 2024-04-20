package com.pswidersk.gradle.python.sdkimport

data class SdkImportConfig(
    val import: MutableList<SdkImportConfigEntry> = mutableListOf()
)

data class SdkImportConfigEntry(
    var module: String,
    var path: String,
    var type: SdkType,
)

enum class SdkType {
    PYTHON
}
