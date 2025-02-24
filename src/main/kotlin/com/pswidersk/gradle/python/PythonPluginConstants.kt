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
 * Default Python version. [List of available releases](https://anaconda.org/conda-forge/python/).
 */
const val DEFAULT_PYTHON_VERSION = "3.13.0"

/**
 * Default Conda installer.
 */
const val DEFAULT_CONDA_INSTALLER = "Miniconda3"

/**
 * Default Conda version. [List of available releases](https://repo.anaconda.com/miniconda).
 */
const val DEFAULT_CONDA_VERSION = "py312_24.9.2-0"

/**
 * Default Conda repository URL.
 */
const val DEFAULT_CONDA_REPO_URL = "https://repo.anaconda.com/miniconda"

/**
 * Default Intellij Idea config directory.
 */
const val DEFAULT_IDEA_DIR = ".idea"

/**
 * SDK Import Intellij plugin config file name.
 */
const val SDK_IMPORT_FILE_NAME = "sdk-import.yml"
