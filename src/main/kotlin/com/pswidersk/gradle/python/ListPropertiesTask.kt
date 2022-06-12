package com.pswidersk.gradle.python

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.TaskAction

abstract class ListPropertiesTask : DefaultTask() {

    @get:Nested
    val pythonPluginExtension: PythonPluginExtension = project.pythonPlugin

    init {
        group = "python"
        description = "List basic plugin properties."
    }

    @TaskAction
    fun setup() = with(pythonPluginExtension) {
        logger.lifecycle(
            """
                Operating system: $os
                Arch: $arch
                Install directory: ${installDir.get()}
                Python: ${pythonEnvName.get()}
                Python environment: ${pythonEnvDir.get()}
                $DEFAULT_MINICONDA_RELEASE version: ${minicondaVersion.get()}
                $DEFAULT_MINICONDA_RELEASE directory: ${minicondaDir.get()}
                Conda activate path: ${condaActivatePath.get()}
                Conda exec location: ${condaExec.get()}
            """.trimIndent()
        )
    }

}
