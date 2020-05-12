package com.pswidersk.gradle.python

import com.jetbrains.python.envs.PythonEnvsExtension
import org.apache.tools.ant.taskdefs.condition.Os
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
     * Script path, relative to project
     *
     * Default: main.py
     */
    @get:Input
    var script: Property<String> = project.objects.property(String::class.java).convention("main.py")

    /**
     * Working dir, relative to project root dir.
     *
     * Default: main
     */
    @get:Input
    var workDir: Property<String> = project.objects.property(String::class.java).convention("main")

    @TaskAction
    fun exec() {
        val path = project.extensions.getByType(PythonEnvsExtension::class.java).virtualEnvs.first().envDir
        val pythonPath = if (Os.isFamily(Os.FAMILY_WINDOWS))
            path.resolve("Scripts").resolve("python.exe").absolutePath
        else
            path.resolve("bin").resolve("python").absolutePath
        project.exec {
            with(it) {
                executable = pythonPath
                workingDir = project.projectDir.resolve(workDir.get())
                environment("PYTHONPATH", project.projectDir.resolve("main"))
                args(script.get())
            }
        }
    }

}