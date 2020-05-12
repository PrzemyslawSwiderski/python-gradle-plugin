package com.pswidersk.gradle.python

import com.jetbrains.python.envs.PythonEnvsExtension
import com.jetbrains.python.envs.PythonEnvsPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.gradle.plugins.ide.idea.model.IdeaModel

class PythonPlugin : Plugin<Project> {

    override fun apply(project: Project) {
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
                    sourceDirs = mutableSetOf(project.projectDir.resolve(PYTHON_SRC_DIR))
                    testSourceDirs = mutableSetOf(project.projectDir.resolve(PYTHON_TEST_DIR))
                }
            }

    private fun configurePythonEnvsPlugin(project: Project) = project.extensions.configure(PythonEnvsExtension::class.java) {
        with(it) {
            bootstrapDirectory = project.rootDir.resolve(GRADLE_FILES_DIR).resolve(PYTHON_ENVS_DIR).resolve(PYTHON_BOOTSTRAP_DIR)
            envsDirectory = project.rootDir.resolve(GRADLE_FILES_DIR).resolve(PYTHON_ENVS_DIR).resolve(PYTHON_VENVS_DIR)
            python("python-3.8.2", "3.8.2")
            virtualenv("virtualenv-3.8.2", "python-3.8.2")
        }
    }


}