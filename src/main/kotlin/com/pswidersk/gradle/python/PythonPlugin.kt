package com.pswidersk.gradle.python

import com.jetbrains.python.envs.PythonEnvsExtension
import com.jetbrains.python.envs.PythonEnvsPlugin
import com.pswidersk.gradle.python.utils.pythonPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.gradle.plugins.ide.idea.model.IdeaModel

class PythonPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        project.extensions.add(PYTHON_PLUGIN_EXTENSION_NAME, PythonPluginExtension(project))

        addPlugins(project)
        configureIdeaPlugin(project)
        configurePythonEnvsPlugin(project)
    }

    private fun addPlugins(project: Project) {
        val additionalPlugins = listOf(
                IdeaPlugin::class.java,
                JavaPlugin::class.java,
                PythonEnvsPlugin::class.java
        )
        additionalPlugins.forEach { project.pluginManager.apply(it) }
    }

    private fun configureIdeaPlugin(project: Project) =
            project.extensions.configure(IdeaModel::class.java) {
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
