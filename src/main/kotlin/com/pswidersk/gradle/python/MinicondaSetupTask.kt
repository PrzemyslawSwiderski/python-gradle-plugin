package com.pswidersk.gradle.python

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecResult
import java.io.File
import java.net.URL
import java.net.URLConnection
import java.util.*


open class MinicondaSetupTask : DefaultTask() {

    init {
        group = "python"
        description = "Setup $DEFAULT_MINICONDA_RELEASE"
        this.onlyIf {
            !project.condaBinDir.exists()
        }
    }

    @TaskAction
    fun setup(): ExecResult = with(project) {
        minicondaDir.mkdirs()
        val minicondaInstaller = minicondaDir.resolve("$DEFAULT_MINICONDA_RELEASE-$minicondaVersion-$os-$arch.$exec")
        downloadMiniconda(minicondaRepoUrl, minicondaRepoUsername, minicondaRepoPassword, minicondaInstaller)
        allowInstallerExecution(minicondaInstaller)
        logger.lifecycle("Installing $DEFAULT_MINICONDA_RELEASE...")
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

    private fun downloadMiniconda(
        minicondaRepoUrl: String,
        minicondaRepoUsername: String,
        minicondaRepoPassword: String,
        minicondaFile: File
    ) {
        logger.lifecycle("Downloading $DEFAULT_MINICONDA_RELEASE to: ${minicondaFile.absolutePath} from: $minicondaRepoUrl")
        val connection = URL("${minicondaRepoUrl}/${minicondaFile.name}").openConnection()
        if (minicondaRepoUsername.isNotBlank()) {
            addBasicAuth(minicondaRepoUsername, minicondaRepoPassword, connection)
        }
        val minicondaInputStream = connection.getInputStream()
        minicondaInputStream.use { inputStream ->
            minicondaFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
    }

    private fun addBasicAuth(
        minicondaRepoUsername: String,
        minicondaRepoPassword: String,
        connection: URLConnection
    ) {
        val userpass = "$minicondaRepoUsername:$minicondaRepoPassword"
        val basicAuth = "Basic " + String(Base64.getEncoder().encode(userpass.toByteArray()))
        connection.setRequestProperty("Authorization", basicAuth)
    }
}