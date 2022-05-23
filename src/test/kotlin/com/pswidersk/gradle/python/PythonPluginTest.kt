package com.pswidersk.gradle.python

import org.assertj.core.api.Assertions.assertThat
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
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

        assertThat(project.plugins.size).isEqualTo(1)
    }

    @Test
    fun `test if properties were correctly set`() {
        // given
        val buildFile = File(tempDir, "build.gradle.kts")
        buildFile.writeText(
            """
            import com.pswidersk.gradle.python.VenvTask
            
            plugins {
                id("com.pswidersk.python-plugin")
            }
        """.trimIndent()
        )
        val runner = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(tempDir)
            .forwardOutput()
            .withArguments(":listPluginProperties")

        // when
        val runResult = runner.build()

        // then
        with(runResult) {
            assertThat(task(":listPluginProperties")!!.outcome).isEqualTo(TaskOutcome.UP_TO_DATE)
            assertThat(output).contains("Miniconda3 version: latest")
        }
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
            assertThat(task(":minicondaSetup")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":envSetup")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":runTestScript")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
            assertThat(output).contains(pythonMessage)
        }
        with(secondRunResult) {
            assertThat(task(":minicondaSetup")!!.outcome).isEqualTo(TaskOutcome.SKIPPED)
            assertThat(task(":envSetup")!!.outcome).isEqualTo(TaskOutcome.SKIPPED)
            assertThat(task(":runTestScript")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
            assertThat(output).contains(pythonMessage)
        }
    }
}