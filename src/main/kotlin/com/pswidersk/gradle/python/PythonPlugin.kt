package com.pswidersk.gradle.python

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*

class PythonPlugin : Plugin<Project> {

    override fun apply(project: Project): Unit = with(project) {
        val extension = extensions.create<PythonPluginExtension>(PYTHON_PLUGIN_EXTENSION_NAME)
        tasks.register<ListPropertiesTask>("listPluginProperties") { }
        val minicondaSetupTask = tasks.register<MinicondaSetupTask>("minicondaSetup")
        tasks.register<EnvSetupTask>("envSetup") {
            dependsOn(minicondaSetupTask)
        }
        tasks.withType<EnvSetupTask>().configureEach {
            condaExec.convention(extension.condaExec)
            pythonEnvName.convention(extension.pythonEnvName)
            pythonVersion.convention(extension.pythonVersion)
            pythonEnvDir.convention(extension.pythonEnvDir)
        }
    }

}
