@file:JvmName("PythonPluginConstants")

package com.pswidersk.gradle.python

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature.*
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

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

/**
 * Default Intellij Idea config directory.
 */
const val DEFAULT_IDEA_DIR = ".idea"

/**
 * SDK Import Intellij plugin config file name.
 */
const val SDK_IMPORT_FILE_NAME = "sdk-import.yml"

private val YAML_FACTORY = YAMLFactory()
    .disable(SPLIT_LINES)
    .disable(WRITE_DOC_START_MARKER)

val YAML_MAPPER = ObjectMapper(YAML_FACTORY).registerKotlinModule()
