package com.pswidersk.gradle.python

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register

class PythonPlugin : Plugin<Project> {

    override fun apply(project: Project): Unit = with(project) {
        extensions.create(PYTHON_PLUGIN_EXTENSION_NAME, PythonPluginExtension::class.java)
        tasks.register<ListPropertiesTask>("listPluginProperties")
        val condaSetupTask = tasks.register<CondaSetupTask>("condaSetup")
        tasks.register<EnvSetupTask>("envSetup") {
            dependsOn(condaSetupTask)
        }
    }

}
