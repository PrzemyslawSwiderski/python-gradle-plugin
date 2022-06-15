package com.pswidersk.gradle.python

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import org.gradle.process.ExecResult
import java.io.File
import java.net.URL
import javax.inject.Inject
import java.net.URLConnection
import java.util.*


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
        downloadMiniconda(
            minicondaRepoUrl,
            minicondaRepoUsername,
            minicondaRepoPassword,
            minicondaRepoHeaders,
            minicondaInstaller
        )
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

    private fun downloadMiniconda(
        minicondaRepoUrl: String,
        minicondaRepoUsername: String,
        minicondaRepoPassword: String,
        minicondaRepoHeaders: Map<String, String>,
        minicondaFile: File
    ) {
        logger.lifecycle("Downloading $DEFAULT_MINICONDA_RELEASE to: ${minicondaFile.canonicalPath} from: $minicondaRepoUrl")
        val connection = URL("${minicondaRepoUrl}/${minicondaFile.name}").openConnection()
        if (minicondaRepoUsername.isNotBlank()) {
            addBasicAuth(minicondaRepoUsername, minicondaRepoPassword, connection)
        }
        minicondaRepoHeaders.forEach { connection.addRequestProperty(it.key, it.value) }
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
        logger.lifecycle("Adding basic authorization headers for '$minicondaRepoUsername' user.")
        val userAndPass = "$minicondaRepoUsername:$minicondaRepoPassword"
        val basicAuth = "Basic ${String(Base64.getEncoder().encode(userAndPass.toByteArray()))}"
        connection.setRequestProperty("Authorization", basicAuth)
    }
}