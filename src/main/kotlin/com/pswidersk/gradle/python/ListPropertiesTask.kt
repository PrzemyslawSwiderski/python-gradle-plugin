package com.pswidersk.gradle.python

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class ListPropertiesTask : DefaultTask() {

    private val pythonPluginExtension: PythonPluginExtension = project.pythonPlugin

    init {
        group = "python"
        description = "List basic plugin properties."
    }

    @TaskAction
    fun setup() {
        with(pythonPluginExtension) {
            logger.lifecycle(
                """
                Operating system: $os
                Arch: ${systemArch.get()}
                Install directory: ${installDir.get()}
                Python: ${pythonEnvName.get()}
                Python environment: ${pythonEnvDir.get()}
                Conda repo URL: ${condaRepoUrl.get()}
                ${condaInstaller.get()} version: ${condaVersion.get()}
                ${condaInstaller.get()} directory: ${condaDir.get()}
                Conda activate path: ${condaActivatePath.get()}
                Conda exec location: ${condaExec.get()}
            """.trimIndent()
            )
        }
    }
}