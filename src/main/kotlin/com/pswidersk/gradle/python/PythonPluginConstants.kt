@file:JvmName("PythonPluginConstants")

package com.pswidersk.gradle.python

/**
 * Name of python plugin extension in projects.
 */
const val PYTHON_PLUGIN_EXTENSION_NAME = "pythonPlugin"

/**
 * Directory where gradle specific files are stored.
 */
const val GRADLE_FILES_DIR = ".gradle"

/**
 * Name of directory where python environments will be stored.
 */
const val PYTHON_ENVS_DIR = "python"

/**
 * Name of directory where virtual environments will be stored.
 */
const val PYTHON_VENVS_DIR = "pythonVenvs"

/**
 * Name of python envs bootstrap directory.
 */
const val PYTHON_BOOTSTRAP_DIR = "bootstrap"

/**
 * Name of task to build envs from Jetbrains plugin.
 */
const val BUILD_ENVS_TASK_NAME = "build_envs"

/**
 * Plugin tasks group name.
 */
const val PLUGIN_TASKS_GROUP_NAME = "python"