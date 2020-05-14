package com.pswidersk.gradle.python

import com.jetbrains.python.envs.PythonEnvsExtension
import com.jetbrains.python.envs.PythonEnvsPlugin
import com.pswidersk.gradle.python.utils.pythonPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.gradle.plugins.ide.idea.model.IdeaModel

class PythonPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.extensions.create(PYTHON_PLUGIN_EXTENSION_NAME, PythonPluginExtension::class.java, project)
        project.pluginManager.apply(IdeaPlugin::class.java)
        configureIdeaPlugin(project)
        project.afterEvaluate {
            project.pluginManager.apply(PythonEnvsPlugin::class.java)
            configurePythonEnvsPlugin(project)
        }
    }

    private fun configureIdeaPlugin(project: Project) = project.extensions.configure(IdeaModel::class.java) {
        with(it.module) {
            sourceDirs = mutableSetOf(project.pythonPlugin.pythonSrcDir)
            testSourceDirs = mutableSetOf(project.pythonPlugin.pythonTestDir)
        }
    }

    private fun configurePythonEnvsPlugin(project: Project) = project.extensions.configure(PythonEnvsExtension::class.java) {
        with(it) {
            bootstrapDirectory = project.pythonPlugin.bootstrapDir
            envsDirectory = project.pythonPlugin.vEnvsDir
            val pythonVersion = project.pythonPlugin.pythonVersion.get()
            python("python-$pythonVersion", pythonVersion)
            virtualenv("virtualenv-$pythonVersion", "python-$pythonVersion")
        }
    }

}
