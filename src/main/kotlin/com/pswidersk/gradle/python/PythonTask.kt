package com.pswidersk.gradle.python

import com.pswidersk.gradle.python.utils.property
import com.pswidersk.gradle.python.utils.pythonPlugin
import org.apache.tools.ant.types.Commandline
import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

open class PythonTask : DefaultTask() {

    init {
        group = PLUGIN_TASKS_GROUP_NAME
        dependsOn(BUILD_ENVS_TASK_NAME)
    }

    /**
     * Parses an argument list from {@code args} and passes it to execArgs.
     * It overrides any execArgs passed to task in build script.
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
    fun setArgsByCmd(args: String) = this.args.set(Commandline.translateCommandline(args).toList())

    /**
     * Arguments list, for example script path, relative to project.
     *
     * For example: listOf("main.py","--arg1='1'")
     * Must be present.
     *
     */
    @get:Input
    val args: ListProperty<String> = project.objects.listProperty(String::class.java)

    /**
     * Working dir, relative to project root dir.
     *
     * Default: main
     */
    @get:Input
    val workDir: Property<String> = project.objects.property<String>().convention(PYTHON_SRC_DIR)

    /**
     * Map with environment variables to be passed during execution.
     *
     */
    @get:Input
    val environment: MapProperty<String, String> = project.objects.mapProperty(String::class.java, String::class.java)

    @TaskAction
    fun exec() {
        project.exec { execSpec ->
            val workDir = project.projectDir.resolve(workDir.get())
            execSpec.executable = project.pythonPlugin.pythonPath()
            execSpec.workingDir = workDir
            execSpec.environment("PYTHONPATH", workDir)
            execSpec.environment.putAll(environment.get())
            execSpec.args(args.get())
        }
    }

}