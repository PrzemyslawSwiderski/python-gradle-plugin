package com.pswidersk.gradle.python

import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject


private const val DEFAULT_PYTHON_VERSION = "3.10.4"

private const val DEFAULT_MINICONDA_VERSION = "latest"

open class PythonPluginExtension @Inject constructor(
    @Suppress("UNUSED_PARAMETER") project: Project,
    objects: ObjectFactory
) {

    val pythonVersion: Property<String> = objects.property<String>().convention(DEFAULT_PYTHON_VERSION)

    val minicondaVersion: Property<String> = objects.property<String>().convention(DEFAULT_MINICONDA_VERSION)

}