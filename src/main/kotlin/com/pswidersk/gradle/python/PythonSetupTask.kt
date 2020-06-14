package com.pswidersk.gradle.python

import com.pswidersk.gradle.python.utils.pythonEnvs
import com.pswidersk.gradle.python.utils.pythonPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Task which configures python env plugin and installs configured python virtual env.
 */
open class PythonSetupTask : DefaultTask() {

    init {
        group = PLUGIN_TASKS_GROUP_NAME
        dependsOn(BUILD_ENVS_TASK_NAME)
    }

    @TaskAction
    fun setup() {
        with(project.pythonEnvs) {
            bootstrapDirectory = project.pythonPlugin.bootstrapDir
            envsDirectory = project.pythonPlugin.vEnvsDir
            val pythonVersion = project.pythonPlugin.pythonVersion.get()
            python("python-$pythonVersion", pythonVersion)
            virtualenv("virtualenv-$pythonVersion", "python-$pythonVersion")
        }
    }
}