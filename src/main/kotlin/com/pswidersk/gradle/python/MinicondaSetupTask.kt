package com.pswidersk.gradle.python

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import org.gradle.process.ExecResult
import java.io.File
import java.net.URL
import javax.inject.Inject

abstract class MinicondaSetupTask @Inject constructor(
    private val execOperations: ExecOperations,
) : DefaultTask() {

    private val pythonPluginExtension: PythonPluginExtension = project.pythonPlugin

    init {
        group = "python"
        description = "Setup $DEFAULT_MINICONDA_RELEASE"
        this.onlyIf {
            !pythonPluginExtension.condaBinDir.get().asFile.exists()
        }
    }

    @TaskAction
    fun setup(): ExecResult = with(pythonPluginExtension) {
        val minicondaDirFile = minicondaDir.get().asFile
        minicondaDirFile.mkdirs()
        val minicondaInstaller =
            minicondaDirFile.resolve("$DEFAULT_MINICONDA_RELEASE-${minicondaVersion.get()}-$os-$arch.$exec")
        downloadMiniconda(minicondaInstaller)
        allowInstallerExecution(minicondaInstaller)
        logger.lifecycle("Installing $DEFAULT_MINICONDA_RELEASE...")
        execOperations.exec {
            it.executable = minicondaInstaller.canonicalPath
            val execArgs = if (isWindows)
                listOf(
                    "/InstallationType=JustMe",
                    "/RegisterPython=0",
                    "/AddToPath=0",
                    "/S",
                    "/D=${minicondaDirFile.canonicalPath}"
                )
            else
                listOf("-b", "-u", "-p", minicondaDirFile.canonicalPath)
            it.args(execArgs)
        }
    }

    private fun allowInstallerExecution(minicondaInstaller: File) {
        if (!isWindows) {
            logger.lifecycle("Allowing user to run installer...")
            execOperations.exec {
                it.executable = "chmod"
                it.args("u+x", minicondaInstaller.canonicalPath)
            }
        }
    }

    private fun downloadMiniconda(minicondaFile: File) {
        logger.lifecycle("Downloading $DEFAULT_MINICONDA_RELEASE to: ${minicondaFile.canonicalPath}")
        val minicondaInputStream = URL("https://repo.anaconda.com/miniconda/${minicondaFile.name}").openStream()
        minicondaInputStream.use { inputStream ->
            minicondaFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
    }
}