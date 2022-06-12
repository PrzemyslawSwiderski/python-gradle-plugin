package com.pswidersk.gradle.python

import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import org.gradle.process.ExecResult

abstract class EnvSetupTask @Inject constructor(
    private val execOperations: ExecOperations,
) : DefaultTask() {

    @get:InputFile
    abstract val condaExec: RegularFileProperty
    @get:Input
    abstract val pythonEnvName: Property<String>
    @get:Input
    abstract val pythonVersion: Property<String>

    @get:OutputDirectory
    abstract val pythonEnvDir: DirectoryProperty

    init {
        group = "python"
        description = "Setup python env"
    }

    @TaskAction
    fun setup(): ExecResult {
        val condaExec = condaExec.get().asFile.canonicalPath
        val pythonVersion = pythonVersion.get()
        val pythonEnvName = pythonEnvName.get()

        return execOperations.exec {
            executable = condaExec
            args(listOf("create", "--name", pythonEnvName, "python=${pythonVersion}"))
        }
    }
}
