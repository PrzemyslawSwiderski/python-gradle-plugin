package com.pswidersk.gradle.python.utils

import com.jetbrains.python.envs.PythonEnvsExtension
import com.pswidersk.gradle.python.PythonPluginExtension
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property


/**
 * Creates a [Property] to hold values of the given type.
 *
 * @param T the type of the property
 * @return the property
 */
internal inline fun <reified T : Any> ObjectFactory.property(): Property<T> =
        property(T::class.javaObjectType)

/**
 * Gets the [PythonPluginExtension] that is installed on the project.
 */
internal val Project.pythonPlugin: PythonPluginExtension
    get() = extensions.getByType(PythonPluginExtension::class.java)

/**
 * Gets the [PythonEnvsExtension] that is applied to a project.
 */
internal val Project.pythonEnvs: PythonEnvsExtension
    get() = extensions.getByType(PythonEnvsExtension::class.java)