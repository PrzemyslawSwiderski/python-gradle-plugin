package com.pswidersk.gradle.python.sdkimport

import com.fasterxml.jackson.module.kotlin.readValue
import com.pswidersk.gradle.python.YAML_MAPPER
import com.pswidersk.gradle.python.sdkimport.SdkType.PYTHON
import org.slf4j.Logger
import java.io.File

internal fun updateSdkConfig(sdkConfigFile: File, pythonSdkPath: String, moduleName: String, logger: Logger) {
    val testSdkImport = try {
        YAML_MAPPER.readValue<SdkImportConfig>(sdkConfigFile)
    } catch (exception: Exception) {
        logger.warn("Could not parse existing: $sdkConfigFile, creating a new one...")
        SdkImportConfig()
    }

    if (testSdkImport.import.any { it.module == moduleName }) {
        testSdkImport.import.filter { it.module == moduleName }.forEach {
            it.module = moduleName
            it.path = pythonSdkPath
            it.type = PYTHON
        }
    } else {
        testSdkImport.import.add(
            SdkImportConfigEntry(
                module = moduleName,
                path = pythonSdkPath,
                type = PYTHON
            )
        )
    }

    logger.info("Writing config: '$testSdkImport' to file: '$sdkConfigFile'")
    YAML_MAPPER.writeValue(sdkConfigFile, testSdkImport)
}
