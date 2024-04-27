package com.pswidersk.gradle.python.sdkimport

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.io.File.createTempFile
import kotlin.io.path.createTempDirectory


class SdkImportUtilsTest {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Test
    fun `it should properly update config`() {
        // given
        val sdkConfig = createTempFile("SdkImportUtilsTest", "testFile")
        sdkConfig.writeText(
            """
import:
- module: sample-python-project
  path: sample-path-1
  type: PYTHON
- module: sample-python-project-2
  path: P:\python-3.9.2\python.exe
  type: PYTHON
- module: sample-python-project
  path: P:\JAVA\azul-home
  type: JAVA
        """.trimIndent()
        )

        // when
        updateSdkConfig(sdkConfig, "some-other-path", "sample-python-project", log)

        // then
        assertThat(sdkConfig.readText()).hasToString(
            """
import:
- module: sample-python-project
  path: some-other-path
  type: PYTHON
- module: sample-python-project-2
  path: P:\python-3.9.2\python.exe
  type: PYTHON
- module: sample-python-project
  path: P:\JAVA\azul-home
  type: JAVA

        """.trimIndent()
        )
    }

    @Test
    fun `it should add new entry`() {
        // given
        val sdkConfig = createTempFile("SdkImportUtilsTest", "testFile")
        sdkConfig.writeText(
            """
import:
- type: PYTHON
  path: sample-path-2
  module: sample-python-project-2
        """.trimIndent()
        )

        // when
        updateSdkConfig(sdkConfig, "P:\\python-3.9.2\\python.exe", "sample-python-project", log)

        // then
        assertThat(sdkConfig).hasContent(
            """
import:
- module: sample-python-project-2
  path: sample-path-2
  type: PYTHON
- module: sample-python-project
  path: P:\python-3.9.2\python.exe
  type: PYTHON
        """.trimIndent()
        )
    }

    @Test
    fun `it should not fail on empty file`() {
        // given
        val emptySdkConfigFile = createTempFile("SdkImportUtilsTest", "testFile")

        // when
        updateSdkConfig(emptySdkConfigFile, "some-path", "sample-python-project", log)

        // then
        assertThat(emptySdkConfigFile).hasContent(
            """
import:
- module: sample-python-project
  path: some-path
  type: PYTHON
        """.trimIndent()
        )
    }

    @Test
    fun `it should not fail on missing file`() {
        // given
        val emptySdkConfigFile = createTempDirectory("SdkImportUtilsTest").toFile().resolve("nonExistingConfig")

        // when
        updateSdkConfig(emptySdkConfigFile, "some-path", "sample-python-project", log)

        // then
        assertThat(emptySdkConfigFile).hasContent(
            """
import:
- module: sample-python-project
  path: some-path
  type: PYTHON
        """.trimIndent()
        )
    }

}
