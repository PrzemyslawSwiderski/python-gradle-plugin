package com.pswidersk.gradle.python

import org.apache.commons.io.FileUtils.copyInputStreamToFile
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import org.gradle.process.ExecResult
import java.io.File
import java.net.URL
import java.net.URLConnection
import java.util.*
import javax.inject.Inject


abstract class CondaSetupTask @Inject constructor(
    private val execOperations: ExecOperations,
) : DefaultTask() {

    private val pythonPluginExtension: PythonPluginExtension = project.pythonPlugin

    init {
        group = "python"
        description = "Setup $DEFAULT_CONDA_INSTALLER"
        this.onlyIf {
            !pythonPluginExtension.condaBinDir.get().asFile.exists()
        }
    }

    @TaskAction
    fun setup(): ExecResult = with(pythonPluginExtension) {
        val condaDirFile = condaDir.get().asFile
        condaDirFile.mkdirs()
        val condaInstaller =
            condaDirFile.resolve("${condaInstaller.get()}-${condaVersion.get()}-$os-${systemArch.get()}.$exec")
        downloadConda(condaInstaller)
        allowInstallerExecution(condaInstaller)
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

    private fun allowInstallerExecution(condaInstaller: File) {
        if (!isWindows) {
            logger.lifecycle("Allowing user to run installer...")
            execOperations.exec {
                it.executable = "chmod"
                it.args("u+x", condaInstaller.canonicalPath)
            }
        }
    }

    private fun downloadConda(condaFile: File) {
        val condaRepoUrl = pythonPluginExtension.condaRepoUrl.get().dropLastWhile { it == '/' }
        val condaInstaller = pythonPluginExtension.condaInstaller.get()
        logger.lifecycle("Downloading $condaInstaller to: ${condaFile.canonicalPath} from: $condaRepoUrl (please wait, it can take a while)")
        val connection = URL("${condaRepoUrl}/${condaFile.name}").openConnection()
        addBasicAuth(connection)
        addCustomHeaders(connection)
        val condaInputStream = connection.getInputStream()
        copyInputStreamToFile(condaInputStream, condaFile)
    }

    private fun addBasicAuth(connection: URLConnection) {
        if (pythonPluginExtension.condaRepoUsername.isPresent) {
            val condaRepoUsername = pythonPluginExtension.condaRepoUsername.get()
            val condaRepoPassword = pythonPluginExtension.condaRepoPassword.get()
            logger.lifecycle("Adding basic authorization headers for '$condaRepoUsername' user.")
            val userAndPass = "$condaRepoUsername:$condaRepoPassword"
            val basicAuth = "Basic ${String(Base64.getEncoder().encode(userAndPass.toByteArray()))}"
            connection.setRequestProperty("Authorization", basicAuth)
        }
    }

    private fun addCustomHeaders(connection: URLConnection) {
        val condaRepoHeaders = pythonPluginExtension.condaRepoHeaders.get()
        condaRepoHeaders.forEach { connection.addRequestProperty(it.key, it.value) }
    }
}