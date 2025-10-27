package com.pswidersk.gradle.python

import org.apache.commons.io.input.ObservableInputStream
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.net.URI
import java.net.URLConnection
import java.util.*

abstract class CondaDownloadTask : DefaultTask() {

    private val pythonPlugin: PythonPluginExtension = project.pythonPlugin
    private val progressLogger = ProgressLogger(consumer = { msg -> logger.lifecycle(msg) })

    init {
        group = "python"
        description = "Download ${pythonPlugin.condaInstaller.get()}"
        this.onlyIf {
            !pythonPlugin.condaBinDir.get().asFile.exists()
        }
    }

    @TaskAction
    fun setup() {
        with(pythonPlugin) {
            val condaInstaller = condaInstallerFile.get()
            downloadConda(condaInstaller.asFile)
        }
    }

    private fun downloadConda(destinationFile: File) {
        val condaRepoUrl = pythonPlugin.condaRepoUrl.get().dropLastWhile { it == '/' }
        val condaInstaller = pythonPlugin.condaInstaller.get()
        val condaVersion = pythonPlugin.condaVersion.get()
        logger.lifecycle("Downloading $condaInstaller to: ${destinationFile.canonicalPath} from: $condaRepoUrl (please wait, it can take a while)")
        val connection = URI.create("$condaRepoUrl/$condaVersion/${destinationFile.name}").toURL().openConnection()
        addBasicAuth(connection)
        addCustomHeaders(connection)
        getExecutable(connection, destinationFile)
    }

    private fun addBasicAuth(connection: URLConnection) {
        if (pythonPlugin.condaRepoUsername.isPresent) {
            val condaRepoUsername = pythonPlugin.condaRepoUsername.get()
            val condaRepoPassword = pythonPlugin.condaRepoPassword.get()
            logger.lifecycle("Adding basic authorization headers for '$condaRepoUsername' user.")
            val userAndPass = "$condaRepoUsername:$condaRepoPassword"
            val basicAuth = "Basic ${String(Base64.getEncoder().encode(userAndPass.toByteArray()))}"
            connection.setRequestProperty("Authorization", basicAuth)
        }
    }

    private fun addCustomHeaders(connection: URLConnection) {
        val condaRepoHeaders = pythonPlugin.condaRepoHeaders.get()
        condaRepoHeaders.forEach { connection.addRequestProperty(it.key, it.value) }
    }

    private fun getExecutable(connection: URLConnection, destinationFile: File) {
        val progressObserver = ProgressObserver(progressLogger, connection)
        val urlInputStream = ObservableInputStream(connection.getInputStream().buffered(), progressObserver)

        urlInputStream.use { input ->
            destinationFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        val res = destinationFile.setExecutable(true)
        if (!res) {
            logger.warn("Conda exec file could not be changed to executable.")
        }
    }

}
