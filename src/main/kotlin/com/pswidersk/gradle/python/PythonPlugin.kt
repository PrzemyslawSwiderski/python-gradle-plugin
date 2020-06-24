package com.pswidersk.gradle.python

import org.gradle.api.Plugin
import org.gradle.api.Project

class PythonPlugin : Plugin<Project> {

    override fun apply(project: Project): Unit = with(project) {
        extensions.create(PYTHON_PLUGIN_EXTENSION_NAME, PythonPluginExtension::class.java, project)
        val minicondaSetupTask = tasks.register("minicondaSetup", MinicondaSetupTask::class.java)
        tasks.register("envSetup", EnvSetupTask::class.java) {
            it.dependsOn(minicondaSetupTask)
        }
    }

}
