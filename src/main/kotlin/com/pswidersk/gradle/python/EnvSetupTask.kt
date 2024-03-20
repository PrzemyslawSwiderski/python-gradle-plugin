package com.pswidersk.gradle.python

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
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
    fun setup() {
        with(pythonPluginExtension) {
            val condaExec = condaExec.get().asFile.absolutePath
            val condaArgs = listOf(
                "create",
                "--name",
                pythonEnvName.get(),
                "python=${pythonVersion.get()}",
                "--yes"
            )
            logger.lifecycle("Executing command: $condaExec ${condaArgs.joinToString(" ")}")
            execOperations.exec {
                it.executable = condaExec
                it.args(condaArgs)
            }
        }
    }
}