package com.pswidersk.gradle.python

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.io.File

class GradleSupportTest {

    @TempDir
    lateinit var tempDir: File

    @ValueSource(strings = ["7.5", "7.0.2", "6.9.2"])
    @ParameterizedTest
    fun `test if plugin is working for older gradle versions`(gradleVersion: String) {
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
            .withGradleVersion(gradleVersion)
            .withProjectDir(tempDir)
            .forwardOutput()
            .withArguments(":listPluginProperties")

        // when
        val runResult = runner.build()

        // then
        with(runResult) {
            assertThat(task(":listPluginProperties")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
        }
    }

}
