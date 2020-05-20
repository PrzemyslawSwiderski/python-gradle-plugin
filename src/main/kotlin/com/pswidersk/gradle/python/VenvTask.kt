package com.pswidersk.gradle.python

import com.pswidersk.gradle.python.utils.getExecInVenvPath
import org.apache.tools.ant.types.Commandline
import org.gradle.api.tasks.AbstractExecTask
import org.gradle.api.tasks.options.Option

open class VenvTask : AbstractExecTask<VenvTask>(VenvTask::class.java) {

    init {
        group = PLUGIN_TASKS_GROUP_NAME
        dependsOn(BUILD_ENVS_TASK_NAME)
        executable = project.getExecInVenvPath("python")
    }

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
    fun setArgsByCmd(args: String) = this.setArgs(Commandline.translateCommandline(args).toList())

    /**
     * Executable which have to exist in virtual env.
     * For example: "python", "pip", "wheel"
     *
     * Default: "python"
     */
    var venvExec: String = "python"
        set(value) {
            this.executable = project.getExecInVenvPath(value)
            field = value
        }
}