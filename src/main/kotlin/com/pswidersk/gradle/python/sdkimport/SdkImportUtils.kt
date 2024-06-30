package com.pswidersk.gradle.python.sdkimport

import org.slf4j.Logger
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.DumperOptions.FlowStyle.BLOCK
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.nodes.Tag.MAP
import org.yaml.snakeyaml.representer.Representer
import java.io.File

internal fun updateSdkConfig(sdkConfigFile: File, pythonSdkPath: String, moduleName: String, logger: Logger) {
    val sdkImport = if (sdkConfigFile.exists()) {
        sdkConfigFile.loadAsYamlImportConfig()
    } else {
        logger.warn("Could not locate: $sdkConfigFile, creating a new one...")
        SdkImportConfig()
    }

    if (sdkImport.import.any { entryMatches(it, moduleName) }) {
        sdkImport.import.filter { entryMatches(it, moduleName) }.forEach {
            it.module = moduleName
            it.path = pythonSdkPath
        }
    } else {
        sdkImport.import.add(
            SdkImportConfigEntry().apply {
                module = moduleName
                path = pythonSdkPath
            }
        )
    }

    logger.info("Writing config: '$sdkImport' to file: '$sdkConfigFile'")
    sdkImport.saveAsYamlFile(sdkConfigFile)
}

private fun entryMatches(
    entry: SdkImportConfigEntry,
    moduleName: String
) = entry.module == moduleName && entry.type == PYTHON_SDK_TYPE

internal fun File.loadAsYamlImportConfig(): SdkImportConfig = this.inputStream().use {
    Yaml(Constructor(SdkImportConfig::class.java, LoaderOptions()))
        .load(it) ?: SdkImportConfig()
}


internal fun SdkImportConfig.saveAsYamlFile(file: File) {
    val dumperOptions = DumperOptions()
    dumperOptions.defaultFlowStyle = BLOCK
    val representer = Representer(dumperOptions)
    representer.addClassTag(SdkImportConfig::class.java, MAP)
    file.writer().use {
        Yaml(representer, dumperOptions).dump(this, it)
    }
}
