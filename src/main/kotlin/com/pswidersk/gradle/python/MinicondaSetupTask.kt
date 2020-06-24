package com.pswidersk.gradle.python

import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecResult
import java.io.File
import java.net.URL

open class MinicondaSetupTask : DefaultTask() {

    private val miniCondaDir = project.pythonPlugin.minicondaDir

    init {
        group = "python"
        description = "Setup Miniconda"
        onlyIf {
            !miniCondaDir.exists()
        }
    }

    @TaskAction
    fun setup(): ExecResult = with(project) {
        miniCondaDir.mkdirs()
        val minicondaInstaller = miniCondaDir.resolve("Miniconda3-latest-$os-$arch.$exec")
        downloadMiniconda(minicondaInstaller)
        if (!Os.isFamily(Os.FAMILY_WINDOWS))
            exec {
                it.executable = "chmod"
                it.args("u+x", minicondaInstaller)
            }
        exec {
            it.executable = minicondaInstaller.path
            val execArgs = if (Os.isFamily(Os.FAMILY_WINDOWS))
                listOf("/InstallationType=JustMe", "/RegisterPython=0", "/S", "/D=${miniCondaDir.path}")
            else
                listOf("-b", "-u", "-p", miniCondaDir.path)
            it.args(execArgs)
        }
    }

    private fun downloadMiniconda(minicondaFile: File) {
        val minicondaInputStream = URL("https://repo.anaconda.com/miniconda/${minicondaFile.name}").openStream()
        minicondaInputStream.use { inputStream ->
            minicondaFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
    }
}