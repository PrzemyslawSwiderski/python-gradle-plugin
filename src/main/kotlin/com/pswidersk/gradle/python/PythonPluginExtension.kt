package com.pswidersk.gradle.python

import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject


open class PythonPluginExtension @Inject constructor(
    @Suppress("UNUSED_PARAMETER") project: Project,
    objects: ObjectFactory
) {

    val pythonVersion: Property<String> = objects.property<String>().convention(DEFAULT_PYTHON_VERSION)

    val minicondaVersion: Property<String> = objects.property<String>().convention(DEFAULT_MINICONDA_VERSION)

    val minicondaRepoUrl: Property<String> = objects.property<String>().convention(DEFAULT_MINICONDA_REPO_URL)

    val minicondaRepoUsername: Property<String> = objects.property<String>().convention("")

    val minicondaRepoPassword: Property<String> = objects.property<String>().convention("")

}