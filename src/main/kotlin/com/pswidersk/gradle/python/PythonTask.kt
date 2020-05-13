package com.pswidersk.gradle.python

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
     * Script path, relative to project.
     *
     * Default: main.py
     */
    @get:Input
    val script: Property<String> = project.objects.property(String::class.java).convention("main.py")

    /**
     * Working dir, relative to project root dir.
     *
     * Default: main
     */
    @get:Input
    val workDir: Property<String> = project.objects.property(String::class.java).convention(PYTHON_SRC_DIR)

    @TaskAction
    fun exec() {
        project.exec {
            with(it) {
                val workDir = project.projectDir.resolve(workDir.get())
                executable = project.pythonPlugin.pythonPath()
                workingDir = workDir
                environment("PYTHONPATH", workDir)
                args(script.get())
            }
        }
    }

}