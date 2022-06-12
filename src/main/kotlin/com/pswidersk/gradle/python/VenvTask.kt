package com.pswidersk.gradle.python

import org.apache.tools.ant.types.Commandline
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.gradle.process.ExecOperations
import org.gradle.process.ExecResult
import org.gradle.process.internal.streams.SafeStreams
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

abstract class VenvTask @Inject constructor(
    private val execOperations: ExecOperations,
    objects: ObjectFactory,
    projectLayout: ProjectLayout
) : DefaultTask() {

    @Internal
    val pythonPluginExtension: PythonPluginExtension = project.pythonPlugin

    init {
        group = PLUGIN_TASKS_GROUP_NAME
        this.dependsOn("envSetup")
        notCompatibleWithConfigurationCache("Input and Output streams are disallowed fields for config cache.")
    }

    /**
     * Args to pass
     * For example: "install", "./main.py"
     *
     */
    @Input
    var args: List<String> = emptyList()

    /**
     * Working directory
     *
     */
    @Input
    val workingDir: DirectoryProperty = objects.directoryProperty().convention(projectLayout.projectDirectory)

    @Input
    var environment: Map<String, Any> = mapOf()

    @Input
    var standardInput: InputStream = SafeStreams.emptyInput()

    @Input
    var standardOutput: OutputStream = SafeStreams.systemOut()

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

    /**
     * Executable which have to exist in virtual env.
     * For example: "python", "pip", "wheel"
     *
     * Default: "python"
     */
    @Input
    var venvExec: String = "python"

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
            it.commandLine(allArgs)
            it.workingDir(workingDir)
            it.environment(environment)
            it.standardInput = standardInput
            it.standardOutput = standardOutput
        }
    }
}

