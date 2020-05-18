package com.pswidersk.gradle.python

import com.pswidersk.gradle.python.utils.property
import com.pswidersk.gradle.python.utils.pythonEnvs
import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject


open class PythonPluginExtension @Inject constructor(private val project: Project,
                                                     objects: ObjectFactory) {

    val pythonVersion: Property<String> = objects.property<String>().convention("3.8.2")

    private val pythonEnvsDir = project.rootDir.resolve(GRADLE_FILES_DIR).resolve(PYTHON_ENVS_DIR)

    internal val bootstrapDir = pythonEnvsDir.resolve(PYTHON_BOOTSTRAP_DIR)

    internal val vEnvsDir = pythonEnvsDir.resolve(PYTHON_VENVS_DIR)

    internal fun pythonPath(): String {
        val path = project.pythonEnvs.virtualEnvs.first().envDir
        return if (Os.isFamily(Os.FAMILY_WINDOWS))
            path.resolve("Scripts").resolve("python.exe").absolutePath
        else
            path.resolve("bin").resolve("python").absolutePath

    }

    internal fun pipPath(): String {
        val path = project.pythonEnvs.virtualEnvs.first().envDir
        return if (Os.isFamily(Os.FAMILY_WINDOWS))
            path.resolve("Scripts").resolve("pip.exe").absolutePath
        else
            path.resolve("bin").resolve("pip").absolutePath

    }
}