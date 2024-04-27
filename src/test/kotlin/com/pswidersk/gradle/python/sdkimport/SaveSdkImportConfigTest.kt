package com.pswidersk.gradle.python.sdkimport

import com.pswidersk.gradle.python.loadResource
import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.io.File
import java.nio.file.Files

class SaveSdkImportConfigTest {

    private lateinit var parentModule: File

    @BeforeEach
    fun setup() {
        val tmpDirection = Files.createTempDirectory("SaveSdkImportConfigTest")
        parentModule = tmpDirection.toFile().resolve("parent").also { it.mkdir() }
    }

    @ValueSource(
        strings = [
            "singleModuleTest",
            "multiModuleTest"
        ]
    )
    @ParameterizedTest
    fun `single module case`(caseName: String) {
        // given
        val expectedSdkImportFile = loadResource("sdkimport/$caseName/expected-sdk-import.yml")
        loadResource("sdkimport/$caseName").copyRecursively(parentModule)
        val testSdkConfigFile = File(parentModule, "sdk-import.yml")

        val runner = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(parentModule)
            .forwardOutput()
            .withArguments("saveSdkImportConfigTest")

        // when
        runner.build()

        // then
        assertThat(testSdkConfigFile).hasSameTextualContentAs(expectedSdkImportFile)
    }

}
