@file:JvmName("PythonPluginConstants")

package com.pswidersk.gradle.python

/**
 * Directory where gradle specific files are stored.
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
const val PYTHON_VENVS_DIR = "python"

/**
 * Name of python envs bootstrap directory.
 */
const val PYTHON_BOOTSTRAP_DIR = "bootstrap"

/**
 * Name of directory where python sources should be stored.
 */
const val PYTHON_SRC_DIR = "main"

/**
 * Name of directory where python test sources should be stored.
 */
const val PYTHON_TEST_DIR = "test"

/**
 * Name of task to build envs from Jetbrains plugin.
 */
const val BUILD_ENVS_TASK_NAME = "build_envs"

/**
 * Plugin tasks group name.
 */
const val PLUGIN_TASKS_GROUP_NAME = "python"