package com.pswidersk.gradle.python

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecResult

open class EnvSetupTask : DefaultTask() {

    init {
        group = "python"
        description = "Setup python env"
        this.onlyIf { !project.pythonEnvDir.exists() }
    }

    @TaskAction
    fun setup(): ExecResult = with(project) {
        val pythonVersion = project.pythonPlugin.pythonVersion.get()
        exec {
            it.executable = condaExec
            it.args(listOf("create", "--name", pythonEnvName, "python=$pythonVersion"))
        }
    }

}