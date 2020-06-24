package com.pswidersk.gradle.python

import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject


open class PythonPluginExtension @Inject constructor(project: Project,
                                                     objects: ObjectFactory) {

    val pythonVersion: Property<String> = objects.property<String>().convention("3.8.2")

    private val pythonBaseDir = project.rootDir.resolve(GRADLE_FILES_DIR).resolve(PYTHON_ENVS_DIR)

    internal val minicondaDir = pythonBaseDir.resolve(PYTHON_MINICONDA_DIR)

}