package com.pswidersk.gradle.python

import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import java.io.File


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

internal val Project.minicondaDir: File
    get() = this.rootDir.resolve(GRADLE_FILES_DIR).resolve(PYTHON_ENVS_DIR).resolve(PYTHON_MINICONDA_DIR)

internal val Project.pythonEnvName: String
    get() = "python-${project.pythonPlugin.pythonVersion.get()}"

internal val Project.pythonEnvDir: File
    get() = this.minicondaDir.resolve("envs").resolve(pythonEnvName)

internal val Project.condaBinDir: File
    get() {
        return this.minicondaDir.resolve("condabin")
    }

internal val Project.condaExec: String
    get() {
        return if (Os.isFamily(Os.FAMILY_WINDOWS))
            this.condaBinDir.resolve("conda.bat").path
        else
            this.condaBinDir.resolve("conda").path
    }

/**
 * Returns simplified operating system name
 */
internal val Project.os: String
    get() {
        return when {
            Os.isFamily(Os.FAMILY_MAC) -> "MacOSX"
            Os.isFamily(Os.FAMILY_WINDOWS) -> "Windows"
            else -> "Linux"
        }
    }

/**
 * Returns system architecture name
 */
internal val Project.arch: String
    get() {
        val arch = System.getProperty("os.arch")
        return when {
            Os.isFamily(Os.FAMILY_MAC) -> "x86_64"
            Os.isFamily(Os.FAMILY_WINDOWS) -> when (arch) {
                "x86_64", "amd64" -> "x86_64"
                else -> "x86"
            }
            else -> "x86_64"
        }
    }

/**
 * Returns exec extensions
 */
internal val Project.exec: String
    get() {
        return when {
            Os.isFamily(Os.FAMILY_MAC) -> "sh"
            Os.isFamily(Os.FAMILY_WINDOWS) -> "exe"
            else -> "sh"
        }
    }
