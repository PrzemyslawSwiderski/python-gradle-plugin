package com.pswidersk.gradle.python

import com.jetbrains.python.envs.PythonEnvsPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class PythonPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.pluginManager.apply(PythonEnvsPlugin::class.java)
        project.extensions.create(PYTHON_PLUGIN_EXTENSION_NAME, PythonPluginExtension::class.java, project)
        project.tasks.register(PYTHON_SETUP_TASK_NAME, PythonSetupTask::class.java)
    }

}
