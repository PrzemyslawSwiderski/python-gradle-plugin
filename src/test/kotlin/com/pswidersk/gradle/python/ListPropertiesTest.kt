package com.pswidersk.gradle.python

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.io.File.separatorChar

class ListPropertiesTest {
    @TempDir
    lateinit var tempDir: File

    @Test
    fun `test if default properties were correctly set`() {
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
            .withArguments(":listPluginProperties")

        // when
        val runResult = runner.build()

        // then
        with(runResult) {
            assertThat(task(":listPluginProperties")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
            assertThat(output).contains(
                ".gradle${separatorChar}python",
                "Miniforge3 version: 25.3.1-0",
                "Python: python-3.14.0",
                "Conda repo URL: https://github.com/conda-forge/miniforge/releases/download/"
            )
        }
    }

    @Test
    fun `test if defaults are overridden by user`() {
        // given
        val customInstallDir = tempDir.resolve(".gradleCustomPath").resolve("python").invariantSeparatorsPath
        val buildFile = File(tempDir, "build.gradle.kts")
        buildFile.writeText(
            """
            plugins {
                id("com.pswidersk.python-plugin")
            }
            pythonPlugin {
                pythonVersion.set("3.9.1")
                condaVersion.set("25.2")
                installDir.set(file("$customInstallDir"))
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
            assertThat(output).contains(
                ".gradleCustomPath${separatorChar}python",
                "Python: python-3.9.1",
                "Miniforge3 version: 25.2"
            )
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

    @Test
    fun `test if home dir was used`() {
        // given
        val customInstallDir = System.getProperty("user.home")
        val buildFile = File(tempDir, "build.gradle.kts")
        buildFile.writeText(
            """
            plugins {
                id("com.pswidersk.python-plugin")
            }
            pythonPlugin {
                useHomeDir = true
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
            assertThat(output).contains(
                "Use home directory: true",
                "Install directory: $customInstallDir"
            )
        }
    }
}
