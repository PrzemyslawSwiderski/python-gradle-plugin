package com.pswidersk.gradle.python

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import org.gradle.process.ExecResult
import javax.inject.Inject

abstract class CondaSetupTask @Inject constructor(
    private val execOperations: ExecOperations,
) : DefaultTask() {

    private val pythonPluginExtension: PythonPluginExtension = project.pythonPlugin

    init {
        group = "python"
        description = "Setup ${pythonPluginExtension.condaInstaller.get()}"
        this.onlyIf {
            !pythonPluginExtension.condaBinDir.get().asFile.exists()
        }
    }

    @TaskAction
    fun setup(): ExecResult = with(pythonPluginExtension) {
        val condaDirFile = condaDir.get().asFile
        val condaInstaller = condaInstallerFile.get().asFile
        logger.lifecycle("Installing ${this.condaInstaller.get()}...")
        execOperations.exec {
            it.executable = condaInstaller.canonicalPath
            val execArgs = if (isWindows)
                listOf(
                    "/InstallationType=JustMe",
                    "/RegisterPython=0",
                    "/AddToPath=0",
                    "/S",
                    "/D=${condaDirFile.canonicalPath}"
                )
            else
                listOf("-b", "-u", "-p", condaDirFile.canonicalPath)
            it.args(execArgs)
        }
    }

}