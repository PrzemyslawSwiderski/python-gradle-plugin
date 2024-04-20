package com.pswidersk.gradle.python.sdkimport

import com.pswidersk.gradle.python.PythonPluginExtension
import com.pswidersk.gradle.python.SDK_IMPORT_FILE_NAME
import com.pswidersk.gradle.python.pythonPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class SaveSdkImportConfigTask : DefaultTask() {

    private val pythonPluginExtension: PythonPluginExtension = project.pythonPlugin

    init {
        group = "python"
        description = "Saves SDK reference to `$SDK_IMPORT_FILE_NAME` file."

        this.onlyIf {
            pythonPluginExtension.ideaDir.get().asFile.exists()
        }
    }

    private val moduleName = project.name

    @get:OutputFile
    lateinit var sdkConfigFile: File

    @get:InputFile
    lateinit var inputFile: File

    @TaskAction
    fun setup() {
        val pythonEnvs = inputFile.readLines()
        val pythonSdkPath = pythonEnvs.firstOrNull()

        if (pythonSdkPath != null) {
            logger.info("Saving python SDK reference: $pythonSdkPath")
            updateSdkConfig(sdkConfigFile, pythonSdkPath, moduleName, logger)
        } else {
            logger.warn("No python SDK was found.")
        }
    }

}
