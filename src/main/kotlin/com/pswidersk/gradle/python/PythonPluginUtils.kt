package com.pswidersk.gradle.python

import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem
import org.gradle.kotlin.dsl.getByType

/**
 * Gets the [PythonPluginExtension] that is installed on the project.
 */
internal val Project.pythonPlugin: PythonPluginExtension
    get() = extensions.getByType()

/**
 * Returns if operating system is Windows
 */
internal val isWindows: Boolean
    get() = OperatingSystem.current().isWindows

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
            OperatingSystem.current().isMacOsX -> when (arch) {
                "amd64" -> "x86_64"
                "aarch64" -> "arm64"
                else -> arch
            }

            isWindows -> when (arch) {
                "amd64" -> "x86_64"
                "aarch64" -> "x86_64" // no aarch64/arm64 installer available for windows
                else -> arch
            }

            else -> when (arch) {
                "aarch64" -> "aarch64"
                "amd64" -> "x86_64"
                else -> arch
            }
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

/**
 * Converts Gradle project path to Intellij one
 * For example: :nestedModule:someOtherModule -> parent.nestedModule.someOtherModule
 */
internal fun Project.intellijModuleName(): String = this.path
    .replace(':', '.')
    .replaceFirst(".", this.rootProject.name + '.')
    .trimEnd('.')

