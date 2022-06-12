package com.pswidersk.gradle.python

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.internal.os.OperatingSystem


/**
 * Creates a [Property] to hold values of the given type.
 *
 * @param T the type of the property
 * @return the property
 */
internal inline fun <reified T : Any> ObjectFactory.property(): Property<T> =
    property(T::class.javaObjectType)

/**
 * Registers a [Task].
 *
 * @param T the type of the task
 * @param name task name
 * @param configurationAction config block
 * @return task
 */
internal inline fun <reified T : Task> TaskContainer.register(
    name: String,
    configurationAction: Action<T>
): TaskProvider<T> =
    register(name, T::class.javaObjectType, configurationAction)

/**
 * Gets the [PythonPluginExtension] that is installed on the project.
 */
internal val Project.pythonPlugin: PythonPluginExtension
    get() = extensions.getByType(PythonPluginExtension::class.java)

/**
 * Returns if operating system is Windows
 */
internal val isWindows: Boolean
    get() {
        return OperatingSystem.current().isWindows
    }

/**
 * Returns simplified operating system name
 */
internal val os: String
    get() {
        return when {
            OperatingSystem.current().isMacOsX -> "MacOSX"
            isWindows -> "Windows"
            else -> "Linux"
        }
    }


/**
 * Returns system architecture name
 */
internal val arch: String
    get() {
        val arch = System.getProperty("os.arch")
        return when {
            OperatingSystem.current().isMacOsX -> "x86_64"
            isWindows -> when (arch) {
                "x86_64", "amd64" -> "x86_64"
                else -> "x86"
            }
            else -> "x86_64"
        }
    }

/**
 * Returns exec extensions
 */
internal val exec: String
    get() {
        return when {
            OperatingSystem.current().isLinux -> "sh"
            isWindows -> "exe"
            else -> "sh"
        }
    }
