package com.pswidersk.gradle.python

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class ListPropertiesTest {
    @TempDir
    lateinit var tempDir: File

    @Test
    fun `test if default properties were correctly set`() {
        // given
        val defaultWorkingDir = tempDir.resolve(".gradle").resolve("python")
        val buildFile = File(tempDir, "build.gradle.kts")
        buildFile.writeText(
            """
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
            assertThat(task(":listPluginProperties")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
            assertThat(output).contains("Install directory: $defaultWorkingDir")
            assertThat(output).contains("Miniconda3 version: latest")
            assertThat(output).contains("Conda repo URL: https://repo.anaconda.com/miniconda")
        }
    }

    @Test
    fun `test if defaults are overridden by user`() {
        // given
        val customWorkingDir = tempDir.resolve(".gradleCustomPath").resolve("python")
        val buildFile = File(tempDir, "build.gradle.kts")
        buildFile.writeText(
            """
            plugins {
                id("com.pswidersk.python-plugin")
            }
            pythonPlugin {
                pythonVersion.set("3.9.1")
                condaVersion.set("py38_4.8.0")
                installDir.set(file("${customWorkingDir.invariantSeparatorsPath}"))
            }
        """.trimIndent()
        )
        val runner = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(tempDir)
            .forwardOutput()
            .withArguments("--configuration-cache", ":listPluginProperties")

        // when
        val runResult = runner.build()

        // then
        with(runResult) {
            assertThat(task(":listPluginProperties")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
            assertThat(output).contains("Install directory: $customWorkingDir")
            assertThat(output).contains("Python: python-3.9.1")
            assertThat(output).contains("Miniconda3 version: py38_4.8.0")
        }
    }

    @Test
    fun `test if config cache works without warnings`() {
        // given
        val buildFile = File(tempDir, "build.gradle.kts")
        buildFile.writeText(
            """
            plugins {
                id("com.pswidersk.python-plugin")
            }
        """.trimIndent()
        )
        val runner = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(tempDir)
            .forwardOutput()
            .withArguments("--configuration-cache", ":listPluginProperties")

        // when
        val runResult = runner.build()

        // then
        with(runResult) {
            assertThat(task(":listPluginProperties")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
            assertThat(output).doesNotContain("Configuration cache problems found in this build.")
        }
    }
}