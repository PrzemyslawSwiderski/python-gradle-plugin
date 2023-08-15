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
 * Plugin tasks group name.
 */
const val PLUGIN_TASKS_GROUP_NAME = "python"

/**
 * Default Python version.
 */
const val DEFAULT_PYTHON_VERSION = "3.10.12"

/**
 * Default Conda installer.
 */
const val DEFAULT_CONDA_INSTALLER = "Miniconda3"

/**
 * Default Conda version.
 */
const val DEFAULT_CONDA_VERSION = "py311_23.5.2-0"

/**
 * Default Conda repository URL.
 */
const val DEFAULT_CONDA_REPO_URL = "https://repo.anaconda.com/miniconda"