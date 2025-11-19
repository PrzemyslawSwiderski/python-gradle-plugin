package com.pswidersk.gradle.python

import com.pswidersk.gradle.python.sdkimport.SaveSdkImportConfigTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register

class PythonPlugin : Plugin<Project> {

    override fun apply(project: Project): Unit = with(project) {
        val extension = extensions.create(PYTHON_PLUGIN_EXTENSION_NAME, PythonPluginExtension::class.java, this)

        tasks.register<ListPropertiesTask>("listPluginProperties")
        val condaDownloadTask = tasks.register<CondaDownloadTask>("condaDownload") {
            doFirst {
                extension.installDir.get().asFile.mkdirs()
            }
        }
        val condaSetupTask = tasks.register<CondaSetupTask>("condaSetup") {
            dependsOn(condaDownloadTask)
        }
        tasks.register<EnvSetupTask>("envSetup") {
            dependsOn(condaSetupTask)
            finalizedBy("saveSdkImportConfig")
        }
        registerSdkImportTasks(extension)
    }

    private fun Project.registerSdkImportTasks(pythonExtension: PythonPluginExtension) {
        val locatePythonTask = tasks.register<VenvTask>("locatePython") {
            inputs.property("uniquePythonDir", pythonExtension.pythonEnvDir.get().asFile.path)
            val pythonEnvFileName = "${project.name}-pythonEnv.txt"
            val pythonEnvFile = temporaryDir.resolve(pythonEnvFileName)
            description = "Saves Python SDK reference to a temporary file"
            args = listOf("-c", "\"import sys;print(sys.executable);\"")
            outputFile.set(pythonEnvFile)
            outputs.file(pythonEnvFile)
        }

        tasks.register<SaveSdkImportConfigTask>("saveSdkImportConfig") {
            sdkConfigFile = pythonExtension.ideaDir.file(SDK_IMPORT_FILE_NAME).get().asFile
            inputFile = locatePythonTask.get().outputFile.get().asFile
            dependsOn(locatePythonTask)
            onlyIf { pythonExtension.ideaDir.get().asFile.exists() }
        }
    }

}
