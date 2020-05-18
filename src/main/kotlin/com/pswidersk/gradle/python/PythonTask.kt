package com.pswidersk.gradle.python

import com.pswidersk.gradle.python.utils.property
import com.pswidersk.gradle.python.utils.pythonPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

open class PythonTask : DefaultTask() {

    init {
        group = PLUGIN_TASKS_GROUP_NAME
        dependsOn(BUILD_ENVS_TASK_NAME)
    }

    /**
     * Arguments string, for example script path, relative to project.
     * For example: main.py
     * Must be present.
     *
     */
    @get:Input
    val execArgs: Property<String> = project.objects.property()

    /**
     * Working dir, relative to project root dir.
     *
     * Default: main
     */
    @get:Input
    val workDir: Property<String> = project.objects.property<String>().convention(PYTHON_SRC_DIR)

    @TaskAction
    fun exec() {
        project.exec {
            with(it) {
                val workDir = project.projectDir.resolve(workDir.get())
                executable = project.pythonPlugin.pythonPath()
                workingDir = workDir
                environment("PYTHONPATH", workDir)
                args(execArgs.get())
            }
        }
    }

}