package com.pswidersk.gradle.python

import com.pswidersk.gradle.python.sdkimport.SaveSdkImportConfigTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register

class PythonPlugin : Plugin<Project> {

    override fun apply(project: Project): Unit = with(project) {
        extensions.create(PYTHON_PLUGIN_EXTENSION_NAME, PythonPluginExtension::class.java, this)
        tasks.register<ListPropertiesTask>("listPluginProperties")
        val condaDownloadTask = tasks.register<CondaDownloadTask>("condaDownload")
        val condaSetupTask = tasks.register<CondaSetupTask>("condaSetup") {
            dependsOn(condaDownloadTask)
        }
        tasks.register<EnvSetupTask>("envSetup") {
            dependsOn(condaSetupTask)
            finalizedBy("saveSdkImportConfig")
        }
        registerSdkImportTasks()
    }

    private fun Project.registerSdkImportTasks() {
        val locatePythonTask = tasks.register<VenvTask>("locatePython") {
            inputs.property("uniquePythonDir", pythonPlugin.pythonEnvDir.get().asFile.path)
            val pythonEnvFileName = "${project.name}-pythonEnv.txt"
            val pythonEnvFile = temporaryDir.resolve(pythonEnvFileName)
            description = "Saves Python SDK reference to a temporary file"
            args = listOf("-c", "\"import sys;print(sys.executable);\"")
            outputFile.set(pythonEnvFile)
            outputs.file(pythonEnvFile)
        }

        tasks.register<SaveSdkImportConfigTask>("saveSdkImportConfig") {
            sdkConfigFile = pythonPlugin.ideaDir.file(SDK_IMPORT_FILE_NAME).get().asFile
            inputFile = locatePythonTask.get().outputFile.get().asFile
            dependsOn(locatePythonTask)
        }
    }

}
