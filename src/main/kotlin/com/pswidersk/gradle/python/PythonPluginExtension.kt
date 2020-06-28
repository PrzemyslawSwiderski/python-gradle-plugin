package com.pswidersk.gradle.python

import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject


open class PythonPluginExtension @Inject constructor(@Suppress("UNUSED_PARAMETER") project: Project,
                                                     objects: ObjectFactory) {

    val pythonVersion: Property<String> = objects.property<String>().convention("3.8.2")

}