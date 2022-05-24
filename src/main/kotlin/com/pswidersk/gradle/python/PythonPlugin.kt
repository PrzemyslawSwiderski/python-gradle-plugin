package com.pswidersk.gradle.python

import org.gradle.api.Plugin
import org.gradle.api.Project

class PythonPlugin : Plugin<Project> {

    override fun apply(project: Project): Unit = with(project) {
        extensions.create(PYTHON_PLUGIN_EXTENSION_NAME, PythonPluginExtension::class.java, project)
        tasks.register("listPluginProperties") {
            group = "python"
            description = "List basic plugin properties."
            it.doFirst {
                logger.lifecycle(
                    """
                Operating system: $os
                Arch: $arch
                Python: $pythonEnvName
                Python environment: $pythonEnvDir
                $DEFAULT_MINICONDA_RELEASE version: $minicondaVersion
                $DEFAULT_MINICONDA_RELEASE directory: $minicondaDir
                Conda activate path: $condaActivatePath
                Conda exec location: $condaExec
            """.trimIndent()
                )
            }
        }
        val minicondaSetupTask = tasks.register("minicondaSetup", MinicondaSetupTask::class.java)
        tasks.register("envSetup", EnvSetupTask::class.java) {
            it.dependsOn(minicondaSetupTask)
        }
    }

}
