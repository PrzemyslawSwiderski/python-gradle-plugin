package com.pswidersk.gradle.python

import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecResult
import java.io.File
import java.net.URL

open class MinicondaSetupTask : DefaultTask() {

    init {
        group = "python"
        description = "Setup Miniconda"
        onlyIf {
            !project.minicondaDir.exists()
        }
    }

    @TaskAction
    fun setup(): ExecResult = with(project) {
        minicondaDir.mkdirs()
        val minicondaInstaller = minicondaDir.resolve("Miniconda3-latest-$os-$arch.$exec")
        downloadMiniconda(minicondaInstaller)
        if (!Os.isFamily(Os.FAMILY_WINDOWS))
            exec {
                it.executable = "chmod"
                it.args("u+x", minicondaInstaller.absolutePath)
            }
        exec {
            it.executable = minicondaInstaller.absolutePath
            val execArgs = if (Os.isFamily(Os.FAMILY_WINDOWS))
                listOf("/InstallationType=JustMe", "/RegisterPython=0", "/S", "/D=${minicondaDir.absolutePath}")
            else
                listOf("-b", "-u", "-p", minicondaDir.absolutePath)
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