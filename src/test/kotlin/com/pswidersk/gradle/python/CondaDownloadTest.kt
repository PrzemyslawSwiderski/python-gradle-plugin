package com.pswidersk.gradle.python

import com.github.tomakehurst.wiremock.client.BasicCredentials
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

@WireMockTest
class CondaDownloadTest {

    @TempDir
    lateinit var tempDir: File

    @Test
    fun `test if request is correct for custom repo`(wmRuntimeInfo: WireMockRuntimeInfo) {
        // given
        // Mocking 404 return to stop setup task (we only care about the request)
        stubFor(
            get(urlMatching("/conda/.*"))
                .willReturn(aResponse().withStatus(404))
        )

        val buildFile = File(tempDir, "build.gradle.kts")
        buildFile.writeText(
            """
            import com.pswidersk.gradle.python.VenvTask
            
            plugins {
                id("com.pswidersk.python-plugin")
            }
            
            pythonPlugin {
                minicondaVersion.set("py38_4.8.3")
                minicondaRepoUrl.set("${wmRuntimeInfo.httpBaseUrl}/conda")
                minicondaRepoUsername.set("user")
                minicondaRepoPassword.set("pass")
                minicondaRepoHeaders.set(mapOf(
                    "SOME_HEADER_1" to "testValue1",
                    "SOME_HEADER_2" to "testValue2"
                ))
            }
        """.trimIndent()
        )
        val runner = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(tempDir)
            .forwardOutput()
            .withArguments("--stacktrace", ":minicondaSetup")

        // when
        runner.buildAndFail()

        // then
        verify(
            getRequestedFor(urlMatching("/conda/.*"))
                .withUrl("/conda/Miniconda3-py38_4.8.3-$os-$arch.$exec")
                .withBasicAuth(BasicCredentials("user", "pass"))
                .withHeader("SOME_HEADER_1", equalTo("testValue1"))
                .withHeader("SOME_HEADER_2", equalTo("testValue2"))
        )
    }
}