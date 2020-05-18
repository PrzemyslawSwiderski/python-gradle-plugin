package com.pswidersk.gradle.python

import com.pswidersk.gradle.python.utils.property
import com.pswidersk.gradle.python.utils.pythonPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

open class PipTask : DefaultTask() {

    init {
        group = PLUGIN_TASKS_GROUP_NAME
        dependsOn(BUILD_ENVS_TASK_NAME)
    }

    /**
     * Execution args.
     * For example 'listOf("install", "-r", "requirements.txt")'
     * Must be present.
     */
    @get:Input
    val execArgs: ListProperty<String> = project.objects.listProperty(String::class.java)

    /**
     * Working dir, relative to project root dir.
     *
     * Default: main
     */
    @get:Input
    val workDir: Property<String> = project.objects.property<String>().convention(project.projectDir.absolutePath)

    @TaskAction
    fun exec() {
        project.exec {
            with(it) {
                val workDir = project.projectDir.resolve(workDir.get())
                executable = project.pythonPlugin.pipPath()
                workingDir = workDir
                environment("PYTHONPATH", workDir)
                args(execArgs.get())
            }
        }
    }

}