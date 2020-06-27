package com.pswidersk.gradle.python

import org.apache.tools.ant.taskdefs.condition.Os
import org.apache.tools.ant.types.Commandline
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.gradle.process.ExecResult
import org.gradle.process.internal.streams.SafeStreams
import java.io.InputStream
import java.io.OutputStream

open class VenvTask : DefaultTask() {

    init {
        group = PLUGIN_TASKS_GROUP_NAME
        dependsOn("envSetup")
    }

    /**
     * Args to pass
     * For example: "install", "./main.py"
     *
     */
    @Input
    var args: List<String> = emptyList()

    @Input
    var main: String = "sh"

    @Input
    var arg1: String = "-c"

    @Input
    var arg2: String = "source"

    @Input
    var arg3: String = "."

    @Input
    var arg4: String = "."


    /**
     * Working directory
     *
     */
    @InputDirectory
    val workingDir: DirectoryProperty = project.objects.directoryProperty().convention(project.layout.projectDirectory)

    /**
     * Args to pass
     * For example: "install", "./main.py"
     *
     */
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

    @Option(option = "main", description = "Command line arguments overriding execArgs.")
    fun setArgsByCmd2(args: String) {
        this.main = args
    }

    @Option(option = "arg1", description = "Command line arguments overriding execArgs.")
    fun setArgsByCmd3(args: String) {
        this.arg1 = args
    }

    @Option(option = "arg2", description = "Command line arguments overriding execArgs.")
    fun setArgsByCmd5(args: String) {
        this.arg2 = args
    }

    @Option(option = "arg3", description = "Command line arguments overriding execArgs.")
    fun setArgsByCmd6(args: String) {
        this.arg3 = args
    }

    @Option(option = "arg4", description = "Command line arguments overriding execArgs.")
    fun setArgsByCmd7(args: String) {
        this.arg4 = args
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
    fun execute(): ExecResult = with(project) {
        return exec {
            logger.quiet("args: $args")
            logger.quiet("main: $main")
            val args = if (Os.isFamily(Os.FAMILY_WINDOWS))
                listOf("cmd", "/c", condaBinDir.resolve("activate.bat").absolutePath, pythonEnvName, ">nul", "&&", venvExec) + args
            else
                listOf(main, arg1, arg2, arg3, arg4)
//                listOf("sh", "-c", "'. $condaActivatePath $pythonEnvName >null && $venvExec ${args.joinToString(" ")}'")
//                listOf("sh", "-c", "'. $condaActivatePath $pythonEnvName >null && $venvExec ${args.joinToString(" ")}'")
            it.commandLine(args)
            it.workingDir(workingDir)
            it.environment(environment)
            it.standardInput = standardInput
            it.standardOutput = standardOutput
        }
    }
}

