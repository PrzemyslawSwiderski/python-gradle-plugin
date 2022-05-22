package com.pswidersk.gradle.python

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecResult
import java.io.File
import java.net.URL

open class MinicondaSetupTask : DefaultTask() {

    init {
        group = "python"
        description = "Setup Miniconda"
        this.onlyIf {
            !project.condaBinDir.exists()
        }
    }

    @TaskAction
    fun setup(): ExecResult = with(project) {
        minicondaDir.mkdirs()
        val minicondaInstaller = minicondaDir.resolve("Miniconda3-$minicondaVersion-$os-$arch.$exec")
        downloadMiniconda(minicondaInstaller)
        allowInstallerExecution(minicondaInstaller)
        logger.lifecycle("Installing Miniconda...")
        exec {
            it.executable = minicondaInstaller.absolutePath
            val execArgs = if (isWindows)
                listOf(
                    "/InstallationType=JustMe",
                    "/RegisterPython=0",
                    "/AddToPath=0",
                    "/S",
                    "/D=${minicondaDir.absolutePath}"
                )
            else
                listOf("-b", "-u", "-p", minicondaDir.absolutePath)
            it.args(execArgs)
        }
    }

    private fun Project.allowInstallerExecution(minicondaInstaller: File) {
        if (!isWindows) {
            logger.lifecycle("Allowing user to run installer...")
            exec {
                it.executable = "chmod"
                it.args("u+x", minicondaInstaller.absolutePath)
            }
        }
    }

    private fun downloadMiniconda(minicondaFile: File) {
        logger.lifecycle("Downloading Miniconda to: ${minicondaFile.absolutePath}")
        val minicondaInputStream = URL("https://repo.anaconda.com/miniconda/${minicondaFile.name}").openStream()
        minicondaInputStream.use { inputStream ->
            minicondaFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
    }
}