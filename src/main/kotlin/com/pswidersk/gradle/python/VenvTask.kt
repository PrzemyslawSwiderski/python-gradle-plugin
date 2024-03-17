package com.pswidersk.gradle.python

import org.apache.tools.ant.types.Commandline
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.logging.LogLevel.LIFECYCLE
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.gradle.process.ExecOperations
import org.gradle.process.ExecResult
import javax.inject.Inject

abstract class VenvTask @Inject constructor(
    private val execOperations: ExecOperations,
    objects: ObjectFactory,
    projectLayout: ProjectLayout
) : DefaultTask() {

    private val pythonPluginExtension: PythonPluginExtension = project.pythonPlugin

    init {
        group = PLUGIN_TASKS_GROUP_NAME
        this.dependsOn("envSetup")
    }

    /**
     * Args to pass
     * For example: "install", "./main.py"
     *
     */
    @Internal
    var args: List<String> = emptyList()

    /**
     * Working directory
     *
     */
    @Internal
    val workingDir: DirectoryProperty = objects.directoryProperty().convention(projectLayout.projectDirectory)

    /**
     * Environment variables map
     */
    @Internal
    var environment: Map<String, Any> = mapOf()

    /**
     * Input file to be passed into standard input.
     */
    @Internal
    val inputFile: RegularFileProperty = objects.fileProperty()

    /**
     * Output file to be passed into standard output.
     */
    @Internal
    val outputFile: RegularFileProperty = objects.fileProperty()

    /**
     * Executable which have to exist in virtual env.
     * For example: "python", "pip", "wheel"
     *
     * Default: "python"
     */
    @Internal
    var venvExec: String = "python"

    /**
     * Parses an argument list from {@code args} and passes it to args.
     * It overrides any args passed to task in build script.
     *
     * <p>
     * The parser supports both single quote ({@code '}) and double quote ({@code "}) as quote delimiters.
     * For example, to pass the argument {@code foo bar}, use {@code "foo bar"}.
     * </p>
     * <p>
     * Note: the parser does <strong>not</strong> support using backslash to escape quotes. If this is needed,
     * use the other quote delimiter around it.
     * For example, to pass the argument {@code 'singly quoted'}, use {@code "'singly quoted'"}.
     * </p>
     *
     * @param args Args for the main class. Will be parsed into an argument list.
     * @return this
     */
    @Option(option = "args", description = "Command line arguments overriding execArgs.")
    fun setArgsByCmd(args: String) {
        this.args = Commandline.translateCommandline(args).toList()
    }

    @TaskAction
    fun execute(): ExecResult = with(pythonPluginExtension) {
        val allArgs = if (isWindows)
            listOf(
                "cmd",
                "/c",
                condaBinDir.get().asFile.resolve("activate.bat").absolutePath,
                pythonEnvName.get(),
                ">nul",
                "2>&1",
                "&&",
                venvExec
            ) + args
        else
            listOf(
                "sh", "-c", ". ${condaActivatePath.get()} >/dev/null 2>&1 && " +
                        "conda activate ${pythonEnvName.get()} >/dev/null 2>&1 && " +
                        "$venvExec ${args.joinToString(" ")}"
            )
        logger.lifecycle("Executing command: '${allArgs.joinToString(" ")}'")
        execOperations.exec {
            logging.captureStandardOutput(LIFECYCLE)
            it.commandLine(allArgs)
            it.workingDir(workingDir)
            it.environment(environment)
            if (inputFile.isPresent)
                it.standardInput = inputFile.get().asFile.inputStream()
            if (outputFile.isPresent)
                it.standardOutput = outputFile.get().asFile.outputStream()
        }
    }
}

