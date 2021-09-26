package com.pswidersk.gradle.python

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File


internal class PythonPluginTest {
    @TempDir
    lateinit var tempDir: File

    @Test
    fun `test if plugin was applied`() {
        val project: Project = ProjectBuilder.builder().build()
        project.pluginManager.apply(PythonPlugin::class.java)

        assertEquals(1, project.plugins.size)
    }

    @Test
    fun `test if test python script was run successfully`() {
        // given
        val pythonMessage = "Hello world from Gradle Python Plugin :)"
        val buildFile = File(tempDir, "build.gradle.kts")
        val testScriptFile = File(tempDir, "testScript.py")
        buildFile.writeText(
            """
            import com.pswidersk.gradle.python.VenvTask
            
            plugins {
                id("com.pswidersk.python-plugin")
            }
            
            tasks {
                register<VenvTask>("runTestScript") {
                    args = listOf("testScript.py")
                }
            }
        """.trimIndent()
        )
        testScriptFile.writeText(
            """
            print("$pythonMessage")
        """.trimIndent()
        )
        val runner = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(tempDir)
            .forwardOutput()
            .withArguments(":runTestScript")

        // when
        val firstRunResult = runner.build()
        val secondRunResult = runner.build()

        // then
        with(firstRunResult) {
            assertEquals(TaskOutcome.SUCCESS, task(":minicondaSetup")!!.outcome)
            assertEquals(TaskOutcome.SUCCESS, task(":envSetup")!!.outcome)
            assertEquals(TaskOutcome.SUCCESS, task(":runTestScript")!!.outcome)
            assertTrue { output.contains(pythonMessage) }
        }
        with(secondRunResult) {
            assertEquals(TaskOutcome.SKIPPED, task(":minicondaSetup")!!.outcome)
            assertEquals(TaskOutcome.SKIPPED, task(":envSetup")!!.outcome)
            assertEquals(TaskOutcome.SUCCESS, task(":runTestScript")!!.outcome)
            assertTrue { output.contains(pythonMessage) }
        }
    }
}