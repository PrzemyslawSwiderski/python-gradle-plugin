package com.pswidersk.gradle.python

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import org.gradle.process.ExecResult
import javax.inject.Inject

abstract class EnvSetupTask @Inject constructor(
    private val execOperations: ExecOperations,
) : DefaultTask() {

    private val pythonPluginExtension: PythonPluginExtension = project.pythonPlugin

    init {
        group = "python"
        description = "Setup python env"
        this.onlyIf { !pythonPluginExtension.pythonEnvDir.get().asFile.exists() }
    }

    @TaskAction
    fun setup(): ExecResult = with(pythonPluginExtension) {
        execOperations.exec {
            it.executable = condaExec.get().asFile.canonicalPath
            it.args(listOf("create", "--name", pythonEnvName.get(), "python=${pythonVersion.get()}"))
        }
    }
}