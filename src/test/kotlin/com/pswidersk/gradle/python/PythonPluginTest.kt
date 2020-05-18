package com.pswidersk.gradle.python

import com.jetbrains.python.envs.PythonEnvsExtension
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PythonPluginTest {

    @Test
    fun `test if main external plugins were successfully applied`() {
        val project: Project = ProjectBuilder.builder().build()
        project.pluginManager.apply(PythonPlugin::class.java)

        assertEquals(1, project.plugins.size)
    }

    @Test
    fun `test if python version was changed successfully`() {
        val project: Project = ProjectBuilder.builder().build()
        project.pluginManager.apply(PythonPlugin::class.java)
        project.extensions.configure<PythonPluginExtension>("pythonPlugin") {
            it.pythonVersion.set("3.7.0")
        }
        project.afterEvaluate {
            val pythonVersion = project.extensions.getByType(PythonEnvsExtension::class.java).pythons.first().version
            assertEquals("3.7.0", pythonVersion)
        }
    }

}