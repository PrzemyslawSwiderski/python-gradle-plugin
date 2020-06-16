package com.pswidersk.gradle.python

import com.jetbrains.python.envs.PythonEnvsPlugin
import com.pswidersk.gradle.python.utils.pythonEnvs
import com.pswidersk.gradle.python.utils.pythonPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class PythonPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.extensions.create(PYTHON_PLUGIN_EXTENSION_NAME, PythonPluginExtension::class.java, project)
        project.afterEvaluate {
            project.pluginManager.apply(PythonEnvsPlugin::class.java)
            configurePythonEnvsPlugin(project)
        }
    }

    private fun configurePythonEnvsPlugin(project: Project) = with(project.pythonEnvs) {
        bootstrapDirectory = project.pythonPlugin.bootstrapDir
        envsDirectory = project.pythonPlugin.vEnvsDir
        val pythonVersion = project.pythonPlugin.pythonVersion.get()
        python("python-$pythonVersion", pythonVersion)
        virtualenv("virtualenv-$pythonVersion", "python-$pythonVersion")
    }

}
